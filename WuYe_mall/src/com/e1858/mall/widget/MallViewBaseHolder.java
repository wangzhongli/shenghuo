package com.e1858.mall.widget;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.view.View;

public abstract class MallViewBaseHolder {

	protected WeakReference<View>	view;
	protected View					realView;
	protected Context				context;

	protected abstract void initSubviews();

	public MallViewBaseHolder(Context context, View view, int layoutID) {
		this.context = context;
		if (view == null) {
			setView(View.inflate(context, layoutID, null), true);
		} else {
			setView(view, false);
		}
	}

	protected MallViewBaseHolder setView(View view, boolean createdBySelf) {
		realView = createdBySelf ? view : null;
		view.setTag(this);
		this.view = new WeakReference<View>(view);
		initSubviews();
		return this;
	}

	public View detach() {
		View view = realView;
		realView = null;
		return view;
	}

	public View getView() {
		return view.get();
	}

	public View findViewById(int id) {
		return getView().findViewById(id);
	}

	public Context getContext() {
		return context;
	}
}
