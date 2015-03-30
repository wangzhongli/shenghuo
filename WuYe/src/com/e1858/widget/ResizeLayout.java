package com.e1858.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ResizeLayout extends LinearLayout {

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
