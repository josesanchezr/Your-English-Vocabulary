package com.example.android.yourenglishvocabulary.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import android.widget.Toast;

import com.example.android.yourenglishvocabulary.R;
import com.example.android.yourenglishvocabulary.photo.PhotoLoader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveNewWordActivity extends AppCompatActivity implements TabWordFragment.ImageViewFromTabWordFragment {

    private static final String TAG = "SaveNewWordActivity";

    @BindView(R.id.image_new_word_imageview)
    ImageView imageNewWordImageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TITLE_ACTIVITY = "Save New Word";

    public static String ACTIVE_TAG;
    public static final String WORD_TAG = "WORD";
    public static final String EXAMPLES_TAG = "EXAMPLES";

    private GoogleSignInClient mGoogleSignInClient;
    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;
    private Bitmap mBitmapToSave;

    private static final int REQUEST_CODE_SIGN_IN = 0;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container)
    ViewPager mViewPager;

    TabWordFragment tabWordFragment;
    TabExamplesFragment tabExamplesFragment;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_word);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Uri urlImage = Uri.parse("http://alegorias.es/wp-content/uploads/2017/03/sonar-flor-blanca-alegorias.es-1-360x200.jpg");

        Picasso.with(this)
                .load(urlImage)
                .resize(360, 200)
                .centerCrop()
                .into(new PhotoLoader("photoexample1.jpg", imageNewWordImageView, getApplicationContext()));

        requesRecordAudioPermission();
    }

    private boolean isExternalStorageWritable() {
        Log.d(TAG, "Version de Android " + Build.VERSION.RELEASE + " - version del SDK " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permisos obtenidos 1 para guardar en almacenamiento externo");
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        } else {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                Log.d(TAG, "Permisos obtenidos 2 para guardar en almacenamiento externo");
                return true;
            }
        }

        Log.d(TAG, "No tiene permisos para guardar en almacenamiento externo");
        return false;
    }

    private void requesRecordAudioPermission(){
        Log.d(TAG, "Version de Android " + Build.VERSION.RELEASE + " - version del SDK " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Record Audio permission granted");
            } else {
                Log.d(TAG, "Requesting Record Audio permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
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

        imageNewWordImageView = (ImageView) view.findViewById(R.id.image_new_word_imageview);

        Picasso.with(this)
                .load(urlImage)
                .resize(360, 200)
                .centerCrop()
                .into(new PhotoLoader(nameNewWord, imageNewWordImageView, view.getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //signIn2();
    }

    /**
     * Start sign in activity.
     */
    private void signIn2() {
        Log.i(TAG, "Iniciando el inicio de sesion en google");
        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    /**
     * Starts the sign-in process and initializes the Drive client.
     */
    protected void signIn() {
        Set<Scope> requiredScopes = new HashSet<>(2);
        requiredScopes.add(Drive.SCOPE_FILE);
        requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
            initializeDriveClient(signInAccount);
        } else {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestScopes(Drive.SCOPE_APPFOLDER)
                            .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
            startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }

    /**
     * Continues the sign-in process, initializing the Drive clients with the current
     * user's account.
     */
    private void initializeDriveClient(GoogleSignInAccount signInAccount) {
        Log.i(TAG, "Inicializando cliente de google drive");
        mDriveClient = Drive.getDriveClient(getApplicationContext(), signInAccount);
        mDriveResourceClient = Drive.getDriveResourceClient(getApplicationContext(), signInAccount);
        //createFolderInGoogleDrive();
        createFileInAppFolder();
    }

    /**
     * Shows a toast message.
     */
    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected DriveClient getDriveClient() {
        return mDriveClient;
    }

    protected DriveResourceClient getDriveResourceClient() {
        return mDriveResourceClient;
    }

    /**
     * Build a Google SignIn client.
     */
    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_APPFOLDER)
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i(TAG, "Codigo de solicitud de inicio de sesion");
            // Called after user is signed in.
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "Inicio de sesion fue exitoso");

                Log.i(TAG, "Solicitando cuenta de google");
                Task<GoogleSignInAccount> getAccountTask =
                        GoogleSignIn.getSignedInAccountFromIntent(data);
                if (getAccountTask.isSuccessful()) {
                    Log.i(TAG, "Solicitando de cuenta de google exitosa");
                    initializeDriveClient(getAccountTask.getResult());
                } else {
                    Log.e(TAG, "Inicio de sesion fallido");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createFolderInGoogleDrive() {
        Log.i(TAG, "Creando folder en google drive");
        mDriveResourceClient.getRootFolder().continueWithTask(new Continuation<DriveFolder, Task<DriveFolder>>() {
            @Override
            public Task<DriveFolder> then(@NonNull Task<DriveFolder> task) throws Exception {
                DriveFolder parentFolder = task.getResult();
                Log.i(TAG, "Obteniendo el folder root: " + parentFolder.getDriveId().toString());
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("YourEnglisVocabulary22")
                        .setMimeType(DriveFolder.MIME_TYPE)
                        .setStarred(true)
                        .build();
                Log.i(TAG, "Creando folder YourEnglisVocabulary22 en google drive");
                return mDriveResourceClient.createFolder(parentFolder, changeSet);
            }
        }).addOnSuccessListener(this, new OnSuccessListener<DriveFolder>() {
            @Override
            public void onSuccess(DriveFolder driveFolder) {
                Log.d(TAG, "Folder creado con exito: " + driveFolder.getDriveId().toString());
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Inhabilidado para crear folder en google drive", e);
            }
        });
    }

    private void createFileInAppFolder() {
        Log.d(TAG, "Start createFileInAppFolder");
        final Task<DriveFolder> appFolderTask = getDriveResourceClient().getAppFolder();
        final Task<DriveContents> createContentsTask = getDriveResourceClient().createContents();
        Tasks.whenAll(appFolderTask, createContentsTask)
                .continueWithTask(new Continuation<Void, Task<DriveFile>>() {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<Void> task) throws Exception {
                        Log.d(TAG, "Creating file into the AppFolder");

                        DriveFolder parent = appFolderTask.getResult();
                        DriveContents contents = createContentsTask.getResult();
                        OutputStream outputStream = contents.getOutputStream();
                        try (Writer writer = new OutputStreamWriter(outputStream)) {
                            writer.write("Hello World!");
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("New file")
                                .setMimeType("text/plain")
                                .setStarred(true)
                                .build();

                        return getDriveResourceClient().createFile(parent, changeSet, contents);
                    }
                })
                .addOnSuccessListener(this, new OnSuccessListener<DriveFile>() {
                    @Override
                    public void onSuccess(DriveFile driveFile) {
                        Log.d(TAG, "File created into the AppFolder");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error creating file into the AppFolder: " + e);
                    }
                });
    }
}
