package com.example.locker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LockedApps extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "apps.db";
    public static final String TABLE_NAME = "locked_apps";
    public static final String col1 = "package_name";

    public LockedApps(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "create table " + TABLE_NAME + "(" + col1 + " TEXT" +")";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1,name);
        long res = db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return res!=-1;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME,null);
    }

    public void deleteData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(TABLE_NAME,col1 + "=?",new String[]{name});
        db.close();
    }

}
