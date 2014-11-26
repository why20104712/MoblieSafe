package cn.why.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {

	private GridView gridView;
	private ImageView gridViewImage;
	private TextView gridViewText;
	private String[] titles = { "手机卫士", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
			"缓存清理", "高级工具", "设置中心" };
	private int[] images = { R.drawable.image01, R.drawable.image02,
			R.drawable.image03, R.drawable.image04, R.drawable.image05,
			R.drawable.image06, R.drawable.image07, R.drawable.image08,
			R.drawable.image09 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gridView = (GridView) findViewById(R.id.gridView);
		CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter();
		gridView.setAdapter(gridViewAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
				switch (position) {
				case 8://设置中心
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
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
			View view = View.inflate(HomeActivity.this, R.layout.grid_view_list_item, null);
			gridViewImage = (ImageView) view.findViewById(R.id.gridViewImage);
			gridViewImage.setBackgroundResource(images[position]);
			gridViewText = (TextView) view.findViewById(R.id.gridViewText);
			gridViewText.setText(titles[position]);
			return view;
		}

	}

}
