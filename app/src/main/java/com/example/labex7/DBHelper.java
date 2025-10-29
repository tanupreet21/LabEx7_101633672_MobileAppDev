package com.example.labex7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME="BookDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME="books";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        // cursor factory is when you're using your own custom cursor factory
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "author TEXT," +
                "genre TEXT," +
                "year INTEGER)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    // CRUD methods

    boolean addBook(String title, String author, String genre, int year){
        // we need a writeable instance of db
        SQLiteDatabase db = this.getWritableDatabase();
        //we need content value instance to write into db
        ContentValues cv = new ContentValues();
        //We're passing the table names as key to the contentvalues
        cv.put("title", title);
        cv.put("author", author);
        cv.put("genre", genre);
        cv.put("year", year);
        //The insert method in sqlite database
        // returns the number of rows affected(inserted)
        // if the transaction is not successful we get -1
        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    //read - fetch data from db
    Cursor getAllBooks(){

        //We need readonly instance of db
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME+ ";";
        return sqLiteDatabase.rawQuery(sql, null);
    }

    boolean updateBook(int id, String title, String author, String genre, int year){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("author", author);
        cv.put("genre", genre);
        cv.put("year", year);

        //The update method returns the number of rows affected

        return db.update(
                TABLE_NAME,
                cv,
                id + "=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    boolean deleteBook(int id){
        //we need writeable instance
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        //The delete returns the no of rows affected
        return sqLiteDatabase.delete(
                TABLE_NAME,
                id + "=?",
                new String[]{String.valueOf(id)}) > 0;
    }
}
