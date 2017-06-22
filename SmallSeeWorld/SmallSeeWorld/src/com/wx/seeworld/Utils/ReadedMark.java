package com.wx.seeworld.Utils;

import android.content.Context;

public class ReadedMark {
	
	private static final String MASK="is_readed";
	
	public static void setReaded(String postid,Context mContext){
		String readed = SharedPreUtils.getComSharePref(mContext, MASK, "");
		if(readed.contains(postid)){
			return;
		}else{
			if(readed.equals("")){
				readed=postid;
			}else{
				readed=readed+","+postid;
			}
		}
		SharedPreUtils.setComSharePref(mContext, MASK, readed);
	}
	
	public static boolean getReaded(String postid,Context mContext){
		String readed = SharedPreUtils.getComSharePref(mContext, MASK, "");
		if(readed.contains(postid)){
			return true;
		}
		return false;
	}

}
