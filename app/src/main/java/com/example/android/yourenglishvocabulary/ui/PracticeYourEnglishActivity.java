package com.example.android.yourenglishvocabulary.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.yourenglishvocabulary.R;

public class PracticeYourEnglishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_your_english);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PracticeYourEnglishFragment fragment = new PracticeYourEnglishFragment();
        fragmentTransaction.add(R.id.practice_fragment, fragment);
        fragmentTransaction.commit();
    }
}
