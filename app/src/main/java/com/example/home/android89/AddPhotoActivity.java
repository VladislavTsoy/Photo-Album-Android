package com.example.home.android89;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
/**
 * Select and Save Image from Camera Roll
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

public class AddPhotoActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final String PHOTO_URI = "uriString";
    private int REQUEST_CODE = 1;
    private ImageView imgPicture;
    private Uri uri;
    private String uriString;
    private String currentAlbum;

    Button cancelBtn, selectBtn;

    //  Button saveBtn = (Button)findViewById(R.id.saveBtn);
    //   Button addTagBtn = (Button)findViewById(R.id.addTagBtn);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        // Bundle bundle = getIntent().getExtras();
        // currentAlbum = bundle.getString(PhotosActivity.CURRENT_ALBUM);

        selectBtn = (Button) findViewById(R.id.selectBtn);
        imgPicture = (ImageView) findViewById(R.id.imgPicture);


        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgPicture.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public void savePhoto(View v) {
        if (uri == null) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                    "Please select a photo from the gallery.");

            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "error");

            return;
        }

        uriString = uri.toString();
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_URI, uriString);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Executes Cancel Button
     */

    public void cancelAddPhoto(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
}