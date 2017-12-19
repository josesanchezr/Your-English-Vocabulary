package com.example.android.yourenglishvocabulary.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.drive.GoogleDriveAPIUtil;
import com.example.android.yourenglishvocabulary.photo.PhotoLoader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveNewWordActivity extends AppCompatActivity implements TabWordFragment.ImageViewFromTabWordFragment {

    private static final String TAG = "SaveNewWordActivity";

    @BindView(R.id.image_new_word_imageview)
    ImageView imageNewWordImageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static String ACTIVE_TAG;
    public static final String EXAMPLES_TAG = "EXAMPLES";

    private static final int REQUEST_CODE_SIGN_IN = 0;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container)
    ViewPager mViewPager;

    TabWordFragment tabWordFragment;
    TabExamplesFragment tabExamplesFragment;

    // Requesting permission to RECORD_AUDIO
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_word);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Uri urlImage = Uri.parse("http://www.ikea.com/gb/en/images/products/lerhamn-table-black-brown__0238241_pe377689_s5.jpg");

        Picasso.with(this)
                .load(urlImage)
                .resize(360, 200)
                .centerInside()
                .into(new PhotoLoader("photoexample1.jpg", imageNewWordImageView,
                        getApplicationContext(), this, false));

        requesRecordAudioPermission();
    }

    private void requesRecordAudioPermission() {
        Log.d(TAG, "Version of Android " + Build.VERSION.RELEASE + " - SDK version " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionToRecordAccepted = checkRecordAudioPermission();
            if (!permissionToRecordAccepted) {
                Log.d(TAG, "Requesting Record Audio permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        } else {
            permissionToRecordAccepted = checkRecordAudioPermission();
        }
    }

    private boolean checkRecordAudioPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Record Audio permission granted");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // returning the current tab
            switch (position) {
                case 0:
                    tabWordFragment = new TabWordFragment();
                    return tabWordFragment;
                case 1:
                    tabExamplesFragment = new TabExamplesFragment();
                    return tabExamplesFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    @Override
    public void updateImage(String urlImage, String nameNewWord, FragmentActivity view) {

        imageNewWordImageView = view.findViewById(R.id.image_new_word_imageview);

        Picasso.with(this)
                .load(urlImage)
                .resize(360, 200)
                .centerInside()
                .into(new PhotoLoader(nameNewWord, imageNewWordImageView,
                        view.getApplicationContext(), this, true));
    }

    @Override
    protected void onStart() {
        super.onStart();
        signIn();
    }

    /**
     * Start sign in activity.
     */
    private void signIn() {
        Log.i(TAG, "Starting session in google");
        GoogleSignInClient mGoogleSignInClient = GoogleDriveAPIUtil.buildGoogleSignInClient(this);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i(TAG, "Request code of session start");
            // Called after user is signed in.
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "Session start successful");

                Log.i(TAG, "Request of google account");
                Task<GoogleSignInAccount> getAccountTask =
                        GoogleSignIn.getSignedInAccountFromIntent(data);
                if (getAccountTask.isSuccessful()) {
                    Log.i(TAG, "Request of google account successful");
                    GoogleDriveAPIUtil.initializeDriveClient(getAccountTask.getResult(), this);
                } else {
                    Log.e(TAG, "Session start failed");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
