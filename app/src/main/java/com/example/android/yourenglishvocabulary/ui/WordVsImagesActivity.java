package com.example.android.yourenglishvocabulary.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.yourenglishvocabulary.R;

public class WordVsImagesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_vs_images);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AnimationsWordVsImagesFragment animationsWordVsImagesFragment = new AnimationsWordVsImagesFragment();
        fragmentTransaction.add(R.id.word_vs_images_fragment, animationsWordVsImagesFragment);
        fragmentTransaction.commit();
    }
}
