package com.example.android.yourenglishvocabulary.drive;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by joseluis on 16/12/2017.
 */

public class GoogleDriveAPIUtil {

    private static final String TAG = "GoogleDriveAPIUtil";

    private static DriveResourceClient mDriveResourceClient;
    private static DriveFolder yourEnglishVocabularyFolder;

    /**
     * Build a Google SignIn client.
     */
    public static GoogleSignInClient buildGoogleSignInClient(Activity activity) {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .requestScopes(Drive.SCOPE_APPFOLDER)
                        .build();
        return GoogleSignIn.getClient(activity, signInOptions);
    }

    /**
     * Continues the sign-in process, initializing the Drive clients with the current
     * user's account.
     */
    public static void initializeDriveClient(GoogleSignInAccount signInAccount, Activity activity) {
        Log.i(TAG, "Starting google drive client");
        mDriveResourceClient = Drive.getDriveResourceClient(activity.getApplicationContext(), signInAccount);
        createFolderInGoogleDrive(activity);
    }

    private static DriveResourceClient getDriveResourceClient() {
        return mDriveResourceClient;
    }

    private static DriveFolder getYourEnglishVocabularyFolder() {
        return yourEnglishVocabularyFolder;
    }

    private static void createFolderInGoogleDrive(Activity activity) {
        Log.i(TAG, "Creating the folder in google drive");
        mDriveResourceClient.getAppFolder().continueWithTask((task) -> {
            DriveFolder parentFolder = task.getResult();
            Log.i(TAG, "Get appfolder: " + parentFolder.getDriveId().toString());
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle("YourEnglisVocabulary")
                    .setMimeType(DriveFolder.MIME_TYPE)
                    .setStarred(true)
                    .build();
            Log.i(TAG, "Creating YourEnglisVocabulary folder in google drive");
            return mDriveResourceClient.createFolder(parentFolder, changeSet);
        }).addOnSuccessListener(activity, (driveFolder) -> {
            yourEnglishVocabularyFolder = driveFolder;
            Log.d(TAG, "Folder created with success: " + driveFolder.getDriveId().toString());
        }).addOnFailureListener(activity, (e) -> {
            Log.e(TAG, "Unable was creating folder in google drive", e);
        });
    }

    public static void createImageFileInAppFolder(Activity activity, Bitmap bitmap, String fileName) {
        Log.d(TAG, "Start createFileInAppFolder");
        final Task<DriveContents> createContentsTask = getDriveResourceClient().createContents();
        Tasks.whenAll(createContentsTask)
                .continueWithTask((task) -> {
                    Log.d(TAG, "Creating file of image " + fileName + ".jpg into the AppFolder");

                    DriveContents contents = createContentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(fileName)
                            .setMimeType("image/jpeg")
                            .setStarred(true)
                            .build();

                    return getDriveResourceClient().createFile(getYourEnglishVocabularyFolder(), changeSet, contents);
                })
                .addOnSuccessListener(activity, (driveFile) -> {
                    Log.d(TAG, "File " + fileName + ".jpg created into the YourEnglishVocabulary Folder");
                })
                .addOnFailureListener(activity, (e) -> {
                    Log.e(TAG, "Error creating file " + fileName + ".jpg into the YourEnglishVocabulary Folder: " + e);
                });
    }
}
