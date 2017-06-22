package com.wx.seeworld.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.CacheResource.ImageLoaderJar;
import com.wx.seeworld.NetYINews.Bean.NewsDetails;
import com.wx.seeworld.Tools.VolleyTools;
import com.wx.seeworld.Utils.DensityUtils;
import com.wx.seeworld.Utils.ReadedMark;
import com.wx.seeworld.Utils.SharedPreUtils;

public class RecyclerNetAdapter extends RecyclerView.Adapter<ViewHolder>{
	
	private Context mContext;
	private List<NewsDetails> mRequestResult=new ArrayList<NewsDetails>();
	private LayoutInflater mInflater;
	private SwipeRefreshLayout mRefreshLayout;
	private OnItemNetClickListener mOnItemClickListener;

	private static final int HOLDER_BASE = 1001;
	private static final int HOLDER_FOOTER = 1002;

	private  static ProgressBar progressBarFooter;
	private  static TextView tvFooterLoading;

	private OnLoadingMoreNetListener mLoadingMoreListener;
	private boolean loadingMoreRes;
	
	public String mCid;
	
	public interface OnLoadingMoreNetListener {
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
		
		String LastLoadingMore = SharedPreUtils.getComSharePref(mContext,"LastLoadingMore"+mCid, "false");
		loadingMoreRes = false;
		
		if (LastLoadingMore.equals("false")) {
			progressBarFooter.setVisibility(View.VISIBLE);
			tvFooterLoading.setText("正在加载中... ...");
			SharedPreUtils.setComSharePref(mContext, "LastLoadingMore"+mCid, "true");
			return ;
		}
		
		SharedPreUtils.setComSharePref(mContext, "LastLoadingMore"+mCid, "trues");

		progressBarFooter.setVisibility(View.GONE);
		tvFooterLoading.setText("已是最后一条新闻");

	}

	public void setLoadingMoreListener(
			OnLoadingMoreNetListener mLoadingMoreListener) {
		this.mLoadingMoreListener = mLoadingMoreListener;
	}

