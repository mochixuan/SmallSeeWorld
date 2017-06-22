package com.wx.seeworld.Utils;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.TextView;

public class TimerUtils {

	private static int duration;
	private static String stCotent;
	private static TextView textView;
	private static Timer timer;
	private static TimerTask timerTask;

	public static OnTimerListener mOnTimerListener;

	public interface OnTimerListener {
		void end();
	}
	
	public static void setmOnTimerListener(OnTimerListener mOnTimerListener) {
		if(TimerUtils.mOnTimerListener  == null)
		TimerUtils.mOnTimerListener = mOnTimerListener;
	}

	public static void setMyTimer(int time, TextView tv, String content) {
		duration = time;
		textView = tv;
		stCotent = content;

		initEvent();
	}

	private static void initEvent() {
		if (timer == null) {
			timer = new Timer();
		}

		timerTask = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};

		timer.schedule(timerTask, 0, 950);
	}

	static final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (duration == 1) {
				stopTimer();
				if (mOnTimerListener != null) {
					stopTimer();
					mOnTimerListener.end();
				}
			}
			textView.setText(stCotent + " " + (duration--));
		};
	};

	public static void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			if (timerTask != null) {
				timerTask.cancel();
				timerTask = null;
			}
		}
	}
	
	public static void JumpAdvertising(){
		mOnTimerListener.end();
	}

}
