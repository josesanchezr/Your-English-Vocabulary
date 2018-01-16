package com.example.android.yourenglishvocabulary.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.yourenglishvocabulary.R;

public class SoundVsImagesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_vs_images);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AnimationsSoundVsImagesFragment animationsSoundVsImagesFragment = new AnimationsSoundVsImagesFragment();
        fragmentTransaction.add(R.id.sound_vs_images_fragment, animationsSoundVsImagesFragment);
        fragmentTransaction.commit();
    }
}
