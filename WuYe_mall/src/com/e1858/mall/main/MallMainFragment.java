package com.e1858.mall.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.cart.MallCartActivity;
import com.e1858.mall.product.MallCategoryActivity;
import com.e1858.mall.product.MallProductActivity;
import com.e1858.mall.product.MallProductSearchActivity;
import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.mall.protocol.packet.MallGetMainDataRequest;
import com.e1858.mall.protocol.packet.MallGetMainDataResponse;
import com.e1858.mall.recommend.MallPersonRecommActivity;
import com.e1858.mall.utils.MallCartBadgeManager;
import com.e1858.point.PointActivity;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;
import com.e1858.wuye.protocol.http.BannerBean;
import com.hg.android.app.ImagePagerAdapter.OnImageClickListener;
import com.hg.android.utils.HGUtils;
import com.hg.android.widget.ImagePagerLayout;

public class MallMainFragment extends Fragment {

	MallCartBadgeManager		cartBadgeManager	= new MallCartBadgeManager();
	ImagePagerLayout			imagePager;

	MallGetMainDataResponse		getMainDataResponse;
	ListView					listView_0;
	GridView					gridView_0;
	MallMainRecommendAdapter	recommendAdapter;
	MallMainMoreAdapter			moreAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.mall_fragment_main, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		initViews();
		loadMainData();
		updateForMainData();
	}

	void initViews() {
		final int imageButtonIds[] = { R.id.imageButton_0, R.id.imageButton_1, R.id.imageButton_2, R.id.imageButton_3 };
		final int imageButtonDrawables[] = { R.drawable.mall_main_shortcut_category,
				R.drawable.mall_main_shortcut_origin, R.drawable.mall_main_shortcut_point,
				R.drawable.mall_main_shortcut_recommend };
		final String imageButtonTexts[] = { "商品分类", "产地直供", "我的积分", "业主推荐" };
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.imageButton_0) {
					startActivity(new Intent(getActivity(), MallCategoryActivity.class));
				} else if (v.getId() == R.id.imageButton_1) {
					boolean tagIsEmpty = getMainDataResponse == null
							|| TextUtils.isEmpty(getMainDataResponse.getOriginTagID());
					if (!tagIsEmpty) {
						Intent intent = new Intent(v.getContext(), MallProductActivity.class);
						intent.putExtra(MallProductActivity.IntentKey_TagID, getMainDataResponse.getOriginTagID());
						intent.putExtra(MallProductActivity.IntentKey_Title, imageButtonTexts[1]);
						startActivity(intent);
					}
				} else if (v.getId() == R.id.imageButton_2) {
					if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(getActivity())) {
						startActivity(new Intent(getActivity(), PointActivity.class));
					}
				} else if (v.getId() == R.id.imageButton_3) {
					startActivity(new Intent(getActivity(), MallPersonRecommActivity.class));
				}
			}
		};
		for (int i = 0; i < imageButtonIds.length; i++) {
			View view = getView().findViewById(imageButtonIds[i]);
			((ImageView) view.findViewById(R.id.imageView)).setImageResource(imageButtonDrawables[i]);
			((TextView) view.findViewById(R.id.textView)).setText(imageButtonTexts[i]);
			view.setOnClickListener(listener);
		}

		imagePager = (ImagePagerLayout) getView().findViewById(R.id.imagePagerLayout);
		imagePager.setOnImageClickListener(new OnImageClickListener() {
			@Override
			public void onClick(int position) {
				if (getMainDataResponse != null && getMainDataResponse.getTopBanners() != null
						&& getMainDataResponse.getTopBanners().size() > position)
					BaseApplication.getBaseInstance().judgeForBannerUrl(getActivity(),
							getMainDataResponse.getTopBanners().get(position));
			}
		});

		listView_0 = (ListView) getView().findViewById(R.id.listView_0);
		gridView_0 = (GridView) getView().findViewById(R.id.gridView_0);

		listView_0.setAdapter(recommendAdapter = new MallMainRecommendAdapter());
		gridView_0.setAdapter(moreAdapter = new MallMainMoreAdapter());

		gridView_0.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MallProductCategoryBean category = (MallProductCategoryBean) parent.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), MallProductActivity.class);
				intent.putExtra(MallProductActivity.IntentKey_Category, category);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		cartBadgeManager.unregister();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.mall_main, menu);
		cartBadgeManager.register(menu.findItem(R.id.mall_action_cart), this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_cart) {
			if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(getActivity())) {
				startActivity(new Intent(getActivity(), MallCartActivity.class));
			}
			return true;
		}
		if (item.getItemId() == R.id.mall_action_search) {
			startActivity(new Intent(getActivity(), MallProductSearchActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void updateForMainData() {
		if (getMainDataResponse == null) {
			return;
		}
		recommendAdapter.setRecommendedCategories(getMainDataResponse.getRecommendedCategories());
		moreAdapter.setMoreCategories(getMainDataResponse.getMoreCategories());
		List<String> imageUrls = new ArrayList<String>();
		for (BannerBean banner : getMainDataResponse.getTopBanners()) {
			imageUrls.add(banner.getImageUrl());
		}
		imagePager.setImageUrls(imageUrls);
	}

	private void loadMainData() {
		ResponseHandler<MallGetMainDataResponse> responseHandler = new ResponseHandler<MallGetMainDataResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetMainDataResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)
						&& !HGUtils.isListEmpty(response.getTopBanners())) {
					getMainDataResponse = response;
					updateForMainData();
				}
			}
		};

		HttpPacketClient.postPacketAsynchronous(new MallGetMainDataRequest(), MallGetMainDataResponse.class,
				responseHandler, true);
	}
}
