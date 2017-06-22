package com.wx.seeworld.Activity;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.wx.seeworld.R;
import com.wx.seeworld.Bean.collect;
import com.wx.seeworld.Fragment.FragmentUser;
import com.wx.seeworld.RecyclerView.RecyclerViewCollect;
import com.wx.seeworld.RecyclerView.RecyclerViewCollect.onListener;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.dao.Collectdao;

public class CollectActivity extends Activity {

	private RecyclerView mRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String themeStyle = SharedPreUtils.getComSharePref(CollectActivity.this, "theme_style", "day");
		if(themeStyle.equals("night")){
			setTheme(R.style.MyThemeNight);
		}
		
		setContentView(R.layout.activity_collect);

		initView();
	}

	private void initView() {
		List<collect> querySelectedList = Collectdao.querySelectedList(this);
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_collect);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		final RecyclerViewCollect recyclerViewCollect = new RecyclerViewCollect(this,
				querySelectedList);
		mRecyclerView.setAdapter(recyclerViewCollect);

		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		
		ImageView ivGoBack = (ImageView) findViewById(R.id.iv_collect_goback);
		ivGoBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		recyclerViewCollect.setItemLongListener(new onListener() {
			@Override
			public void longListener(final int position, final String url) {
				AlertDialog.Builder builder=new AlertDialog.Builder(CollectActivity.this);
				builder.setTitle("温情提示");
				builder.setMessage("您是否删除当前所选的收藏项。。。");
				builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Collectdao.deleteSelected(CollectActivity.this, url);
						List<collect> querySelectedList = Collectdao.querySelectedList(CollectActivity.this);
						recyclerViewCollect.changeRecycler(querySelectedList, position);
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});
		
		String isFirstEnter = SharedPreUtils.getComSharePref(this, "first_collect", "false");
		if(isFirstEnter.equals("false")){
			AlertDialog.Builder builder=new AlertDialog.Builder(CollectActivity.this);
			builder.setTitle("温情提示");
			builder.setMessage("长按可删除收藏哦");
			builder.setPositiveButton("确定", null);
			builder.create().show();
			SharedPreUtils.setComSharePref(this, "first_collect", "true");
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		FragmentUser.ChangeData();
	}

}
