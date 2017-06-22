package com.wx.seeworld.RequestVideo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindCallback;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wx.seeworld.Bean.TopicBean;
import com.wx.seeworld.RecyclerView.RecyclerTopicContent;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.ToastUtils;

@SuppressLint("SimpleDateFormat")
public class TopicRequest {

	private static final int initTopic = 1001;
	private static final int requestTopic = 1002;

	public static void requestBmob(final Context mContext,
			final SwipeRefreshLayout refreshLayout,
			final RecyclerTopicContent mRecyclerAdapter) {

		BmobQuery<TopicBean> bmobQuery = new BmobQuery<TopicBean>("TopicBean");
		bmobQuery.order("-createdAt");

		String lastRefreshTime = SharedPreUtils.getComSharePref(mContext,
				"Topic_detail_createdAt", "");
		if (!lastRefreshTime.equals("")) {
			try {
				SimpleDateFormat myFmt = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = myFmt.parse(lastRefreshTime);
				bmobQuery.addWhereGreaterThan("createdAt", new BmobDate(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		bmobQuery.findObjects(mContext, new FindCallback() {
			@Override
			public void onFailure(int arg0, String arg1) {
				ToastUtils.makeText(mContext, "请求失败");
				refreshLayout.setRefreshing(false);
			};

			@Override
			public void onSuccess(JSONArray result) {
				AnayleDate(refreshLayout, mRecyclerAdapter, result, mContext);
			}

		});
	}

	private static void AnayleDate(final SwipeRefreshLayout refreshLayout,
			final RecyclerTopicContent mRecyclerAdapter, JSONArray result,
			Context mContext) {

		if (result.length() == 0) {
			Toast.makeText(mContext, "已是最新内容", Toast.LENGTH_SHORT).show();
			refreshLayout.setRefreshing(false);
			return;
		}

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SharedPreUtils.setComSharePref(mContext, "Topic_detail_createdAt",myFmt.format(date).toString());

		StorageTopic(result, mContext);
		JsonData(refreshLayout, mRecyclerAdapter, result.toString(),
				requestTopic);
	}

	private static void StorageTopic(JSONArray result, Context mContext) {
		String jsonTopic = SharedPreUtils.getComSharePref(mContext,
				"Topic_detail_data", "");
		if (jsonTopic.equals("")) {
			jsonTopic = result.toString();
		} else {
			jsonTopic = result.toString() + "W&%&X" + jsonTopic;
		}
		SharedPreUtils
				.setComSharePref(mContext, "Topic_detail_data", jsonTopic);
	}

	private static void JsonData(final SwipeRefreshLayout refreshLayout,
			final RecyclerTopicContent mRecyclerAdapter, String result,
			int requestType) {
		Gson gson = new Gson();
		List<TopicBean> topicBeans = new ArrayList<TopicBean>();
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = jsonParser.parse(result.toString())
				.getAsJsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement jsonElement = jsonArray.get(i);
			TopicBean topicBean = gson.fromJson(jsonElement, TopicBean.class);
			topicBeans.add(topicBean);
		}
		if (requestType == initTopic) {
			mRecyclerAdapter.AddInitData(topicBeans);
		} else {
			mRecyclerAdapter.AddData(topicBeans);
			refreshLayout.setRefreshing(false);
		}

	}

	public static void AnayleStoregle(final Context mContext,
			final SwipeRefreshLayout refreshLayout,
			final RecyclerTopicContent mRecyclerAdapter) {
		String jsonTopic = SharedPreUtils.getComSharePref(mContext,
				"Topic_detail_data", "");
		if (jsonTopic.equals("")) {
			Toast.makeText(mContext, "初始化数据中。。。", Toast.LENGTH_SHORT).show();
			requestBmob(mContext, refreshLayout, mRecyclerAdapter);
		} else {
			String[] jsonDatas = jsonTopic.split("W&%&X");
			int i = 0;
			for (String result : jsonDatas) {
				JsonData(refreshLayout, mRecyclerAdapter, result, initTopic);
				if (i == 8) {
					DeleteContentJson(mContext);
				}
				i++;
			}
		}
	}

	private static void DeleteContentJson(Context mContext) {
		String jsonTopic = SharedPreUtils.getComSharePref(mContext,"Topic_detail_data", "");
		String[] jsonDatas = jsonTopic.split("W&%&X");
		jsonTopic="";
		int i = 0;
		for (String result : jsonDatas) {
			if(i<8){
				if(i==0){
					jsonTopic=result;
				}else{
					jsonTopic=jsonTopic+ "W&%&X" +result;
				}
				i++;
			}else{
				SharedPreUtils.setComSharePref(mContext,"Topic_detail_data",jsonTopic);
				return;
			}
		}
	}

}
