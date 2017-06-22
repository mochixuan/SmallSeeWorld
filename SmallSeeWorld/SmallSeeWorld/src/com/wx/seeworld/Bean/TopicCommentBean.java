package com.wx.seeworld.Bean;

import cn.bmob.v3.BmobObject;

public class TopicCommentBean extends BmobObject{

	private String topicId;
	private String authorPicture;
	private String authorName;		
	private String CommentDetail;
	
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getAuthorPicture() {
		return authorPicture;
	}
	public void setAuthorPicture(String authorPicture) {
		this.authorPicture = authorPicture;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getCommentDetail() {
		return CommentDetail;
	}
	public void setCommentDetail(String commentDetail) {
		CommentDetail = commentDetail;
	}		
	
}
