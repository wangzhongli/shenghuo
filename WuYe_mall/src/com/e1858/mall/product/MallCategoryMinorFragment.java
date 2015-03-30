package com.e1858.mall.product;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView.OnHeaderClickListener;

public class MallCategoryMinorFragment extends Fragment {

	public static final String	IntentKey_Category	= "IntentKey_Category";

	CategoryAdapter				categoryAdapter;
	OnCategoryChangedListener	categoryChangedListener;
	LayoutInflater				inflater;
	StickyGridHeadersGridView	gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		gridView = new StickyGridHeadersGridView(getActivity());
		gridView.setNumColumns(3);
		gridView.setCacheColorHint(Color.TRANSPARENT);
		gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridView.setBackgroundColor(0xfff8f8f8);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (categoryChangedListener != null) {
					categoryChangedListener.onChanged(categoryAdapter.getItem(position));
				}
			}
		});
		gridView.setOnHeaderClickListener(new OnHeaderClickListener() {

			@Override
			public void onHeaderClick(AdapterView<?> parent, View view, long id) {
				if (categoryChangedListener != null) {
					categoryChangedListener.onChanged(categoryAdapter.getHeaderCategory((int) id));
				}
			}
		});
		this.inflater = inflater;
		return gridView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MallProductCategoryBean majorCategory = (MallProductCategoryBean) getArguments().getSerializable(
				IntentKey_Category);
		categoryAdapter = new CategoryAdapter();
		categoryAdapter.setMajorCategory(majorCategory);
		gridView.setAdapter(categoryAdapter);
	}

	public OnCategoryChangedListener getOnCategoryChangedListener() {
		return categoryChangedListener;
	}

	public void setOnCategoryChangedListener(OnCategoryChangedListener categoryChangedListener) {
		this.categoryChangedListener = categoryChangedListener;
	}

	class CategoryAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

		private List<MallProductCategoryBean>	categories2	= new ArrayList<MallProductCategoryBean>();
		private List<MallProductCategoryBean>	categories3	= new ArrayList<MallProductCategoryBean>();

		void setMajorCategory(MallProductCategoryBean majorCategory) {
			categories2.clear();
			categories3.clear();
			if (majorCategory == null) {
				return;
			}
			if (!HGUtils.isListEmpty(majorCategory.getChildren())) {
				for (MallProductCategoryBean child2 : majorCategory.getChildren()) {
					if (!HGUtils.isListEmpty(child2.getChildren())) {
						categories2.add(child2);
						for (MallProductCategoryBean child3 : child2.getChildren()) {
							categories3.add(child3);
						}
					}
				}
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return categories3.size();
		}

		@Override
		public MallProductCategoryBean getItem(int position) {
			return categories3.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.mall_griditem_minorcategory, null);
			}
			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
			TextView textView = (TextView) convertView.findViewById(R.id.textView);
			MallProductCategoryBean category = getItem(position);
			ImageLoader.getInstance().displayImage(category.getImageUrl(), imageView,
					BaseApplication.displayImageOptions_empty());
			textView.setText(category.getName());
			return convertView;
		}

		/*
		 */
//		public long getHeaderId(int position) {
//			return categories3.get(position).getParentID().hashCode();
//		}

		public MallProductCategoryBean getHeaderCategory(long id) {
			return categories2.get((int) id);
		}

		/*
		 */
		@Override
		public View getHeaderView(int position, View convertView, ViewGroup parent) {
			TextView textView = (TextView) convertView;
			if (convertView == null) {
				convertView = textView = (TextView) inflater.inflate(R.layout.mall_sectionhead_minorcategory, parent,
						false);
			}
			MallProductCategoryBean child2 = categories2.get(position);
			textView.setText(child2.getName());
			return textView;
		}

		/*
		 */
		@Override
		public int getCountForHeader(int header) {
			List<MallProductCategoryBean> children = categories2.get(header).getChildren();
			return children == null ? 0 : children.size();
		}

		/*
		 */
		@Override
		public int getNumHeaders() {
			return categories2.size();
		}
	}

}
