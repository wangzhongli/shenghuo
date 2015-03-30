package com.e1858.mall;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e1858.wuye.mall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 日期: 2015年2月3日 下午3:43:49
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class MallPullRefreshListFragment extends Fragment {
	protected PullToRefreshListView	listView;
	protected View					emptyView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mall_pulltorefresh_listview, null);
		listView = (PullToRefreshListView) view.findViewById(R.id.listView);
		listView.setMode(Mode.BOTH);
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
//		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
//		listView.getRefreshableView().setDividerHeight(HGUtils.dip2px(getActivity(), 8));
		emptyView = View.inflate(getActivity(), R.layout.hg_include_loading, null);
		emptyView.setVisibility(View.VISIBLE);
		((TextView) emptyView.findViewById(R.id.hg_textView)).setText("没有数据");
		return view;
	}

	public void setEmptyText(String text) {
		((TextView) emptyView.findViewById(R.id.hg_textView)).setText(text);
	}

	public void updateEmptyStatus(boolean showProgress, boolean showText) {
		emptyView.findViewById(R.id.hg_progressBar).setVisibility(showProgress ? View.VISIBLE : View.GONE);
		emptyView.findViewById(R.id.hg_textView).setVisibility(showText ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				listView.setEmptyView(emptyView);
			}
		}, 200);

	}

}
