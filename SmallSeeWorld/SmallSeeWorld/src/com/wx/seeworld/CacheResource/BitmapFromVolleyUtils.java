package com.wx.seeworld.CacheResource;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.wx.seeworld.R;

public class BitmapFromVolleyUtils {

	public static void getImgFromNet(final String imgUrl, ImageView imageView,final Context mContext) {
		
		if (!imageView.getTag().equals(imgUrl)) {
			return;
		}
		
		long maxMemory = Runtime.getRuntime().maxMemory()/8;
		final LruCache<String, Bitmap> lruCache=new LruCache<String, Bitmap>((int) (maxMemory)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
		ImageCache imageCache = new ImageCache() {
			public void putBitmap(String key, Bitmap value) {
				lruCache.put(imgUrl, value);
			}
			public Bitmap getBitmap(String key) {
				return lruCache.get(imgUrl);
			}
		};
		
		RequestQueue requestQueue = Volley.newRequestQueue(mContext);
		ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
		ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.default_image, R.drawable.default_image);
		imageLoader.get(imgUrl, listener,200,144);
	}
	
}
