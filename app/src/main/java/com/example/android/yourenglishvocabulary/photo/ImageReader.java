package com.example.android.yourenglishvocabulary.photo;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

/**
 * Created by joseluis on 8/01/2018.
 */

public class ImageReader {

    private static final String TAG = "ImageReader";

    private static final String IMAGE_DIRECTORY = "PHOTOS";

    public static Bitmap readImageFromFile(Context context, String wordName) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File photosFileDirectory = contextWrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        File imageFile = new File(photosFileDirectory, wordName + ".jpg");
        Log.d(TAG, "Image file path: " + imageFile.getAbsolutePath());
        Bitmap imageBitmap = null;
        if (imageFile.exists()) {
            imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            Log.d(TAG, "Image file not exists");
        }
        return imageBitmap;
    }
}
