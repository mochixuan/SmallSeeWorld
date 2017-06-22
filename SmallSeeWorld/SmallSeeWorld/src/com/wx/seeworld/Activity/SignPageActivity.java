package com.wx.seeworld.Activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import com.wx.seeworld.R;
import com.wx.seeworld.Fragment.FragmentUser;
import com.wx.seeworld.Utils.EncryptionUtils;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.View.CircleImageView;

public class SignPageActivity extends Activity implements OnClickListener {

	private ImageView ivQQ;
	private ImageView ivWeiChat;
	private ImageView ivSina;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_page);
		ShareSDK.initSDK(this);
		initView();
	}

	private void initView() {
		ivQQ = (ImageView) findViewById(R.id.user_qq_sign);
		ivWeiChat = (ImageView) findViewById(R.id.user_weichat_sign);
		ivSina = (ImageView) findViewById(R.id.user_sina_sign);
		ivQQ.setOnClickListener(this);
		ivWeiChat.setOnClickListener(this);
		ivSina.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_qq_sign:
			Platform qq = ShareSDK.getPlatform(QZone.NAME);
			if (qq.isValid()) {
				qq.removeAccount();
			}
			qq.setPlatformActionListener(paListener);
			qq.SSOSetting(false); // 设置false表示使用SSO授权方式
			qq.showUser(null);
			Toast.makeText(SignPageActivity.this, "加载中·。。。。", Toast.LENGTH_LONG).show();
			break;
		case R.id.user_weichat_sign:
			Toast.makeText(SignPageActivity.this, "权限问题没开发", Toast.LENGTH_LONG).show();
			break;
		case R.id.user_sina_sign:
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			if (weibo.isValid()) {
				weibo.removeAccount();
			}
			weibo.setPlatformActionListener(paListener);
			weibo.SSOSetting(false); // 设置false表示使用SSO授权方式
			weibo.showUser(null);
			Toast.makeText(SignPageActivity.this, "加载中·。。。。", Toast.LENGTH_LONG).show();
			break;
		}
	}

	private PlatformActionListener paListener = new PlatformActionListener() {

		@Override
		public void onError(Platform platform, int arg1, Throwable arg2) {
			Toast.makeText(SignPageActivity.this, "授权失败", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onComplete(Platform platform, int arg1,
				HashMap<String, Object> res) {
			setonComplete(platform, res);
		}

		@Override
		public void onCancel(Platform platform, int arg1) {
			Toast.makeText(SignPageActivity.this, "取消授权", Toast.LENGTH_LONG).show();
		}
	};

	protected void setonComplete(Platform platform, HashMap<String, Object> res) {
		// 解析部分用户资料字段
		String userId = null, userName = null, imgUrl = null;
		if (platform.getName().equals(QZone.NAME)) {
			userName = res.get("nickname").toString(); 				// 用户名
			imgUrl = res.get("figureurl_qq_2").toString(); 				// 头像链接
			userId = platform.getDb().getUserId();							// ID
		} else if (platform.getName().equals(SinaWeibo.NAME)) {
			userId = res.get("id").toString(); 									
			userId=EncryptionUtils.setEncryptionMD5(userId);		// ID
			userName = res.get("name").toString(); 						// 用户名
			imgUrl = res.get("profile_image_url").toString(); 		// 头像链接
		}
		SharedPreUtils.setComSharePref(SignPageActivity.this, "user_name", userName);
		SharedPreUtils.setComSharePref(SignPageActivity.this, "user_name_img", imgUrl);
		SharedPreUtils.setComSharePref(SignPageActivity.this, "user_Id", userId);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK();
	}

}
