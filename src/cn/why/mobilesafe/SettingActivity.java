package cn.why.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.why.mobilesafe.ui.SettingItemView;

public class SettingActivity extends Activity {

	private SettingItemView settingItemUpdate;//自动更新的设置项
	private SharedPreferences sharedPreferences;//保存用户的偏好设置
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		settingItemUpdate = (SettingItemView) this.findViewById(R.id.setting_item_update);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);//设置配置文件名为config，模式为私有
		boolean isChecked = sharedPreferences.getBoolean("update", false);
		if (isChecked) {
			settingItemUpdate.setChecked(true);
			settingItemUpdate.setDesc("自动升级已经打开");
		}else {
			settingItemUpdate.setChecked(false);
			settingItemUpdate.setDesc("自动升级已经关闭");
		}
		
		settingItemUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sharedPreferences.edit();
				if (settingItemUpdate.isChecked()) {
					settingItemUpdate.setChecked(false);//设置未选中
					settingItemUpdate.setDesc("自动升级已经关闭");
					editor.putBoolean("update", false);
				}else {
					settingItemUpdate.setChecked(true);//设置选中
					settingItemUpdate.setDesc("自动升级已经打开");
					editor.putBoolean("update", true);
				}
				editor.commit();//必须提交，否则无法保存偏好设置
			}
		});
	}

}
