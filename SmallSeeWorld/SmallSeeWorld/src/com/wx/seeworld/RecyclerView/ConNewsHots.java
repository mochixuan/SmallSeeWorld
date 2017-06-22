package com.wx.seeworld.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wx.seeworld.Bean.NewsHot;
import com.wx.seeworld.Bean.NewsHotRes;
import com.wx.seeworld.Bean.RequestResult;
import com.wx.seeworld.CacheResource.LocalCacheUtils;
import com.wx.seeworld.NetAddress.GenerateUrl;
import com.wx.seeworld.Tools.VolleyTools;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.ToastUtils;

public class ConNewsHots {

	private List<String> newsHotRes = new ArrayList<String>();
	private boolean FlagNews = false;
	private int newsLocation = 0;
	private List<RequestResult> requestRes=new ArrayList<RequestResult>();
	private static Context mContext;
	private static RecycleAdapter mAdapter;
	private static ConNewsHots conNewsHots;
	private long currentTime;
	private static SharedPreferences preferences;
	private static SwipeRefreshLayout mRefreshLayout;
	private int errorPage=0;
	private boolean requestSuccess;
	private static int loadingMore;
	
	
	@SuppressWarnings("static-access")
	public static ConNewsHots getInstance(Context mcontext,RecycleAdapter adapter,SwipeRefreshLayout refreshLayout, int loadingmore){
		mContext = mcontext;
		preferences = mContext.getSharedPreferences("config", mcontext.MODE_PRIVATE);
		mAdapter=adapter;
		loadingMore=loadingmore;
		mRefreshLayout=refreshLayout;
		if(conNewsHots==null){
			conNewsHots=new ConNewsHots();
		}
		return conNewsHots;
	}

	public void request() {
		
		currentTime=System.currentTimeMillis();
		newsLocation=preferences.getInt("newsLocation", newsLocation);
		
		if (newsLocation == newsHotRes.size()&& newsHotRes.size() != 0) {
			
			if(System.currentTimeMillis()-preferences.getLong("newstimer", 0)>1000*30){
				preferences.edit().putLong("newstimer",currentTime).putInt("newsLocation", 0).commit();
				request();
				SharedPreUtils.setComSharePref(mContext, "LastLoadingMore", "false");
				return;
			}
			
			mRefreshLayout.setRefreshing(false);
			ToastUtils.makeText(mContext, "已是最新新闻");
			return;
			
		}
		
		if (!FlagNews) {
			String newsHotResUrl = GenerateUrl.getHotSpotUrl("json", "GET");
			boolean initDatarequest = initData(newsHotResUrl, "NewsHotRes");
			if(initDatarequest){
				FlagNews = true;
			}
		} else {
			
			if (newsHotRes == null) {
				FlagNews = false;
				return;
			}
			
			int w=0;
			
			if (newsHotRes.size() - newsLocation <=5) {
				w=newsHotRes.size() - newsLocation;
				newsLocation += w;
				FlagNews = false;
			} else {
				w=5;
				newsLocation += w;
			}
			
			for (int i = (newsLocation - w); i < newsLocation; i++) {
				String retrievalUrl = GenerateUrl.getRetrievalUrl("json","GET", newsHotRes.get(i));
				initData(retrievalUrl, "NewsHot");
			}
			
			preferences.edit().putLong("newstimer",currentTime).commit();
		}
	}
	
	private boolean initData(final String urlRequest, final String orignal) {
		RequestQueue queue = VolleyTools.getRequestQueue(mContext);
		StringRequest request = new StringRequest(Request.Method.GET,urlRequest, new Listener<String>() {
					public void onResponse(String result) {
						if (orignal.equals("NewsHotRes")) {
							analyJsonDataRes(result);
						} else if (orignal.equals("NewsHot")) {
							analyJsonData(result);
							LocalCacheUtils.setCacheToLocal(urlRequest, mContext,loadingMore);
						}
						requestSuccess=true;
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						mRefreshLayout.setRefreshing(false);
						VolleyTools.cancelRequest("VolleyTagHotRes");
						requestSuccess=false;
						//ToastUtils.makeText(mContext, "请求失败");
					}
				});
		//第一个:设置超时时间	第二个:默认最大尝试次数 
		request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ));
		request.setTag("VolleyTagHotRes");
		queue.add(request);
		return requestSuccess;
	}

	protected void analyJsonDataRes(String result) {
		Gson gson = new Gson();
		NewsHotRes newsHotRess = gson.fromJson(result, NewsHotRes.class);
		String error_code = newsHotRess.getError_code();
		
		if (Integer.parseInt(error_code) != 0) {
			return ;
		}
		newsHotRes = newsHotRess.getResult();
		request();
	}
	
	protected void analyJsonData(String result) {
		Gson gson = new Gson();
		NewsHot newsHot = gson.fromJson(result, NewsHot.class);
		String error_code = newsHot.getError_code();
		
		if (Integer.parseInt(error_code) != 0) {
			++errorPage;
		}else{
			requestRes.add(0,newsHot.getResults().get(0));
		}
		
		if (requestRes != null){
			if((newsLocation-errorPage)%5==requestRes.size() || requestRes.size()/5==1 ){
				mAdapter.addData(requestRes,loadingMore);
				requestRes.clear();
				errorPage=0;
				preferences.edit().putInt("newsLocation", newsLocation).commit();
			}
		}
	}
	
}
