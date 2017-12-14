package com.example.android.yourenglishvocabulary.photo;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by joseluis on 8/12/2017.
 */

public class PhotoLoader implements Target {

    private final String TAG = "PhotoLoader";

    private final String IMAGE_DIRECTORY = "PHOTOS";
    private final String name;
    private ImageView imageView;
    private Context context;

    public PhotoLoader(String name, ImageView imageView, Context context) {
        this.name = name;
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File photosFileDirectory = contextWrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        FileOutputStream outputStream = null;
        try {
            Log.d(TAG, "Photos directory" + photosFileDirectory.getPath());
            File imageFile = new File(photosFileDirectory, name);
            if(!imageFile.exists()) {
                imageFile.createNewFile();
                outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
                Log.i(TAG, "Photo saved on " + imageFile);
            }
            imageView.setImageBitmap(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
