package com.noida.just200;

/**
 * Created by intex on 11/19/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "database_name";

    // Table Names
    private static final String DB_TABLE = "table_image";

    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "("+
            KEY_NAME + " Text, " +
            KEY_IMAGE + " BLOB NOT NULL );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating table
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // create new table
        onCreate(db);
    }

    public void addEntry( String name, byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(KEY_NAME,    name);
        cv.put(KEY_IMAGE, image);
        database.insert(DB_TABLE, null, cv);
        database.close();
    }

    public byte[] retreiveImageFromDB() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cur = database.rawQuery("select * from " + DB_TABLE, null);

        //Cursor cur = database.query(true, DB_TABLE, new String[]{KEY_IMAGE,},null, null, null, null,KEY_NAME + " DESC", "1");

        if (cur.moveToFirst()) {
            String pro = cur.getString(0);
            byte[] blob = cur.getBlob(1);
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

    public void deleteSingleRow()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //  db.execSQL("DELETE FROM " + TABLE_DASHBOARD + " WHERE " + KEY_EMAIL + "='" + value + "'");
        db.execSQL("DELETE FROM " + DB_TABLE + " ;");
        db.close();
    }


}