package com.example.dominik.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ItemsSQLiteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "articles_db";
    public static final int DATABASE_VERSION = 1;
    public static final String USER_NAME = "User";

    public static final String TABLE_ARTICLES = "articles";
    public static final String ARTICLE_ID = "_id";
    public static final String ARTICLE_NAME = "name";
    public static final String ARTICLE_EAN = "EAN";
    public static final String ARTICLE_QUANTITY = "quantity";


    public ItemsSQLiteDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       String command = "CREATE TABLE " + TABLE_ARTICLES + " ("
                + ARTICLE_ID + " INTEGER PRIMARY KEY,"
                + ARTICLE_EAN + " VARCHAR(255),"
                + ARTICLE_NAME + " VARCHAR(255),"
                + ARTICLE_QUANTITY + " INTEGER"
                + ");";
        Log.i("Create command", command);
        db.execSQL(command);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES + ";");
        onCreate(db);
    }
}
