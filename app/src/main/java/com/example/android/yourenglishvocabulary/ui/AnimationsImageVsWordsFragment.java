package com.example.android.yourenglishvocabulary.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.data.WordsContract;
import com.example.android.yourenglishvocabulary.util.QuestionsUtil;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnimationsImageVsWordsFragment extends Fragment
        implements ImageVsWordsFragment.OnImageVsWordsFragmentInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "AnImageVsWordsFragment";
    private final int ID_WORDS_LOADER = 0;
    private int maxQuestions;
    private int currentPositionQuestion = 0;

    private static final long MOVE_DEFAULT_TIME = 500;
    private static final long FADE_DEFAULT_TIME = 300;

    public AnimationsImageVsWordsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animations_image_vs_words, container, false);
    }

    private void createInitialFragment() {
        if (QuestionsUtil.getWordsInEnglish().size() > 0) {
            Log.d(TAG, "creating the initial fragment");
            ImageVsWordsFragment fragment = createFragment(0);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.content_image_vs_words_fragment, fragment)
                    .commit();
        }
    }

    private ImageVsWordsFragment createFragment(int position) {
        String wordEnglish = QuestionsUtil.getWordsInEnglish().get(position);
        List<String> wordsInEnglish = QuestionsUtil.getMapWordVsWords().get(wordEnglish);
        String rightWord = wordsInEnglish.get(0);
        String option1 = wordsInEnglish.get(1);
        String option2 = wordsInEnglish.get(2);
        String option3 = wordsInEnglish.get(3);
        return ImageVsWordsFragment.newInstance(wordEnglish, option1, option2, option3, rightWord);
    }

    private void updateFragment(int position, View view) {
        if (position < QuestionsUtil.getWordsInEnglish().size()) {
            Log.d(TAG, "Updating the fragment");

            Fragment previousFragment = getChildFragmentManager().findFragmentById(R.id.content_image_vs_words_fragment);
            ImageVsWordsFragment nextFragment = createFragment(position);

            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

            // 1. Exit for previous Fragment
            Fade exitFade = new Fade();
            exitFade.setDuration(FADE_DEFAULT_TIME);
            previousFragment.setExitTransition(exitFade);

            // 2. Enter Transition for New Fragment
            Fade enterFade = new Fade();
            enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
            enterFade.setDuration(FADE_DEFAULT_TIME);
            nextFragment.setEnterTransition(enterFade);

            fragmentTransaction.replace(R.id.content_image_vs_words_fragment, nextFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            Snackbar.make(view, "There isn't more words", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageVsWordsFragmentInteraction(View view) {
        Log.d(TAG, "Click in Fragment of Animation");
        currentPositionQuestion++;
        updateFragment(currentPositionQuestion, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        Log.d(TAG, "Initializing the Loader");
        getLoaderManager().initLoader(ID_WORDS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "Creating the Loader");
        return new CursorLoader(
                getActivity(),
                WordsContract.WordsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Receiving the data from the Loader");
        maxQuestions = 10;
        QuestionsUtil.buildQuestions(data, maxQuestions);
        createInitialFragment();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
