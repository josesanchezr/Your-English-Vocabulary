package com.example.android.yourenglishvocabulary.audio;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;

/**
 * Created by joseluis on 10/12/2017.
 */

public class MediaRecordUtil {

    private static final String TAG = "MediaRecordUtil";

    private static final String SOUNDS_DIRECTORY = "SOUNDS";
    private static final String EXTENSION_SOUND = ".mp3";

    public static MediaRecorder buildMediaRecord(String fileName, Context context) {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        String audioFilePath = buildAudioFilePath(fileName, context);

        mediaRecorder.setOutputFile(audioFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        return mediaRecorder;
    }

    public static String buildAudioFilePath(String fileName, Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File soundsFileDirectory = contextWrapper.getDir(SOUNDS_DIRECTORY, Context.MODE_PRIVATE);
        File audioFilePath = new File(soundsFileDirectory, fileName + EXTENSION_SOUND);
        Log.d(TAG, "Complete path of audio file: " + audioFilePath);
        return audioFilePath.getPath();
    }
}
