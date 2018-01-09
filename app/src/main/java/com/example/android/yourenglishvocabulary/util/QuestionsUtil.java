package com.example.android.yourenglishvocabulary.util;

import android.database.Cursor;
import android.util.Log;

import com.example.android.yourenglishvocabulary.data.WordsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by joseluis on 8/01/2018.
 */

public class QuestionsUtil {

    private static final String TAG = "QuestionsUtil";

    private static List<String> wordsInEnglish;
    private static Map<String, List<String>> mapWordVsWords;

    public static List<String> getWordsInEnglish() {
        return wordsInEnglish;
    }

    public static Map<String, List<String>> getMapWordVsWords() {
        return mapWordVsWords;
    }

    public static void buildQuestions(Cursor cursor, int maxQuestions) {
        Log.d(TAG, "Build questions");
        if (cursor != null) {
            int indexWordEnglish = cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_WORD_ENGLISH);
            //int indexWordSpanish = cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_WORD_SPANISH);

            int count = cursor.getCount();
            Log.d(TAG, "Number of rows in the cursor " + count);
            Random random = new Random();
            int indexPosition = random.nextInt(count);
            Log.d(TAG, "index position " + indexPosition);
            int currentPosition;

            String wordEnglish;
            String wordEnglish2;

            wordsInEnglish = new ArrayList<>();
            mapWordVsWords = new HashMap<>();

            while (cursor.moveToPosition(indexPosition)) {
                currentPosition = cursor.getPosition();
                Log.d(TAG, "current position " + currentPosition);
                wordEnglish = cursor.getString(indexWordEnglish);
                wordEnglish2 = cursor.getString(indexWordEnglish);

                if (!mapWordVsWords.containsKey(wordEnglish)) {
                    List<String> wordsInEnglish2 = new ArrayList<>();
                    wordsInEnglish2.add(wordEnglish2);
                    Log.d(TAG, "Added the word " + wordEnglish2);

                    while (wordsInEnglish2.size() < 4) {
                        indexPosition = random.nextInt(count);
                        cursor.moveToPosition(indexPosition);
                        Log.d(TAG, "New index position " + indexPosition);
                        if (currentPosition != indexPosition) {
                            wordEnglish2 = cursor.getString(indexWordEnglish);
                            if (!wordsInEnglish2.contains(wordEnglish2)) {
                                wordsInEnglish2.add(wordEnglish2);
                                Log.d(TAG, "Added the word in English " + wordEnglish2);
                            }
                        }
                    }
                    mapWordVsWords.put(wordEnglish, wordsInEnglish2);
                    wordsInEnglish.add(wordEnglish);
                    Log.d(TAG, "Added the word " + wordEnglish + " to the list");
                }

                if (mapWordVsWords.size() == maxQuestions) {
                    break;
                } else if (mapWordVsWords.size() == count) {
                    break;
                }
                indexPosition = random.nextInt(count);
            }
            Log.d(TAG, "Builded questions " + mapWordVsWords.size());
        }
    }
}
