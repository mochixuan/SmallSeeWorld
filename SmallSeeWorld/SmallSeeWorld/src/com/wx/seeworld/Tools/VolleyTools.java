package com.wx.seeworld.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class VolleyTools {

	public static RequestQueue mQueue;

	public static RequestQueue getRequestQueue(Context context) {
		
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context);
		}
		
		return mQueue;
	}

	public static void cancelRequest(String Tag) {
		mQueue.cancelAll(Tag);
	}

	public static void getImageLoader(Context mContext,String Tag,String imgUrl,ImageView imageView,int resouce) {
		
		RequestQueue requestQueue = getRequestQueue(mContext);
		ImageCache imageCache = new ImageCache() {
			public void putBitmap(String key, Bitmap value) {
			}
			public Bitmap getBitmap(String key) {
				return null;
			}
		};
		ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
		ImageListener listener = ImageLoader.getImageListener(imageView,resouce, resouce);
		imageLoader.get(imgUrl, listener);
	}
	
}