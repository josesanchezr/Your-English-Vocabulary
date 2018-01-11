package com.example.android.yourenglishvocabulary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.yourenglishvocabulary.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PracticeYourEnglishFragment extends Fragment {

    @BindView(R.id.word_vs_words_button)
    Button wordVsWordsButton;

    @BindView(R.id.word_vs_images_button)
    Button wordVsImagesButton;

    @BindView(R.id.sound_vs_words_button)
    Button soundVsWords;

    @BindView(R.id.sound_vs_images_button)
    Button soundVsImages;

    @BindView(R.id.image_vs_words_button)
    Button imageVsWords;

    public PracticeYourEnglishFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_practice_your_english, container, false);
        ButterKnife.bind(this, rootView);

        wordVsWordsButton.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), WordVsWordsActivity.class);
            startActivity(intent);
        });

        wordVsImagesButton.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), WordVsImagesActivity.class);
            startActivity(intent);
        });

        soundVsWords.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), SoundVsImagesActivity.class);
            startActivity(intent);
        });

        imageVsWords.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), ImageVsWordsActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
