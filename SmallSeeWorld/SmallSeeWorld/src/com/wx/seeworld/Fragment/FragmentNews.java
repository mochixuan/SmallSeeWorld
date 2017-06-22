package com.wx.seeworld.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gridviewa.DragGridView;
import com.flyco.tablayout.SlidingTabLayout;
import com.wx.seeworld.R;
import com.wx.seeworld.Activity.MainActivity;
import com.wx.seeworld.FragemntNews.FragementMainHots;
import com.wx.seeworld.FragemntNews.NetFragment;
import com.wx.seeworld.NetYINews.NewsJsonTools.NewsJsonTools;
import com.wx.seeworld.RecyclerView.RecyclerNetAdapter;
import com.wx.seeworld.Tools.GridViewTools;
import com.wx.seeworld.Tools.GridViewTools.RefreshTopTitle;
import com.wx.seeworld.Tools.TitleTools;
import com.wx.seeworld.Utils.DisPlayUtils;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.dao.Titledao;

public class FragmentNews extends Fragment {

	private String[] titles = TitleTools.getTitle();
	
	private FragmentManager fragmentManager;
	private ImageView ivAddChannel;
	public static SlidingTabLayout slidingTabLayout;
	private int currentTab = 0;
	private String querySelected;
	private TabFragmentAdapter tabFragmentAdapter;
	private ViewPager viewPager;

	private String themeStyle;

	private LinearLayout llSlideTop;
	
