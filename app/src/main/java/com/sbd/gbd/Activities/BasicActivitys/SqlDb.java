package com.sbd.gbd.Activities.BasicActivitys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlDb extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "cart_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "productid";
    public static final String COL_3 = "productname";
    public static final String COL_4 = "productprice";
    public static final String COL_5 = "productquentity";



    public SqlDb(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,productid TEXT UNIQUE,productname TEXT,productprice TEXT,productquentity TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String productid, String productname, String productprice,String productquentity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,productid);
        contentValues.put(COL_3,productname);
        contentValues.put(COL_4,productprice);
        contentValues.put(COL_5,productquentity);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    /*public boolean insertSeckond(String cool, String id, String status, String ip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,cool);
        contentValues.put(COL3,id);
        contentValues.put(COL4,status);
        contentValues.put(COL5,ip);
        long result = db.insert(TABLE_SECKOND,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }*/

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getid() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID from "+TABLE_NAME,null);
        return res;
    }


   /* public Cursor getcomponents(int section) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_SECKOND+ " where " + COL3 + " = "+ section ,null);
        return res;
    }*/



    public int getUpdate(int snNO, String quentity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_5,quentity);
        int res=db.update(TABLE_NAME, cv, "ID="+(snNO+1), null);
        return res;

    }

    /*public int getUpdate2(int snNO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL4,"false");
        int res=db.update(TABLE_SECKOND, cv, "SN="+snNO, null);
        return res;

    }*/

    /*public Cursor getsn() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_SECKOND, null);
        return res;
    }*/

    public void deleteItem() {
        SQLiteDatabase db = getWritableDatabase();
        //String whereArgs[] = {item.id.toString()};
        //db.delete( TABLE_NAME,COL_1 + "=" + item,null);
        db.execSQL("delete from "+TABLE_NAME );

    }
}
