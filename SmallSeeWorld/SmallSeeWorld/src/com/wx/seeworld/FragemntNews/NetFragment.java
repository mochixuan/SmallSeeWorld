package com.wx.seeworld.FragemntNews;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wx.seeworld.R;
import com.wx.seeworld.Activity.NewsDetailActivity;
import com.wx.seeworld.Fragment.FragmentNews;
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
import com.wx.seeworld.NetYINews.NewsJsonTools.NewsJsonTools;
import com.wx.seeworld.NetYINews.NewsJsonTools.RefreshNewsTools;
import com.wx.seeworld.RecyclerView.RecyclerNetAdapter;
import com.wx.seeworld.RecyclerView.RecyclerNetAdapter.OnItemNetClickListener;
import com.wx.seeworld.Tools.TitleTools;
import com.wx.seeworld.Utils.JudgmentIsNetUtils;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.ToastUtils;
import com.wx.seeworld.dao.Titledao;

public class NetFragment extends Fragment implements OnRefreshListener {

	private RecyclerView recyclerView;
	private SwipeRefreshLayout mRefreshLayout;
	private RecyclerNetAdapter netAdapter;
	public String mCid;
	public ViewPager mViewPager;

	private static final int LOADINGHEADER = 0;
	private static final int LOADINGFOOTER = 1;
	
