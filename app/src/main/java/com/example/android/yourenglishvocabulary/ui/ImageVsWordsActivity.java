package com.example.android.yourenglishvocabulary.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.yourenglishvocabulary.R;

public class ImageVsWordsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_vs_words);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AnimationsImageVsWordsFragment animationsImageVsWordsFragment = new AnimationsImageVsWordsFragment();
        fragmentTransaction.add(R.id.image_vs_words_fragment, animationsImageVsWordsFragment);
        fragmentTransaction.commit();
    }
}
