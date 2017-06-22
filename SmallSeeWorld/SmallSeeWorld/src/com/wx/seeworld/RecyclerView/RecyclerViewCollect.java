package com.wx.seeworld.RecyclerView;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.Activity.NewsDetailActivity;
import com.wx.seeworld.Bean.collect;

public class RecyclerViewCollect extends RecyclerView.Adapter<ViewHolder> {

	private Context mContext;
	private LayoutInflater mInflater;
	
	List<collect> querySelectedList;
	
	private onListener mItemLogListener;
	public void setItemLongListener(onListener mItemLogListener) {
		this.mItemLogListener = mItemLogListener;
	}
	public interface onListener{
		void longListener(int position,String url);
	}
	
	public RecyclerViewCollect(Context context,List<collect> querySelectedList) {
		this.mContext=context;
		mInflater=LayoutInflater.from(context);
		this.querySelectedList=querySelectedList;
	}
	
	@Override
	public int getItemCount() {
		return querySelectedList.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		((myViewHolder)viewHolder).tvTitle.setText(querySelectedList.get(position).getTitle());
		((myViewHolder)viewHolder).tvDigest.setText(querySelectedList.get(position).getDigest());
		((myViewHolder)viewHolder).itemView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					String detailsUrl = querySelectedList.get(position).getUrl();
					String title= querySelectedList.get(position).getTitle();
					String imgUrl="http://img4.cache.netease.com/3g/2016/5/4/20160504162707d200b.jpg";
					String digest=querySelectedList.get(position).getDigest();
					Intent intent = new Intent();
					intent.setClass(mContext, NewsDetailActivity.class);
					intent.putExtra("detailsUrl", detailsUrl);
					intent.putExtra("Title", title);
					intent.putExtra("imgUrl", imgUrl);
					intent.putExtra("digest", digest);
					mContext.startActivity(intent);
			}
		});
		((myViewHolder)viewHolder).itemView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mItemLogListener.longListener(position,querySelectedList.get(position).getUrl());
				return true;
			}
		});
		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = mInflater.inflate(R.layout.item_collect, viewGroup, false);
		myViewHolder viewHolder=new myViewHolder(view);
		return viewHolder;
	}
	
	class myViewHolder extends ViewHolder{

		private TextView tvTitle;
		private TextView tvDigest;

		public myViewHolder(View view) {
			super(view);
			tvTitle = (TextView) view.findViewById(R.id.tv_collect_title);
			tvDigest = (TextView) view.findViewById(R.id.tv_collect_digest);
		}
		
	}
	
	public void changeRecycler(List<collect> querySelectedList,int position){
		this.querySelectedList=querySelectedList;
		notifyDataSetChanged();
	}
	

}
