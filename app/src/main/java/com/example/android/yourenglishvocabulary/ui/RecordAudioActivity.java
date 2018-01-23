package com.example.android.yourenglishvocabulary.ui;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.audio.MediaRecordUtil;
import com.example.android.yourenglishvocabulary.drive.AudioFileObserver;

import java.io.IOException;

public class RecordAudioActivity extends Activity implements VoiceView.OnRecordListener {

    private static final String TAG = RecordAudioActivity.class.getName();

    public static final String WORD_NAME = "wordName";

    private TextView mTextView;
    private VoiceView mVoiceView;
    private MediaRecorder mMediaRecorder;
    private Handler mHandler;

    private boolean mIsRecording = false;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        mTextView = findViewById(R.id.text);
        mVoiceView = findViewById(R.id.voiceview);
        mVoiceView.setOnRecordListener(this);

        mHandler = new Handler(Looper.getMainLooper());

        if (getIntent().hasExtra(WORD_NAME)) {
            fileName = getIntent().getStringExtra(WORD_NAME);
        }
    }

    @Override
    public void onRecordStart() {
        Log.d(TAG, "onRecordStart");
        try {
            mMediaRecorder = MediaRecordUtil.buildMediaRecord(fileName, this);

            FileObserver audioFileObserver = new AudioFileObserver(fileName,
                    MediaRecordUtil.buildAudioFilePath(fileName, this), FileObserver.CLOSE_WRITE, this);
            audioFileObserver.startWatching();

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mIsRecording = true;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    float radius = (float) Math.log10(Math.max(1, mMediaRecorder.getMaxAmplitude() - 500)) *
                            ScreenUtils.dp2px(RecordAudioActivity.this, 20);
                    mTextView.setText(String.valueOf(radius));
                    mVoiceView.animateRadius(radius);
                    if (mIsRecording) {
                        mHandler.postDelayed(this, 50);
                    }
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.mediarecorder_failed), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRecordFinish() {
        Log.d(TAG, "onRecordFinish");
        mIsRecording = false;
        mMediaRecorder.stop();
    }


    @Override
    protected void onDestroy() {
        if (mMediaRecorder != null) {
            if (mIsRecording) {
                mMediaRecorder.stop();
                mIsRecording = false;
            }
            mMediaRecorder.release();
        }
        super.onDestroy();
    }
}
