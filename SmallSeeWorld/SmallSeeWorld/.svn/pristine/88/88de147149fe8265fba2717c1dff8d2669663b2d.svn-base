package com.wx.seeworld.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wx.seeworld.R;
import com.wx.seeworld.Utils.DensityUtils;
import com.wx.seeworld.Utils.SharedPreUtils;

public class GuideActivity extends Activity {

	private ViewPager viewPager;
	private List<ImageView> mImgLists;
	private int pointwidth;
	private int[] grayPoint;
	private int[] ImgGuideId = new int[] { R.drawable.launch_0,
			R.drawable.launch_1, R.drawable.launch_2 };
	private LinearLayout linerLayout;
	private View viewPoint;
	private TextView guideText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		initView();
		initData();
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
		guideText = (TextView) findViewById(R.id.guide_text_enter);
		mImgLists = new ArrayList<ImageView>();
		linerLayout = (LinearLayout) findViewById(R.id.guide_linear);
		viewPoint = findViewById(R.id.guide_point_move);
	}

	@SuppressWarnings("deprecation")
	private void initData() {

		grayPoint = new int[] { DensityUtils.dpChangepx(this, 15),
				DensityUtils.dpChangepx(this, 20) }; // 1圆点大小 2圆点之间的距离px
														// 15dp转化为px(在这里初始化)

		for (int i = 0; i < ImgGuideId.length; i++) {
			ImageView imageView = new ImageView(GuideActivity.this);
			imageView.setImageResource(ImgGuideId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImgLists.add(imageView);
		}

		// 设置圆点 绿点默认是压在默认点的最左边上
		for (int i = 0; i < ImgGuideId.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.share_point_white);
			LayoutParams params = new LinearLayout.LayoutParams(grayPoint[0],
					grayPoint[0]);
			if (i != 0) {
				params.leftMargin = grayPoint[1]; // 是从原来位置偏移的距离
			}
			point.setLayoutParams(params);
			linerLayout.addView(point);
		}

		linerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						pointwidth = linerLayout.getChildAt(1).getLeft()
								- linerLayout.getChildAt(0).getLeft();
						linerLayout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

		MyPagerAdapter adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				if (position == ImgGuideId.length - 1) {
					guideText.setVisibility(View.VISIBLE);
					
					linerLayout.setVisibility(View.INVISIBLE);
					viewPoint.setVisibility(View.INVISIBLE);
					
					guideText.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(GuideActivity.this,MainActivity.class);
							startActivity(intent);
							SharedPreUtils.setSharePref(GuideActivity.this, "config_guide", "end");
							GuideActivity.this.finish();
						}
					});
				} else {
					guideText.setVisibility(View.GONE);
					linerLayout.setVisibility(View.VISIBLE);
					viewPoint.setVisibility(View.VISIBLE);
				}
			}

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPoint
					.getLayoutParams();

			// positionOffset百分比
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				float pointRemove = positionOffset * pointwidth; // 移到的距离
				params.leftMargin = (int) pointRemove + pointwidth * position;
				viewPoint.setLayoutParams(params); // 设置是从原来的位置上开始移动
			}
			public void onPageScrollStateChanged(int position) {
			}
		});
	}

	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mImgLists.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mImgLists.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mImgLists.get(position));
			return mImgLists.get(position);
		}
	}

}
