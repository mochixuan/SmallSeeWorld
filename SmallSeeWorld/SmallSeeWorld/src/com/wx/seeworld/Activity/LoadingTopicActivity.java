package com.wx.seeworld.Activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wx.seeworld.R;
import com.wx.seeworld.Bean.TopicBean;
import com.wx.seeworld.Bean.TopicCommentBean;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.SystemBarTintUtils;
import com.wx.seeworld.loading.MonIndicator;

public class LoadingTopicActivity extends Activity {

	private String topicObjectId;
	private String themeStyle;
	private WebView webView;
	private MonIndicator monIndicator;
	private static List<TopicCommentBean> commentBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		themeStyle = SharedPreUtils.getComSharePref(LoadingTopicActivity.this,"theme_style", "day");
		if (themeStyle.equals("night")) {
			setTheme(R.style.MyThemeNight);
		}
		
		setContentView(R.layout.activity_topic_loading);
		initSystemBarTint();
		topicObjectId = getIntent().getStringExtra("topic_objectId");
		initView();
	}

	private void initView() {
		monIndicator = (MonIndicator) findViewById(R.id.loading_monindicator1);
		monIndicator.setColors(new int[] { 0xFF66cccc, 0xFFccff99,0xFFccffcc, 0xFF66cc99, 0xFF66cc66 });
		webView = (WebView) findViewById(R.id.web_news_view1);
		setBrowseAdd(topicObjectId, this);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setBrowseAdd(final String topicObjectId,final Context mContext){
		BmobQuery bmobQuery=new BmobQuery<>("TopicBean");
		bmobQuery.getObject(mContext, topicObjectId, new GetListener<TopicBean>() {
			@Override
			public void onFailure(int arg0, String arg1) {
				setErrorPage();
			}
			@Override
			public void onSuccess(TopicBean topicBean) {
				String browseNumber = topicBean.getAttentionNumber();
				SetAdd(Integer.parseInt(browseNumber),mContext,topicBean);
			}
		});
		
	}
	
	private  void SetAdd(final int attentionNumber,Context mContext,final TopicBean mTopicBean) {
		TopicBean topicBean=new TopicBean();
		topicBean.setAttentionNumber((attentionNumber+1)+"");
		topicBean.update(mContext, topicObjectId, new UpdateListener() {
			@Override
			public void onSuccess() {
				QueryDiscuss(mTopicBean,attentionNumber);
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				setErrorPage();
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	protected void QueryDiscuss(final TopicBean mTopicBean,final int attentionNumber) {
		
		BmobQuery bmobQuery=new BmobQuery<>("TopicCommentBean");
		bmobQuery.addWhereEqualTo("topicId", mTopicBean.getObjectId());
		bmobQuery.order("-createdAt");
		bmobQuery.findObjects(LoadingTopicActivity.this, new FindCallback() {

			@Override
			public void onFailure(int arg0, String arg1) {
				setErrorPage();
			}
			
			@Override
			public void onSuccess(JSONArray jSonArray) {
				commentBeans = new ArrayList<TopicCommentBean>();
				Gson gson=new Gson();
				JsonParser jsonParser=new JsonParser();
				JsonArray jsonArray = jsonParser.parse(jSonArray.toString()).getAsJsonArray();
				for(int i=0;i<jsonArray.size();i++){
					JsonElement jsonElement = jsonArray.get(i);
					TopicCommentBean commentBean = gson.fromJson(jsonElement, TopicCommentBean.class);
					commentBeans.add(commentBean);
				}
				
				Intent intent = new Intent(LoadingTopicActivity.this,TopicDetails.class);
				intent.putExtra("author_img_url",mTopicBean.getAuthorPicture());
				intent.putExtra("author_name", mTopicBean.getAuthorName());
				intent.putExtra("topic_caption", mTopicBean.getCaption());
				intent.putExtra("caption_imgUrl", mTopicBean.getBmobFileCapImg().getFileUrl(LoadingTopicActivity.this));
				intent.putExtra("topic_attation_number",attentionNumber+"");
				intent.putExtra("topic_objectId", mTopicBean.getObjectId());
				intent.putExtra("topic_createdAt", mTopicBean.getCreatedAt());
				intent.putExtra("topic_discuss_number",mTopicBean.getDiscussNumber());
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			}
		});
	}

	private void setErrorPage(){
		webView.setVisibility(View.VISIBLE);
		monIndicator.setVisibility(View.INVISIBLE);
		if (themeStyle.equals("day")) {
			webView.loadUrl("file:///android_asset/error_day.html");
		} else {
			webView.loadUrl("file:///android_asset/error_night.html");
		}
	}
	
	public static List<TopicCommentBean> getTopicData(){
		return commentBeans;
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
