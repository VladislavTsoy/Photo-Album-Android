package com.example.home.android89;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

/**
 * FullImageActivity Class Displays Full Size Images
 * Displays Tags
 * Available options include: delete photo,
 * add tags, move image, and manual slide show
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */
public class FullImageActivity extends AppCompatActivity {
    public static final String CURRENT_ALBUM = "name";
    public static final String CURRENT_PHOTO_INDEX = "position";
    public static final int ADD_TAG_CODE = 3;

    private ImageView imageView;
    private ListView tagView;
    private int position;
    private Button nextBtn, prevBtn, backBtn;
    private ArrayList<Photo> photos;
    ArrayList<Tag> tags;
    private String name;
    private Animation in, out, in2, out2;
    private ImageSwitcher imageSwitcher;
    private PhotoAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();

        name = i.getExtras().getString(PhotosActivity.CURRENT_ALBUM);
        for (int j = 0; j < Albums.getAlbumList().size(); j++) {
            if (Albums.getAlbumList().get(j).getName().equalsIgnoreCase(name)) {
                photos = Albums.getAlbumList().get(j).getPhotos();
            }
        }

        setTitle(name);

        position = i.getExtras().getInt("id");
        adapter = new PhotoAdapter(this, R.layout.grid_view_items, photos);

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageDrawable(new BitmapDrawable(getResources(), photos.get(position).getBitmap()));


        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(
                        new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));


                return imageView;
            }
        });


        in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
        out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
        in2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in2);
        out2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out2);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });


        /**
         * ListView of associated tags
         */
        tagView = findViewById(R.id.tagList);
        tags = photos.get(position).getTags();
        tagView.setAdapter(new ArrayAdapter<Tag>(this, R.layout.album_text, tags));

        // click on tag to prompt deletion
        tagView.setOnItemClickListener((p, V, pos, id) -> {
            deleteTag(pos);
        });

    }

    /**
     * Displays next Photo in the slide show
     */

    public void nextPhoto(View v) {
        imageSwitcher.setInAnimation(in2);
        imageSwitcher.setOutAnimation(out2);
        if (position < adapter.photos.size() - 1) {
            position++;
            imageSwitcher.setImageDrawable(new BitmapDrawable(getResources(), photos.get(position).getBitmap()));
        }
        tagView.setAdapter(new ArrayAdapter<Tag>(this, R.layout.album_text, photos.get(position).getTags()));
    }

    /**
     * Displays previous Photo in the slide show
     */

    public void prevPhoto(View v) {
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
        if (position > 0) {
            position--;
            imageSwitcher.setImageDrawable(new BitmapDrawable(getResources(), photos.get(position).getBitmap()));
        }
        tagView.setAdapter(new ArrayAdapter<Tag>(this, R.layout.album_text, photos.get(position).getTags()));
    }

    /**
     *  Add Tags to Photos
     */

    public void addTag(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_ALBUM, name);
        bundle.putInt(CURRENT_PHOTO_INDEX, position);

        Intent intent = new Intent(this, Tags.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_TAG_CODE);
    }

    /**
     * Deletes Tags to Photos
     */

    public void deleteTag(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this tag?");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("CANCEL", null);

        AlertDialog alert = builder.create();
        alert.show();

        Button yes = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        yes.setOnClickListener(view -> {
            Photo photo = photos.get(position);
            photo.getTags().remove(pos);
            alert.dismiss();
            onActivityResult(0, RESULT_OK, null);
        });
    }

    /**
     * Moves Photo to a different Album
     */

    public void moveTo(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_ALBUM, name);
        bundle.putInt(CURRENT_PHOTO_INDEX, position);

        Intent intent = new Intent(this, Albums.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Delete Photo from current album
     */

    public void deletePhoto(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this photo?");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("CANCEL", null);

        AlertDialog alert = builder.create();
        alert.show();

        Button yes = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        yes.setOnClickListener(view -> {
            photos.remove(position);
            setResult(RESULT_OK);
            finish();
        });

        return;
    }

    /**
     * Displays Tags
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        }
        //if (requestCode == ADD_TAG_CODE) {
            tagView.setAdapter(new ArrayAdapter<Tag>(this, R.layout.album_text, photos.get(position).getTags()));
        //}
    }
}