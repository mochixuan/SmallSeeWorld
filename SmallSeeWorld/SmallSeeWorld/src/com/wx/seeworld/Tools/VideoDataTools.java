package com.wx.seeworld.Tools;

import android.content.Context;

import com.wx.seeworld.Utils.SharedPreUtils;

public class VideoDataTools {
	
	private static final int[] Ids=new int[]{1,3,5,6,13,16,18,19,27,31,59,62,63,64,193};
	
	public static int getVideoId(Context context){
		String videoId = SharedPreUtils.getComSharePref(context, "video_id", "0");
		int intId = Integer.parseInt(videoId);
		if(intId>=14){
			intId=0;
		}else{
			intId=intId+1;
		}
		SharedPreUtils.setComSharePref(context, "video_id", intId+"");
		return Ids[intId];
	}

}
