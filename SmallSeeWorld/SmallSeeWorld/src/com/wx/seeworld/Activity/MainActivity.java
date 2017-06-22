package com.wx.seeworld.Activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.Fragment.FragmentNews;
import com.wx.seeworld.Fragment.FragmentTopic;
import com.wx.seeworld.Fragment.FragmentUser;
import com.wx.seeworld.Fragment.FragmentVideo;
import com.wx.seeworld.RecyclerView.RecyclerVideoAdapder;
import com.wx.seeworld.Tools.ViewPlayerUI;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.SystemBarTintUtils;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private LinearLayout llBottomNews;
	private LinearLayout llBottomVideo;
	private LinearLayout llBottomTopic;
	private LinearLayout llBottomUser;

	private ImageView bottomIvNews;
	private ImageView bottomIvVideo;
	private ImageView bottomIvTopic;
	private ImageView bottomIvUser;
	private TextView bottomTvNews;
	private TextView bottomTvVideo;
	private TextView bottomTvTopic;
	private TextView bottomTvUser;
	private int bottomTextColor;
	private int bottomTextColordefault;
	private int bottomDrawablenews;
	private int bottomDrawablevideo;
	private int bottomDrawabletopic;
	private int bottomDrawableuser;

	private static final int NEWS = 1;
	private static final int VIDEO = 2;
	private static final int TOPIC = 3;
	private static final int USER = 4;
	private FragmentNews fragmentNews;
	private FragmentVideo fragmentVideo;
	private FragmentTopic fragmentTopic;
	private FragmentUser fragmentUser;
	private String querySelected = null;
	private String themeStyle;
	private LinearLayout llMainBg;
	private LinearLayout llRaido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		themeStyle = SharedPreUtils.getComSharePref(MainActivity.this,
				"theme_style", "day");
		if (themeStyle.equals("night")) {
			setTheme(R.style.MyThemeNight);
		}

		setContentView(R.layout.activity_main);

		initIntentNews();
		initView();
		initData();

		initSystemBarTint();
	}

	private void initIntentNews() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			querySelected = bundle.getString("querySelected");
		}
	}

	private void initView() {

		llBottomNews = (LinearLayout) findViewById(R.id.ll_radio_button_news);
		llBottomVideo = (LinearLayout) findViewById(R.id.ll_radio_button_video);
		llBottomTopic = (LinearLayout) findViewById(R.id.ll_radio_button_topic);
		llBottomUser = (LinearLayout) findViewById(R.id.ll_radio_button_user);

		bottomIvNews = (ImageView) findViewById(R.id.bottom_iv_news);
		bottomIvVideo = (ImageView) findViewById(R.id.bottom_iv_video);
		bottomIvTopic = (ImageView) findViewById(R.id.bottom_iv_topic);
		bottomIvUser = (ImageView) findViewById(R.id.bottom_iv_user);

		bottomTvNews = (TextView) findViewById(R.id.bottom_tv_news);
		bottomTvVideo = (TextView) findViewById(R.id.bottom_tv_video);
		bottomTvTopic = (TextView) findViewById(R.id.bottom_tv_topic);
		bottomTvUser = (TextView) findViewById(R.id.bottom_tv_user);

		llMainBg = (LinearLayout) findViewById(R.id.ll_main_bg);
		llRaido = (LinearLayout) findViewById(R.id.ll_raido);
	}

	private void initData() {
		llBottomNews.setOnClickListener(this);
		llBottomVideo.setOnClickListener(this);
		llBottomTopic.setOnClickListener(this);
		llBottomUser.setOnClickListener(this);

		initBottomStyle();
		restoreStyleDrawable(R.id.bottom_iv_news, llBottomNews);

		initFragment();
	}

	private void initFragment() {
		if (fragmentNews == null) {
			fragmentNews = new FragmentNews(getSupportFragmentManager(),
					querySelected);
		}
		if (fragmentVideo == null) {
			fragmentVideo = new FragmentVideo();
		}
		if (fragmentTopic == null) {
			fragmentTopic = new FragmentTopic();
		}

		if (fragmentUser == null) {
			fragmentUser = new FragmentUser();
		}

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.framelayout_main, fragmentNews, "news");
		transaction.add(R.id.framelayout_main, fragmentVideo, "video");
		transaction.add(R.id.framelayout_main, fragmentTopic, "topic");
		transaction.add(R.id.framelayout_main, fragmentUser, "user");
		transaction.commit();
		SelectedFragment(NEWS);

	}

	private void HideAllFragement() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.hide(fragmentNews);
		transaction.hide(fragmentVideo);
		transaction.hide(fragmentTopic);
		transaction.hide(fragmentUser);
		transaction.commit();
	}

	@SuppressWarnings("static-access")
	private void SelectedFragment(int SelectId) {
		HideAllFragement();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		switch (SelectId) {
		case NEWS:
			transaction.show(fragmentNews);
			break;
		case VIDEO:
			transaction.show(fragmentVideo);
			break;
		case TOPIC:
			transaction.show(fragmentTopic);
			break;
		case USER:
			transaction.show(fragmentUser);
			fragmentUser.ChangeData();
			break;
		}
		transaction.commit();
	}

	private void initBottomStyle() {
		TypedArray typedArray = obtainStyledAttributes(null, R.styleable.bottom);
		bottomTextColor = typedArray.getColor(R.styleable.bottom_bottomTextColor, 0xff0000);
		bottomTextColordefault = typedArray.getColor(R.styleable.bottom_bottomTextColordefault, 0xff0000);

		bottomDrawablenews = typedArray.getResourceId(R.styleable.bottom_bottomDrawablenews, 0);
		bottomDrawablevideo = typedArray.getResourceId(R.styleable.bottom_bottomDrawablevideo, 0);
		bottomDrawabletopic = typedArray.getResourceId(R.styleable.bottom_bottomDrawabletopic, 0);
		bottomDrawableuser = typedArray.getResourceId(R.styleable.bottom_bottomDrawableuser, 0);
		typedArray.recycle(); // 必须回收
	}

	public void onClick(View v) {
		restoreDefaultDrawable(v.getId());
		switch (v.getId()) {
		case R.id.ll_radio_button_news:
			SelectedFragment(NEWS);
			restoreStyleDrawable(R.id.bottom_iv_news, llBottomNews);
			break;
		case R.id.ll_radio_button_video:
			SelectedFragment(VIDEO);
			restoreStyleDrawable(R.id.bottom_iv_video, llBottomVideo);
			break;
		case R.id.ll_radio_button_topic:
			SelectedFragment(TOPIC);
			restoreStyleDrawable(R.id.bottom_iv_topic, llBottomTopic);
			break;
		case R.id.ll_radio_button_user:
			SelectedFragment(USER);
			restoreStyleDrawable(R.id.bottom_iv_user, llBottomUser);
			break;
		}
	}

	// 设置颜色
	private void restoreStyleDrawable(int imgId, LinearLayout llBottom) {
		TextView textView = (TextView) llBottom.getChildAt(1);
		textView.setTextColor(bottomTextColor);
		switch (imgId) {
		case R.id.bottom_iv_news:
			bottomIvNews.setImageResource(bottomDrawablenews);
			break;
		case R.id.bottom_iv_video:
			bottomIvVideo.setImageResource(bottomDrawablevideo);
			break;
		case R.id.bottom_iv_topic:
			bottomIvTopic.setImageResource(bottomDrawabletopic);
			break;
		case R.id.bottom_iv_user:
			bottomIvUser.setImageResource(bottomDrawableuser);
			break;
		}
	}

	// 修改为默认
	private void restoreDefaultDrawable(int i) {
		bottomIvNews.setImageResource(R.drawable.bottom_news_default);
		bottomIvVideo.setImageResource(R.drawable.bottom_video_default);
		bottomIvTopic.setImageResource(R.drawable.bottom_topic_default);
		bottomIvUser.setImageResource(R.drawable.bottom_user_default);

		bottomTvNews.setTextColor(bottomTextColordefault);
		bottomTvTopic.setTextColor(bottomTextColordefault);
		bottomTvVideo.setTextColor(bottomTextColordefault);
		bottomTvUser.setTextColor(bottomTextColordefault);

		if (R.id.ll_radio_button_video != i) {
			if (ViewPlayerUI.mVideoView != null) {
				ViewPlayerUI.StopPlayer();
				RecyclerVideoAdapder.currentPlayer = -1;
				ViewPlayerUI.restoreView();
			}
		}

	}

	@SuppressWarnings("static-access")
	private void initSystemBarTint() {
		SystemBarTintUtils mBarTintUtils = new SystemBarTintUtils(this);
		mBarTintUtils.setNavigationBarTint(bottomTextColor);
		mBarTintUtils.setStatusBarTint(bottomTextColor);
	}

	// 夜间模式
	@Override
	protected void onStart() {
		super.onStart();
		String themeChange = SharedPreUtils.getComSharePref(MainActivity.this,
				"theme_style", "day");
		if (!themeStyle.equals(themeChange)) {
			if (themeChange.equals("day")) {
				setTheme(R.style.MyThemeDefault);
				themeStyle = "day";
			} else {
				setTheme(R.style.MyThemeNight);
				themeStyle = "night";
			}
			setTheme();
		}
	}

	@SuppressWarnings("static-access")
	private void setTheme() {
		TypedArray typedMainText = obtainStyledAttributes(null,R.styleable.maintext);
		int mainBg = typedMainText.getColor(R.styleable.maintext_news_bg,0xffff0000);
		llMainBg.setBackgroundColor(mainBg); // 设置背景色
		typedMainText.recycle(); // 回收

		TypedArray typedTheme = obtainStyledAttributes(null, R.styleable.theme);
		int mainColor = typedTheme.getColor(R.styleable.theme_mainBackgroundColor, 0xffffffff);
		llRaido.setBackgroundColor(mainColor); // 下面组件按钮
		typedTheme.recycle(); // 回收

		initBottomStyle();// 设置底部
		
		TypedArray typedBottom = obtainStyledAttributes(null, R.styleable.bottom);
		
		String userNightMode = SharedPreUtils.getComSharePref(this,"user_night_mode", "false");
		if(userNightMode.equals("false")){
			bottomTvNews.setTextColor(bottomTextColor);
			int newsDrawable = typedBottom.getResourceId(R.styleable.bottom_bottomDrawablenews, 0);
			bottomIvNews.setImageResource(newsDrawable);
		}else{
			SharedPreUtils.setComSharePref(this,"user_night_mode", "false");
			bottomTvUser.setTextColor(bottomTextColor);
			int userDrawable = typedBottom.getResourceId(R.styleable.bottom_bottomDrawableuser, 0);
			bottomIvUser.setImageResource(userDrawable);
		}

		int navigaColor = typedBottom.getColor(R.styleable.bottom_bottomTextColor, 0xffffffff);
		SystemBarTintUtils mBarTintUtils = new SystemBarTintUtils(this);
		mBarTintUtils.setNavigationBarTint(navigaColor);
		mBarTintUtils.setStatusBarTint(navigaColor);
		typedBottom.recycle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (ViewPlayerUI.mVideoView != null) {
			ViewPlayerUI.StopPlayer();
			RecyclerVideoAdapder.currentPlayer = -1;
			ViewPlayerUI.restoreView();
		}
		SharedPreUtils.setComSharePref(this, "mobile_player", "false");// 下次播放提醒
		SharedPreUtils.setComSharePref(this,"user_night_mode", "false");//消除
	}

	private static NightModeListener nightModeListener;

	public interface NightModeListener {
		void onStop();
	}

	public static void setNightModeListener(NightModeListener listener) {
		nightModeListener = listener;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (nightModeListener != null) {
			nightModeListener.onStop();
		}
	}

}
