package com.example.locker.Database;

import android.app.LauncherActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Password_Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "password.db";
    public static final String TABLE_NAME = "password_table";
    public static final String col1 = "password";

    public Password_Database(@Nullable Context context) {
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE  TABLE " + TABLE_NAME + "(password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1,name);
        long result = db.insert(TABLE_NAME, null,contentValues);
        db.close();
        return result != -1;
    }

    public Cursor getData()
    {
        SQLiteDatabase db  = this.getWritableDatabase();
        Cursor cursor =  db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    public Integer deleteData(String name)
    {
        SQLiteDatabase  db = this.getWritableDatabase();
        int res = db.delete(TABLE_NAME,col1+" =?",new String[]{name});
        db.close();
        return  res;
    }

    public boolean update_password(String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("CREATE  TABLE " + TABLE_NAME + "(password TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1,pass);
        long result = db.insert(TABLE_NAME, null,contentValues);
        db.close();
        return result != -1;
    }
}