	public NetFragment(String Cid, ViewPager mViewPager) {
		this.mCid = Cid;
		this.mViewPager = mViewPager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_hots, container,
				false);
		initView(view);
		initData();
		return view;
	}

	private void initView(View view) {
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_refresh);
		
		themeStyle = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
	}

	@SuppressWarnings("deprecation")
	private void initData() {

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
				getActivity(), LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(linearLayoutManager);

		netAdapter = new RecyclerNetAdapter(getActivity(), mRefreshLayout, mCid);

		recyclerView.setAdapter(netAdapter);
		
		FragmentNews.hashMap.put(mCid, netAdapter);

		// 单击-双击
		netAdapter.setOnItemClickListener(new OnItemNetClickListener() {
			public void onItemClick(View view, int position) {
				if (position == netAdapter.getItemCount() - 1) {
					int networkAvailable = JudgmentIsNetUtils
							.isNetworkAvailable(getActivity());
					if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
						netAdapter.failureLoadingMore();
						return;
					}

					String LastLoadingMore = SharedPreUtils.getComSharePref(
							getActivity(), "LastLoadingMore" + mCid, "false");
					if (LastLoadingMore.equals("trues"))
						return;

					RefreshNewsTools refreshNewsTools = RefreshNewsTools
							.getInstance(getActivity(), netAdapter,
									mRefreshLayout, LOADINGFOOTER, mCid);

					refreshNewsTools.request();

				} else {
					enterPosition=position;
					EnterNewsDetails(netAdapter.getRequestResult(position));
				}
			}
			public void onItemLongClick(View view, int position) {
			}
		});

		// 上啦刷新
		netAdapter
				.setLoadingMoreListener(new RecyclerNetAdapter.OnLoadingMoreNetListener() {
					public void onLoading() {
						int networkAvailable = JudgmentIsNetUtils
								.isNetworkAvailable(getActivity());
						if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
							netAdapter.failureLoadingMore();
							return;
						}
						String LastLoadingMore = SharedPreUtils
								.getComSharePref(getActivity(),
										"LastLoadingMore" + mCid, "false");
						if (LastLoadingMore.equals("trues")) {
							netAdapter.EndLoadingMore();
							return;
						}

						RefreshNewsTools refreshNewsTools = RefreshNewsTools
								.getInstance(getActivity(), netAdapter,
										mRefreshLayout, LOADINGFOOTER, mCid);
						refreshNewsTools.request();
					}

				});

		mRefreshLayout.setColorSchemeResources(
				R.color.refresh_color_blue_shallow,
				R.color.refresh_color_blue_deep,
				R.color.refresh_color_green_deep);
		TypedArray typeTheme=getActivity().obtainStyledAttributes(null, R.styleable.theme);
		int backgroundColor = typeTheme.getColor(R.styleable.theme_mainBackgroundColor, 0xffffffff);
		mRefreshLayout.setProgressBackgroundColorSchemeColor(backgroundColor);
		typeTheme.recycle();
		mRefreshLayout.setOnRefreshListener(this);

		// 设置监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			// 数据初始化
			@Override
			public void onPageSelected(int position) {
				String selectedTitle = Titledao.querySelectedId(getActivity(),
						position);
				String mCid = TitleTools.getmCid(selectedTitle);
				initNewsData(getActivity(), mCid);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		// 设置加载数据
		String loadinginit = SharedPreUtils.getComSharePref(getActivity(),
				"loading_init_data", "false");
		if (loadinginit.equals("true")) {
			SharedPreUtils.setComSharePref(getActivity(), "loading_init_data",
					"false");
			initNewsData(getActivity(), mCid);
		}
		
	}

	@Override
	public void onRefresh() {
		mRefreshLayout.setRefreshing(true);
		int networkAvailable = JudgmentIsNetUtils
				.isNetworkAvailable(getActivity());
		if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
			ToastUtils.makeText(getActivity(), "未连接网络");
		}
		RefreshNewsTools refreshNewsTools = RefreshNewsTools.getInstance(
				getActivity(), netAdapter, mRefreshLayout, LOADINGHEADER, mCid);
		refreshNewsTools.request();
	}

	private int enterPosition=-1;
	private String themeStyle;
	// 进入新闻详情页
	protected void EnterNewsDetails(List<String> lists) {
		String detailsUrl = lists.get(0);
		String title= lists.get(1);
		String imgUrl=lists.get(2);
		String digest=lists.get(3);
		Intent intent = new Intent();
		intent.setClass(getActivity(), NewsDetailActivity.class);
		intent.putExtra("detailsUrl", detailsUrl);
		intent.putExtra("Title", title);
		intent.putExtra("imgUrl", imgUrl);
		intent.putExtra("digest", digest);
		getActivity().startActivity(intent);
	}

	// 初始化新闻数据
	public void initNewsData(final Context context, final String mCid) {
		new InitDataAsyncTask().execute(context,"NewsJson" + mCid);
		
	}
	
	class InitDataAsyncTask extends AsyncTask<Object , Void, List<NewsDetails>>{
		private boolean initdatanews;
		private String selfmcid;
		@Override
		protected List<NewsDetails> doInBackground(Object... params) {
			Context context=(Context) params[0];
			selfmcid = (String) params[1];
			String storageJsonData = SharedPreUtils.getComSharePref(context,selfmcid, "null");
			if (storageJsonData.equals("null")) {
				initdatanews=true;
			} else {
				// 解析
				List<NewsDetails> newsDetails =AnalyzeData(storageJsonData, selfmcid.substring(8, selfmcid.length()));
				if(newsDetails !=null && newsDetails.size()!=0){
					List<NewsDetails> lists = new ArrayList<NewsDetails>();
					for (int i = 0; i <newsDetails.size(); i++) {
						if (newsDetails.get(i).getUrl_3w() != null && ! newsDetails.get(i).getUrl_3w().equals("")) {
							if (! newsDetails.get(i).getUrl_3w().substring(7, 11).equals("help")) {
								lists.add(newsDetails.get(i));
							}
						}
					}
					newsDetails=lists;
				}
				return newsDetails;
			}
			return null;
		}
		

		@Override
		protected void onPostExecute(List<NewsDetails> newsDetails) {
			super.onPostExecute(newsDetails);
			if(newsDetails !=null && newsDetails.size()!=0){
				RecyclerNetAdapter netAdapter1 = FragmentNews.hashMap.get(selfmcid.substring(8, selfmcid.length()));
				if(netAdapter1.getItemCount()==0){
					netAdapter1.addData(newsDetails, LOADINGHEADER);
				}
			}
			
			if(initdatanews){
				// 发送初始化一些数据
				int networkAvailable = JudgmentIsNetUtils.isNetworkAvailable(getActivity());
				if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
					ToastUtils.makeText(getActivity(), "未连接网络");
				}else{
					ToastUtils.makeText(getActivity(), "初始化数据中。。。");
					RecyclerNetAdapter netAdapter = FragmentNews.hashMap.get(selfmcid.substring(8, selfmcid.length()));
					RefreshNewsTools refreshNewsTools = RefreshNewsTools.getInstance(
							getActivity(), netAdapter, mRefreshLayout, LOADINGHEADER, selfmcid.substring(8, selfmcid.length()));
					refreshNewsTools.request();
					initdatanews=false;
				}
			}
			
		}
		
	}
	
	// 解析函数
	private static List<NewsDetails> AnalyzeData(String results, String mCid) {
		List<NewsDetails> newsDetails = null;
		Gson gson = new Gson();
		switch (mCid) {
		case NewsJsonTools.HEADLINES:
			Headlines headlines = gson.fromJson(results, Headlines.class);
			newsDetails = headlines.getHeadlines();
			break;
		case NewsJsonTools.ORIGNAL:
			Orignal orignal = gson.fromJson(results, Orignal.class);
			newsDetails = orignal.getOrignal();
			break;
		case NewsJsonTools.NBA:
			NBA nba = gson.fromJson(results, NBA.class);
			newsDetails = nba.getNba();
			break;
		case NewsJsonTools.ENTERTAINMENT:
			Entertainment entertainment = gson.fromJson(results, Entertainment.class);
			newsDetails = entertainment.getEntertainment();
			break;
		case NewsJsonTools.SPORT:
			Sport sport = gson.fromJson(results, Sport.class);
			newsDetails = sport.getSport();
			break;
		case NewsJsonTools.PHONE:
			Phone phone = gson.fromJson(results, Phone.class);
			newsDetails = phone.getPhone();
			break;
		case NewsJsonTools.MOBILEINTERNET:
			Mobileinternet mobileinternet = gson.fromJson(results, Mobileinternet.class);
			newsDetails = mobileinternet.getMobileinternet();
			break;
		case NewsJsonTools.SCIENCETECHNOLOGY:
			Sciencetechnology sciencetechnology = gson.fromJson(results, Sciencetechnology.class);
			newsDetails = sciencetechnology.getSciencetechnology();
			break;
		case NewsJsonTools.TROMIST:
			Tromist tromist = gson.fromJson(results, Tromist.class);
			newsDetails = tromist.getTromist();
			break;
		case NewsJsonTools.FINANCE:
			Finance finance = gson.fromJson(results, Finance.class);
			newsDetails = finance.getFinance();
			break;
		}
		return newsDetails;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(enterPosition != -1){
			netAdapter.refreshRecycler(enterPosition);
			enterPosition=-1;
		}
		
		String themeChange = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		if(! themeStyle.equals(themeChange)){
			TypedArray typeTheme=getActivity().obtainStyledAttributes(null, R.styleable.theme);
			int backgroundColor = typeTheme.getColor(R.styleable.theme_mainBackgroundColor, 0xffffffff);
			mRefreshLayout.setProgressBackgroundColorSchemeColor(backgroundColor);
			typeTheme.recycle();
			if(themeChange.equals("day")){
				themeStyle="day";
				netAdapter.refreshCardBg("day");
			}else{
				themeStyle="night";
				netAdapter.refreshCardBg("night");
			}
		}
		
	}
	
	
	
}
