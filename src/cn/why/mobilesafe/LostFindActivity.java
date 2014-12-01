package cn.why.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LostFindActivity extends Activity {

	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = sharedPreferences.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
		}else {
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
		}
	}
}
