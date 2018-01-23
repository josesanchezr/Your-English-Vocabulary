package com.example.android.yourenglishvocabulary.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.audio.MediaRecordUtil;
import com.example.android.yourenglishvocabulary.data.WordsContract;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by joseluis on 25/11/2017.
 */

public class TabExamplesFragment extends Fragment {

    private static final String TAG = "TabExamplesFragment";

    @BindView(R.id.example1_edittext)
    EditText example1EditText;

    @BindView(R.id.example2_edittext)
    EditText example2EditText;

    @BindView(R.id.example3_edittext)
    EditText example3EditText;

    @BindView(R.id.example4_edittext)
    EditText example4EditText;

    @BindView(R.id.example5_edittext)
    EditText example5EditText;

    @BindView(R.id.fab_example_tab)
    FloatingActionButton fabExampleTabFloatingActionButton;

    @BindView(R.id.play_example1_imageview)
    ImageView playExample1ImageView;

    @BindView(R.id.play_example2_imageview)
    ImageView playExample2ImageView;

    @BindView(R.id.play_example3_imageview)
    ImageView playExample3ImageView;

    @BindView(R.id.play_example4_imageview)
    ImageView playExample4ImageView;

    @BindView(R.id.play_example5_imageview)
    ImageView playExample5ImageView;

    @BindView(R.id.record_example1_imageview)
    ImageView recordExample1ImageView;

    @BindView(R.id.record_example2_imageview)
    ImageView recordExample2ImageView;

    @BindView(R.id.record_example3_imageview)
    ImageView recordExample3ImageView;

    @BindView(R.id.record_example4_imageview)
    ImageView recordExample4ImageView;

    @BindView(R.id.record_example5_imageview)
    ImageView recordExample5ImageView;

    private MediaPlayer mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaveNewWordActivity.ACTIVE_TAG = SaveNewWordActivity.EXAMPLES_TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_examples, container, false);
        ButterKnife.bind(this, rootView);

        fabExampleTabFloatingActionButton.setOnClickListener((view) -> {
            insertExample(rootView);
        });

        recordExample1ImageView.setOnClickListener((view) -> {
            recordAudioExample(view, 1);
        });

        playExample1ImageView.setOnClickListener((view) -> {
            startPlaying(TabWordFragment.word + "_example1", view);
        });

        recordExample2ImageView.setOnClickListener((view) -> {
            recordAudioExample(view, 2);
        });

        playExample2ImageView.setOnClickListener((view) -> {
            startPlaying(TabWordFragment.word + "_example2", view);
        });

        recordExample3ImageView.setOnClickListener((view) -> {
            recordAudioExample(view, 3);
        });

        playExample3ImageView.setOnClickListener((view) -> {
            startPlaying(TabWordFragment.word + "_example3", view);
        });

        recordExample4ImageView.setOnClickListener((view) -> {
            recordAudioExample(view, 4);
        });

        playExample4ImageView.setOnClickListener((view) -> {
            startPlaying(TabWordFragment.word + "_example4", view);
        });

        recordExample5ImageView.setOnClickListener((view) -> {
            recordAudioExample(view, 5);
        });

        playExample5ImageView.setOnClickListener((view) -> {
            startPlaying(TabWordFragment.word + "_example5", view);
        });

        return rootView;
    }

    private void recordAudioExample(View view, int example) {
        if (TextUtils.isEmpty(TabWordFragment.word)) {
            showMessage(getString(R.string.must_save_a_word_first), view);
            return;
        }
        Intent intent = new Intent(view.getContext(), RecordAudioActivity.class);
        intent.putExtra(RecordAudioActivity.WORD_NAME, TabWordFragment.word + "_example" + example);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void startPlaying(String fileName, View view) {
        if (TextUtils.isEmpty(TabWordFragment.word)) {
            showMessage(getString(R.string.must_save_a_word_first), view);
            return;
        }
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(MediaRecordUtil.buildAudioFilePath(
                    fileName, getContext()));
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare playback audio failed");
        }
    }

    private void showMessage(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action), null).show();
        return;
    }

    private void insertExample(View view) {
        boolean isExamples = false;
        if (!TextUtils.isEmpty(example1EditText.getText().toString())) {
            isExamples = true;
        }
        if (!TextUtils.isEmpty(example2EditText.getText().toString())) {
            isExamples = true;
        }
        if (!TextUtils.isEmpty(example3EditText.getText().toString())) {
            isExamples = true;
        }
        if (!TextUtils.isEmpty(example4EditText.getText().toString())) {
            isExamples = true;
        }
        if (!TextUtils.isEmpty(example5EditText.getText().toString())) {
            isExamples = true;
        }
        if (!isExamples) {
            showMessage(getString(R.string.add_one_example), view);
            return;
        }
        if (TabWordFragment.idWord == 0) {
            showMessage(getString(R.string.must_save_a_word_first), view);
            return;
        }

        ContentValues wordContentValues = new ContentValues();
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_FIRST_EXAMPLE, example1EditText.getText().toString());
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_SECOND_EXAMPLE, example2EditText.getText().toString());
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_THIRD_EXAMPLE, example3EditText.getText().toString());
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_FOURTH_EXAMPLE, example4EditText.getText().toString());
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_FIFTH_EXAMPLE, example5EditText.getText().toString());

        int mRowsUpdated = getActivity().getContentResolver().update(
                WordsContract.WordsEntry.buildFlavorsUri(TabWordFragment.idWord),
                wordContentValues,
                null,
                null);

        if (mRowsUpdated > 0) {
            showMessage(getString(R.string.examples_saved), view);
        }
    }
}
