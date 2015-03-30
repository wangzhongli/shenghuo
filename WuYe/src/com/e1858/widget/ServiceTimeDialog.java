package com.e1858.widget;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.Formatter;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;

import com.e1858.wuye.R;

/**
 * 日期: 2015年1月7日 上午11:06:03
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class ServiceTimeDialog extends AlertDialog {

	DatePicker		datePicker;
	NumberPicker	timePicker_begin;
	NumberPicker	timePicker_end;		;
	Formatter		formatter	= new Formatter() {

									@Override
									public String format(int value) {
										return String.format("%02d:00", value);
									}
								};

	/**
	 * @param context
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ServiceTimeDialog(Context context, final OnFinishedListener listener) {
		super(context);
		View view = View.inflate(context, R.layout.dialog_service_time, null);
		setView(view);
		setTitle("上门服务服务时间");
		datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			datePicker.setCalendarViewShown(false);
		}
		//获取TimePicker对象
		timePicker_begin = (NumberPicker) view.findViewById(R.id.timePicker_begin);
		timePicker_end = (NumberPicker) view.findViewById(R.id.timePicker_end);

		timePicker_begin.setMinValue(7);
		timePicker_begin.setMaxValue(20);

		timePicker_begin.setFormatter(formatter);
		timePicker_end.setFormatter(formatter);

		timePicker_end.setMinValue(7);
		timePicker_end.setMaxValue(20);
		timePicker_end.setValue(8);
		timePicker_begin.findViewById(R.id.np__numberpicker_input).setVisibility(View.GONE);
		timePicker_end.findViewById(R.id.np__numberpicker_input).setVisibility(View.GONE);
		timePicker_begin.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal >= timePicker_end.getValue()) {
					timePicker_begin.setValue(Math.max(timePicker_begin.getMinValue(), timePicker_end.getValue() - 1));
					timePicker_end.setValue(Math.max(timePicker_begin.getValue() + 1, timePicker_end.getValue()));
				}
			}
		});
		timePicker_end.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal <= timePicker_begin.getValue()) {
					timePicker_end.setValue(Math.min(timePicker_begin.getValue() + 1, timePicker_end.getMaxValue()));
					timePicker_begin.setValue(Math.max(timePicker_begin.getMinValue(), timePicker_end.getValue() - 1));
				}
			}
		});

		setButton(DialogInterface.BUTTON_POSITIVE, "确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null) {
					String text = String.format("%d-%02d-%02d %s-%s", datePicker.getYear(), datePicker.getMonth() + 1,
							datePicker.getDayOfMonth(), formatter.format(timePicker_begin.getValue()),
							formatter.format(timePicker_end.getValue()));
					listener.onFinished(text);
				}
			}
		});
		setButton(DialogInterface.BUTTON_NEGATIVE, "取消", (OnClickListener) null);
	}

	public interface OnFinishedListener {
		public void onFinished(String fmtText);
	}
}
