package com.example.shubham.navigationdrawer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyCartDatabse extends SQLiteOpenHelper{



    public MyCartDatabse(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE cart(_id INTEGER PRIMARY KEY AUTOINCREMENT, itemname TEXT, itemdesc TEXT, baseprice TEXT, totalprice TEXT, quantity TEXT)";
        db.execSQL(CREATE_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS cart");

        onCreate(db);
    }
}
