package com.wx.seeworld.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CollectOpenHelper extends SQLiteOpenHelper {
	
	public CollectOpenHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table collectNews(_id integer primary key autoincrement,collectedUrl char(50),Title char(40),digest char char(120))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
