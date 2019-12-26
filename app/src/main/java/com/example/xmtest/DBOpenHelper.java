package com.example.xmtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String gs = "Create table gsBank ("
            + "account text primary key,"
            + "password text,"
            + "money real)";

    private static final String zg = "Create table zgBank ("
            + "account text primary key,"
            + "password text,"
            + "money real)";

    private static final String js = "Create table jsBank ("
            + "account text primary key,"
            + "password text,"
            + "money real)";

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(gs);
        db.execSQL(zg);
        db.execSQL(js);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
