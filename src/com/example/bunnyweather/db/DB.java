package com.example.bunnyweather.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.bunnyweather.model.ListItem;
import com.example.bunnyweather.util.Const;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DB {
	private static SQLiteDatabase db;
	public static DB mDb;
	final static private String DATABASE_NAME = "WEATHER.db";
	final static private int VERSION = 1;
	private List<ListItem> listData = new ArrayList<ListItem>();
	
	private DB(Context context) {
		DatabaseHelper helper = new DatabaseHelper(context, DATABASE_NAME, null, VERSION);
		db = helper.getWritableDatabase();
	}
	
	public synchronized static DB getInstance(Context context) {
		if(mDb == null) {
			mDb = new DB(context);
		}
		return mDb;
	}
	
	public List<ListItem> getListData(String parentCode) {
		String selection = Const.COLUMN_PARENT_CODE + "=?";
		String[] selectionArgs = new String[]{parentCode};
		
		Cursor cursor = db.query(Const.TABLE, new String[] {Const.COLUMN_ID, Const.COLUMN_NAME, Const.COLUMN_CODE, Const.COLUMN_PARENT_CODE}, selection, selectionArgs, null, null, Const.COLUMN_ID);
		return add2List(cursor);
	}
	
	public String getParentCode(String code){
		String selection = Const.COLUMN_CODE + "=?";
		String[] columns = new String[] {Const.COLUMN_PARENT_CODE};
		String[] selectionArgs = new String[]{code};
		String limit = "1";
		
		Cursor cursor = db.query(Const.TABLE, columns, selection, selectionArgs, null, null, null, limit);
		if(cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(Const.COLUMN_PARENT_CODE));
		}
		return "";
	}
	
	
	private List<ListItem> add2List(Cursor cursor) {
		Log.e("cursor.getCount(): ", cursor.getCount()+ "");
		listData.clear();
//		while(cursor.moveToNext()){
//			Log.e("id:" , cursor.getInt(0)+"");
//			Log.e("name:" , cursor.getString(1)+"");
//			Log.e("code:" , cursor.getString(2)+"");
//			Log.e("pcode:" , cursor.getString(3)+"");
//		}
		
		while(cursor.moveToNext()) {
			ListItem item = new ListItem();
//			item.setId(cursor.getInt(0));
//			item.setName(cursor.getString(1));
//			item.setCode(cursor.getString(2));
//			item.setParentCode(cursor.getString(3));
			
//			Log.e("id:" , cursor.getColumnIndex(Const.COLUMN_ID) + "");
//			Log.e("name:" , cursor.getColumnIndex(Const.COLUMN_NAME) + "");
//			Log.e("code:" , cursor.getColumnIndex(Const.COLUMN_CODE) + "");
//			Log.e("pcode:" , cursor.getColumnIndex(Const.COLUMN_PARENT_CODE) + "");
			
			item.setId(cursor.getInt(cursor.getColumnIndex(Const.COLUMN_ID)));
			item.setName(cursor.getString(cursor.getColumnIndex(Const.COLUMN_NAME)));			
			item.setCode(cursor.getString(cursor.getColumnIndex(Const.COLUMN_CODE)));
			item.setParentCode(cursor.getString(cursor.getColumnIndex(Const.COLUMN_PARENT_CODE)));
			
			listData.add(item);
		}
		Log.e("listData.size()", listData.size() + "");
		return listData;
	}

	public void insert2Table(ArrayList<String[]> list, String parentCode) {
		ContentValues values = new ContentValues();
		for(int i = 0; i < list.size(); ++i) {
			Log.e("insert2Table", "insert2Table");
			values.clear();
			values.put(Const.COLUMN_CODE, list.get(i)[0]);
			values.put(Const.COLUMN_NAME, list.get(i)[1]);
			values.put(Const.COLUMN_PARENT_CODE, parentCode);
			db.insert(Const.TABLE, null, values);
		}		
	}
	
}
