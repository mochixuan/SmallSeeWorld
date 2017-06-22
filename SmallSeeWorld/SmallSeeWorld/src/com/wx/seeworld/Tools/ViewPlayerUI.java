package com.wx.seeworld.Tools;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.seeworld.R;
import com.wx.seeworld.Activity.VideoPlayer;
import com.wx.seeworld.RecyclerView.RecyclerVideoAdapder;
import com.wx.seeworld.Utils.JudgmentIsNetUtils;
import com.wx.seeworld.Utils.ToastUtils;

public class ViewPlayerUI{

	public static VideoView mVideoView;
	private static Context mContext;
	private static String html;
	public static RelativeLayout progressLoading;
	private static ImageView mIvPlayer;
	private static ImageView mIvPicture;

	private static LinearLayout llVideoBottom;
	private static ImageView ivVideoPlayer;
	private static TextView tvCurrentTime;
	private static TextView tvSumTime;
	private static ImageView ivVideoFull;
	private static SeekBar seekBar;

	private static int AGAINREQUEST = 0;
	protected static final int PROGRESS = 0;
	public static long currentPlayerLocation = 0;
	public static boolean isRestart;
	private static boolean isLLButton = true;
	private static String mVideoUrl;
	public static boolean idDestroy;

	
	public static void Player(VideoView videoView,Context context,String Html,RelativeLayout progressloading
			,ImageView ivPlayer,ImageView ivPicture,LinearLayout llvideoBottom,ImageView ivvideoPlayer,
			TextView tvcurrentTime,SeekBar seekbar,TextView tvsumTime,ImageView ivvideoFull) {
		mVideoView = videoView;
		mContext = context;
		html = Html;
		progressLoading = progressloading;
		mIvPlayer = ivPlayer;
		mIvPicture = ivPicture;

		llVideoBottom = llvideoBottom;
		ivVideoPlayer = ivvideoPlayer;
		tvCurrentTime = tvcurrentTime;
		seekBar = seekbar;
		tvSumTime = tvsumTime;
		ivVideoFull = ivvideoFull;
		
		llVideoBottom=llvideoBottom;
		ivVideoPlayer=ivvideoPlayer;
		tvCurrentTime=tvcurrentTime;
		seekBar=seekbar;
		tvSumTime=tvsumTime;
		ivVideoFull=ivvideoFull;
		
		getVideoData();
	}

