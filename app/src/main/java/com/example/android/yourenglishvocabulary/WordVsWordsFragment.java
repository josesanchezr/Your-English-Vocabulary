package com.example.android.yourenglishvocabulary;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WordVsWordsFragment.OnWordVsWordsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WordVsWordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordVsWordsFragment extends Fragment {

    private static final String TAG = "WordVsWordsFragment";

    private static final String ARG_WORD = "word";
    private static final String ARG_OPTION1 = "option1";
    private static final String ARG_OPTION2 = "option2";
    private static final String ARG_OPTION3 = "option3";
    private static final String ARG_OPTION4 = "option4";
    private static final String ARG_RIGHT_WORD = "rightWord";

    private String mWord;
    private String mOption1;
    private String mOption2;
    private String mOption3;
    private String mRightWord;

    private OnWordVsWordsFragmentInteractionListener mListener;

    @BindView(R.id.option1_button)
    Button option1Button;

    @BindView(R.id.option2_button)
    Button option2Button;

    @BindView(R.id.option3_button)
    Button option3Button;

    @BindView(R.id.option4_button)
    Button option4Button;

    @BindView(R.id.valid_button)
    Button validButton;

    @BindView(R.id.word_in_english)
    TextView wordInEnglishTextView;

    public WordVsWordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WordVsWordsFragment.
     */
    public static WordVsWordsFragment newInstance(String word, String option1, String option2,
                                                  String option3, String rightWord) {
        WordVsWordsFragment fragment = new WordVsWordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORD, word);
        args.putString(ARG_OPTION1, option1);
        args.putString(ARG_OPTION2, option2);
        args.putString(ARG_OPTION3, option3);
        args.putString(ARG_RIGHT_WORD, rightWord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWord = getArguments().getString(ARG_WORD);
            mOption1 = getArguments().getString(ARG_OPTION1);
            mOption2 = getArguments().getString(ARG_OPTION2);
            mOption3 = getArguments().getString(ARG_OPTION3);
            mRightWord = getArguments().getString(ARG_RIGHT_WORD);
        }
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_word_vs_words, container, false);
        ButterKnife.bind(this, rootView);

        validButton.setOnClickListener((view) -> {
            onButtonPressed(view);
        });

        wordInEnglishTextView.setText(mWord);

        Random random = new Random();
        int index = random.nextInt(4);

        switch (index) {
            case 0:
                option1Button.setText(mRightWord);
                option2Button.setText(mOption1);
                option3Button.setText(mOption2);
                option4Button.setText(mOption3);
                break;
            case 1:
                option2Button.setText(mRightWord);
                option1Button.setText(mOption1);
                option3Button.setText(mOption2);
                option4Button.setText(mOption3);
                break;
            case 2:
                option3Button.setText(mRightWord);
                option1Button.setText(mOption1);
                option2Button.setText(mOption2);
                option4Button.setText(mOption3);
                break;
            case 3:
                option4Button.setText(mRightWord);
                option1Button.setText(mOption1);
                option2Button.setText(mOption2);
                option3Button.setText(mOption3);
                break;
            default:
                Log.d(TAG, "Option not valid");
        }

        option1Button.setOnClickListener((view) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option1Button.setBackgroundColor(getResources().getColor(R.color.selectedButton, null));
            } else {
                option1Button.setBackgroundColor(getResources().getColor(R.color.selectedButton));
            }

        });

        return rootView;
    }

    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onWordVsWordsFragmentInteraction(view);
        }
    }

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment instanceof OnWordVsWordsFragmentInteractionListener) {
            mListener = (OnWordVsWordsFragmentInteractionListener) parentFragment;
        } else {
            throw new RuntimeException(parentFragment.toString()
                    + " must implement OnWordVsWordsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnWordVsWordsFragmentInteractionListener {
        void onWordVsWordsFragmentInteraction(View view);
    }
}
