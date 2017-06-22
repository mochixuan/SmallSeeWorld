package com.wx.seeworld.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.wx.seeworld.R;
import com.wx.seeworld.Activity.MainActivity.NightModeListener;
import com.wx.seeworld.Tools.Screenshot;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.SystemBarTintUtils;

public class maskActivity extends Activity {

	private String currentStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mask);
		
		SharedPreUtils.setComSharePref(this, "user_night_mode", "true");
		initSystemBarTint();
		
		ImageView viewMask=(ImageView) findViewById(R.id.view_mask_mode);
		
		currentStyle = SharedPreUtils.getComSharePref(this, "theme_style", "day");
		if(currentStyle.equals("day")){
			if(Screenshot.dayImgPath()!=null){
				Bitmap bitmap=BitmapFactory.decodeFile(Screenshot.dayImgPath());
				viewMask.setImageBitmap(bitmap);
			}
		}else{
			if(Screenshot.nightImgPath()!=null){
				Bitmap bitmap=BitmapFactory.decodeFile(Screenshot.nightImgPath());
				viewMask.setImageBitmap(bitmap);
			}
		}
		
		MainActivity.setNightModeListener(new NightModeListener() {
			@Override
			public void onStop() {
				finish();
				overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
			}
		});
	}
	
	@SuppressWarnings("static-access")
	private void initSystemBarTint() {
		SystemBarTintUtils mBarTintUtils = new SystemBarTintUtils(this);
		mBarTintUtils.setNavigationBarTint(0x2266ffff);
		mBarTintUtils.setStatusBarTint(0x2266ffff);
	}
	
}
