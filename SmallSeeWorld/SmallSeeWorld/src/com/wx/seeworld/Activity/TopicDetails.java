package com.wx.seeworld.Activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

import com.wx.seeworld.R;
import com.wx.seeworld.Bean.TopicBean;
import com.wx.seeworld.Bean.TopicCommentBean;
import com.wx.seeworld.Bean.TopicHeaderBean;
import com.wx.seeworld.RecyclerView.RecyclerTopicDetails;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.SystemBarTintUtils;

public class TopicDetails extends Activity {

	private RecyclerView mRecyclerView;
	private RecyclerTopicDetails mRecyclerAdapter;
	private TopicHeaderBean headerBean;
	String topicObjectId;
	private LinearLayout llSumbit;
	private EditText edDiscuss;
	private TextView tvSubmit;
	private ImageView ivSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String themeStyle = SharedPreUtils.getComSharePref(TopicDetails.this,
				"theme_style", "day");
		if (themeStyle.equals("night")) {
			setTheme(R.style.MyThemeNight);
		}else{
			setTheme(R.style.MyThemeDefault);
		}
		setContentView(R.layout.activity_topic_details);
		
		initSystemBarTint();
		topicObjectId = getIntent().getStringExtra("topic_objectId");

		getIntentData();
		initView();
		initData();
	}

	private void getIntentData() {
		headerBean = new TopicHeaderBean();
		String autherImgUrl = getIntent().getStringExtra("author_img_url");
		String authorName = getIntent().getStringExtra("author_name");
		String topicCaption = getIntent().getStringExtra("topic_caption");
		String captionImgBgUrl = getIntent().getStringExtra("caption_imgUrl");
		String topicBrowseNumber = getIntent().getStringExtra(
				"topic_attation_number");
		String topicCreatedAt = getIntent().getStringExtra("topic_createdAt");
		String topicDiscussNumber = getIntent().getStringExtra(
				"topic_discuss_number");

		headerBean.setAuthorPicture(autherImgUrl);
		headerBean.setAuthorName(authorName);
		headerBean.setCaption(topicCaption);
		headerBean.setCaptionImg(captionImgBgUrl);
		headerBean.setBrowseNumber(topicBrowseNumber);
		headerBean.setCreatedAt(topicCreatedAt);
		headerBean.setDiscussNumber(topicDiscussNumber);

	}

	private void initView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_topic_detail);
		mRecyclerView.setLayoutManager(linearLayoutManager);

		llSumbit = (LinearLayout) findViewById(R.id.ll_topic_discuss);
		edDiscuss = (EditText) findViewById(R.id.ed_discuss);
		tvSubmit = (TextView) findViewById(R.id.tv_discuss_submit);
		ivSubmit = (ImageView) findViewById(R.id.iv_submit);
	}

	private void initData() {
		mRecyclerAdapter = new RecyclerTopicDetails(TopicDetails.this);
		mRecyclerAdapter.setHeaderData(headerBean);
		mRecyclerView.setAdapter(mRecyclerAdapter);
		
		List<TopicCommentBean> commentBeans = LoadingTopicActivity.getTopicData();
		if(commentBeans != null && commentBeans.size()!=0)
			mRecyclerAdapter.AddData(commentBeans);
		
		DealtListener();
	}


	private void DealtListener() {
		edDiscuss.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (edDiscuss.length() != 0) {
					tvSubmit.setTextColor(0xffffc125);
				} else {
					tvSubmit.setTextColor(0xffffffff);
				}
			}
		});

		tvSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edDiscuss.length() == 0) {
					Toast.makeText(TopicDetails.this, "请输入评论",
							Toast.LENGTH_LONG).show();
				} else {
					UpLoadDiscuss();
				}
			}
		});

		ivSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ivSubmit.setVisibility(View.INVISIBLE);
				llSumbit.setVisibility(View.VISIBLE);
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
				alphaAnimation.setDuration(400);
				llSumbit.startAnimation(alphaAnimation);
				edDiscuss.requestFocus();
			}
		});
		
		mRecyclerView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if(newState==1|| newState==2){
					ivSubmit.setVisibility(View.VISIBLE);
					ivSubmit.setAlpha(0.2f);
					llSumbit.setVisibility(View.INVISIBLE);
				}else{
					ivSubmit.setAlpha(1.0f);
				}
			}
		});

	}

	protected void UpLoadDiscuss() {
		String userName = SharedPreUtils.getComSharePref(TopicDetails.this,"user_name", "");
		String userImg = SharedPreUtils.getComSharePref(TopicDetails.this,"user_name_img", "");
		if (userName.equals("") || userImg.equals("")) {
			Intent intent = new Intent(TopicDetails.this,SignPageActivity.class);
			startActivity(intent);
		} else {
			final TopicCommentBean commentBean = new TopicCommentBean();
			commentBean.setTopicId(topicObjectId);
			commentBean.setCommentDetail(edDiscuss.getText().toString());
			commentBean.setAuthorName(userName);
			commentBean.setAuthorPicture(userImg);
			commentBean.save(TopicDetails.this, new SaveListener() {
				@Override
				public void onSuccess() {
					Toast.makeText(TopicDetails.this, "发表成功",Toast.LENGTH_SHORT).show();
					mRecyclerAdapter.AddDataOne(commentBean);
					ivSubmit.setVisibility(View.VISIBLE);
					llSumbit.setVisibility(View.GONE);
					edDiscuss.setText("");
					
					setAddRequest();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(TopicDetails.this, "发表失败",
							Toast.LENGTH_SHORT).show();
					ivSubmit.setVisibility(View.VISIBLE);
					llSumbit.setVisibility(View.INVISIBLE);
				}
			});
		}
	}

	private String discussNumber="0";
	protected void setAddRequest() {
		
		BmobQuery<TopicBean> bmobQuery = new BmobQuery<TopicBean>("TopicBean");
		bmobQuery.getObject(TopicDetails.this, topicObjectId, new GetListener<TopicBean>() {
			@Override
			public void onSuccess(TopicBean topicBean) {
				discussNumber= topicBean.getDiscussNumber();
				setAdd();
			}
			private void setAdd() {
				TopicBean topicBean=new TopicBean();
				topicBean.setDiscussNumber((Integer.parseInt(discussNumber)+1)+"");
				topicBean.update(TopicDetails.this, topicObjectId, null);
			}
			@Override
			public void onFailure(int arg0, String arg1) {
			}
		});
	}
	
	@SuppressWarnings("static-access")
	private void initSystemBarTint() {
		TypedArray typedBottom=obtainStyledAttributes(null, R.styleable.bottom);
		int navigaColor = typedBottom.getColor(R.styleable.bottom_bottomTextColor, 0xffffffff);
		SystemBarTintUtils mBarTintUtils = new SystemBarTintUtils(this);
		mBarTintUtils.setNavigationBarTint(navigaColor);
		mBarTintUtils.setStatusBarTint(navigaColor);
	}

}
