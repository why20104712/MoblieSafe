package cn.why.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import cn.why.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {

	protected static final int URL_ERROR = 0;
	protected static final int URLCONNECTION_ERROR = 1;
	protected static final int JSON_ERROR = 2;
	protected static final int ENTER_HOME = 3;
	protected static final int UPDATE_APP = 4;
	private String apkUrl;
	
	private TextView downloadProgress;
	private SharedPreferences sharedPreferences;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Editor editor = sharedPreferences.edit();
			switch (msg.what) {
			case ENTER_HOME:
				handler.postDelayed(new Runnable() {
					public void run() {
						enterHome();// 进入主页
					}
				}, 3000);// 延迟3秒
				break;
			case UPDATE_APP:
				// 展示更新app对话框
				showUpdateDialog();
				break;
			}
		}
	};

	/**
	 * 展示升级对话框
	 */
	private void showUpdateDialog() {
		AlertDialog.Builder updateDialog = new AlertDialog.Builder(
				SplashActivity.this);
		updateDialog.setTitle("更新提示");
		// updateDialog.setCancelable(false);//强制更新
		updateDialog.setMessage("有新版本里，赶紧去下载吧");
		/**
		 * 取消更新按钮触发的事件
		 */
		updateDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				enterHome();
			}
		});
		updateDialog.setPositiveButton("下载更新", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 判断外部存储卡是否已经挂载
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkUrl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/MobileSafe2.0.apk",
							new AjaxCallBack<File>() {
								/**
								 * 下载失败
								 */
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									Toast.makeText(getApplicationContext(), "下载出错", 1).show();
									super.onFailure(t, errorNo, strMsg);
								}
								@Override
								/**
								 * 下载过程中显示下载进度
								 */
								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									int progress = (int) (current * 100 / count);
									downloadProgress.setText("已经下载"+progress+"%");
								}
								@Override
								/**
								 * 下载成功
								 */
								public void onSuccess(File t) {
									super.onSuccess(t);
									//下载成功，进行安装
									installApk(t);
								}
								/**
								 * 安装apk
								 * @param t
								 */
								private void installApk(File t) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
									startActivity(intent);
								}
							});
				}else {
					Toast.makeText(getApplicationContext(), "外部sdcard未挂载，请挂载后重试",	0).show();
					return;
				}
			}
		});
		updateDialog.setNegativeButton("下次再说", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();// 进入主页
			}
		});
		updateDialog.show();//显示提示对话框
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		downloadProgress = (TextView) findViewById(R.id.progress);
		initView();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		//查看偏好设置
		boolean isChecked = sharedPreferences.getBoolean("update", false);
		if (isChecked) {
			// 检查版本信息，是否有新版，有新版本则升级
			checkUpdate();
		}else {
			//延迟2秒进入主页
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}
	}

	/**
	 * 检查更新
	 */
	private void checkUpdate() {

		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					URL url = new URL(getString(R.string.server_url));
					try {
						HttpURLConnection urlConnection = (HttpURLConnection) url
								.openConnection();
						urlConnection.setRequestMethod("GET");
						urlConnection.setConnectTimeout(4000);
						int code = urlConnection.getResponseCode();
						if (code == 200) {
							InputStream inputStream = urlConnection
									.getInputStream();
							String result = StreamTools
									.readFromStream(inputStream);
							JSONObject jsonObject = new JSONObject(result);
							String version = (String) jsonObject.get("version");// 获取服务器上的版本信息
							apkUrl = (String) jsonObject.get("apkUrl");
							if (getAppVersion().equals(version)) {
								// handler.sendEmptyMessageDelayed(0,
								// 3000);//延迟3秒进入主页
								msg.what = ENTER_HOME;
							} else {
								// 下载版本
								System.out.println("需要更新版本！！！");
								msg.what = UPDATE_APP;
							}
						}
					} catch (IOException e) {
						msg.what = URLCONNECTION_ERROR;
						e.printStackTrace();
					} catch (JSONException e) {
						msg.what = JSON_ERROR;
						e.printStackTrace();
					}
				} catch (MalformedURLException e) {
					msg.what = URL_ERROR;
					e.printStackTrace();
				} finally {
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * 得到版本号
	 * 
	 * @return
	 */
	public String getAppVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// 不可能发生；
			return "";
		}
	}

	/**
	 * 进入主页
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();

	}
}
