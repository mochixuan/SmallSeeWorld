package com.wx.seeworld.Fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.Activity.CollectActivity;
import com.wx.seeworld.Activity.SignPageActivity;
import com.wx.seeworld.Activity.maskActivity;
import com.wx.seeworld.Bean.collect;
import com.wx.seeworld.CacheResource.ImageLoaderJar;
import com.wx.seeworld.Tools.Screenshot;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.View.CircleImageView;
import com.wx.seeworld.dao.Collectdao;

public class FragmentUser extends Fragment {

	private static TextView tvUserNumber2;
	private LinearLayout llUserNumber2;
	private static Context mContext;
	private RelativeLayout rlNightMode;
	private ImageView ivNight;
	private ImageView ivNightBotton;
	private String themeStyle;
	private RelativeLayout rlUserBg;
	private LinearLayout llUserMainBg;
	private RelativeLayout rlUserMessage;
	private RelativeLayout rlUserNightMode;
	private ImageView ivMessage;
	private TextView tvUserName;
	private CircleImageView circleImageView;
	private LinearLayout llThreeNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user, container, false);

		initView(view);
		initData();
		return view;
	}

	@SuppressLint("CutPasteId")
	private void initView(View view) {

		themeStyle = SharedPreUtils.getComSharePref(getActivity(),
				"theme_style", "day");

		llUserNumber2 = (LinearLayout) view.findViewById(R.id.ll_user_number2);
		tvUserNumber2 = (TextView) view.findViewById(R.id.tv_user_number2);
		mContext = getActivity();

		rlNightMode = (RelativeLayout) view
				.findViewById(R.id.rl_user_night_mode);
		ivNight = (ImageView) view.findViewById(R.id.iv_user_nigh);
		ivNightBotton = (ImageView) view.findViewById(R.id.iv_user_night_mode);

		rlUserBg = (RelativeLayout) view.findViewById(R.id.rl_user_bg);
		llUserMainBg = (LinearLayout) view.findViewById(R.id.ll_user_bg);

		llThreeNumber = (LinearLayout) view.findViewById(R.id.ll_three_number);

		rlUserMessage = (RelativeLayout) view
				.findViewById(R.id.rl_user_message);
		rlUserNightMode = (RelativeLayout) view
				.findViewById(R.id.rl_user_night_mode);
		ivMessage = (ImageView) view.findViewById(R.id.iv_user_message);
		tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
		circleImageView = (CircleImageView) view.findViewById(R.id.circlr_view);
	}

	private void initData() {

		llUserNumber2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CollectActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.translate_in,
						R.anim.translate_out);
			}
		});

		rlNightMode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String currentStyle = SharedPreUtils.getComSharePref(
						getActivity(), "theme_style", "day");

				if (currentStyle.equals("day")) {
					Screenshot.GetandSaveCurrentImage(getActivity(), "day_mode");
				} else {
					Screenshot.GetandSaveCurrentImage(getActivity(),"night_mode");
				}

				Intent intent = new Intent(getActivity(), maskActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.translate_in,
						R.anim.translate_out);

				if (currentStyle.equals("day")) {
					setThemeNight();
				} else {
					setThemeDay();
				}

			}
		});

		tvUserName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SignPageActivity.class);
				getActivity().startActivity(intent);
			}
		});

	}

	public static void ChangeData() {
		List<collect> querySelectedList = Collectdao
				.querySelectedList(mContext);
		tvUserNumber2.setText(querySelectedList.size() + "");
	}

	private void setThemeDay() {
		TypedArray typedUserTheme = getActivity().obtainStyledAttributes(null,
				R.styleable.usertheme);
		int userBgColor = typedUserTheme.getColor(
				R.styleable.usertheme_user_bg, 0xffffffff);
		int userMessageColor = typedUserTheme.getResourceId(
				R.styleable.usertheme_user_message, 0);
		int userNameColor = typedUserTheme.getColor(
				R.styleable.usertheme_textcolorname, 0xffffffff);
		tvUserName.setTextColor(userNameColor);
		circleImageView.setBorderColor(userNameColor);
		ivMessage.setImageResource(userMessageColor);
		rlUserBg.setBackgroundColor(userBgColor);

		TypedArray typedMainText = getActivity().obtainStyledAttributes(null,
				R.styleable.maintext);
		int cardbgcolor = typedMainText.getColor(
				R.styleable.maintext_cardbackground, 0xffffffff);
		int newsBg = typedMainText.getColor(R.styleable.maintext_news_bg,
				0xffffffff);
		llUserMainBg.setBackgroundColor(cardbgcolor);
		llThreeNumber.setBackgroundColor(newsBg);
		rlUserMessage.setBackgroundColor(newsBg);
		rlUserNightMode.setBackgroundColor(newsBg);

		ivNight.setImageResource(R.drawable.menu_day_night1);
		ivNightBotton.setImageResource(R.drawable.switch_day_mode);
		SharedPreUtils.setComSharePref(getActivity(), "theme_style", "day");

		typedUserTheme.recycle();
		typedMainText.recycle();
	}

	private void setThemeNight() {
		TypedArray typedUserTheme = getActivity().obtainStyledAttributes(null,
				R.styleable.usertheme);
		int userBgColor = typedUserTheme.getColor(
				R.styleable.usertheme_user_bg, 0xffffffff);
		int userMessageColor = typedUserTheme.getResourceId(
				R.styleable.usertheme_user_message, 0);
		int userNameColor = typedUserTheme.getColor(
				R.styleable.usertheme_textcolorname, 0xffffffff);
		tvUserName.setTextColor(userNameColor);
		circleImageView.setBorderColor(userNameColor);
		ivMessage.setImageResource(userMessageColor);
		rlUserBg.setBackgroundColor(userBgColor);

		TypedArray typedMainText = getActivity().obtainStyledAttributes(null,
				R.styleable.maintext);
		int cardbgcolor = typedMainText.getColor(
				R.styleable.maintext_cardbackground, 0xffffffff);
		int newsBg = typedMainText.getColor(R.styleable.maintext_news_bg,
				0xffffffff);
		llUserMainBg.setBackgroundColor(cardbgcolor);
		llThreeNumber.setBackgroundColor(newsBg);
		rlUserMessage.setBackgroundColor(newsBg);
		rlUserNightMode.setBackgroundColor(newsBg);

		ivNight.setImageResource(R.drawable.menu_day_night2);
		ivNightBotton.setImageResource(R.drawable.switch_night_mode);
		SharedPreUtils.setComSharePref(getActivity(), "theme_style", "night");

		typedUserTheme.recycle();
		typedMainText.recycle();
	}

	@Override
	public void onStart() {
		super.onStart();

		String userName = SharedPreUtils.getComSharePref(getActivity(),
				"user_name", "");
		String userImg = SharedPreUtils.getComSharePref(getActivity(),
				"user_name_img", "");
		if (!userName.equals("") && !userImg.equals("")) {
			tvUserName.setText(userName);
			ImageLoaderJar.getImgFromNetCircle(userImg, circleImageView);
		}

		String themeChange = SharedPreUtils.getComSharePref(getActivity(),
				"theme_style", "day");
		if (!themeStyle.equals(themeChange)) {
			if (themeChange.equals("day")) {
				themeStyle = "day";
				setThemeDay();
			} else {
				themeStyle = "night";
				setThemeNight();
			}
		}
	}

}
