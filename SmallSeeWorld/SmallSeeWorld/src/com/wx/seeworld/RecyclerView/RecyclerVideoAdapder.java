package com.wx.seeworld.RecyclerView;

import io.vov.vitamio.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.seeworld.R;
import com.wx.seeworld.Bean.VideoDemo;
import com.wx.seeworld.CacheResource.ImageLoaderJar;
import com.wx.seeworld.Tools.ViewPlayerUI;
import com.wx.seeworld.Utils.JudgmentIsNetUtils;
import com.wx.seeworld.Utils.ShareUtils;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.View.CircleImageView;
import com.wx.seeworld.loading.MonIndicator;

public class RecyclerVideoAdapder extends RecyclerView.Adapter<ViewHolder> {

	private List<VideoDemo> videoLists = new ArrayList<VideoDemo>();
	private LayoutInflater mInflater;
	public static int currentPlayer = -1;
	private Context context;
	private boolean isRefresh;

	private static final int HOLDER_BASE = 1001;
	private static final int HOLDER_FOOTER = 1002;
	private boolean isloadingMore;

	private MonIndicator loadingMore;
	private LinearLayout llLoadingError;
	
	private onItemListener mItemListener;
	public void setItemListener(onItemListener mItemListener) {
		this.mItemListener = mItemListener;
	}

	public interface onItemListener {
		void onClickShare();
	}
	
	public void successLoadingMore() {
		isloadingMore = false;
	}
	
	public void failureLoadingMore() {
		llLoadingError.setVisibility(View.VISIBLE);
		isloadingMore = false;
	}

