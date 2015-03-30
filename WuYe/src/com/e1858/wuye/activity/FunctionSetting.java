package com.e1858.wuye.activity;
import com.e1858.wuye.R;
import com.igexin.sdk.PushManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
/**
 * 是否推送消息设置
 * @author jiajia
 *
 */
public class FunctionSetting extends BaseActivity {
	private ToggleButton push_switch;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_function);
		 sp=getSharedPreferences("Push", Context.MODE_PRIVATE);
		initView();
	}
	private void initView(){
		bar_title=(TextView)findViewById(R.id.bar_title);
		bar_leftBtn=(Button)findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title.setText(getResources().getString(R.string.text_function));
		push_switch=(ToggleButton)findViewById(R.id.push_switch);
		if(sp.getBoolean("Enable", true)){
			push_switch.setChecked(true);
		}else{
			push_switch.setChecked(false);
		}
		
		push_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Editor editor=sp.edit();
				if(isChecked){
					editor.putBoolean("Enable", true);
					editor.commit();
					PushManager.getInstance().turnOnPush(getApplicationContext());
				}else{
					editor.putBoolean("Enable", false);
					editor.commit();
					PushManager.getInstance().turnOffPush(getApplicationContext());
				}
			}
		});
	}

}
