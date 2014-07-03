package com.example.maze2014;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteActivity extends SQLiteOpenHelper {
	private final static int _DBVersion = 1; //<-- ª©¥»
	private final static String _DBName = "BigOrder.db";  //<-- db name
	private final static String _TableName = "MyFavor"; //<-- table name

	public SQLiteActivity(Context context, String name, CursorFactory factory,
			int version) {
		super(context, _DBName, factory, _DBVersion);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	               "favorname VARCHAR(50), " +
	               "datalist VARCHAR(300)," +
	               "datacost VARCHAR(50)," +
	               "datacount VARCHAR(50)" +
	           ");";
		
		db.execSQL(SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		final String SQL = "DROP TABLE IF EXISTS " + _TableName;
        db.execSQL(SQL);
        onCreate(db);
	}

}
