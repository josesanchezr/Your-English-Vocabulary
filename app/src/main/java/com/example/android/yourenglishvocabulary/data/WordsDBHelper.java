package com.example.android.yourenglishvocabulary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by joseluis on 26/11/2017.
 */

public class WordsDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = WordsDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "words.db";
    private static final int DATABASE_VERSION = 1;

    public WordsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WORDS_TABLE = "CREATE TABLE " +
                WordsContract.WordsEntry.TABLE_WORDS + "(" + WordsContract.WordsEntry._ID +
                " INTEGER PRIMARY KEY, " +
                WordsContract.WordsEntry.COLUMN_WORD_ENGLISH + " TEXT NOT NULL, " +
                WordsContract.WordsEntry.COLUMN_WORD_SPANISH + " TEXT NOT NULL, " +
                WordsContract.WordsEntry.COLUMN_KIND_WORD + " TEXT NOT NULL, " +
                WordsContract.WordsEntry.COLUMN_URL_IMAGE + " TEXT NOT NULL, " +

                WordsContract.WordsEntry.COLUMN_FIRST_EXAMPLE + " TEXT NULL, " +
                WordsContract.WordsEntry.COLUMN_SECOND_EXAMPLE + " TEXT NULL, " +
                WordsContract.WordsEntry.COLUMN_THIRD_EXAMPLE + " TEXT NULL, " +
                WordsContract.WordsEntry.COLUMN_FOURTH_EXAMPLE + " TEXT NULL, " +
                WordsContract.WordsEntry.COLUMN_FIFTH_EXAMPLE + " TEXT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_WORDS_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WordsContract.WordsEntry.TABLE_WORDS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                WordsContract.WordsEntry.TABLE_WORDS + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
