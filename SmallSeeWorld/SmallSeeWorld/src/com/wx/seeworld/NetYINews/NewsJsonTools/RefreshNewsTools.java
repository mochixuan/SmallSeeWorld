package com.wx.seeworld.NetYINews.NewsJsonTools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wx.seeworld.NetYINews.Bean.NewsDetails;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Entertainment;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Finance;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Headlines;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Mobileinternet;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.NBA;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Orignal;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Phone;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Sciencetechnology;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Sport;
import com.wx.seeworld.NetYINews.Bean.NewsSummaryClass.Tromist;
import com.wx.seeworld.RecyclerView.RecyclerNetAdapter;
import com.wx.seeworld.Tools.VolleyTools;
import com.wx.seeworld.Utils.SharedPreUtils;

public class RefreshNewsTools {

	private static Context mContext;
	private static RecyclerNetAdapter netAdapter;
	private static SwipeRefreshLayout mRefreshLayout;
	private static int loadingorRefresh;
	private static RefreshNewsTools mNewsTools;

	private boolean requestSuccess = true;
	private List<NewsDetails> newsDetails = new ArrayList<NewsDetails>();
	private static String mCid;
	//private int requestCount=10;

	public static RefreshNewsTools getInstance(Context mcontext,RecyclerNetAdapter netadapter, SwipeRefreshLayout refreshLayout,int LoadingOrRefresh, String mcid) {
		mContext = mcontext;
		mRefreshLayout = refreshLayout;
		netAdapter = netadapter;
		loadingorRefresh = LoadingOrRefresh;
		mCid = mcid;
		if (mNewsTools == null) {
			mNewsTools = new RefreshNewsTools();
		}
		return mNewsTools;
	}

	// 请求数据
	public void request() {
		String newsCatUrl = NewsJsonTools.getJsonData(mCid, 10, mContext);
		initData(newsCatUrl);
	}

	public void SendData() {
		if (newsDetails.size() != 0) {
			List<NewsDetails> lists = new ArrayList<NewsDetails>();
			for (int i = 0; i <10; i++) {
				if (newsDetails.get(i).getUrl_3w() != null && ! newsDetails.get(i).getUrl_3w().equals("")) {
					if (newsDetails.get(i).getUrl_3w().substring(7, 11).equals("help")) {
					} else {
						lists.add(newsDetails.get(i));
					}
				}
			}
			if (lists.size() != 0){
				netAdapter.addData(lists, loadingorRefresh);
			}else{
				mRefreshLayout.setRefreshing(false);
			}
			
		}

	}

	private boolean initData(final String urlRequest) {
		RequestQueue queue = VolleyTools.getRequestQueue(mContext);
		StringRequest request = new StringRequest(Request.Method.GET,
				urlRequest, new Listener<String>() {
					public void onResponse(String result) {
						requestSuccess = true;
						JugementFromClass(result);// 解析数据
						storageJsonString(result);
						VolleyTools.cancelRequest("VolleyTagHotRes");
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						requestSuccess = false;
						mRefreshLayout.setRefreshing(false);
						VolleyTools.cancelRequest("VolleyTagHotRes");
						storageJsonString("");
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1.0f));
		//request.setRetryPolicy(new DefaultRetryPolicy(5000, 1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag("VolleyTagHotRes");
		queue.add(request);
		return requestSuccess;
	}

	protected void storageJsonString(final String result) {
		if(! requestSuccess){
			return ;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreUtils.setComSharePref(mContext, "NewsJson" + mCid,result); 			// 存一份
			}
		}).start();
	}
	
	private void JugementFromClass(String results) {
		Gson gson = new Gson();
		switch (mCid) {
		case NewsJsonTools.HEADLINES:
			Headlines headlines = gson.fromJson(results, Headlines.class);
			newsDetails = headlines.getHeadlines();
			SendData();
			break;
		case NewsJsonTools.ORIGNAL:
			Orignal orignal = gson.fromJson(results, Orignal.class);
			newsDetails = orignal.getOrignal();
			SendData();
			break;
		case NewsJsonTools.NBA:
			NBA nba = gson.fromJson(results, NBA.class);
			newsDetails = nba.getNba();
			SendData();
			break;
		case NewsJsonTools.ENTERTAINMENT:
			Entertainment entertainment = gson.fromJson(results, Entertainment.class);
			newsDetails = entertainment.getEntertainment();
			SendData();
			break;
		case NewsJsonTools.SPORT:
			Sport sport = gson.fromJson(results, Sport.class);
			newsDetails = sport.getSport();
			SendData();
			break;
		case NewsJsonTools.PHONE:
			Phone phone = gson.fromJson(results, Phone.class);
			newsDetails = phone.getPhone();
			SendData();
			break;
		case NewsJsonTools.MOBILEINTERNET:
			Mobileinternet mobileinternet = gson.fromJson(results, Mobileinternet.class);
			newsDetails = mobileinternet.getMobileinternet();
			SendData();
			break;
		case NewsJsonTools.SCIENCETECHNOLOGY:
			Sciencetechnology sciencetechnology = gson.fromJson(results, Sciencetechnology.class);
			newsDetails = sciencetechnology.getSciencetechnology();
			SendData();
			break;
		case NewsJsonTools.TROMIST:
			Tromist tromist = gson.fromJson(results, Tromist.class);
			newsDetails = tromist.getTromist();
			SendData();
			break;
		case NewsJsonTools.FINANCE:
			Finance finance = gson.fromJson(results, Finance.class);
			newsDetails = finance.getFinance();
			SendData();
			break;
		}
	}

}