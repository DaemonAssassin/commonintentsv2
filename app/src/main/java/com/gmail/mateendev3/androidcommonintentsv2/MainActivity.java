package com.gmail.mateendev3.androidcommonintentsv2;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.actions.NoteIntents;

public class MainActivity extends AppCompatActivity {
    Button btnShowMap;
    Button btnPlayMedia;
    Button btnCreateNote;
    Button btnCreateCall;
    Button btnOpenSettings;
    Button btnTakeVideo;
    Button btnTakePhoto;

    ImageView ivResultImage;

    ActivityResultLauncher<Uri> mTakeVideoLauncher;
    ActivityResultLauncher<Uri> mTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowMap = findViewById(R.id.btn_show_map);
        btnPlayMedia = findViewById(R.id.btn_play_media);
        btnCreateNote = findViewById(R.id.btn_create_note);
        btnCreateCall = findViewById(R.id.btn_call);
        btnOpenSettings = findViewById(R.id.btn_open_settings);
        btnTakeVideo = findViewById(R.id.btn_take_video);
        btnTakePhoto = findViewById(R.id.btn_take_photo);

        ivResultImage = findViewById(R.id.iv_result_image);

        ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result)
                            createCall();
                        else
                            Toast.makeText(MainActivity.this, "You didn't give permission", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        //Initializing launchers
        initLaunchers();

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap(Uri.parse("geo:0,0?q=Bahawalpur"));
            }
        });
        btnPlayMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(Uri.parse("https://www.youtube.com/watch?v=iWcC3VZzRJ4"));
            }
        });
        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote("Android", "Learn Kotlin, Android Jetpack");
            }
        });
        btnCreateCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*boolean hasNotCallPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED;
                if (hasNotCallPermission)
                    permissionLauncher.launch(Manifest.permission.CALL_PHONE);
                else
                    createCall();*/
                createCall();

            }
        });
        btnOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        btnTakeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakeVideoLauncher.launch(Uri.parse("storage/downloads"));
            }
        });
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhoto.launch(Uri.parse("storage/downloads"));
            }
        });

    }

    private void initLaunchers() {
        takeVideoLauncher();
        takePhotoLauncher();
    }

    private void takePhotoLauncher() {
        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            ivResultImage.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        );
    }

    private void takeVideoLauncher() {
        mTakeVideoLauncher = registerForActivityResult(
                new ActivityResultContracts.TakeVideo(),
                new ActivityResultCallback<Bitmap>() {
                    @Override
                    public void onActivityResult(Bitmap result) {
                        ivResultImage.setImageBitmap(result);
                    }
                }
        );
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No activity found", Toast.LENGTH_SHORT).show();
        }
    }

    private void createCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:03467560225"));
        startActivity(intent);
    }

    private void createNote(String title, String text) {
        Intent intent = new Intent(NoteIntents.ACTION_CREATE_NOTE)
                .putExtra(NoteIntents.EXTRA_NAME, title)
                .putExtra(NoteIntents.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No activity found", Toast.LENGTH_SHORT).show();
        }
    }

    private void playVideo(Uri videoURL) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(videoURL);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showMap(Uri geoLocation) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}