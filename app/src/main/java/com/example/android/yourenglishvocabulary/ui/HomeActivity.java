package com.example.android.yourenglishvocabulary.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.yourenglishvocabulary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.save_new_word_button)
    Button saveNewWordButton;

    @BindView(R.id.practice_button)
    Button practiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        saveNewWordButton.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), SaveNewWordActivity.class);
            view.getContext().startActivity(intent);
        });

        practiceButton.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), PracticeYourEnglishActivity.class);
            view.getContext().startActivity(intent);
        });
    }
}
