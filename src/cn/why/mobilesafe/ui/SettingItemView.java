package cn.why.mobilesafe.ui;

import cn.why.mobilesafe.R;
import android.R.bool;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private CheckBox cb_status;
	private TextView tv_title;
	private TextView tv_desc;
	private String descOn;
	private String descOff;
	private String title;
	/**
	 * 初始化view
	 * @param context
	 */
	private void initView(Context context) {
		View view = View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) view.findViewById(R.id.cb_status);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_desc = (TextView) view.findViewById(R.id.tv_desc);
		tv_title.setText("设置自动升级");
		setDesc(descOff);
	}
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		title = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.why.mobilesafe", "desc_title");
		descOn = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.why.mobilesafe", "desc_title");
		descOff = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.why.mobilesafe", "desc_title");
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * checkbox是否选中
	 * @return
	 */
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	/**
	 * 设置选中
	 */
	public void setChecked(boolean checked){
		if (checked) {
			tv_desc.setText(descOn);
		}else {
			tv_desc.setText(descOff);
		}
		cb_status.setChecked(checked);
	}
	/**
	 * 设置描述信息
	 */
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}
}
