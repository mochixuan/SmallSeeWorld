package com.wx.seeworld.Tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;

import com.wx.seeworld.R;
import com.wx.seeworld.loading.MonIndicator;

public class DialogTools {

	private static AlertDialog alertDialog;

	public static void setDialog(Context mContext){
		alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(
				Color.TRANSPARENT)); // 设置背景无色
		View view = View.inflate(mContext,R.layout.dialog_view, null);
		window.setContentView(view);
		
		MonIndicator monIndicator=(MonIndicator) view.findViewById(R.id.loading_monindicator_dialog);
		monIndicator.setColors(new int[] { 0xFF66cccc, 0xFFccff99,
				0xFFccffcc, 0xFF66cc99, 0xFF66cc66 });
	}
	
	public static void Dismass(){
		if(alertDialog!=null && alertDialog.isShowing()){
			alertDialog.dismiss();
		}
	}
	
}
