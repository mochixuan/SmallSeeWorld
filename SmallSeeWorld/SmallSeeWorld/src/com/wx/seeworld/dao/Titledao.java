package com.wx.seeworld.dao;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Titledao {

	public static void addTitleDao(Context context,HashMap<String, Object> hashMap) {
		TitleOpenHelper titleOpenHelper = new TitleOpenHelper(context,
				"title.db", null, 1);
		SQLiteDatabase database = titleOpenHelper.getWritableDatabase();
		database.delete("topTitle", null, null);
		for (int i = 0; i < hashMap.size(); i++) {
			ContentValues values = new ContentValues();
			values.put("titleTag", i);
			values.put("titleName", (String) hashMap.get(""+i));
			database.insert("topTitle", null, values);
			values.clear();
		}
		database.close();
	}
	
	public static HashMap<String, Object> queryTitleDao(Context context) {
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		TitleOpenHelper titleOpenHelper = new TitleOpenHelper(context,"title.db", null, 1);
		SQLiteDatabase database = titleOpenHelper.getWritableDatabase();
		Cursor cursor = database.query("topTitle", null, null, null, null, null, null, null);
		int i=0;
		while (cursor.moveToNext()) {
			String titleName=cursor.getString(cursor.getColumnIndex("titleName"));
			hashMap.put(""+i, titleName);
			i++;
		}
		cursor.close();
		database.close();
		return hashMap;
	}
	
	
	public static String querySelectedId(Context context,int selectedId){
		TitleOpenHelper titleOpenHelper = new TitleOpenHelper(context,"title.db", null, 1);
		SQLiteDatabase database = titleOpenHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select titleName from topTitle where titleTag=?", new String[]{selectedId+""});
		String titleName;
		if(cursor.moveToNext()){
			titleName = cursor.getString(cursor.getColumnIndex("titleName"));
		}else{
			titleName=null;
		}
		cursor.close();
		database.close();
		return titleName;
	}
	
	public static int querySelectedString(Context context,String selectedString){
		TitleOpenHelper titleOpenHelper = new TitleOpenHelper(context,"title.db", null, 1);
		SQLiteDatabase database = titleOpenHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select titleTag from topTitle where titleName=?", new String[]{selectedString});
		
		String titleTag;
		int parseInt=0;
		if(cursor.moveToNext()){
			titleTag= cursor.getString(cursor.getColumnIndex("titleTag"));
			parseInt= Integer.parseInt(titleTag);
		}
		
		cursor.close();
		database.close();
		return parseInt;
	}
	
}
