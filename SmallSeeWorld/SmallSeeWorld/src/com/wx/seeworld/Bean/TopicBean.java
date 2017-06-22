package com.wx.seeworld.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class TopicBean extends BmobObject{

	private String userId;
	private String authorName;								
	private String authorPicture;
	private BmobFile bmobFileCapImg;
	private String caption;
	private String attentionNumber;
	private String discussNumber;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorPicture() {
		return authorPicture;
	}
	public void setAuthorPicture(String authorPicture) {
		this.authorPicture = authorPicture;
	}
	public BmobFile getBmobFileCapImg() {
		return bmobFileCapImg;
	}
	public void setBmobFileCapImg(BmobFile bmobFileCapImg) {
		this.bmobFileCapImg = bmobFileCapImg;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getAttentionNumber() {
		return attentionNumber;
	}
	public void setAttentionNumber(String attentionNumber) {
		this.attentionNumber = attentionNumber;
	}
	public String getDiscussNumber() {
		return discussNumber;
	}
	public void setDiscussNumber(String discussNumber) {
		this.discussNumber = discussNumber;
	}
	
}
