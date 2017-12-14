package com.example.android.yourenglishvocabulary.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by joseluis on 26/11/2017.
 */

public class WordsProvider extends ContentProvider {
    private static final String LOG_TAG = WordsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WordsDBHelper wordsDBHelper;

    // Codes for the UriMatcher //////
    private static final int WORD = 100;
    private static final int WORD_WITH_ID = 200;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WordsContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, WordsContract.WordsEntry.TABLE_WORDS, WORD);
        matcher.addURI(authority, WordsContract.WordsEntry.TABLE_WORDS + "/#", WORD_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        wordsDBHelper = new WordsDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WORD:
                return WordsContract.WordsEntry.CONTENT_DIR_TYPE;
            case WORD_WITH_ID:
                return WordsContract.WordsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // All Flavors selected
            case WORD:
                retCursor = wordsDBHelper.getReadableDatabase().query(
                        WordsContract.WordsEntry.TABLE_WORDS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            // Individual flavor based on Id selected
            case WORD_WITH_ID:
                retCursor = wordsDBHelper.getReadableDatabase().query(
                        WordsContract.WordsEntry.TABLE_WORDS,
                        projection,
                        WordsContract.WordsEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            default:
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = wordsDBHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case WORD:
                long _id = db.insert(WordsContract.WordsEntry.TABLE_WORDS, null, contentValues);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = WordsContract.WordsEntry.buildFlavorsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = wordsDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case WORD:
                numDeleted = db.delete(
                        WordsContract.WordsEntry.TABLE_WORDS, selection, selectionArgs);
                break;
            case WORD_WITH_ID:
                numDeleted = db.delete(WordsContract.WordsEntry.TABLE_WORDS,
                        WordsContract.WordsEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = wordsDBHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (sUriMatcher.match(uri)) {
            case WORD:
                numUpdated = db.update(WordsContract.WordsEntry.TABLE_WORDS,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case WORD_WITH_ID:
                numUpdated = db.update(WordsContract.WordsEntry.TABLE_WORDS,
                        contentValues,
                        WordsContract.WordsEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
