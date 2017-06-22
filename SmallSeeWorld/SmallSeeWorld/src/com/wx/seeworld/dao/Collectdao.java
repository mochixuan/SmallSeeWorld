package com.wx.seeworld.dao;

import java.util.ArrayList;
import java.util.List;

import com.wx.seeworld.Bean.collect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Collectdao {

	public static void addCollectDao(Context context, String newsUrl,String Title,String digest) {
		CollectOpenHelper collectOpenHelper = new CollectOpenHelper(context,"collect.db", null, 1);
		SQLiteDatabase database = collectOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("collectedUrl", newsUrl);
		values.put("Title", Title);
		values.put("digest", digest);
		database.insert("collectNews", null, values);
		values.clear();
		database.close();
	}


	public static boolean querySelectedId(Context context, String newsUrl) {
		CollectOpenHelper collectOpenHelper = new CollectOpenHelper(context,"collect.db", null, 1);
		SQLiteDatabase database = collectOpenHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from collectNews where collectedUrl=?",new String[] {newsUrl });
		boolean collected;
		if (cursor.moveToNext()) {
			collected=true;
		} else {
			collected =false;
		}
		cursor.close();
		database.close();
		return collected;
	}
	
	public static List<collect> querySelectedList(Context context) {
		CollectOpenHelper collectOpenHelper = new CollectOpenHelper(context,"collect.db", null, 1);
		SQLiteDatabase database = collectOpenHelper.getWritableDatabase();
		Cursor cursor = database.query("collectNews", new String[]{"collectedUrl","Title","digest"}, null, null, null, null, null);
		List<collect> lists=new ArrayList<collect>();
		while (cursor.moveToNext()) {
			collect collect1=new collect();
			 collect1.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
			 collect1.setUrl( cursor.getString(cursor.getColumnIndex("collectedUrl")));
			 collect1.setDigest(cursor.getString(cursor.getColumnIndex("digest")));
			lists.add(collect1);
		} 
		cursor.close();
		database.close();
		return lists;
	}
	
	
	public static void deleteSelected(Context context,String newsUrl) {
		CollectOpenHelper collectOpenHelper = new CollectOpenHelper(context,"collect.db", null, 1);
		SQLiteDatabase database = collectOpenHelper.getWritableDatabase();
		database.delete("collectNews", "collectedUrl=?", new String[]{newsUrl});
		database.close();
	}


}
