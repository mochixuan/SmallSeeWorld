package com.wx.seeworld.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.Bean.TopicCommentBean;
import com.wx.seeworld.Bean.TopicHeaderBean;
import com.wx.seeworld.CacheResource.ImageLoaderJar;
import com.wx.seeworld.CacheResource.VolleyImageEasy;
import com.wx.seeworld.View.CircleImageView;

public class RecyclerTopicDetails extends RecyclerView.Adapter<ViewHolder>{
	
	private static final int VIEWHEAD=1001;
	private static final int VIEWBASE=1002;
	List<TopicCommentBean> commentBeans=new ArrayList<TopicCommentBean>();
	private LayoutInflater mInflater;
	private Context mContext;
	
	public RecyclerTopicDetails(Context mContext){
		this.mContext=mContext;
		mInflater=LayoutInflater.from(mContext);
		TopicCommentBean commentBean=new TopicCommentBean();
		commentBeans.add(commentBean);
	}
	
	@Override
	public int getItemCount() {
		return commentBeans.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		
		if(viewHolder instanceof MyViewHolder){
			MyViewHolder myViewHolder=(MyViewHolder)viewHolder;
			ImageLoaderJar.getImgFromNetCircle(commentBeans.get(position).getAuthorPicture(), myViewHolder.circleImageView);
			myViewHolder.tvUserName.setText(commentBeans.get(position).getAuthorName());
			myViewHolder.tvCaption.setText(commentBeans.get(position).getCommentDetail());
			myViewHolder.tvCommentTime.setText(commentBeans.get(position).getCreatedAt());
		}else{
			ViewHolderHead viewHolderHead=((ViewHolderHead)viewHolder);
			ImageLoaderJar.getImgFromNetCircle(headerBean.getAuthorPicture(), viewHolderHead.circleImageView);
			viewHolderHead.tvAutherName.setText(headerBean.getAuthorName());
			viewHolderHead.tvTopicCaption.setText("		"+headerBean.getCaption());
			
			//ImageLoaderJar.getImgFromNet(headerBean.getCaptionImg(), viewHolderHead.ivHeadBg);
			VolleyImageEasy.setTemporary(mContext, viewHolderHead.ivHeadBg, headerBean.getCaptionImg());
			
			viewHolderHead.tvBrowseNumber.setText(headerBean.getBrowseNumber()+"  浏览");
			viewHolderHead.tvTopicCreatedAt.setText(headerBean.getCreatedAt());
			viewHolderHead.tvDiscussNumber.setText(headerBean.getDiscussNumber()+"  评论");
		}
		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		if(position==VIEWBASE){
			View view=mInflater.inflate(R.layout.topic_details_item, viewGroup, false);
			MyViewHolder viewHolder=new MyViewHolder(view);
			return viewHolder;
		}else{
			View view=mInflater.inflate(R.layout.topic_detail_header, viewGroup, false);
			ViewHolderHead viewHolder=new ViewHolderHead(view);
			return viewHolder;
		}
		
	}
	
	class MyViewHolder extends ViewHolder{

		private CircleImageView circleImageView;
		private TextView tvUserName;
		private TextView tvCaption;
		private TextView tvCommentTime;

		public MyViewHolder(View view) {
			super(view);
			circleImageView = (CircleImageView) view.findViewById(R.id.circlr_topic_picture);
			tvUserName = (TextView) view.findViewById(R.id.tv_comment_name);
			tvCaption = (TextView)view.findViewById(R.id.tv_comment_details);
			tvCommentTime = (TextView)view.findViewById(R.id.tv_comment_time);
		}
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position==0){
			return VIEWHEAD;
		}else{
			return VIEWBASE;
		}
	}
	
	class ViewHolderHead extends ViewHolder{
		
		private ImageView ivHeadBg;
		private CircleImageView circleImageView;
		private TextView tvAutherName;
		private TextView tvTopicCaption;
		private TextView tvTopicCreatedAt;
		private TextView tvBrowseNumber;
		private TextView tvDiscussNumber;
		
		public ViewHolderHead(View view) {
			super(view);
			ivHeadBg = (ImageView) view.findViewById(R.id.iv_topic_head_bg);
			circleImageView = (CircleImageView) view.findViewById(R.id.circlr_topic_picture);
			tvAutherName = (TextView) view.findViewById(R.id.tv_topic_userName);
			tvTopicCaption = (TextView) view.findViewById(R.id.tv_topic_caption);
			tvTopicCreatedAt = (TextView) view.findViewById(R.id.tv_topic_time);
			tvBrowseNumber = (TextView) view.findViewById(R.id.tv_browse_number);
			tvDiscussNumber = (TextView) view.findViewById(R.id.tv_discuss_number);
		}
	}
	
	private TopicHeaderBean headerBean=new TopicHeaderBean();
	public void setHeaderData(TopicHeaderBean headerBean){
		this.headerBean=headerBean;
	}
	
	public void AddData(List<TopicCommentBean> commentBeans){
		this.commentBeans.addAll(1, commentBeans);
		notifyItemRangeInserted(1, commentBeans.size());
	}
	
	public void AddDataOne(TopicCommentBean commentBean){
		this.commentBeans.add(1,commentBean);
		notifyItemInserted(1);
	}

}
