package com.example.bunnyweather.db;

import com.example.bunnyweather.util.Const;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	final static String CREATE_TABLE = "CREATE TABLE " + Const.TABLE +  "(" +
			"_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			Const.COLUMN_NAME + " TEXT, " + 
			Const.COLUMN_CODE + " TEXT, " + 
			Const.COLUMN_PARENT_CODE + " TEXT )";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
