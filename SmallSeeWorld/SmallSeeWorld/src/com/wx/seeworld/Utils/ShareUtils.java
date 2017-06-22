package com.wx.seeworld.Utils;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.moments.WechatMoments.ShareParams;

import com.wx.seeworld.R;
import com.wx.seeworld.Activity.ShareSina;

public class ShareUtils {

	public static final int QQZON = 0;
	public static final int WECOMMENT = 1;
	public static final int SINAWEB = 2;

	public static void getShareSDk(Context mContext, final String Title,
			final String url, String imgUrl) {
		ShareSDK.initSDK(mContext);
		final OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

		// text是分享文本，所有平台都需要这个字段
		oks.setText("世界虽大，你我同在。" + "\n" + "\t" + "\t" + "\t" + "\t" + "\t"
				+ "\t" + "\t" + "\t" + "--小眼看世界");

		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			@Override
			public void onShare(Platform platform,
					cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
				if (platform.getName().equals(SinaWeibo.NAME)) {
					paramsToShare.setText(Title + "         来源：小眼看世界" + url);
				}
			}
		});
		oks.setTitle(Title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(url);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		oks.setImageUrl(imgUrl);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(mContext.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);
		// 启动分享GUI
		oks.show(mContext);
	}

	public static void shareCustomize(int name, final Context context,
			String Title, String url, String imgUrl) {

		if (name == SINAWEB) {
			Intent intent = new Intent(context, ShareSina.class);
			intent.putExtra("Title", Title);
			intent.putExtra("url", url);
			intent.putExtra("imgUrl", imgUrl);
			context.startActivity(intent);
			((Activity) context).overridePendingTransition(R.anim.translate_in,
					R.anim.scale_translate_out);
			return;
		}

		ShareSDK.initSDK(context);
		ShareParams shareParams = new ShareParams();
		Platform platform = null;
		if (name == QQZON) {
			platform = ShareSDK.getPlatform(context, QZone.NAME);
		} else if (name == WECOMMENT) {
			platform = ShareSDK.getPlatform(context, WechatMoments.NAME);
		}
		shareParams.setText("世界虽大，你我同在。" + "\n" + "\t" + "\t" + "\t" + "\t"
				+ "\t" + "\t" + "\t" + "\t" + "--小眼看世界");
		platform.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform platform, int arg1, Throwable arg2) {
				ToastUtils.makeText(context, "分享失败");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				ToastUtils.makeText(context, "分享成功");
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				ToastUtils.makeText(context, "分享取消");
			}
		});

		shareParams.setTitle(Title);
		shareParams.setTitleUrl(url);
		shareParams.setImageUrl(imgUrl);
		shareParams.setUrl(url);
		shareParams.setSite(context.getString(R.string.app_name));
		shareParams.setSiteUrl(url);
		shareParams.shareType = Platform.SHARE_VIDEO;
		platform.share(shareParams);

	}

}
