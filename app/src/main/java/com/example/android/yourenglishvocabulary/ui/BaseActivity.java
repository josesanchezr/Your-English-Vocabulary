package com.example.android.yourenglishvocabulary.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.yourenglishvocabulary.R;

/**
 * Created by joseluis on 16/01/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_new_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_save_new_word:
                Intent intentSave = new Intent(this, SaveNewWordActivity.class);
                startActivity(intentSave);
                return true;
            case R.id.action_practice:
                Intent intentPractice = new Intent(this, PracticeYourEnglishActivity.class);
                startActivity(intentPractice);
                return true;
            case R.id.action_exit:
                Intent intentExit = new Intent(this, HomeActivity.class);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentExit.putExtra(HomeActivity.EXIT_APP, true);
                startActivity(intentExit);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
