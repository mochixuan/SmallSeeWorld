package com.wx.seeworld.Fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.bmob.v3.Bmob;

import com.flyco.tablayout.SlidingTabLayout;
import com.wx.seeworld.R;
import com.wx.seeworld.CacheResource.ImageLoaderJar;
import com.wx.seeworld.FragemntNews.TopicFragmentContent;
import com.wx.seeworld.FragemntNews.TopicFragmentPublication;
import com.wx.seeworld.Tools.TitleTools;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.View.CircleImageView;

public class FragmentTopic extends Fragment {

	private SlidingTabLayout slidingTabLayout;
	private ViewPager mViewPager;
	private String[] titles = TitleTools.getTopic();
	private CircleImageView circleImageView;
	private String themeStyle;
	private RelativeLayout rlTopicTop;
	private LinearLayout llTopic;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_topic, container,false);
		Bmob.initialize(getActivity(), "2c78fca2ab44917b363e9656cb2e4563");
		initView(view);
		initData();
		return view;
	}

	private void initView(View view) {
		themeStyle = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.flyco_layout_topic);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager_topic);
		circleImageView = (CircleImageView) view.findViewById(R.id.circlr_view_topic);
		rlTopicTop = (RelativeLayout) view.findViewById(R.id.rl_topic_top);
		llTopic = (LinearLayout) view.findViewById(R.id.ll_topic);
	}
	
	private void initData() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		TopicFragmentContent fragmentContent=new TopicFragmentContent();
		TopicFragmentPublication fragmentPublication=new TopicFragmentPublication();
		fragments.add(fragmentContent);
		fragments.add(fragmentPublication);
		TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(fragments, titles,
				getActivity().getSupportFragmentManager(), getActivity());
		mViewPager.setAdapter(tabFragmentAdapter);
		slidingTabLayout.setViewPager(mViewPager);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		String userImg = SharedPreUtils.getComSharePref(getActivity(),"user_name_img", "");
		if (!userImg.equals("")) {
			ImageLoaderJar.getImgFromNetCircle(userImg, circleImageView);
		}
		
		String themeChange = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		if(! themeStyle.equals(themeChange)){
			if(themeChange.equals("day")){
				themeStyle="day";
			}else{
				themeStyle="night";
			}
			
			TypedArray typedUserTheme = getActivity().obtainStyledAttributes(null, R.styleable.usertheme);
			int textColorName = typedUserTheme.getColor(R.styleable.usertheme_textcolorname, 0xffffffff);
			slidingTabLayout.setIndicatorColor(textColorName);//被选中背景
			slidingTabLayout.setTextSelectColor(textColorName);
			circleImageView.setBorderColor(textColorName);
			int userBg = typedUserTheme.getColor(R.styleable.usertheme_user_bg1, 0xffffffff);
			rlTopicTop.setBackgroundColor(userBg);
			
			int secondBackground = typedUserTheme.getColor(R.styleable.usertheme_second_background, 0xffffffff);
			llTopic.setBackgroundColor(secondBackground);
			
			TypedArray typedBottom = getActivity().obtainStyledAttributes(null, R.styleable.bottom);
			int textcolordefault= typedBottom.getColor(R.styleable.bottom_bottomTextColordefault, 0xffffffff);
			slidingTabLayout.setTextUnselectColor(textcolordefault);
			
			typedUserTheme.recycle();
			typedBottom.recycle();
			
		}
		
	}
	
	
}
