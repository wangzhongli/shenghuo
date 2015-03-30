package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.object.CityBean;
import com.e1858.utils.StringUtils;
import com.e1858.widget.MyLetterListView;
import com.e1858.widget.MyLetterListView.OnTouchingLetterChangedListener;
import com.e1858.wuye.R;
/**
 * 选择城市
 * @author jiajia
 *
 */
public class SwitchCityActivity extends BaseActivity {
	private BaseAdapter adapter;
	private ListView mCityLit;
	private TextView overlay;
	private TextView currentCity;
	private MyLetterListView letterListView;
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread;
	private ArrayList<CityBean> mCityNames = new ArrayList<CityBean>();
	private SQLiteDatabase db;
	private WindowManager windowManager;
	private String currentCity_Str = "";
	private int remark = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_city_list);
		currentCity_Str = getIntent().getExtras().getString("CurrentCity");
		remark = getIntent().getExtras().getInt(Constant.PIC_REMARK);
		application.getDbManager().closeDatabase();
		application.getDbManager().openDatabase();
		db = application.getDbManager().getDatabase();
		initView();
		initData();
	}

	private void initView() {
		mCityLit = (ListView) findViewById(R.id.city_list);
		letterListView = (MyLetterListView) findViewById(R.id.cityLetterListView);
		currentCity = (TextView) findViewById(R.id.currentCity);
		currentCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!TextUtils.isEmpty(currentCity_Str)) {
					onClickCity(currentCity_Str);
				}
			}
		});
	}

	private void initData() {
		// 取到数据
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(getResources()
				.getString(R.string.text_bar_switchCity));
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
		if(!StringUtils.isEmpty(currentCity_Str)){
			currentCity.setText("当前定位城市:" + currentCity_Str);
		}else{
			currentCity.setText("当前定位城市:无");
		}
		
		if (getCityData() != null) {
			mCityNames = getCityData();
		} else {
			mCityNames = new ArrayList<CityBean>();
		}
		setAdapter(mCityNames);
		mCityLit.setOnItemClickListener(new CityListOnItemClick());
	}

	private ArrayList<CityBean> getCityData() {
		ArrayList<CityBean> names = new ArrayList<CityBean>();
		Cursor cursor = db.rawQuery("SELECT * FROM T_City ORDER BY NameSort",
				null);
		if (cursor != null) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				CityBean cityModel = new CityBean();
				cityModel.setCityName(cursor.getString(cursor
						.getColumnIndex("CityName")));
				cityModel.setNameSort(cursor.getString(cursor
						.getColumnIndex("NameSort")));
				names.add(cityModel);
			}
		}
		return names;
	}
	
	void onClickCity(String cityName) {
		Intent intent = new Intent(SwitchCityActivity.this,
				SelectCommunityNewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("CurrentCity", cityName);
		if (remark == -1) {
			bundle.putInt(Constant.PIC_REMARK, -1);
		} else {
			bundle.putInt(Constant.PIC_REMARK, 1);
		}
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 城市列表点击事件
	 * 
	 * @author sy
	 * 
	 */
	class CityListOnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			CityBean cityModel = (CityBean) mCityLit.getAdapter().getItem(pos);
			onClickCity(cityModel.getCityName());
		}
	}

	/**
	 * 为ListView设置适配器
	 * 
	 * @param list
	 */
	private void setAdapter(List<CityBean> list) {
		if (list != null) {
			adapter = new ListAdapter(this, list);
			mCityLit.setAdapter(adapter);
		}

	}

	/**
	 * ListViewAdapter
	 * 
	 * @author sy
	 * 
	 */
	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<CityBean> list;

		public ListAdapter(Context context, List<CityBean> list) {

			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				// getAlpha(list.get(i));
				String currentStr = list.get(i).getNameSort();
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? list.get(i - 1)
						.getNameSort() : " ";
				if (!previewStr.equals(currentStr)) {
					String name = list.get(i).getNameSort();
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.switch_city_list_item,
						null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(list.get(position).getCityName());
			String currentStr = list.get(position).getNameSort();
			String previewStr = (position - 1) >= 0 ? list.get(position - 1)
					.getNameSort() : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha;
			TextView name;
		}

	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.switch_city_overlay,
				null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mCityLit.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		windowManager.removeView(overlay);
	}

}
