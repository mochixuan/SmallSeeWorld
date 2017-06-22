package com.wx.seeworld.Activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.moments.WechatMoments.ShareParams;

import com.wx.seeworld.R;
import com.wx.seeworld.CacheResource.ImageLoaderJar;
import com.wx.seeworld.Utils.ToastUtils;

public class ShareSina extends Activity implements OnClickListener {

	private ImageView ivCancel;
	private ImageView ivSend;
	private EditText edSina;
	private String title;
	private String url;
	private String imgUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_sina);
		initView();
	}

	private void initView() {
		title = getIntent().getStringExtra("Title");
		url = getIntent().getStringExtra("url");
		imgUrl = getIntent().getStringExtra("imgUrl");
		ivCancel = (ImageView) findViewById(R.id.btn_share_cancel);
		ivSend = (ImageView) findViewById(R.id.btn_share_send);
		edSina = (EditText) findViewById(R.id.et_sina);
		ImageView ivShareSina=(ImageView) findViewById(R.id.iv_share_sina);
		ivCancel.setOnClickListener(this);
		ivSend.setOnClickListener(this);
		edSina.setText(title+"   来源：小眼看世界---"+url);
		ImageLoaderJar.getImgFromNet(imgUrl,ivShareSina);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share_cancel:
			finish();
			overridePendingTransition(R.anim.translate_in, R.anim.scale_translate_out);
			break;
		case R.id.btn_share_send:
			Send();
			break;
		}
	}

	private void Send() {
		ShareSDK.initSDK(this);
		ShareParams shareParams = new ShareParams();
		Platform platform = ShareSDK.getPlatform(ShareSina.this, SinaWeibo.NAME);
		platform.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform platform, int arg1, Throwable arg2) {
				ToastUtils.makeText(ShareSina.this, "分享失败");
			}
			@Override
			public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
				ToastUtils.makeText(ShareSina.this, "分享成功");
			}
			@Override
			public void onCancel(Platform arg0, int arg1) {
				ToastUtils.makeText(ShareSina.this, "分享取消");
			}
		});
		shareParams.setText(edSina.getText().toString());
		shareParams.setImageUrl(imgUrl);
		platform.share(shareParams);
		finish();
		Toast.makeText(ShareSina.this, "正在分享到新浪微博。。。", Toast.LENGTH_LONG).show();
	}

}
