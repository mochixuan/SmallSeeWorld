package com.wx.seeworld.NetYINews.NewsJsonTools;

import android.content.Context;

public class NewsJsonTools {

	private static final String GlobalUrl = "http://c.m.163.com/nc/article/list/";
	private static String requestUrl;
	private static NewsJsonTools mNewsJsonTools;
	
	public static final String HEADLINES = "T1348647909107";
	public static final String ORIGNAL="T1370583240249";
	public static final String NBA="T1348649145984";
	public static final String ENTERTAINMENT="T1348648517839";
	public static final String SPORT="T1348649079062";
	public static final String PHONE="T1348649654285";
	public static final String MOBILEINTERNET="T1351233117091";
	public static final String SCIENCETECHNOLOGY="T1348649580692";
	public static final String TROMIST="T1348654204705";
	public static final String FINANCE="T1348648756099";

	public static NewsJsonTools getInstance() {
		if (mNewsJsonTools == null) {
			mNewsJsonTools = new NewsJsonTools();
		}
		return mNewsJsonTools;
	}
	
	public static String getJsonData(String Cid, int requestCount,
			Context mContext) {
		requestUrl = GlobalUrl + Cid + "/0-" + requestCount + ".html";
		return requestUrl;
	}

}
