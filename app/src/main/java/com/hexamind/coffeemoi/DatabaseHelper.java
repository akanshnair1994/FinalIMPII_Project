package com.hexamind.coffeemoi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "CoffeeMoi";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "ORDERS";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USERNAME";
    public static final String COL_3 = "SIZE";
    public static final String COL_4 = "TYPE";
    public static final String COL_5 = "EXPRESSOSHOT";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "  + TABLE_NAME +
                COL_1 + "( INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " VARCHAR(255), " +
                COL_3 + " VARCHAR(255), " +
                COL_4 + " VARCHAR(255), " +
                COL_5 + " BOOLEAN);";
        db.execSQL(sql);
    }

    public boolean makeCoffee(Orders orders) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_2, orders.getSize());
        values.put(COL_3, orders.getSize());
        values.put(COL_4, orders.getType());
        values.put(COL_5, orders.isExpressoShot());
        db.insert(TABLE_NAME, null, values);
        db.close();

        return true;
    }

    public boolean updateCoffee(String id, Orders orders) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_2, orders.getSize());
        values.put(COL_3, orders.getType());
        values.put(COL_4, orders.isExpressoShot());
        db.update(TABLE_NAME, values, "ID = ?", new String[] {"id"});
        db.close();

        return true;
    }

    public int removeCoffee(String id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(TABLE_NAME, "ID = ?", new String[] {id});
        db.close();

        return delete;
    }

    public String getId(Orders orders) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_1 + " FROM " + TABLE_NAME + " WHERE "  + COL_3 + " = ? AND "  + COL_4 + " = ? AND "  + COL_5 + " = ?;",
                new String[]{orders.getUsername(), orders.getSize(), orders.getType()});

        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getString(0);
    }

    public Cursor getAllCoffee(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = ?", new String[]{username});

        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);

        onCreate(db);
    }
}
