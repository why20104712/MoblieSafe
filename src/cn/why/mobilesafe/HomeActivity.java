package cn.why.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private GridView gridView;
	private ImageView gridViewImage;
	private TextView gridViewText;
	private String[] titles = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
			"缓存清理", "高级工具", "设置中心" };
	private int[] images = { R.drawable.image01, R.drawable.image02,
			R.drawable.image03, R.drawable.image04, R.drawable.image05,
			R.drawable.image06, R.drawable.image07, R.drawable.image08,
			R.drawable.image09 };

	private SharedPreferences sharedPreferences;
	private EditText et_setup_pwd;
	private Button ok;
	private Button cancel;
	private EditText et_setup_confirm;
	private AlertDialog dialog;
	private EditText et_enter_pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		gridView = (GridView) findViewById(R.id.gridView);
		CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter();
		gridView.setAdapter(gridViewAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
				switch (position) {
				case 0://手机防盗
					showLostFindDialog();//展示对话框
					break;
				case 8://设置中心
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}
	/**
	 * 展示密码设置对话框
	 */
	protected void showLostFindDialog() {
		if (isSetUpPassword()) {//设置过密码，显示输入密码对话框
			showEnterDialog();
		}else {//没有设置过密码，显示设置密码的对话框
			showSetupPwdDialog();
		}
	}
	/**
	 * 设置密码对话框
	 */
	private void showSetupPwdDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		View dialogView = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
		et_setup_pwd = (EditText) dialogView.findViewById(R.id.et_setup_pwd);
		et_setup_confirm = (EditText) dialogView.findViewById(R.id.et_confirm_pwd);
		ok = (Button) dialogView.findViewById(R.id.btn_ok);
		cancel = (Button) dialogView.findViewById(R.id.btn_cancle);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//取出所输入的密码和确认密码
				String pwd = et_setup_pwd.getText().toString().trim();
				String confirmPwd = et_setup_confirm.getText().toString().trim();
				if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirmPwd)) {
					Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!pwd.equals(confirmPwd)) {
					Toast.makeText(HomeActivity.this, "输入密码不一致", Toast.LENGTH_SHORT).show();
					return;
				}else {
					//保存密码
					Editor edit = sharedPreferences.edit();
					edit.putString("password", pwd);
					edit.commit();
					dialog.dismiss();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.setView(dialogView, 0, 0, 0, 0);
		dialog.show();
	}
	/**
	 * 输入密码对话框
	 */
	private void showEnterDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		View dialogView = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
		et_enter_pwd = (EditText) dialogView.findViewById(R.id.et_setup_pwd);
		ok = (Button) dialogView.findViewById(R.id.btn_ok);
		cancel = (Button) dialogView.findViewById(R.id.btn_cancle);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//取出所输入的密码
				String pwd = et_enter_pwd.getText().toString().trim();
				String savedPwd = sharedPreferences.getString("password", null);
				if (TextUtils.isEmpty(pwd)) {
					Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!pwd.equals(savedPwd)) {
					Toast.makeText(HomeActivity.this, "密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
					et_enter_pwd.setText("");//清空密码
					return;
				}else {
					//进入防盗页面
					//。。。
					dialog.dismiss();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.setView(dialogView,0, 0, 0, 0);
		dialog.show();
	}
	/**
	 * 是否已经设置密码
	 * @return
	 */
	private boolean isSetUpPassword(){
		String password = sharedPreferences.getString("password", null);
		return !TextUtils.isEmpty(password);
	}

	/**
	 * 自定义GridView适配器
	 * 
	 * @author why
	 */
	private class CustomGridViewAdapter extends BaseAdapter {

		public int getCount() {
			return titles.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
			gridViewImage = (ImageView) view.findViewById(R.id.gridViewImage);
			gridViewImage.setBackgroundResource(images[position]);
			gridViewText = (TextView) view.findViewById(R.id.gridViewText);
			gridViewText.setText(titles[position]);
			return view;
		}

	}

}
