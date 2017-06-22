package com.wx.seeworld.FragemntNews;

import java.io.File;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.wx.seeworld.R;
import com.wx.seeworld.Activity.SignPageActivity;
import com.wx.seeworld.Bean.TopicBean;
import com.wx.seeworld.Tools.DialogTools;
import com.wx.seeworld.Tools.StorageBitmap;
import com.wx.seeworld.Utils.SharedPreUtils;
import com.wx.seeworld.Utils.ToastUtils;

public class TopicFragmentPublication extends Fragment implements
		OnClickListener {

	private ImageView imageView;
	private static final int ENTERPICTURE = 1100;
	private static final int PHOTO_REQUEST_CUT = 1101;
	private EditText edTopic;
	private Button btnSubmit;
	private boolean isSelectImg;
	private CardView cardViewBg;
	private String themeStyle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_topic_publition,
				container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		themeStyle = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		
		imageView = (ImageView) view.findViewById(R.id.iv_topic_publition);
		edTopic = (EditText) view.findViewById(R.id.ed_topic_caption);
		btnSubmit = (Button) view.findViewById(R.id.btn_topic_submit);
		cardViewBg = (CardView) view.findViewById(R.id.card_view_topic2);
		imageView.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		
		edTopic.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()>40){
					edTopic.setText(s.toString().substring(0, 40));
					edTopic.setSelection(edTopic.getText().toString().length());              //设置光标在最后
					ToastUtils.makeText(getActivity(), "字数40了，亲");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_topic_publition:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, ENTERPICTURE);
			break;
		case R.id.btn_topic_submit:
			UploadTopic();
			 break;
		}
	}

	private void UploadTopic() {
		String userName = SharedPreUtils.getComSharePref(getActivity(), "user_name", "");
		String userImg = SharedPreUtils.getComSharePref(getActivity(), "user_name_img", "");
		if(userName.equals("") || userImg.equals("")){
			Intent intent=new Intent(getActivity(),SignPageActivity.class);
			getActivity().startActivity(intent);
		}else if(!isSelectImg){
			ToastUtils.makeText(getActivity(), "请添加话题图片");
		}else if(edTopic.getText().length()==0){
			ToastUtils.makeText(getActivity(), "请添加话题描述");
		}else{
			final String userId=SharedPreUtils.getComSharePref(getActivity(), "user_Id", "");
			final String authorName=SharedPreUtils.getComSharePref(getActivity(), "user_name", "");
			final String authorPicture=SharedPreUtils.getComSharePref(getActivity(), "user_name_img", "");
			final BmobFile bmobFileCapImg=new BmobFile(new File(StorageBitmap.getImgPath("topicShare")));
			final String caption=edTopic.getText().toString();
			final String attentionNumber=0+"";
			final String discussNumber=0+"";
			DialogTools.setDialog(getActivity());
			bmobFileCapImg.upload(getActivity(), new UploadFileListener() {
				@Override
				public void onSuccess() {
					TopicBean topicBean=new TopicBean();
					topicBean.setAttentionNumber(attentionNumber);
					topicBean.setAuthorName(authorName);
					topicBean.setAuthorPicture(authorPicture);
					topicBean.setBmobFileCapImg(bmobFileCapImg);
					topicBean.setCaption(caption);
					topicBean.setDiscussNumber(discussNumber);
					topicBean.setUserId(userId);
					topicBean.save(getActivity(), new SaveListener() {
						@Override
						public void onSuccess() {
							Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
							DialogTools.Dismass();
							imageView.setImageResource(R.drawable.topic_pulition_night);
							edTopic.setText("");
							edTopic.setHint("发表成功，再来一发。。。");
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
							DialogTools.Dismass();
						}
					});
				}
				@Override
				public void onFailure(int arg0, String result) {
					Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
					DialogTools.Dismass();
				}
			});
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ENTERPICTURE
				&& resultCode == getActivity().RESULT_OK && data != null) {
			Uri selectedimgUrl = data.getData();
			ShearImg(selectedimgUrl);
		}
		if (requestCode == PHOTO_REQUEST_CUT&& resultCode == getActivity().RESULT_OK && data != null) {
			Bitmap bitmap = data.getParcelableExtra("data");
			imageView.setImageBitmap(bitmap);
			StorageBitmap.GetandSaveCurrentImage(getActivity(), "topicShare", bitmap);
			isSelectImg=true;
		}
		
	}

	private void ShearImg(Uri selectedimgUrl) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(selectedimgUrl, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 400);

		intent.putExtra("outputFormat", "PNG");		// 图片格式
		intent.putExtra("noFaceDetection", true);	// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		String themeChange = SharedPreUtils.getComSharePref(getActivity(), "theme_style", "day");
		if(! themeStyle.equals(themeChange)){
			if(themeChange.equals("day")){
				themeStyle="day";
			}else{
				themeStyle="night";
			}
			TypedArray typedMainText= getActivity().obtainStyledAttributes(null, R.styleable.maintext);
			int bottomTextColor = typedMainText.getColor(R.styleable.maintext_cardtopicbackground, 0xffffffff);
			cardViewBg.setCardBackgroundColor(bottomTextColor);
			typedMainText.recycle();
			TypedArray typedUsetheme= getActivity().obtainStyledAttributes(null, R.styleable.usertheme);
			int textcolorWrite = typedUsetheme.getColor(R.styleable.usertheme_text_color_write, 0xffffffff);
			edTopic.setTextColor(textcolorWrite);
			edTopic.setHintTextColor(textcolorWrite);
			btnSubmit.setTextColor(textcolorWrite);
		}
	}

}
