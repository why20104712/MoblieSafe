package cn.why.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends Activity {

	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
	}
	
	public void showNext(View view) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent = new Intent(this,LostFindActivity.class);
		startActivity(intent);
		finish();
	}

	public void showPre(View view) {
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
	}

}
