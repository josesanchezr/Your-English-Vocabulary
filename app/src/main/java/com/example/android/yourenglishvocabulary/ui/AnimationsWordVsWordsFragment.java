package com.example.android.yourenglishvocabulary.ui;

import android.content.Context;
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
import com.example.android.yourenglishvocabulary.WordVsWordsFragment;
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
 * Use the {@link AnimationsWordVsWordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimationsWordVsWordsFragment extends Fragment
        implements WordVsWordsFragment.OnWordVsWordsFragmentInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "AniWordVsWordsFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnimationsWordVsWordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnimationsWordVsWordsFragment newInstance(String param1, String param2) {
        AnimationsWordVsWordsFragment fragment = new AnimationsWordVsWordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

            /*getChildFragmentManager().beginTransaction()
                    .replace(R.id.content_word_vs_words_fragment, nextFragment)
                    .commit();*/
        } else {
            Snackbar.make(view, "There isn't more words", Snackbar.LENGTH_SHORT).show();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