	public RecyclerVideoAdapder(Context context) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return videoLists.size();
	}

	@SuppressLint("Recycle")
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

		if (viewHolder instanceof VideoHeaderHolder) {
			
			final VideoHeaderHolder mHolder = ((VideoHeaderHolder) viewHolder);
			
			if(refreshTheme){
				TypedArray typeMainText = context.obtainStyledAttributes(null, R.styleable.maintext);
				int cardBgColor = typeMainText.getColor(R.styleable.maintext_cardvideobackground, 0xffffffff);
				((VideoHeaderHolder) viewHolder).cardviewbg.setCardBackgroundColor(cardBgColor);
				int cardTitleColor=typeMainText.getColor(R.styleable.maintext_cardTitleTextColor, 0xffffffff);
				((VideoHeaderHolder) viewHolder).tvTitle.setTextColor(cardTitleColor);
				((VideoHeaderHolder) viewHolder).tvAutherName.setTextColor(cardTitleColor);
				
				typeMainText.recycle();
			}

			mHolder.ivPicture.setImageResource(R.drawable.default_image);

			if (isRefresh) {
				mHolder.ivPlayer.setVisibility(View.VISIBLE);
				mHolder.ivPicture.setVisibility(View.VISIBLE);
				mHolder.progressLoading.setVisibility(View.INVISIBLE);
				mHolder.llVideoBottom.setVisibility(View.INVISIBLE);
				mHolder.mVideoView.setVisibility(View.INVISIBLE);
				isRefresh = false;
			}

			if (currentPlayer != position) {
				mHolder.ivPlayer.setVisibility(View.VISIBLE);
				mHolder.ivPicture.setVisibility(View.VISIBLE);
				mHolder.progressLoading.setVisibility(View.INVISIBLE);
				mHolder.llVideoBottom.setVisibility(View.INVISIBLE);
				mHolder.mVideoView.setVisibility(View.INVISIBLE);
			} else {
				mHolder.ivPlayer.setVisibility(View.INVISIBLE);
				mHolder.ivPicture.setVisibility(View.INVISIBLE);
				mHolder.progressLoading.setVisibility(View.VISIBLE);
				mHolder.llVideoBottom.setVisibility(View.INVISIBLE);
				mHolder.mVideoView.setVisibility(View.VISIBLE);
			}

			mHolder.tvTitle.setText(videoLists.get(position).getCaption());
			mHolder.tvAutherName.setText("       "
					+ videoLists.get(position).getScreen_name());
			ImageLoaderJar.getImgFromNetCircle(videoLists.get(position)
					.getAvatar(), mHolder.ivAutherPicture);
			ImageLoaderJar.getImgFromNet(videoLists.get(position)
					.getCover_pic(), mHolder.ivPicture);

			mHolder.ivPicture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String mobilePlayer = SharedPreUtils.getComSharePref(
							context, "mobile_player", "false");
					int connect = JudgmentIsNetUtils
							.isNetworkAvailable(context);
					if (connect == JudgmentIsNetUtils.NOCONNECT) {
						Toast.makeText(context, "请打开网络", Toast.LENGTH_SHORT)
								.show();
						return;
					} else if (connect == JudgmentIsNetUtils.WIFI
							|| mobilePlayer.equals("true")) {
						if (currentPlayer != -1) {// 一次一个顺序
							ViewPlayerUI.StopPlayer();
							isRefresh = true;
							notifyItemChanged(currentPlayer);
						}
						mHolder.ivPlayer.setVisibility(View.INVISIBLE);
						mHolder.ivPicture.setVisibility(View.INVISIBLE);
						mHolder.progressLoading.setVisibility(View.VISIBLE);
						mHolder.llVideoBottom.setVisibility(View.INVISIBLE);
						mHolder.mVideoView.setVisibility(View.VISIBLE);
						ViewPlayerUI.Player(mHolder.mVideoView, context,
								videoLists.get(position).getUrl(),
								mHolder.progressLoading, mHolder.ivPlayer,
								mHolder.ivPicture, mHolder.llVideoBottom,
								mHolder.ivVideoPlayer, mHolder.tvCurrentTime,
								mHolder.seekBar, mHolder.tvSumTime,
								mHolder.ivVideoFull);
						currentPlayer = position;
					} else {

						if (connect == JudgmentIsNetUtils.MOBILE) {
							if (mobilePlayer.equals("false")) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										context);
								builder.setTitle("温情提示");
								builder.setMessage("你当前处于数据连接状态是否开启播放");
								builder.setPositiveButton("确定",
										new AlertDialog.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												SharedPreUtils
														.setComSharePref(
																context,
																"mobile_player",
																"true");
												if (currentPlayer != -1) {// 一次一个顺序
													ViewPlayerUI.StopPlayer();
													isRefresh = true;
													notifyItemChanged(currentPlayer);
												}
												mHolder.ivPlayer
														.setVisibility(View.INVISIBLE);
												mHolder.ivPicture
														.setVisibility(View.INVISIBLE);
												mHolder.progressLoading
														.setVisibility(View.VISIBLE);
												mHolder.llVideoBottom
														.setVisibility(View.INVISIBLE);
												mHolder.mVideoView
														.setVisibility(View.VISIBLE);
												ViewPlayerUI
														.Player(mHolder.mVideoView,
																context,
																videoLists
																		.get(position)
																		.getUrl(),
																mHolder.progressLoading,
																mHolder.ivPlayer,
																mHolder.ivPicture,
																mHolder.llVideoBottom,
																mHolder.ivVideoPlayer,
																mHolder.tvCurrentTime,
																mHolder.seekBar,
																mHolder.tvSumTime,
																mHolder.ivVideoFull);
												currentPlayer = position;
											}
										});
								builder.setNegativeButton("取消",
										new AlertDialog.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												return;
											}
										});
								builder.show();
							}

						}
					}
				}
			});

			mHolder.ivShare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.show();
					Window window = alertDialog.getWindow();
					window.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT)); // 设置背景无色
					View view = View.inflate(context,
							R.layout.item_dialog_share, null);
					window.setContentView(view);
					(view.findViewById(R.id.iv_qqzon))
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ShareUtils.shareCustomize(ShareUtils.QQZON,
											context, videoLists.get(position)
													.getCaption(), videoLists
													.get(position).getUrl(),
											videoLists.get(position)
													.getCover_pic());
									alertDialog.dismiss();
								}
							});
					(view.findViewById(R.id.iv_weichat))
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ShareUtils.shareCustomize(
											ShareUtils.WECOMMENT, context,
											videoLists.get(position)
													.getCaption(), videoLists
													.get(position).getUrl(),
											videoLists.get(position)
													.getCover_pic());
									alertDialog.dismiss();
								}
							});
					(view.findViewById(R.id.iv_weiblog))
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ShareUtils.shareCustomize(
											ShareUtils.SINAWEB, context,
											videoLists.get(position)
													.getCaption(), videoLists
													.get(position).getUrl(),
											videoLists.get(position)
													.getCover_pic());
									alertDialog.dismiss();
								}
							});
				}
			});

		}else if(viewHolder instanceof VideoFooterHolder){
			TypedArray typeMainText = context.obtainStyledAttributes(null, R.styleable.maintext);
			int loadingBg=typeMainText.getColor(R.styleable.maintext_loading_bg, 0xffffffff);
			loadingMore.setBackgroundColor(loadingBg);
			llLoadingError.setBackgroundColor(loadingBg);
			typeMainText.recycle();
			
			//刷新
			if(mItemListener!=null && ! isloadingMore){
				mItemListener.onClickShare();
				isloadingMore=true;
				llLoadingError.setVisibility(View.INVISIBLE);
			}
			
		}
		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
		if (position == HOLDER_FOOTER) {
			View view = mInflater.inflate(R.layout.item_video_loading,viewGroup, false);
			VideoFooterHolder viewHolder = new VideoFooterHolder(view);
			return viewHolder;
		} else {
			View view = mInflater.inflate(R.layout.item_video, viewGroup, false);
			VideoHeaderHolder viewHolder = new VideoHeaderHolder(view);
			return viewHolder;
		}

	}

	@Override
	public int getItemViewType(int position) {
		if (position >= getItemCount() - 1 && ! isloadingMore ) {
			return HOLDER_FOOTER;
		}
		return HOLDER_BASE;
	}

	class VideoHeaderHolder extends ViewHolder {

		private TextView tvTitle;
		private TextView tvAutherName;
		private ImageView ivPicture;
		private CircleImageView ivAutherPicture;
		private VideoView mVideoView;
		private RelativeLayout progressLoading;
		private ImageView ivPlayer;

		private LinearLayout llVideoBottom;
		private ImageView ivVideoPlayer;
		private TextView tvCurrentTime;
		private TextView tvSumTime;
		private ImageView ivVideoFull;
		private SeekBar seekBar;
		private ImageView ivShare;
		private CardView cardviewbg;

		public VideoHeaderHolder(View view) {
			super(view);
			tvTitle = (TextView) view.findViewById(R.id.tv_video_title);
			tvAutherName = (TextView) view
					.findViewById(R.id.tv_video_autherName);
			ivAutherPicture = (CircleImageView) view
					.findViewById(R.id.iv_video_autherpicture);
			// 播放窗口
			mVideoView = (VideoView) view.findViewById(R.id.video_view); // 视频
			progressLoading = (RelativeLayout) view
					.findViewById(R.id.video_loading); // 加载
			ivPicture = (ImageView) view.findViewById(R.id.iv_video_picture); // 大图
			ivPlayer = (ImageView) view.findViewById(R.id.iv_player);
			// 播放按钮组合
			llVideoBottom = (LinearLayout) view
					.findViewById(R.id.ll_viedo_bottom);
			ivVideoPlayer = (ImageView) view.findViewById(R.id.iv_video_player);
			tvCurrentTime = (TextView) view.findViewById(R.id.tv_video_starttime);
			seekBar = (SeekBar) view.findViewById(R.id.seek_bar_video);
			tvSumTime = (TextView) view.findViewById(R.id.tv_video_sumtime);
			ivVideoFull = (ImageView) view.findViewById(R.id.iv_video_full);
			ivShare = (ImageView) view.findViewById(R.id.iv_video_share);
			cardviewbg = (CardView) view.findViewById(R.id.card_view_video);
		}
	}

	class VideoFooterHolder extends ViewHolder {
		public VideoFooterHolder(View view) {
			super(view);
			loadingMore = (MonIndicator) view.findViewById(R.id.loading_monindicator_video);
			llLoadingError = (LinearLayout) view.findViewById(R.id.ll_loading_error);
			loadingMore.setColors(new int[] { 0xFF66cccc, 0xFFccff99,0xFFccffcc, 0xFF66cc99, 0xFF66cc66 });
		}

	}
	
	private boolean refreshTheme;
	
	public void refreshTheme(){
		refreshTheme=true;
		notifyDataSetChanged();
	}

	public void addData(List<VideoDemo> videoLists,int loadingMore) {
		if(loadingMore==HOLDER_BASE){
			this.videoLists.addAll(0, videoLists);
			notifyItemRangeChanged(0, videoLists.size() - 1);
		}else{
			this.videoLists.addAll(getItemCount(), videoLists);
			notifyItemRangeChanged(getItemCount(), this.videoLists.size()+ getItemCount());
		}
	}

}
