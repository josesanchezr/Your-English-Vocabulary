package com.example.android.yourenglishvocabulary.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.audio.MediaRecordUtil;
import com.example.android.yourenglishvocabulary.data.WordsContract;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by joseluis on 25/11/2017.
 */

public class TabWordFragment extends Fragment {

    private static final String TAG = "TabWordFragment";

    @BindView(R.id.word_english_edittext)
    EditText wordEnglishEditText;

    @BindView(R.id.word_spanish_edittext)
    EditText wordSpanishEditText;

    @BindView(R.id.container_kind_word)
    RadioGroup containerKindWordRadioGroup;

    @BindView(R.id.url_image_edittext)
    EditText urlImageEditText;

    @BindView(R.id.fab_word_tab)
    FloatingActionButton wordTabFloatingActionButton;

    @BindView(R.id.record_word_imageview)
    ImageView recordWordImageView;

    @BindView(R.id.play_word_imageview)
    ImageView playWordImageView;

    private String selectedKindWord;
    public static long idWord;
    public static String word;
    private MediaPlayer mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idWord = 0;
        word = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_word, container, false);
        ButterKnife.bind(this, rootView);

        wordTabFloatingActionButton.setOnClickListener((view) -> {
            int selectedIdRadioButton = containerKindWordRadioGroup.getCheckedRadioButtonId();
            selectedKindWord = obtainSelectedKindWord(selectedIdRadioButton);
            insertWord(rootView);
        });

        recordWordImageView.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), RecordAudioActivity.class);
            intent.putExtra(RecordAudioActivity.WORD_NAME, wordEnglishEditText.getText().toString());
            startActivity(intent);
        });

        playWordImageView.setOnClickListener((view) -> {
            startPlaying();
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(MediaRecordUtil.buildAudioFilePath(
                    wordEnglishEditText.getText().toString(), getContext()));
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare playback audio failed");
        }
    }

    private String obtainSelectedKindWord(int selectedIdRadioButton) {
        String selectedKindWord = "";
        switch (selectedIdRadioButton) {
            case R.id.pronoun_radiobutton:
                selectedKindWord = "PRONOUN";
                break;
            case R.id.adjective_radiobutton:
                selectedKindWord = "ADJECTIVE";
                break;
            case R.id.adverb_radiobutton:
                selectedKindWord = "ADVERB";
                break;
            case R.id.regular_verb_radiobutton:
                selectedKindWord = "REGULAR_VERB";
                break;
            case R.id.irregular_verb_radiobutton:
                selectedKindWord = "IRREGULAR_VERB";
                break;
            case R.id.preposition_radiobutton:
                selectedKindWord = "PREPOSITION";
                break;
            default:
                break;
        }
        return selectedKindWord;
    }

    public void updateImage(String urlImagen, String nameNewWord) {
        ImageViewFromTabWordFragment update = new SaveNewWordActivity();
        update.updateImage(urlImagen, nameNewWord, getActivity());
    }

    public interface ImageViewFromTabWordFragment {
        void updateImage(String urlImage, String nameNewWord, FragmentActivity view);
    }

    private void insertWord(View view) {
        if (TextUtils.isEmpty(wordEnglishEditText.getText().toString()) ||
                TextUtils.isEmpty(wordEnglishEditText.getText().toString().trim())) {
            Snackbar.make(view, getString(R.string.word_english_empty), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action), null).show();
            wordEnglishEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(wordSpanishEditText.getText().toString()) ||
                TextUtils.isEmpty(wordSpanishEditText.getText().toString().trim())) {
            Snackbar.make(view, getString(R.string.word_spanish_empty), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action), null).show();
            wordSpanishEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(selectedKindWord)) {
            Snackbar.make(view, getString(R.string.kind_word_not_selected), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action), null).show();
            return;
        }

        if (TextUtils.isEmpty(urlImageEditText.getText().toString()) ||
                TextUtils.isEmpty(urlImageEditText.getText().toString().trim())) {
            Snackbar.make(view, getString(R.string.url_image_empty), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action), null).show();
            urlImageEditText.requestFocus();
            return;
        }

        String[] mSelectionArgs = {""};
        mSelectionArgs[0] = wordEnglishEditText.getText().toString();

        Cursor cursor = getActivity().getContentResolver().query(
                WordsContract.WordsEntry.CONTENT_URI,
                null,
                WordsContract.WordsEntry.COLUMN_WORD_ENGLISH + " = ?",
                mSelectionArgs,
                null);

        ContentValues wordContentValues = new ContentValues();
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_WORD_ENGLISH, wordEnglishEditText.getText().toString());
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_WORD_SPANISH, wordSpanishEditText.getText().toString());
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_KIND_WORD, selectedKindWord);
        wordContentValues.put(WordsContract.WordsEntry.COLUMN_URL_IMAGE, urlImageEditText.getText().toString());

        if (cursor == null || cursor.getCount() <= 0) {
            Uri newUri = getActivity().getContentResolver().insert(WordsContract.WordsEntry.CONTENT_URI, wordContentValues);
            long id = ContentUris.parseId(newUri);
            if (id > 0) {
                idWord = id;
                word = wordEnglishEditText.getText().toString();
                Snackbar.make(view, getString(R.string.word_saved_with_success), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.action), null).show();
                updateImage(urlImageEditText.getText().toString(), wordEnglishEditText.getText().toString());
            }
        } else if (cursor.getCount() > 0) {
            int indexId = cursor.getColumnIndex(WordsContract.WordsEntry._ID);

            if (cursor.moveToFirst()) {

                String id = cursor.getString(indexId);
                mSelectionArgs[0] = id;
                int mRowsUpdated = getActivity().getContentResolver().update(WordsContract.WordsEntry.CONTENT_URI,
                        wordContentValues,
                        WordsContract.WordsEntry._ID + " = ?",
                        mSelectionArgs);

                if (mRowsUpdated > 0) {
                    idWord = Long.parseLong(id);
                    word = wordEnglishEditText.getText().toString();
                    Snackbar.make(view, getString(R.string.word_updated_with_success), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.action), null).show();
                    updateImage(urlImageEditText.getText().toString(), wordEnglishEditText.getText().toString());
                }
            }
        }
    }
}
