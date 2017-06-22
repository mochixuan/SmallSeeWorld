package com.wx.seeworld.Fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wx.seeworld.R;
import com.wx.seeworld.Bean.VideoDemo;
import com.wx.seeworld.RecyclerView.RecyclerVideoAdapder;
import com.wx.seeworld.RecyclerView.RecyclerVideoAdapder.onItemListener;
import com.wx.seeworld.RequestVideo.VideoUrl;
import com.wx.seeworld.Utils.JudgmentIsNetUtils;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.ToastUtils;

public class FragmentVideo extends Fragment implements OnRefreshListener {
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mRefreshLayout;
	private RecyclerVideoAdapder videoAdapder;
	private static final int HOLDER_BASE = 1001;
	private static final int HOLDER_FOOTER = 1002;
	private String themeStyle;
	private LinearLayout llVideobg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video, container, false);
		initView(view);
		initData();
		return view;
	}

	private void initView(View view) {
		
		themeStyle = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		
		mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_video);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_video);
		llVideobg = (LinearLayout) view.findViewById(R.id.ll_video);
	}

	private void initData() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
				getActivity(), LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		videoAdapder = new RecyclerVideoAdapder(getActivity());
		mRecyclerView.setAdapter(videoAdapder);

		// 设置刷新
		mRefreshLayout.setColorSchemeResources(
				R.color.refresh_color_blue_shallow,
				R.color.refresh_color_blue_deep,
				R.color.refresh_color_green_deep);
		TypedArray typeTheme = getActivity().obtainStyledAttributes(null,R.styleable.theme);
		int backgroundColor = typeTheme.getColor(R.styleable.theme_mainBackgroundColor, 0xffffffff);
		mRefreshLayout.setProgressBackgroundColorSchemeColor(backgroundColor);
		typeTheme.recycle();
		mRefreshLayout.setOnRefreshListener(this);

		visibleData();

		// 监听处理
		videoAdapder.setItemListener(new onItemListener() {
			@Override
			public void onClickShare() {
				int networkAvailable = JudgmentIsNetUtils
						.isNetworkAvailable(getActivity());
				if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
					ToastUtils.makeText(getActivity(), "未连接网络");
				}
				VideoUrl videoUrl = new VideoUrl(getActivity(),
						mRefreshLayout, videoAdapder, HOLDER_FOOTER,
						videoAdapder);
				videoUrl.getVideoUrl();
			}
		});

	}

	private void visibleData() {
		String videodata = SharedPreUtils.getComSharePref(getActivity(),
				"video_data", "");
		if (videodata.equals("")) {
			int networkAvailable = JudgmentIsNetUtils
					.isNetworkAvailable(getActivity());
			if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
				return;
			}
			VideoUrl videoUrl = new VideoUrl(getActivity(),
					mRefreshLayout, videoAdapder, HOLDER_BASE, videoAdapder);
			videoUrl.getVideoUrl();
		} else {
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonArray Jarray = parser.parse(videodata).getAsJsonArray();
			List<VideoDemo> videoDemos = new ArrayList<VideoDemo>();
			for (int i = 0; i < Jarray.size(); i++) {
				JsonElement jsonElement = Jarray.get(i);
				VideoDemo videoDemo = gson.fromJson(jsonElement,
						VideoDemo.class);
				videoDemos.add(videoDemo);
			}
			videoAdapder.addData(videoDemos, HOLDER_BASE);
		}

	}

	@Override
	public void onRefresh() {
		mRefreshLayout.setRefreshing(true);
		int networkAvailable = JudgmentIsNetUtils.isNetworkAvailable(getActivity());
		if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
			ToastUtils.makeText(getActivity(), "未连接网络");
		}
		VideoUrl videoUrl = new VideoUrl(getActivity(),mRefreshLayout,videoAdapder, HOLDER_BASE, videoAdapder);
		videoUrl.getVideoUrl();
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		String themeChange = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		if(! themeStyle.equals(themeChange)){
			if(themeChange.equals("day")){
				themeStyle="day";
			}else{
				themeStyle="night";
			}
			
			TypedArray typedUserTheme = getActivity().obtainStyledAttributes(null, R.styleable.usertheme);
			int secondBackground = typedUserTheme.getColor(R.styleable.usertheme_second_background, 0xffffffff);
			llVideobg.setBackgroundColor(secondBackground);
			typedUserTheme.recycle();
			
			videoAdapder.refreshTheme();
		}
	}


}
