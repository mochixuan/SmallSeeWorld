package com.wx.seeworld.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.Bean.TopicBean;
import com.wx.seeworld.CacheResource.ImageLoaderJar;
import com.wx.seeworld.View.CircleImageView;

public class RecyclerTopicContent extends RecyclerView.Adapter<ViewHolder>{
	
	private List<TopicBean> topicBeans=new ArrayList<TopicBean>();
	private Context mContext;
	private LayoutInflater mInflater;
	
	private onItemTopicListener mItemTopicListener;
	public void setItemTopicListener(onItemTopicListener mItemTopicListener) {
		this.mItemTopicListener = mItemTopicListener;
	}
	public interface onItemTopicListener{
		void onClick(TopicBean topicBean);
	}

	public RecyclerTopicContent(Context mContext) {
		this.mContext=mContext;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getItemCount() {
		return topicBeans.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		
		MyViewHolder myViewHolder=((MyViewHolder)viewHolder);
		
		if(refreshTheme){
			TypedArray typeMainText = mContext.obtainStyledAttributes(null, R.styleable.maintext);
			int cardTitleTextColor = typeMainText.getColor(R.styleable.maintext_cardTitleTextColor, 0xffffffff);
			myViewHolder.tvUserName.setTextColor(cardTitleTextColor);
			myViewHolder.tvTopicCaption.setTextColor(cardTitleTextColor);
			int cardTextBottonColor=typeMainText.getColor(R.styleable.maintext_cardTextBottonColor, 0xffffffff);
			myViewHolder.tvTopicTime.setTextColor(cardTextBottonColor);
			myViewHolder.tvAttationNumber.setTextColor(cardTextBottonColor);
			myViewHolder.tvDiscussNumber.setTextColor(cardTextBottonColor);
			int cardtopicbackground=typeMainText.getColor(R.styleable.maintext_cardtopicbackground, 0xffffffff);
			myViewHolder.cardViewbg.setCardBackgroundColor(cardtopicbackground);
			typeMainText.recycle();
			
			TypedArray typeBottom= mContext.obtainStyledAttributes(null, R.styleable.bottom);
			int bottomTextColor = typeBottom.getColor(R.styleable.bottom_bottomTextColor, 0xffffffff);
			myViewHolder.circleImgUser.setBorderColor(bottomTextColor);
			typeBottom.recycle();
		}
		
		myViewHolder.tvUserName.setText(topicBeans.get(position).getAuthorName());
		myViewHolder.tvTopicCaption.setText(topicBeans.get(position).getCaption());
		myViewHolder.tvAttationNumber.setText(topicBeans.get(position).getAttentionNumber()+"  浏览");
		myViewHolder.tvDiscussNumber.setText(topicBeans.get(position).getDiscussNumber()+"  提问");
		myViewHolder.tvTopicTime.setText(topicBeans.get(position).getCreatedAt().substring(5, 16));
		ImageLoaderJar.getImgFromNetCircle(topicBeans.get(position).getAuthorPicture(), myViewHolder.circleImgUser);
		String imgCaptionUrl=topicBeans.get(position).getBmobFileCapImg().getFileUrl(mContext);
		ImageLoaderJar.getImgFromNet(imgCaptionUrl, myViewHolder.ivTopicCaption);
		
		viewHolder.itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mItemTopicListener!=null)
				    mItemTopicListener.onClick(topicBeans.get(position));
			}
		});
		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = mInflater.inflate(R.layout.item_topic_content, viewGroup,false);
		MyViewHolder viewHolder=new MyViewHolder(view);
		return viewHolder;
	}
	
	class MyViewHolder extends ViewHolder{

		private TextView tvUserName;
		private TextView tvTopicCaption;
		private TextView tvAttationNumber;
		private TextView tvDiscussNumber;
		private TextView tvTopicTime;
		private CircleImageView circleImgUser;
		private ImageView ivTopicCaption;
		private CardView cardViewbg;

		public MyViewHolder(View view) {
			super(view);
			tvUserName = (TextView) view.findViewById(R.id.tv_topic_userName);
			tvTopicCaption = (TextView) view.findViewById(R.id.tv_topic_caption);
			tvAttationNumber = (TextView) view.findViewById(R.id.tv_attation_number);
			tvDiscussNumber = (TextView) view.findViewById(R.id.tv_discuss_number);
			tvTopicTime = (TextView) view.findViewById(R.id.tv_topic_time);
			circleImgUser = (CircleImageView) view.findViewById(R.id.iv_topic_autherpicture);
			ivTopicCaption = (ImageView) view.findViewById(R.id.iv_topic_captionImg);
			cardViewbg = (CardView) view.findViewById(R.id.card_view_topic1);
		}
		
	}
	
	public void AddData(List<TopicBean> topicBeans){
		this.topicBeans.addAll(0, topicBeans);
		notifyItemRangeChanged(0, topicBeans.size()+1);
	}
	
	public void AddInitData(List<TopicBean> topicBeans){
		this.topicBeans.addAll(getItemCount(),topicBeans);
		notifyItemRangeChanged(getItemCount(), topicBeans.size()+getItemCount());
	}
	
	private boolean refreshTheme;
	public void refreshView(){
		refreshTheme=true;
		notifyDataSetChanged();
	}

}
