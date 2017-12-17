package com.example.android.yourenglishvocabulary.drive;

import android.app.Activity;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by joseluis on 17/12/2017.
 */

public class AudioFileObserver extends FileObserver {

    private static final String TAG = "AudioFileObserver";

    Activity activity;
    String audioFilePath;
    String fileName;

    public AudioFileObserver(String fileName, String audioFilePath, int mask, Activity activity) {
        super(audioFilePath, mask);
        this.fileName = fileName;
        this.audioFilePath = audioFilePath;
        this.activity = activity;
        Log.d(TAG, "Creating AudioFileObserver to " + audioFilePath);
    }

    @Override
    public void onEvent(int event, @Nullable String s) {
        Log.d(TAG, "Event " + event +
                " was triggered, it will invoke the createAudioFileInAppFolder method to save the " + audioFilePath + " audio file copy to Google Drive");
        GoogleDriveAPIUtil.createAudioFileInAppFolder(activity, fileName, audioFilePath);
    }
}
