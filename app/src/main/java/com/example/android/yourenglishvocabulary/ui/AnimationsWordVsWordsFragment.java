package com.example.android.yourenglishvocabulary.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.data.WordsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnimationsWordVsWordsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AnimationsWordVsWordsFragment extends Fragment
        implements WordVsWordsFragment.OnWordVsWordsFragmentInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "AniWordVsWordsFragment";

    private Cursor mCursor;
    private final int ID_WORDS_LOADER = 0;
    private int maxQuestions;
    private int currentPositionQuestion = 0;

    List<String> wordsInEnglish;
    Map<String, List<String>> mapWordVsWords;

    private static final long MOVE_DEFAULT_TIME = 500;
    private static final long FADE_DEFAULT_TIME = 300;

    public AnimationsWordVsWordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "Initializing the view of fragment");
        return inflater.inflate(R.layout.fragment_animations_word_vs_words, container, false);

    }

    private void buildQuestions(Cursor cursor, int maxQuestions) {
        Log.d(TAG, "Build questions");
        if (cursor != null) {
            int indexWordEnglish = cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_WORD_ENGLISH);
            int indexWordSpanish = cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_WORD_SPANISH);

            int count = cursor.getCount();
            Log.d(TAG, "Number of rows in the cursor " + count);
            Random random = new Random();
            int indexPosition = random.nextInt(count);
            Log.d(TAG, "index position " + indexPosition);
            int currentPosition;

            String wordEnglish;
            String wordSpanish;

            wordsInEnglish = new ArrayList<>();
            mapWordVsWords = new HashMap<>();

            while (cursor.moveToPosition(indexPosition)) {
                currentPosition = cursor.getPosition();
                Log.d(TAG, "current position " + currentPosition);
                wordEnglish = cursor.getString(indexWordEnglish);
                wordSpanish = cursor.getString(indexWordSpanish);

                if (!mapWordVsWords.containsKey(wordEnglish)) {
                    List<String> wordsInSpanish = new ArrayList<>();
                    wordsInSpanish.add(wordSpanish);
                    Log.d(TAG, "Added the word " + wordEnglish);

                    while (wordsInSpanish.size() < 4) {
                        indexPosition = random.nextInt(count);
                        cursor.moveToPosition(indexPosition);
                        Log.d(TAG, "New index position " + indexPosition);
                        if (currentPosition != indexPosition) {
                            wordSpanish = cursor.getString(indexWordSpanish);
                            if (!wordsInSpanish.contains(wordSpanish)) {
                                wordsInSpanish.add(wordSpanish);
                                Log.d(TAG, "Added the word in Spanish " + wordSpanish);
                            }
                        }
                    }
                    mapWordVsWords.put(wordEnglish, wordsInSpanish);
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

    private void createInitialFragment() {
        if (wordsInEnglish.size() > 0) {
            Log.d(TAG, "creating the initial fragment");
            WordVsWordsFragment fragment = createFragment(0);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.content_word_vs_words_fragment, fragment)
                    .commit();
        } else {
            Toast.makeText(getContext(),
                    "There aren't words to make the questions",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private WordVsWordsFragment createFragment(int position) {
        String wordEnglish = wordsInEnglish.get(position);
        List<String> wordsInSpanish = mapWordVsWords.get(wordEnglish);
        String rightWord = wordsInSpanish.get(0);
        String option1 = wordsInSpanish.get(1);
        String option2 = wordsInSpanish.get(2);
        String option3 = wordsInSpanish.get(3);
        return WordVsWordsFragment.newInstance(wordEnglish, option1, option2, option3, rightWord);
    }

    private void updateFragment(int position, View view) {
        if (position < wordsInEnglish.size()) {
            Log.d(TAG, "Updating the fragment");

            Fragment previousFragment = getChildFragmentManager().findFragmentById(R.id.content_word_vs_words_fragment);
            WordVsWordsFragment nextFragment = createFragment(position);

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

            fragmentTransaction.replace(R.id.content_word_vs_words_fragment, nextFragment);
            fragmentTransaction.commitAllowingStateLoss();

        } else {
            Snackbar.make(view, "There isn't more words", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWordVsWordsFragmentInteraction(View view) {
        Log.d(TAG, "Click en Fragment of Animation");
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
        buildQuestions(data, maxQuestions);
        createInitialFragment();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
