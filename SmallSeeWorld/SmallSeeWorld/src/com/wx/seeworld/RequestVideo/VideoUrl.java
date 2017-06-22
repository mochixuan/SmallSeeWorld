package com.wx.seeworld.RequestVideo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wx.seeworld.Bean.VideoDemo;
import com.wx.seeworld.RecyclerView.RecyclerVideoAdapder;
import com.wx.seeworld.Tools.VideoDataTools;
import com.wx.seeworld.Tools.VolleyTools;
import com.wx.seeworld.Utils.SharedPreUtils;

public class VideoUrl {

	private Context context;
	private SwipeRefreshLayout mRefreshLayout;
	private RecyclerVideoAdapder videoAdapder;
	private int LOADINGMORE;
	private static final int HOLDER_BASE = 1001;
	private static final int HOLDER_FOOTER = 1002;
	private RecyclerVideoAdapder mVideoAdapder;

	public VideoUrl(Context context,
			SwipeRefreshLayout mRefreshLayout,
			RecyclerVideoAdapder videoAdapder, int loadingmore,
			RecyclerVideoAdapder mVideoAdapder) {
		this.context = context;
		this.mRefreshLayout = mRefreshLayout;
		this.videoAdapder = videoAdapder;
		LOADINGMORE = loadingmore;
		this.mVideoAdapder = mVideoAdapder;
	}

	public void getVideoUrl() {
		String url = "http://newapi.meipai.com/output/channels_topics_timeline.json?id="
				+ VideoDataTools.getVideoId(context)+"&count="+6;
		RequestQueue queue = VolleyTools.getRequestQueue(context);
		final StringRequest request = new StringRequest(Request.Method.GET,
				url, new Listener<String>() {
					@Override
					public void onResponse(String result) {
						dealtData(result);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT)
								.show();
						mRefreshLayout.setRefreshing(false);
						VolleyTools.cancelRequest("VolleyTagHotRe");
						if (LOADINGMORE == HOLDER_FOOTER) {
							mVideoAdapder.failureLoadingMore();
						}
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1.0f));
		request.setTag("VolleyTagHotRe");
		queue.add(request);
	}

	protected void dealtData(String result) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(result).getAsJsonArray();
		List<VideoDemo> videoDemos = new ArrayList<VideoDemo>();
		for (int i = 0; i < Jarray.size(); i++) {
			JsonElement jsonElement = Jarray.get(i);
			VideoDemo videoDemo = gson.fromJson(jsonElement, VideoDemo.class);
			videoDemos.add(videoDemo);
		}
		VolleyTools.cancelRequest("VolleyTagHotRe");
		mRefreshLayout.setRefreshing(false);
		Toast.makeText(context, "请求成功", Toast.LENGTH_SHORT).show();
		videoAdapder.addData(videoDemos, LOADINGMORE);
		storeData(result);
		if (LOADINGMORE == HOLDER_FOOTER) {
			mVideoAdapder.successLoadingMore();
		}
	}

	private void storeData(String result) {
		if (LOADINGMORE == HOLDER_BASE)
			SharedPreUtils.setComSharePref(context, "video_data",result);
	}

}
