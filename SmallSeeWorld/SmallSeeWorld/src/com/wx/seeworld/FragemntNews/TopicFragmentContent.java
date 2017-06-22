package com.wx.seeworld.FragemntNews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wx.seeworld.R;
import com.wx.seeworld.Activity.LoadingTopicActivity;
import com.wx.seeworld.Bean.TopicBean;
import com.wx.seeworld.RecyclerView.RecyclerTopicContent;
import com.wx.seeworld.RecyclerView.RecyclerTopicContent.onItemTopicListener;
import com.wx.seeworld.RequestVideo.TopicRequest;
import com.wx.seeworld.Utils.JudgmentIsNetUtils;
import com.wx.seeworld.Utils.SharedPreUtils;

public class TopicFragmentContent extends Fragment implements OnRefreshListener {

	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout refreshLayout;
	private RecyclerTopicContent mRecyclerAdapter;
	private String themeStyle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_topic_content,
				container, false);
		initView(view);
		initData();
		return view;
	}

	private void initView(View view) {
		themeStyle = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		mRecyclerView = (RecyclerView) view
				.findViewById(R.id.recycler_view_topic);
		refreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_refresh_topic);
	}

	private void initData() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
				getActivity(), LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(linearLayoutManager);

		refreshLayout.setColorSchemeResources(
				R.color.refresh_color_blue_shallow,
				R.color.refresh_color_blue_deep,
				R.color.refresh_color_green_deep);
		refreshLayout.setOnRefreshListener(this);

		mRecyclerAdapter = new RecyclerTopicContent(getActivity());
		mRecyclerView.setAdapter(mRecyclerAdapter);

		mRecyclerAdapter.setItemTopicListener(new onItemTopicListener() {
			@Override
			public void onClick(TopicBean topicBean) {
				int networkAvailable = JudgmentIsNetUtils
						.isNetworkAvailable(getActivity());
				if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
					AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
					builder.setTitle("温情");
					builder.setIcon(R.drawable.logo);
					builder.setMessage("亲，你好像没有开网络哦！");
					builder.setPositiveButton("确定", null);
					builder.create().show();
				} else {
					Intent intent = new Intent(getActivity(),LoadingTopicActivity.class);
					intent.putExtra("topic_objectId", topicBean.getObjectId());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
				}
			}
		});

		TopicRequest.AnayleStoregle(getActivity(), refreshLayout,
				mRecyclerAdapter);
	}

	@Override
	public void onRefresh() {
		TopicRequest.requestBmob(getActivity(), refreshLayout, mRecyclerAdapter);
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
			mRecyclerAdapter.refreshView();
		}
	}

}
