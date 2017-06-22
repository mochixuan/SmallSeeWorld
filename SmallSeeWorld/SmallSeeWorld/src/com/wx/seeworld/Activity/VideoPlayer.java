package com.wx.seeworld.Activity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.seeworld.R;
import com.wx.seeworld.RecyclerView.RecyclerVideoAdapder;
import com.wx.seeworld.Tools.ViewPlayerUI;

public class VideoPlayer extends Activity {

	private String videoUrl;
	private VideoView mVideoView;
	private GestureDetector mDetector;

	private ImageView ivVideoPlayer;
	private TextView tvCurrentTime;
	private TextView tvSumTime;
	private ImageView ivVideoFull;
	private SeekBar seekBar;
	private RelativeLayout progressLoading;
	private LinearLayout llVideoBottom;

	protected static final int PROGRESS = 0;
	private boolean idDestroy;
	private boolean isLLButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initOrignal();
		setContentView(R.layout.activity_full_player);
		videoUrl = getIntent().getStringExtra("video_Url");
		currentPlayerLocation =Integer.parseInt(getIntent().getStringExtra("currentPlayer_Time"))+3000;
		initView();
		initData();
	}

	@SuppressLint("InlinedApi")
	private void initOrignal() {
		Vitamio.isInitialized(this);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏

		if (Build.VERSION.SDK_INT >= 14) { // 隐藏导航栏
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
					new OnSystemUiVisibilityChangeListener() {
						@Override
						public void onSystemUiVisibilityChange(int visibility) {
							if (visibility == 0) {
								ShowOrHide();
							}
						}
					});
		}

	}

	private void initView() {
		mVideoView = (VideoView) findViewById(R.id.video_view_full);

		llVideoBottom = (LinearLayout) findViewById(R.id.ll_viedo_bottom1);
		ivVideoPlayer = (ImageView) findViewById(R.id.iv_video_player1);
		tvCurrentTime = (TextView) findViewById(R.id.tv_video_starttime1);
		seekBar = (SeekBar) findViewById(R.id.seek_bar_video1);
		tvSumTime = (TextView) findViewById(R.id.tv_video_sumtime1);
		ivVideoFull = (ImageView) findViewById(R.id.iv_video_full1);

		progressLoading = (RelativeLayout) findViewById(R.id.video_loading); // 加载
	}

	private void initData() {

		initListener();
		mVideoView.setBufferSize(18 * 1024);
		mVideoView.setVideoPath(videoUrl);
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				
				mVideoView.start();
				progressLoading.setVisibility(View.INVISIBLE);
				llVideoBottom.setVisibility(View.VISIBLE);
				
				String sumTime = StringUtils.generateTime(mVideoView
						.getDuration());
				tvSumTime.setText(sumTime);
				seekBar.setMax((int) mVideoView.getDuration());

				idDestroy = false;
				isLLButton=true;
				
				handler.sendEmptyMessage(PROGRESS);
			}
		});

		mDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onSingleTapConfirmed(MotionEvent e) {
						ShowOrHide();
						return super.onSingleTapConfirmed(e);
					}

				});

		mVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(VideoPlayer.this, "播放错误哦", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});

	}

	// handler
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PROGRESS:
				setIsPlayerListener();

				long currentTime = mVideoView.getCurrentPosition();
				String CurrentTime = StringUtils.generateTime(currentTime);

				tvCurrentTime.setText(CurrentTime);
				seekBar.setProgress((int) currentTime);

				seekBar.setSecondaryProgress((int) (mVideoView
						.getBufferPercentage() * mVideoView.getDuration() / 100));
				if (mVideoView.isPlaying()) {
					ivVideoPlayer.setImageResource(R.drawable.video_start_player);
				} else {
					ivVideoPlayer
							.setImageResource(R.drawable.video_pause_player);
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
	
	private int isVisibleButtom = 0;
	private int currentPlayerLocation=0;
	private boolean firstPlayer=true;
	//三秒消失
	@SuppressLint("InlinedApi")
	private void setIsPlayerListener() {
		
		if(firstPlayer){
			mVideoView.seekTo(currentPlayerLocation+1000);
			firstPlayer=false;
		}
		
		if (isLLButton && isVisibleButtom > 3) {
			isLLButton = false;
			isVisibleButtom = 0;
			llVideoBottom.setVisibility(View.INVISIBLE);
			if (Build.VERSION.SDK_INT >= 14) { // 隐藏导航栏
				getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			}
		} else if (isLLButton && isVisibleButtom <= 3) {
			isVisibleButtom++;
		}
		
	}
	

	@SuppressLint("InlinedApi")
	public void ShowOrHide() {
		if (isLLButton) {
			llVideoBottom.setVisibility(View.INVISIBLE);
			isLLButton=false;
			if (Build.VERSION.SDK_INT >= 14) { // 隐藏导航栏
				getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			}
		} else {
			llVideoBottom.setVisibility(View.VISIBLE);
			isLLButton=true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mVideoView.stopPlayback();
		idDestroy = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mVideoView.pause();
	}

	// 设置监听
	@SuppressLint("ClickableViewAccessibility")
	private void initListener() {

		// 播放监听
		ivVideoPlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mVideoView.isPlaying()) {
					ivVideoPlayer.setImageResource(R.drawable.video_pause_player);
					mVideoView.pause();
				} else {
					ivVideoPlayer.setImageResource(R.drawable.video_start_player);
					mVideoView.start();
				}
			}
		});

		// SeekBar监听
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
				mVideoView.seekTo(progress);
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
				ViewPlayerUI.StopPlayer();
				ViewPlayerUI.restoreView();
				RecyclerVideoAdapder.currentPlayer = -1;
				finish();
				overridePendingTransition(R.anim.translate_in, R.anim.scale_translate_out);
			}
		});

		//设置恢复播放
		ivVideoFull.setOnClickListener(new OnClickListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public void onClick(View v) {
				
				finish();
				overridePendingTransition(R.anim.translate_in, R.anim.scale_translate_out);
				ViewPlayerUI.currentPlayerLocation=mVideoView.getCurrentPosition();
				
			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		return true;
	}

}
