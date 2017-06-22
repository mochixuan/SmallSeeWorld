package com.wx.seeworld.CacheResource;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.wx.seeworld.R;
import com.wx.seeworld.Tools.VolleyTools;

public class VolleyImageEasy {

	public static void setTemporary(Context mContext, ImageView imageView,
			String imgUrl) {
		RequestQueue queue = VolleyTools.getRequestQueue(mContext);
		ImageListener listener = ImageLoader.getImageListener(imageView,
				R.drawable.icon_wite_bg, R.drawable.icon_wite_bg);
		final LruCache<String, Bitmap> lruCache=new LruCache<String, Bitmap>((int) (1024*50)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
		ImageCache imageCache=new ImageCache() {
			@Override
			public void putBitmap(String key, Bitmap bitmap) {
				lruCache.put(key, bitmap);
			}
			@Override
			public Bitmap getBitmap(String key) {
				return lruCache.get(key);
			}
		};
		
		ImageLoader imageLoader = new ImageLoader(queue,imageCache);
		imageLoader.get(imgUrl, listener);
	}

}
