package com.wx.seeworld.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private static Toast mToast;
	public static void makeText(Context context,String text){
		if(mToast == null) {    
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);    
        } else {    
            mToast.setText(text);      
            mToast.setDuration(Toast.LENGTH_SHORT);    
        }    
        mToast.show(); 
	}
	
}
