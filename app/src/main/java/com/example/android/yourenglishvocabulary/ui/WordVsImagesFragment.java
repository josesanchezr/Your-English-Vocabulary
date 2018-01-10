package com.example.android.yourenglishvocabulary.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.photo.ImageReader;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WordVsImagesFragment.OnWordVsImagesFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WordVsImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordVsImagesFragment extends Fragment {

    private static final String TAG = "WordVsImagesFragment";

    private static final String ARG_WORD = "word";
    private static final String ARG_OPTION1 = "option1";
    private static final String ARG_OPTION2 = "option2";
    private static final String ARG_OPTION3 = "option3";
    private static final String ARG_RIGHT_WORD = "rightWord";

    private String mWord;
    private String mOption1;
    private String mOption2;
    private String mOption3;
    private String mRightWord;

    private OnWordVsImagesFragmentInteractionListener mListener;

    @BindView(R.id.option1_imageview)
    ImageView option1Button;

    @BindView(R.id.option2_imageview)
    ImageView option2Button;

    @BindView(R.id.option3_imageview)
    ImageView option3Button;

    @BindView(R.id.option4_imageview)
    ImageView option4Button;

    @BindView(R.id.valid_button)
    Button validButton;

    @BindView(R.id.word_in_english)
    TextView wordInEnglishTextView;

    private int selectedOptionRight;
    private int selectedOptionInt;
    private boolean isValidatedQuestion;

    public WordVsImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WordVsImagesFragment.
     */
    public static WordVsImagesFragment newInstance(String word, String option1, String option2,
                                                   String option3, String rightWord) {
        WordVsImagesFragment fragment = new WordVsImagesFragment();
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
        isValidatedQuestion = false;
        selectedOptionInt = 0;
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_word_vs_images, container, false);
        ButterKnife.bind(this, rootView);

        validButton.setOnClickListener((view) -> {
            if (!isValidatedQuestion) {
                isValidatedQuestion = true;
                validAnswer(view);
            } else {
                nextQuestion(view);
            }
        });

        wordInEnglishTextView.setText(mWord);

        Random random = new Random();
        int index = random.nextInt(4);
        selectedOptionRight = index + 1;

        switch (index) {
            case 0:
                option1Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mRightWord));
                option2Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption1));
                option3Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption2));
                option4Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption3));
                break;
            case 1:
                option2Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mRightWord));
                option1Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption1));
                option3Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption2));
                option4Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption3));
                break;
            case 2:
                option3Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mRightWord));
                option1Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption1));
                option2Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption2));
                option4Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption3));
                break;
            case 3:
                option4Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mRightWord));
                option1Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption1));
                option2Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption2));
                option3Button.setImageBitmap(ImageReader.readImageFromFile(getContext(), mOption3));
                break;
            default:
                Log.d(TAG, "Option not valid");
        }

        option1Button.setOnClickListener((view) -> {
            selectedOptionInt = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option1Button.setBackgroundColor(getResources().getColor(R.color.selectedButton, null));
                option2Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option3Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option4Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                option1Button.setBackgroundColor(getResources().getColor(R.color.selectedButton));
                option2Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option3Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option4Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        option2Button.setOnClickListener((view) -> {
            selectedOptionInt = 2;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option2Button.setBackgroundColor(getResources().getColor(R.color.selectedButton, null));
                option1Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option3Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option4Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                option2Button.setBackgroundColor(getResources().getColor(R.color.selectedButton));
                option1Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option3Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option4Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        option3Button.setOnClickListener((view) -> {
            selectedOptionInt = 3;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option3Button.setBackgroundColor(getResources().getColor(R.color.selectedButton, null));
                option1Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option2Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option4Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                option3Button.setBackgroundColor(getResources().getColor(R.color.selectedButton));
                option1Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option2Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option4Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        option4Button.setOnClickListener((view) -> {
            selectedOptionInt = 4;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option4Button.setBackgroundColor(getResources().getColor(R.color.selectedButton, null));
                option1Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option2Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                option3Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                option4Button.setBackgroundColor(getResources().getColor(R.color.selectedButton));
                option1Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option2Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option3Button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        return rootView;
    }

    private void validAnswer(View view) {
        if (selectedOptionInt == 0) {
            isValidatedQuestion = false;
            Snackbar.make(view, "Please, you must select one option", Snackbar.LENGTH_LONG).show();
        } else {
            if (selectedOptionRight == selectedOptionInt) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    changeButtonColor(selectedOptionInt, getResources().getColor(R.color.rightAnswer, null));
                    validButton.setBackgroundColor(getResources().getColor(R.color.rightAnswer, null));
                } else {
                    changeButtonColor(selectedOptionInt, getResources().getColor(R.color.rightAnswer));
                    validButton.setBackgroundColor(getResources().getColor(R.color.rightAnswer));
                }
                Snackbar.make(view, "Right answer.", Snackbar.LENGTH_LONG).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    changeButtonColor(selectedOptionInt, getResources().getColor(R.color.wrongAnswer, null));
                    validButton.setBackgroundColor(getResources().getColor(R.color.wrongAnswer, null));
                } else {
                    changeButtonColor(selectedOptionInt, getResources().getColor(R.color.wrongAnswer));
                    validButton.setBackgroundColor(getResources().getColor(R.color.wrongAnswer));
                }
                Snackbar.make(view, "Wrong answer.", Snackbar.LENGTH_LONG).show();
            }
            validButton.setText(getResources().getString(R.string.next_text));
        }
    }

    private void changeButtonColor(int idButton, int colorButton) {
        switch (idButton) {
            case 1:
                option1Button.setBackgroundColor(colorButton);
                break;
            case 2:
                option2Button.setBackgroundColor(colorButton);
                break;
            case 3:
                option3Button.setBackgroundColor(colorButton);
                break;
            case 4:
                option4Button.setBackgroundColor(colorButton);
                break;
        }
    }

    public void nextQuestion(View view) {
        if (mListener != null) {
            mListener.onWordVsImagesFragmentInteraction(view);
        }
    }

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment instanceof WordVsImagesFragment.OnWordVsImagesFragmentInteractionListener) {
            mListener = (WordVsImagesFragment.OnWordVsImagesFragmentInteractionListener) parentFragment;
        } else {
            throw new RuntimeException(parentFragment.toString()
                    + " must implement OnWordVsImagesFragmentInteractionListener");
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
    public interface OnWordVsImagesFragmentInteractionListener {
        void onWordVsImagesFragmentInteraction(View view);
    }
}
