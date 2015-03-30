package com.e1858.mall.product;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.mall.protocol.packet.MallGetProductCategoriesRequest;
import com.e1858.mall.protocol.packet.MallGetProductCategoriesResponse;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;

public class MallCategoryMajorFragment extends Fragment {

	CategoryAdapter				categoryAdapter;
	OnCategoryChangedListener	categoryChangedListener;
	ListView					listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		listView = new ListView(getActivity());
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setBackgroundColor(0xfff2f2f2);
		listView.setDivider(new ColorDrawable(Color.LTGRAY));
		listView.setDividerHeight(1);
		listView.setVerticalScrollBarEnabled(true);
		categoryAdapter = new CategoryAdapter();
		listView.setAdapter(categoryAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (categoryChangedListener != null) {
					categoryChangedListener.onChanged((MallProductCategoryBean) parent.getAdapter().getItem(position));
				}
				categoryAdapter.setSelectedIndex(position);
			}
		});
		loadCategories();
		return listView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public void loadCategories() {
		MallGetProductCategoriesRequest request = new MallGetProductCategoriesRequest();
		ResponseHandler<MallGetProductCategoriesResponse> responseHandler = new ResponseHandler<MallGetProductCategoriesResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetProductCategoriesResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (categoryAdapter != null) {
						categoryAdapter.setCategories(response.getCategories());
						if (!HGUtils.isListEmpty(response.getCategories())) {
							listView.performItemClick(listView, 0, 0);
						}
					}
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGetProductCategoriesResponse.class, responseHandler, true);
	}

	public OnCategoryChangedListener getOnCategoryChangedListener() {
		return categoryChangedListener;
	}

	public void setOnCategoryChangedListener(OnCategoryChangedListener categoryChangedListener) {
		this.categoryChangedListener = categoryChangedListener;
	}

	class CategoryAdapter extends BaseAdapter {
		List<MallProductCategoryBean>	categories;
		int								selectedIndex	= -1;

		@Override
		public int getCount() {
			return categories == null ? 0 : categories.size();
		}

		/**
		 * @param position
		 */
		public void setSelectedIndex(int position) {
			this.selectedIndex = position;
			notifyDataSetChanged();
		}

		@Override
		public MallProductCategoryBean getItem(int position) {
			return categories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.mall_listitem_category, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.textView);
			MallProductCategoryBean category = getItem(position);
			textView.setText(category.getName());
			if (position == selectedIndex) {
				convertView.setBackgroundColor(0xfff8f8f8);
			} else {
				convertView.setBackgroundResource(R.drawable.mall_sl_listitem_category);
			}
			return convertView;
		}

		public List<MallProductCategoryBean> getCategories() {
			return categories;
		}

		public void setCategories(List<MallProductCategoryBean> categories) {
			this.categories = categories;
			notifyDataSetChanged();
		}
	}
}
