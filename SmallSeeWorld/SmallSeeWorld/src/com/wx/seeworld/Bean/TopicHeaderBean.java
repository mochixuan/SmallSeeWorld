package com.wx.seeworld.Bean;


public class TopicHeaderBean{

	private String createdAt;
	private String authorName;								
	private String authorPicture;
	private String CaptionImg;
	private String caption;
	private String browseNumber;
	private String discussNumber;
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
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
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getBrowseNumber() {
		return browseNumber;
	}
	public void setBrowseNumber(String browseNumber) {
		this.browseNumber = browseNumber;
	}
	public String getDiscussNumber() {
		return discussNumber;
	}
	public void setDiscussNumber(String discussNumber) {
		this.discussNumber = discussNumber;
	}
	public String getCaptionImg() {
		return CaptionImg;
	}
	public void setCaptionImg(String captionImg) {
		CaptionImg = captionImg;
	}
}