	public static HashMap<String, RecyclerNetAdapter> hashMap=new HashMap<String, RecyclerNetAdapter>();
	
	
	public FragmentNews(FragmentManager fragmentManager, String querySelected) {
		this.fragmentManager = fragmentManager;
		this.querySelected = querySelected;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);
		initView(view);
		initViewPop();
		return view;
	}

	private void initView(View view) {
		
		themeStyle = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");

		slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.flyco_layout);
		viewPager = (ViewPager) view.findViewById(R.id.view_pager_fly);
		ivAddChannel = (ImageView) view.findViewById(R.id.iv_add_channel);
		llSlideTop = (LinearLayout) view.findViewById(R.id.ll_slide_top);
		
		String themeStyle = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		if(themeStyle.equals("night")){
			ivAddChannel.setImageResource(R.drawable.icon_add_channel_night);
		}else{
			ivAddChannel.setImageResource(R.drawable.icon_add_channel);
		}
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		initHashMap();

		for (int i = 0; i < titles.length; i++) {
			
			if(Titledao.querySelectedString(getActivity(), "教训")==i || Titledao.querySelectedString(getActivity(), "美女") == i){
				Fragment fragment = new FragementMainHots();
				Bundle bundle = new Bundle();
				bundle.putString("text", titles[i]);
				fragment.setArguments(bundle);
				fragments.add(fragment);
			}else{
				NetFragment fragment = null;
				if (Titledao.querySelectedString(getActivity(), "头条") == i) {
					fragment = new NetFragment(NewsJsonTools.HEADLINES,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.HEADLINES);
				}else if(Titledao.querySelectedString(getActivity(), "原创") == i){
					fragment = new NetFragment(NewsJsonTools.ORIGNAL,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.ORIGNAL);
				}else if(Titledao.querySelectedString(getActivity(), "互联") == i){
					fragment = new NetFragment(NewsJsonTools.MOBILEINTERNET,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.MOBILEINTERNET);
				}else if(Titledao.querySelectedString(getActivity(), "NBA") == i){
					fragment = new NetFragment(NewsJsonTools.NBA,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.NBA);
				}else if(Titledao.querySelectedString(getActivity(), "体育") == i){
					fragment = new NetFragment(NewsJsonTools.SPORT,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.SPORT);
				}else if(Titledao.querySelectedString(getActivity(), "娱乐") == i){
					fragment = new NetFragment(NewsJsonTools.ENTERTAINMENT,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.ENTERTAINMENT);
				}else if(Titledao.querySelectedString(getActivity(), "手机") == i){
					fragment = new NetFragment(NewsJsonTools.PHONE,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.PHONE);
				}else if(Titledao.querySelectedString(getActivity(), "科技") == i){
					fragment = new NetFragment(NewsJsonTools.SCIENCETECHNOLOGY,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.SCIENCETECHNOLOGY);
				}else if(Titledao.querySelectedString(getActivity(), "旅游") == i){
					fragment = new NetFragment(NewsJsonTools.TROMIST,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.TROMIST);
				}else if(Titledao.querySelectedString(getActivity(), "财经") == i){
					fragment = new NetFragment(NewsJsonTools.FINANCE,viewPager);
					JugdementInitdata(i, fragment, NewsJsonTools.FINANCE);
				}
				Bundle bundle = new Bundle();
				bundle.putString("text", titles[i]);
				fragment.setArguments(bundle);
				fragments.add(fragment);
			}

		}

		tabFragmentAdapter = new TabFragmentAdapter(fragments, titles,
				fragmentManager, getActivity());
		viewPager.setAdapter(tabFragmentAdapter);
		slidingTabLayout.setViewPager(viewPager);

		if (querySelected == null) {
			slidingTabLayout.setCurrentTab(0);
		} else {
			currentTab = Titledao.querySelectedString(getActivity(),querySelected);
			slidingTabLayout.setCurrentTab(currentTab);
		}
		
		
	}
	
	//判断初始化数据
	public void JugdementInitdata(int position,NetFragment netFragment,String mCid){
		if(slidingTabLayout.getCurrentTab()==position){
			SharedPreUtils.setComSharePref(getActivity(), "loading_init_data", "true");
		}
	}

	// 查询
	private void initHashMap() {
		HashMap<String, Object> hashMap = Titledao.queryTitleDao(getActivity());
		if (hashMap.size() != 0) {
			titles = null;
			titles = new String[hashMap.size()];
			for (int i = 0; i < hashMap.size(); i++) {
				String hashValue = (String) hashMap.get("" + i);
				titles[i] = hashValue;
			}
		} else {
			String first = SharedPreUtils.getSharePref(getActivity(),
					"init_data_first", "first");
			if (first.equals("first")) {
				HashMap<String, Object> hashMapChange = new HashMap<String, Object>();
				for (int i = 0; i < titles.length; i++) {
					hashMapChange.put("" + i, titles[i]);
				}
				Titledao.addTitleDao(getActivity(), hashMapChange);
				SharedPreUtils.setSharePref(getActivity(), "init_data_first",
						"second");
			}
		}

	}

	@SuppressWarnings("static-access")
	private void initViewPop() {
		DisPlayUtils disPlayUtils = new DisPlayUtils(getActivity());
		int windowsHight = disPlayUtils.getWindowsHight();
		View viewchannel = View.inflate(getActivity(), R.layout.item_gridview,null);
		DragGridView mGridView = (DragGridView) viewchannel
				.findViewById(R.id.drag_GridView);
		new GridViewTools(titles, viewchannel, mGridView, getActivity(),
				ivAddChannel, slidingTabLayout, windowsHight);
		GridViewTools.setRefreshTopTitle(new RefreshTopTitle() {
			private Intent intent;

			public void refresh() {
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.translate_in,
						R.anim.translate_out);
				getActivity().finish();
			}

			public void AddClick() {
				currentTab = slidingTabLayout.getCurrentTab();
				querySelected = Titledao.querySelectedId(getActivity(),currentTab);
				intent = new Intent(getActivity(), MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("querySelected", querySelected);
				intent.putExtras(bundle);
			}
		});
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		String themeChange = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		if(! themeStyle.equals(themeChange)){
			if(themeChange.equals("day")){
				getActivity().setTheme(R.style.MyThemeDefault);
				themeStyle="day";
				ivAddChannel.setImageResource(R.drawable.icon_add_channel);
			}else{
				getActivity().setTheme(R.style.MyThemeNight);
				themeStyle="night";
				ivAddChannel.setImageResource(R.drawable.icon_add_channel_night);
			}
			setTheme();
		}
	}

	private void setTheme() {
		TypedArray typedTheme = getActivity().obtainStyledAttributes(null, R.styleable.theme);
		int mainColor = typedTheme.getColor(R.styleable.theme_mainBackgroundColor, 0xffffffff);
		llSlideTop.setBackgroundColor(mainColor); //下面组件按钮
		slidingTabLayout.setBackgroundColor(mainColor); 
		typedTheme.recycle();	//回收
		
		TypedArray typedBottom = getActivity().obtainStyledAttributes(null, R.styleable.bottom);
		int topselectedColor = typedBottom.getColor(R.styleable.bottom_topIndicator, 0xffffffff);
		int topselectedTextColor=typedBottom.getColor(R.styleable.bottom_bottomTextColor, 0xffffffff);
		slidingTabLayout.setIndicatorColor(topselectedColor);//被选中背景
		slidingTabLayout.setTextSelectColor(topselectedTextColor);
		typedBottom.recycle();
	}
	

}
