package com.wx.seeworld.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.example.gridviewa.DragGridView;
import com.example.gridviewa.DragGridView.OnChanageListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.wx.seeworld.R;
import com.wx.seeworld.Utils.AnimationUtils;
import com.wx.seeworld.Utils.DensityUtils;
import com.wx.seeworld.Utils.NaviStaticBarHightUtils;
import com.wx.seeworld.dao.Titledao;

public class GridViewTools {

	private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();

	private String[] titles = null;
	private Context context;
	private View contentView;
	private DragGridView mGridView;
	private ImageView imageViewAdd;
	private int height;
	private PopupWindow pop;
	private SimpleAdapter mSimpleAdapter;
	private SlidingTabLayout slidingTabLayout;
	private int barHight;
	private int windowHight;
	private ImageView imageViewAddC;

	public interface RefreshTopTitle {
		void refresh();
		void AddClick();
	}

	static RefreshTopTitle mRefreshTopTitle;

	public static void setRefreshTopTitle(RefreshTopTitle refreshTopTitle) {
		mRefreshTopTitle = refreshTopTitle;
	}

	public GridViewTools(String[] strings, View contentView,
			DragGridView mGridView, Context context, ImageView imageViewAdd,
			SlidingTabLayout slidingTabLayout, int windowHight) {
		titles = strings;
		this.context = context;
		this.mGridView = mGridView;
		this.imageViewAdd = imageViewAdd;
		this.contentView = contentView;
		this.slidingTabLayout = slidingTabLayout;
		this.windowHight = windowHight;
		initView();
		initData0();
		initData();
	}

	private void initView() {
		imageViewAddC = (ImageView) contentView
				.findViewById(R.id.iv_gridview_click);
		
		imageViewAddC.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				imageViewAddC.startAnimation(getAnimAddC());
				contentView.startAnimation(getAnimationC());
			}
		});
		
	}

	protected void initHashMap() {
		if (dataSourceList != null) {
			HashMap<String, Object> hashMapChange = new HashMap<String, Object>();
			for (int i = 0; i < dataSourceList.size(); i++) {
				HashMap<String, Object> hashMap = dataSourceList.get(i);
				String value = (String) hashMap.get("item_text");
				hashMapChange.put("" + i, value);
			}
			Titledao.addTitleDao(context, hashMapChange);
			if (mRefreshTopTitle != null) {
				mRefreshTopTitle.refresh();
			}
		}
	}

	private void initData0() {
		for (int i = 0; i < titles.length; i++) {
			HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
			itemHashMap.put("item_text", titles[i]);
			dataSourceList.add(itemHashMap);
		}
		mSimpleAdapter = new SimpleAdapter(context, dataSourceList,
				R.layout.item_gridview_text, new String[] { "item_text" },
				new int[] { R.id.item_text });
	}

	private void initData() {
		
		slidingTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					public void onGlobalLayout() {
						height = slidingTabLayout.getHeight(); // 像数
						slidingTabLayout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

		imageViewAdd.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mRefreshTopTitle.AddClick();
				imageViewAdd.startAnimation(getAnimAdd());
				if (pop == null) {
					contentView.startAnimation(AnimationUtils.getAnimationIn());
					setEvent(mSimpleAdapter);
					barHight = NaviStaticBarHightUtils.getNavigationBarHight(context);
					barHight = (int) DensityUtils.pxChangedp(context, barHight);
					pop = new PopupWindow(contentView,LinearLayout.LayoutParams.MATCH_PARENT, windowHight- barHight);
					pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置
					pop.setFocusable(true);
					pop.showAsDropDown(slidingTabLayout, 0, -height); // 像数为单位84
				}else if (pop != null && !pop.isShowing()) {
					// 有可能隐藏了
					contentView.startAnimation(AnimationUtils.getAnimationIn());
					pop.showAsDropDown(slidingTabLayout, 0, -height); // 像数为单位84
				}
			}
		});
	}

	private void setEvent(final SimpleAdapter mSimpleAdapter) {
		mGridView.setAdapter(mSimpleAdapter);
		mGridView.setOnChangeListener(new OnChanageListener() {
			public void onChange(int from, int to) {
				HashMap<String, Object> temp = dataSourceList.get(from);
				if (from < to) {
					for (int i = from; i < to; i++) {
						Collections.swap(dataSourceList, i, i + 1);
					}
				} else if (from > to) {
					for (int i = from; i > to; i--) {
						Collections.swap(dataSourceList, i, i - 1);
					}
				}
				dataSourceList.set(to, temp);
				mSimpleAdapter.notifyDataSetChanged();
			}
		});
	}

	private Animation getAnimAdd() {
		RotateAnimation animation = new RotateAnimation(0, 480f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(1000);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				imageViewAddC.setVisibility(View.VISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});
		return animation;
	}

	private Animation getAnimAddC() {
		RotateAnimation animation = new RotateAnimation(0, 480f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(1000);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}
			public void onAnimationRepeat(Animation animation) {
			}
			public void onAnimationEnd(Animation animation) {
				imageViewAddC.setVisibility(View.INVISIBLE);
				pop.dismiss();
				pop = null;
				initHashMap();
			}
		});

		return animation;
	}


	private Animation getAnimationC() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
		alphaAnimation.setDuration(1000);
		return alphaAnimation;
	}
	
}
