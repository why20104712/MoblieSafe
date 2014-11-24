package cn.why.mobilesafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import cn.why.mobilesafe.utils.StreamTools;
import cn.whymobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{

	protected static final int URL_ERROR = 0;
	protected static final int URLCONNECTION_ERROR = 1;
	protected static final int JSON_ERROR = 2;
	protected static final int ENTER_HOME = 3;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ENTER_HOME:
				handler.postDelayed(new Runnable() {
					public void run() {
						enterHome();//进入主页
					}
				}, 3000);//延迟3秒
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_view);
		initView();
	}
	/**
	 * 初始化view
	 */
	private void initView() {
		//检查版本信息，是否有新版，有新版本则升级
		checkUpdate();
	}
	/**
	 * 检查更新
	 */
	private void checkUpdate() {

		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
					URL url = new URL(getString(R.string.server_url));
					try {
						HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
						urlConnection.setRequestMethod("GET");
						urlConnection.setConnectTimeout(4000);
						int code = urlConnection.getResponseCode();
						if (code == 200) {
							InputStream inputStream = urlConnection.getInputStream();
							String result = StreamTools.readFromStream(inputStream);
							JSONObject jsonObject = new JSONObject(result);
							String version = (String) jsonObject.get("version");//获取服务器上的版本信息
							if (getAppVersion().equals(version)) {
//								handler.sendEmptyMessageDelayed(0, 3000);//延迟3秒进入主页
								msg.what = ENTER_HOME;
							}else {
								//下载版本
								System.out.println("需要更新版本！！！");
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
				}finally{
					handler.sendMessage(msg);
				}
			};
		}.start();
	}
	/**
	 * 得到版本号
	 * @return
	 */
	public String getAppVersion(){
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			//不可能发生；
			return "";
		}
	}
	/**
	 * 进入主页
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();

	}
}