	// 初始化数据
	private static void initData(String videoUrl) {
		
		idDestroy=false;
		initListener();
		mVideoView.setVideoPath(videoUrl);
		initPapare();

		mVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(mContext, "播放错误哦", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

	}

	public static void initPapare() {
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mVideoView.start();
				mVideoView.setBufferSize(10 * 1024);
				isLLButton = true;
				idDestroy = false;
				progressLoading.setVisibility(View.INVISIBLE);
				llVideoBottom.setVisibility(View.VISIBLE);
				mVideoView.setVisibility(View.VISIBLE);
				String sumTime = StringUtils.generateTime(mVideoView.getDuration());
				tvSumTime.setText(sumTime);
				seekBar.setMax((int) mVideoView.getDuration());
				if (currentPlayerLocation == 0) {
					handler.sendEmptyMessage(PROGRESS);
				}
			}
		});
	}

	// handler
	private static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PROGRESS:

				setIsPlayerListener();

				long currentTime = mVideoView.getCurrentPosition();
				String CurrentTime = StringUtils.generateTime(currentTime);

				tvCurrentTime.setText(CurrentTime);
				seekBar.setProgress((int) currentTime);

				seekBar.setSecondaryProgress((int) (mVideoView.getBufferPercentage() * mVideoView.getDuration() / 100));

				if (mVideoView.isPlaying()) {
					ivVideoPlayer.setImageResource(R.drawable.video_start_player);
				} else {
					ivVideoPlayer.setImageResource(R.drawable.video_pause_player);
				}

				if (!idDestroy) {
					handler.removeMessages(PROGRESS);
					handler.sendEmptyMessageDelayed(PROGRESS, 1000);
				}
				break;

			default:
				break;
			}
		};
	};

	// 设置监听
	@SuppressLint("ClickableViewAccessibility")
	
	private static void initListener() {

		// 播放监听
		ivVideoPlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mVideoView.isPlaying()) {
					ivVideoPlayer
							.setImageResource(R.drawable.video_pause_player);
					mVideoView.pause();
				} else {
					ivVideoPlayer
							.setImageResource(R.drawable.video_start_player);
					mVideoView.start();
				}
			}
		});

		// SeekBar监听
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
				mVideoView.seekTo(progress+3000);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});

		// 播放结束监听
		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				StopPlayer();
				RecyclerVideoAdapder.currentPlayer = -1;
				restoreView();
			}
		});

		// 设置全屏播放
		ivVideoFull.setOnClickListener(new OnClickListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, VideoPlayer.class);
				intent.putExtra("video_Url", mVideoUrl);
				intent.putExtra("currentPlayer_Time",mVideoView.getCurrentPosition() + "");
				mContext.startActivity(intent);
				((FragmentActivity) mContext).overridePendingTransition(R.anim.translate_in, R.anim.scale_translate_out);
			}
		});

		// 单击监听
		mVideoView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					if (isLLButton) {
						llVideoBottom.setVisibility(View.INVISIBLE);
					} else {
						llVideoBottom.setVisibility(View.VISIBLE);
					}
					isLLButton = !isLLButton;
				}
				return false;
			}
		});

	}
	
	public static int isVisibleButtom = 0;

	protected static void setIsPlayerListener() {
		if (mVideoView.isPlaying() && mVideoView != null && !isRestart) {
			currentPlayerLocation = seekBar.getProgress();
		} else if (!mVideoView.isPlaying() && mVideoView != null && !isRestart) {
			isRestart = true;
		} else if (mVideoView.isPlaying() && mVideoView != null && isRestart) {
			mVideoView.seekTo(currentPlayerLocation);
			isRestart = false;
		}

		if (isLLButton && isVisibleButtom > 3) {
			isLLButton = false;
			isVisibleButtom = 0;
			llVideoBottom.setVisibility(View.INVISIBLE);
		} else if (isLLButton && isVisibleButtom <= 3) {
			isVisibleButtom++;
		}

	}
	// 初始化
	private static void initOrignal(Context context) {
		Vitamio.isInitialized(context);
	}

	// 停止播放
	public static void StopPlayer() {
		if (mVideoView != null) {
			mVideoView.stopPlayback();
			idDestroy = true;
			isRestart = false;
			currentPlayerLocation = 0;
			isVisibleButtom = 0;
			idDestroy=true;
		}
	}
	/*-----------------------------videoUrl----------------------------------------*/
	private static void getVideoData() {
		int networkAvailable = JudgmentIsNetUtils.isNetworkAvailable(mContext);
		if (networkAvailable == JudgmentIsNetUtils.NOCONNECT) {
			ToastUtils.makeText(mContext, "未连接网络");
			return;
		}
		if (html == null)
			return;
		new AsyncTask<String, Void, Document>() {
			
			@Override
			protected Document doInBackground(String... params) {
				String html = params[0];
				try {
					Document document = Jsoup.connect(html).timeout(10000)
							.data("query", "Java").userAgent("Mozilla") // 证明是电脑
							.cookie("auth", "token").get();
					return document;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			protected void onPostExecute(Document document) {
				super.onPostExecute(document);
				if (document == null) {
					if( AGAINREQUEST==1){
						AGAINREQUEST=0;
						Toast.makeText(mContext, "服务器错误或网络不稳定", Toast.LENGTH_SHORT).show();
						RecyclerVideoAdapder.currentPlayer=-1;
						mIvPicture.setVisibility(View.VISIBLE);
						mIvPlayer.setVisibility(View.VISIBLE);
						progressLoading.setVisibility(View.INVISIBLE);
						mVideoView.setVisibility(View.INVISIBLE);
					}else{
						++AGAINREQUEST;
						getVideoData();//请求两次
					}
					return;
				}
				String videoUrl = document.getElementById("detailVideo").attr("data-video");
				mVideoUrl = videoUrl;
				startPlayerInit(videoUrl);
			}
			
		}.execute(html);
	
	}

	
	public static void startPlayerInit(String videoUrl) {
		initOrignal(mContext);
		initData(videoUrl);
	}

	/*---------------------------------------------------------------------*/

	public static void restoreView() {
		mIvPlayer.setVisibility(View.VISIBLE);
		mIvPicture.setVisibility(View.VISIBLE);
		progressLoading.setVisibility(View.INVISIBLE);
		llVideoBottom.setVisibility(View.INVISIBLE);
		isLLButton = false;
		mVideoView.setVisibility(View.INVISIBLE);
	}
	
	public static void refreshView() {
		mIvPlayer.setVisibility(View.INVISIBLE);
		mIvPicture.setVisibility(View.INVISIBLE);
		progressLoading.setVisibility(View.INVISIBLE);
		llVideoBottom.setVisibility(View.INVISIBLE);
		isLLButton = false;
		mVideoView.setVisibility(View.VISIBLE);
	}

}
