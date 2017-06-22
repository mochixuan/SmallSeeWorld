package com.wx.seeworld.CacheResource;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wx.seeworld.View.CircleImageView;

public class ImageLoaderJar {

	public static void getImgFromNet(final String imgUrl, ImageView imageView){
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(imgUrl,imageView);
	}
	
	public static void getImgFromNetCircle(final String imgUrl, final CircleImageView imageView){
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.loadImage(imgUrl, new SimpleImageLoadingListener(){
			@Override
			public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				imageView.setImageBitmap(loadedImage);
			}
		});
	}
	
}
