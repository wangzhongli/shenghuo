package com.e1858.common.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.e1858.common.R;
import com.e1858.common.app.PaymentMethodActivity.PaymentMethod.Method;
import com.e1858.wuye.alipay.msp.Result;
import com.e1858.wuye.protocol.http.PaymentParamBean;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * 缴费方式
 * 
 * @author jiajia
 */
@SuppressLint("SimpleDateFormat")
public class PaymentMethodActivity extends BaseActionBarActivity {

	public static final String	BroadcastAction_PaySuccess	= "PaymentMethodActivity.BroadcastAction_PaySuccess";

	public static final String	IntentKey_TAG				= "IntentKey_TAG";										// 标示ID，可有可无
	public static final String	IntentKey_Amount			= "IntentKey_Amount";
	public static final String	IntentKey_Description		= "IntentKey_Description";								//float
	public static final String	IntentKey_PaymentParam		= "IntentKey_PaymentParam";							//string

	private TextView			shouldPaidMoney;																	//应支付金额
	private ListView			listView;																			//支付列表
	private List<PaymentMethod>	methods						= new ArrayList<PaymentMethod>();

	float						amount						= 0;
	PaymentParamBean			paymentParam;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent() == null) {
			Toast.makeText(getActivity(), "参数错误", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		amount = getIntent().getFloatExtra(IntentKey_Amount, -1);
		if (amount < 0) {
			Toast.makeText(getActivity(), "支付金额不能小于0", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		paymentParam = (PaymentParamBean) getIntent().getSerializableExtra(IntentKey_PaymentParam);
		if (paymentParam == null) {
			Toast.makeText(getActivity(), "支付参数错误", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		setContentView(R.layout.wuye_cm_payment_method);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initView();
		initData();
	}

	private void initView() {
		shouldPaidMoney = (TextView) findViewById(R.id.shouldPaidMoney);
		shouldPaidMoney.setText(String.format("￥%.2f", amount));
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//进行相应的支付
				PaymentMethod method = (PaymentMethod) parent.getAdapter().getItem(position);
				switch (method.getMethod()) {
				case Alipay:
					submitAlipay();
					break;
				case Guangda:
					submitGuangda();
					break;
				case Unionpay:
					submitUnionpay();
					break;

				default:
					break;
				}
			}
		});

		TextView textView_description = ((TextView) findViewById(R.id.textView_description));
		String description = getIntent().getStringExtra(IntentKey_Description);
		textView_description.setText(description);
		if (TextUtils.isEmpty(description)) {
			textView_description.setVisibility(View.GONE);
		}

	}

	private void initData() {
		if (!TextUtils.isEmpty(paymentParam.getAlipay())) {
			PaymentMethod method = new PaymentMethod();
			method.setMethod(Method.Alipay);
			method.setIcon(R.drawable.alipay_icon);
			method.setName("支付宝快捷支付");
			method.setEdescription("免手续费  实时到账");
			methods.add(method);
		}
		if (!TextUtils.isEmpty(paymentParam.getCebbank())) {
			PaymentMethod method = new PaymentMethod();
			method.setMethod(Method.Guangda);
			method.setIcon(R.drawable.guangda_logo);
			method.setName("光大银行快捷支付");
			method.setEdescription("免手续费  实时到账");
			methods.add(method);
		}
		if (!TextUtils.isEmpty(paymentParam.getUnionpay())) {
			PaymentMethod method = new PaymentMethod();
			method.setMethod(Method.Unionpay);
			method.setIcon(R.drawable.unionpay_logo);
			method.setName("银联支付");
			method.setEdescription("免手续费  实时到账");
			methods.add(method);
		}
		listView.setAdapter(new MethodAdapter());
	}

	class MethodAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return methods.size();
		}

		@Override
		public PaymentMethod getItem(int position) {
			return methods.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(PaymentMethodActivity.this).inflate(
						R.layout.wuye_cm_payment_method_item, null);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder.edescription = (TextView) convertView.findViewById(R.id.edescription);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.icon.setBackgroundResource(methods.get(position).getIcon());
			viewHolder.name.setText(methods.get(position).getName());
			viewHolder.edescription.setText(methods.get(position).getEdescription());
			return convertView;
		}

	}

	class ViewHolder {
		private ImageView	icon;
		private TextView	name;
		private TextView	edescription;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (TextUtils.isEmpty(str)) {
			return;
		} else if (str.equalsIgnoreCase("success")) {
			onPaySuccess();
			return;
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("支付结果通知");
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		// builder.setCustomTitle();
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void onPaySuccess() {
		Intent intent = new Intent(BroadcastAction_PaySuccess);
		intent.putExtra(IntentKey_TAG, getIntent().getStringExtra(IntentKey_TAG));
		sendBroadcast(intent);
		Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_LONG).show();
		finish();
	}

	/**
	 * 光大银行支付
	 */
	protected void submitGuangda() {
		Uri uri = Uri.parse(paymentParam.getCebbank());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
//		Intent intent = new Intent(this, WebViewActivity.class);
//		intent.putExtra(WebViewActivity.IntentKey_URL, paymentParam.getCebbank());
//		startActivity(intent);
	}

	private void submitUnionpay() {
		/*****************************************************************
		 * mMode参数解释：
		 * "00" - 启动银联正式环境
		 * "01" - 连接银联测试环境
		 *****************************************************************/
		String mode = "01";
		UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null, paymentParam.getUnionpay(), mode);
	}

	private void submitAlipay() {
		try {
			new Thread() {
				@Override
				public void run() {
					AliPay alipay = new AliPay(PaymentMethodActivity.this, uiHandler);
					//设置为沙箱模式，不设置默认为线上环境
					//alipay.setSandBox(true);
					String resultText = alipay.pay(paymentParam.getAlipay());
					Log.i("TAG", "result = " + resultText);

					final Result result = new Result(resultText);
					result.parseResult();
					if (result.getResult() != null) {
						uiHandler.post(new Runnable() {
							@Override
							public void run() {
								if (result.getResult().equals("9000")) {
									onPaySuccess();
								} else {
									Toast.makeText(getActivity(), result.getResultStatus(), Toast.LENGTH_LONG).show();
								}
							}
						});
					} else {
						Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_LONG).show();
					}
				}
			}.start();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

/*
 * private String getSignType() {
 * return "sign_type=\"RSA\"";
 * }
 * @SuppressWarnings("deprecation")
 * private String getNewOrderInfo(FeeBill feeBill) {
 * StringBuilder sb = new StringBuilder();
 * sb.append("partner=\"");
 * sb.append(Keys.DEFAULT_PARTNER);
 * sb.append("\"&out_trade_no=\"");
 * sb.append(feeBill.getOrderNumber());
 * sb.append("\"&subject=\"");
 * sb.append(feeBill.getFeeType().getName());
 * sb.append("\"&body=\"");
 * sb.append(feeBill.getEdescription());
 * sb.append("\"&total_fee=\"");
 * sb.append(feeBill.getAmount());
 * sb.append("\"&notify_url=\"");
 * // 网址需要做URL编码
 * sb.append(URLEncoder.encode(Constant.NOTIFY_URL));
 * sb.append("\"&service=\"mobile.securitypay.pay");
 * sb.append("\"&_input_charset=\"UTF-8");
 * sb.append("\"&return_url=\"");
 * sb.append(URLEncoder.encode("http://m.alipay.com"));
 * sb.append("\"&payment_type=\"1");
 * sb.append("\"&seller_id=\"");
 * sb.append(Keys.DEFAULT_SELLER);
 * // 如果show_url值为空，可不传
 * // sb.append("\"&show_url=\"");
 * sb.append("\"&it_b_pay=\"1m");
 * sb.append("\"");
 * return new String(sb);
 * }
 * private String getOutTradeNo() {
 * SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
 * Date date = new Date();
 * String key = format.format(date);
 * java.util.Random r = new java.util.Random();
 * key += r.nextInt();
 * key = key.substring(0, 15);
 * Log.d("TAG", "outTradeNo: " + key);
 * return key;
 * }
 */

	static class PaymentMethod implements Serializable {

		static enum Method {
			Alipay, Guangda, Unionpay
		}

		private Method	method;
		private int		icon;
		private String	name;
		private String	edescription;

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public int getIcon() {
			return icon;
		}

		public void setIcon(int icon) {
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEdescription() {
			return edescription;
		}

		public void setEdescription(String edescription) {
			this.edescription = edescription;
		}

	}
}
