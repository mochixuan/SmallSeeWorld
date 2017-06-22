package com.wx.seeworld.FragemntNews;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.wx.seeworld.R;
import com.wx.seeworld.Activity.NewsDetailActivity;
import com.wx.seeworld.Bean.NewsHot;
import com.wx.seeworld.Bean.RequestResult;
import com.wx.seeworld.CacheResource.LocalCacheUtils;
import com.wx.seeworld.RecyclerView.ConNewsHots;
import com.wx.seeworld.RecyclerView.RecycleAdapter;
import com.wx.seeworld.RecyclerView.RecycleAdapter.OnItemClickListener;
import com.wx.seeworld.Tools.VolleyTools;
import com.wx.seeworld.Utils.JudgmentIsNetUtils;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.ToastUtils;

public class FragementMainHots extends Fragment implements OnRefreshListener {

	private RecyclerView recyclerView;
	private RecycleAdapter adapter;
	private SwipeRefreshLayout mRefreshLayout;

	private static final int LOADINGHEADER = 0;
	private static final int LOADINGFOOTER = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_news_hots, container,false);

		initView(view);
		initData();

		return view;
	}

	private void initView(View view) {
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_refresh);
	}

	private void initData() {

		// 必须定义
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
				getActivity(), LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(linearLayoutManager);
		// 分割线 方向
		// recyclerView.addItemDecoration(new DividerItemDecoration(this,
		// DividerItemDecoration.VERTICAL_LIST));

		// 设置增删动画
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		
		adapter = new RecycleAdapter(getActivity(), mRefreshLayout);

		recyclerView.setAdapter(adapter);

		adapter.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(View view, int position) {

				if (position == adapter.getItemCount() - 1) {
					int networkAvailable = JudgmentIsNetUtils
							.isNetworkAvailable(getActivity());
					if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
						adapter.failureLoadingMore();
						return;
					}

					String LastLoadingMore = SharedPreUtils.getComSharePref(
							getActivity(), "LastLoadingMore", "false");
					if (LastLoadingMore.equals("trues"))
						return;

					ConNewsHots conNewsHots = ConNewsHots.getInstance(
							getActivity(), adapter, mRefreshLayout,
							LOADINGFOOTER);
					conNewsHots.request();

				} else {
					EnterNewsDetails(adapter.getRequestResult(position));
				}

			}

			public void onItemLongClick(View view, int position) {
			}
			
		});

		// 上啦刷新
		adapter.setLoadingMoreListener(new RecycleAdapter.OnLoadingMoreListener() {

			public void onLoading() {

				int networkAvailable = JudgmentIsNetUtils
						.isNetworkAvailable(getActivity());

				if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
					adapter.failureLoadingMore();
					return;
				}

				String LastLoadingMore = SharedPreUtils.getComSharePref(
						getActivity(), "LastLoadingMore", "false");
				if (LastLoadingMore.equals("trues")) {
					adapter.EndLoadingMore();
					return;
				}

				ConNewsHots conNewsHots = ConNewsHots.getInstance(
						getActivity(), adapter, mRefreshLayout, LOADINGFOOTER);
				conNewsHots.request();

			}

		});

		mRefreshLayout.setColorSchemeResources(
				R.color.refresh_color_blue_shallow,
				R.color.refresh_color_blue_deep,
				R.color.refresh_color_green_deep);
		mRefreshLayout.setOnRefreshListener(this);

		//initTenData();
		
		/*删除数据
		 * final String CACHE_PATH = Environment
				.getExternalStorageDirectory().getAbsolutePath() + "/greatworld";
		final String FILE_NAME = EncryptionUtils
				.setEncryptionMD5("seeworlddata.txt");
		File file = new File(CACHE_PATH, FILE_NAME);
		try {
			File parentFile = file.getParentFile(); 
			if (parentFile.exists()) {
				parentFile.delete();
				System.out.println("----ss--");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}

	@SuppressWarnings("unused")
	private void initTenData() {

		// 初始化10个数据
		String initDate = SharedPreUtils.getComSharePref(getActivity(),"init_ten_data", "false");
		if (initDate.equals("false")) {
			int networkAvailable = JudgmentIsNetUtils
					.isNetworkAvailable(getActivity());
			if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
				adapter.failureLoadingMore();
				return;
			}
			for (int i = 0; i < 2; i++) {
				ConNewsHots conNewsHots = ConNewsHots.getInstance(
						getActivity(), adapter, mRefreshLayout, LOADINGHEADER);
				conNewsHots.request();
			}
			SharedPreUtils.setComSharePref(getActivity(), "init_ten_data",
					"true");
			return;
		}

		// 内存读取10条数据
		List<String> locals = LocalCacheUtils.getCacheFromLocal(getActivity());
		if (locals != null) {
			List<RequestResult> localData = getLocalData(locals);
			if (localData.size() != 0)
				adapter.addData(localData, LOADINGHEADER);
		}

	}

	// 初始化获取本地数据
	public List<RequestResult> getLocalData(List<String> localsCache) {

		List<RequestResult> results = new ArrayList<RequestResult>();
		RequestQueue requestQueue = VolleyTools.getRequestQueue(getActivity());
		for (int i = localsCache.size() - 1; i >= 0; i--) {
			if (requestQueue.getCache().get(localsCache.get(i)) != null) {
				String data = new String(requestQueue.getCache().get(
						localsCache.get(i)).data);
				Gson gson = new Gson();
				NewsHot fromJson = gson.fromJson(data, NewsHot.class);
				RequestResult result = fromJson.getResults().get(0);
				results.add(result);
			}
		}
		return results;
	}

	public void onRefresh() {
		mRefreshLayout.setRefreshing(true);

		int networkAvailable = JudgmentIsNetUtils
				.isNetworkAvailable(getActivity());

		if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
			ToastUtils.makeText(getActivity(), "未连接网络");
		}

		ConNewsHots conNewsHots = ConNewsHots.getInstance(getActivity(),
				adapter, mRefreshLayout, LOADINGHEADER);
		conNewsHots.request();

	}

	// 进入新闻详情页
	protected void EnterNewsDetails(String detailsUrl) {
		Intent intent=new Intent();
		intent.setClass(getActivity(), NewsDetailActivity.class);
		intent.putExtra("detailsUrl", detailsUrl);
		getActivity().startActivity(intent);
	}

}
