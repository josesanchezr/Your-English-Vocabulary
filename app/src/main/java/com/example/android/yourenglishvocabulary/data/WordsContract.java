package com.example.android.yourenglishvocabulary.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by joseluis on 26/11/2017.
 */

public class WordsContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.yourenglishvocabulary";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class WordsEntry implements BaseColumns {
        // table name
        public static final String TABLE_WORDS = "words";

        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_WORD_ENGLISH = "word_english";
        public static final String COLUMN_WORD_SPANISH = "word_spanish";
        public static final String COLUMN_KIND_WORD = "kind_word";
        public static final String COLUMN_URL_IMAGE = "url_image";

        public static final String COLUMN_FIRST_EXAMPLE = "first_example";
        public static final String COLUMN_SECOND_EXAMPLE = "second_example";
        public static final String COLUMN_THIRD_EXAMPLE = "third_example";
        public static final String COLUMN_FOURTH_EXAMPLE = "fourth_example";
        public static final String COLUMN_FIFTH_EXAMPLE = "fifth_example";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_WORDS).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + TABLE_WORDS;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_WORDS;

        // for building URIs on insertion
        public static Uri buildFlavorsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