	public interface OnItemNetClickListener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	public void setOnItemClickListener(OnItemNetClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	public RecyclerNetAdapter(Context context,SwipeRefreshLayout mRefreshLayout,String mcid) {
		this.mContext = context;
		this.mCid=mcid;
		mInflater = LayoutInflater.from(context);
		this.mRefreshLayout = mRefreshLayout;
	}

	@Override
	public int getItemCount() {
		return mRequestResult.size();
	}
	
	int card=0;
	// 绑定holder=myViewHolder
	@SuppressWarnings("deprecation")
	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		
		if (holder instanceof MyViewHolder) {
			
			if(refreshCardBg !=null){
				TypedArray typeMainText = mContext.obtainStyledAttributes(null, R.styleable.maintext);
				int cardBgColor = typeMainText.getColor(R.styleable.maintext_cardbackground, 0xffffffff);
				((MyViewHolder) holder).cardViewNews.setCardBackgroundColor(cardBgColor);
				int cardTitleColor=typeMainText.getColor(R.styleable.maintext_cardTitleTextColor, 0xffffffff);
				((MyViewHolder) holder).tvRecyclerTitle.setTextColor(cardTitleColor);
				int cardBottomColor=typeMainText.getColor(R.styleable.maintext_cardTextBottonColor, 0xffffffff);
				((MyViewHolder) holder).tvRecyclerDate.setTextColor(cardBottomColor);
				((MyViewHolder) holder).tvRecyclerOrigin.setTextColor(cardBottomColor);
				int ivCardMask=typeMainText.getColor(R.styleable.maintext_image_mask, 0xffffffff);
				((MyViewHolder) holder).viewCardMask.setBackgroundColor(ivCardMask);
				typeMainText.recycle();
				
			}
			
			((MyViewHolder) holder).tvRecyclerTitle.setText(mRequestResult.get(position).getTitle());
			if(mRequestResult.get(position).getUrl_3w()!=null){
				if(ReadedMark.getReaded(mRequestResult.get(position).getUrl_3w(), mContext)){
					((MyViewHolder) holder).tvRecyclerTitle.setTextColor(0x88666666);
				}
			}
			
			((MyViewHolder) holder).tvRecyclerDate.setText(mRequestResult.get(
					position).getPtime());
			((MyViewHolder) holder).tvRecyclerOrigin.setText(mRequestResult
					.get(position).getSource());
			if (mRequestResult.get(position).getImgsrc() == null|| mRequestResult.get(position).getImgsrc().equals("")) {
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
				((MyViewHolder) holder).ivRecyclerImage.setTag(mRequestResult.get(position).getImgsrc());
				
				ImageLoaderJar.getImgFromNet(mRequestResult.get(position).getImgsrc(),((MyViewHolder) holder).ivRecyclerImage);
			}
		} else if (holder instanceof FooterRefreshViewHolder) {

			if(refreshCardBg !=null){
				TypedArray typeMainText = mContext.obtainStyledAttributes(null, R.styleable.maintext);
				int loadingBg=typeMainText.getColor(R.styleable.maintext_cardbackground, 0xffffffff);
				((FooterRefreshViewHolder)holder).llLoadingBg.setBackgroundColor(loadingBg);
				typeMainText.recycle();
				TypedArray typeBottom = mContext.obtainStyledAttributes(null, R.styleable.bottom);
				int loadingTextColor=typeBottom.getColor(R.styleable.bottom_bottomTextColor, 0xffffffff);
				tvFooterLoading.setTextColor(loadingTextColor);
				typeBottom.recycle();
				if(refreshCardBg.equals("day")){
					Drawable drawable = mContext.getResources().getDrawable(R.drawable.rotate_loading_ring);
					progressBarFooter.setIndeterminateDrawable(drawable);
				}else{
					Drawable drawable = mContext.getResources().getDrawable(R.drawable.rotate_loading_ring_night);
					progressBarFooter.setIndeterminateDrawable(drawable);
				}
			}
			
			if (mLoadingMoreListener != null && !loadingMoreRes) {
				loadingMoreRes = true;
				mLoadingMoreListener.onLoading();
			}

		} 
			
		if (mOnItemClickListener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					int layoutPosition = holder.getPosition();
					mOnItemClickListener.onItemClick(holder.itemView,layoutPosition);
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
			View view = mInflater.inflate(R.layout.item_recycler_content,viewGroup, false);
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
		private CardView cardViewNews;
		private View viewCardMask;

		public MyViewHolder(View view) {
			super(view);

			tvRecyclerTitle = (TextView) view.findViewById(R.id.tv_recycler_title);
			tvRecyclerOrigin = (TextView) view.findViewById(R.id.tv_recycler_origin);
			tvRecyclerDate = (TextView) view.findViewById(R.id.tv_recycler_date);
			ivRecyclerImage = (ImageView) view.findViewById(R.id.iv_recycler_image);
			cardViewNews = (CardView) view.findViewById(R.id.card_view_news);
			viewCardMask = view.findViewById(R.id.view_card_mask);

		}

	}

	 class FooterRefreshViewHolder extends ViewHolder {

		private LinearLayout llLoadingBg;

		public FooterRefreshViewHolder(View view) {
			super(view);
			progressBarFooter = (ProgressBar) view.findViewById(R.id.progress_bar_footer);
			tvFooterLoading = (TextView) view.findViewById(R.id.tv_footer_loading);
			llLoadingBg = (LinearLayout) view.findViewById(R.id.ll_loading_bg);
		}

	}
	 
	 public void refreshRecycler(int position){
		 notifyItemChanged(position);
	 }
	 
	 private String refreshCardBg=null;
	 public void refreshCardBg(String refreshCardBg){
		 this.refreshCardBg=refreshCardBg;
		 notifyDataSetChanged();
	 }

	public void addData(List<NewsDetails> results, int loadingMore) {
		
		switch (loadingMore) {
		case 0:
			
			mRequestResult.addAll(0, results);
			notifyItemRangeChanged(0, results.size());
			mRefreshLayout.setRefreshing(false);
			break;

		case 1:
			String LastLoadingMore = SharedPreUtils.getComSharePref(mContext,"LastLoadingMore"+mCid, "false");
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
	public List<String> getRequestResult(int position){
		List<String> list=new ArrayList<String>();
		list.add(mRequestResult.get(position).getUrl_3w());
		list.add(mRequestResult.get(position).getTitle());
		list.add(mRequestResult.get(position).getImgsrc());
		list.add(mRequestResult.get(position).getDigest());
		return list;
	}
	

}
