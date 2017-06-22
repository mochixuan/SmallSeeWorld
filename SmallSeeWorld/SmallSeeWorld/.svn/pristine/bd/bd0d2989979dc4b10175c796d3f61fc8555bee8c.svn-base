package com.wx.seeworld.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.Bean.RequestResult;
import com.wx.seeworld.CacheResource.BitmapFromVolleyUtils;
import com.wx.seeworld.Tools.VolleyTools;
import com.wx.seeworld.Utils.DensityUtils;
import com.wx.seeworld.Utils.SharedPreUtils;

public class RecycleAdapter extends RecyclerView.Adapter<ViewHolder> {

	private Context mContext;
	private List<RequestResult> mRequestResult=new ArrayList<RequestResult>();
	private LayoutInflater mInflater;
	private SwipeRefreshLayout mRefreshLayout;
	private OnItemClickListener mOnItemClickListener;

	private static final int HOLDER_BASE = 1001;
	private static final int HOLDER_FOOTER = 1002;

	private static ProgressBar progressBarFooter;
	private static TextView tvFooterLoading;

	private OnLoadingMoreListener mLoadingMoreListener;
	private boolean loadingMoreRes;

	public interface OnLoadingMoreListener {
		void onLoading();
	}

	public void successLoadingMore() {
		progressBarFooter.setVisibility(View.GONE);
		tvFooterLoading.setVisibility(View.GONE);
		loadingMoreRes = false;
	}

	public void failureLoadingMore() {
		progressBarFooter.setVisibility(View.GONE);
		tvFooterLoading.setText("网络不给力 * 点击加载试试");
		loadingMoreRes = false;
	}

	public void EndLoadingMore() {

		String LastLoadingMore = SharedPreUtils.getComSharePref(mContext,"LastLoadingMore", "false");

		loadingMoreRes = false;
		
		if (LastLoadingMore.equals("false")) {
			progressBarFooter.setVisibility(View.VISIBLE);
			tvFooterLoading.setText("正在加载中... ...");
			SharedPreUtils.setComSharePref(mContext, "LastLoadingMore", "true");
			return ;
		}
		
		SharedPreUtils.setComSharePref(mContext, "LastLoadingMore", "trues");

		progressBarFooter.setVisibility(View.GONE);
		tvFooterLoading.setText("已是最后一条新闻");

	}

	public void setLoadingMoreListener(
			OnLoadingMoreListener mLoadingMoreListener) {
		this.mLoadingMoreListener = mLoadingMoreListener;
	}

	public interface OnItemClickListener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	public RecycleAdapter(Context context,SwipeRefreshLayout mRefreshLayout) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mRefreshLayout = mRefreshLayout;
	}

	@Override
	public int getItemCount() {
		return mRequestResult.size();
	}

	// 绑定holder=myViewHolder
	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {

		
		if (holder instanceof MyViewHolder) {
			
			((MyViewHolder) holder).tvRecyclerTitle.setText(mRequestResult.get(position).getTitle());
			((MyViewHolder) holder).tvRecyclerDate.setText(mRequestResult.get(position).getPdate());
			((MyViewHolder) holder).tvRecyclerOrigin.setText(mRequestResult.get(position).getSrc());

			if (mRequestResult.get(position).getImg() == null|| mRequestResult.get(position).getImg().equals("")) {
				((MyViewHolder) holder).ivRecyclerImage.setVisibility(View.GONE);
				
				RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			    int changepx = DensityUtils.dpChangepx(mContext, 1);
			    params.setMargins(changepx*7, changepx*4, changepx*4, changepx*4);
				((MyViewHolder) holder).tvRecyclerTitle.setLayoutParams(params);
				
			} else {
				
				RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(DensityUtils.dpChangepx(mContext, 240),LayoutParams.WRAP_CONTENT);
				int changepx = DensityUtils.dpChangepx(mContext, 1);
			   params.setMargins(changepx*7, changepx*4, changepx*4, changepx*4);
				((MyViewHolder) holder).tvRecyclerTitle.setLayoutParams(params);
				
				((MyViewHolder) holder).ivRecyclerImage.setVisibility(View.VISIBLE);
				((MyViewHolder) holder).ivRecyclerImage.setTag(mRequestResult.get(position).getImg());
				
				//网络下载
				BitmapFromVolleyUtils.getImgFromNet(mRequestResult.get(position).getImg(),
						((MyViewHolder) holder).ivRecyclerImage, mContext);
				
			}
		} else if (holder instanceof FooterRefreshViewHolder) {

			if (mLoadingMoreListener != null && !loadingMoreRes) {
				loadingMoreRes = true;
				mLoadingMoreListener.onLoading();
			}

		} 
			
		if (mOnItemClickListener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					int layoutPosition = holder.getPosition();
					mOnItemClickListener.onItemClick(holder.itemView,
							layoutPosition);
				}
			});

			holder.itemView.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					int layoutPosition = holder.getPosition();
					mOnItemClickListener.onItemLongClick(holder.itemView,layoutPosition);
					return true; // 拦截避免被单击监听了
				}
			});
		}

	}

	// 创建ViewHolder
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

		if (position == HOLDER_FOOTER) {
			View view = mInflater.inflate(R.layout.refresh_footer_view,viewGroup, false);
			FooterRefreshViewHolder footerViewHolder = new FooterRefreshViewHolder(view);
			return footerViewHolder;
		} else {
			View view = mInflater.inflate(R.layout.item_recycler_content,
					viewGroup, false);
			MyViewHolder holder = new MyViewHolder(view);
			return holder;
		}

	}

	@Override
	public int getItemViewType(int position) {
		if (position >= getItemCount() - 1) {
			return HOLDER_FOOTER;
		}
		return HOLDER_BASE;
	}

	static class MyViewHolder extends ViewHolder {

		private TextView tvRecyclerTitle;
		private TextView tvRecyclerOrigin;
		private TextView tvRecyclerDate;
		private ImageView ivRecyclerImage;

		public MyViewHolder(View view) {
			super(view);

			tvRecyclerTitle = (TextView) view.findViewById(R.id.tv_recycler_title);
			tvRecyclerOrigin = (TextView) view.findViewById(R.id.tv_recycler_origin);
			tvRecyclerDate = (TextView) view.findViewById(R.id.tv_recycler_date);
			ivRecyclerImage = (ImageView) view.findViewById(R.id.iv_recycler_image);

		}

	}

	static class FooterRefreshViewHolder extends ViewHolder {

		public FooterRefreshViewHolder(View view) {
			super(view);
			progressBarFooter = (ProgressBar) view.findViewById(R.id.progress_bar_footer);
			tvFooterLoading = (TextView) view.findViewById(R.id.tv_footer_loading);
		}

	}


	public void addData(List<RequestResult> results, int loadingMore) {

		switch (loadingMore) {
		case 0:
			
			mRequestResult.addAll(0, results);
			notifyItemRangeChanged(0, results.size());
			mRefreshLayout.setRefreshing(false);
			VolleyTools.cancelRequest("VolleyTagHotRes");
			
			break;

		case 1:
			String LastLoadingMore = SharedPreUtils.getComSharePref(mContext,"LastLoadingMore", "false");
			if (LastLoadingMore.equals("trues"))
				break;
			mRequestResult.addAll(getItemCount(), results);
			notifyItemRangeChanged(getItemCount(), results.size()+ getItemCount());
			EndLoadingMore();
			VolleyTools.cancelRequest("VolleyTagHotRes");
			break;
		}

	}
	
	//获取网页的网址
	public String getRequestResult(int position){
		return mRequestResult.get(position).getUrl();
	}


}
