package com.example.home.android89;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Photo Object Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

class Photo implements Serializable {
    static final long serialVersionUID = 1L;
    private transient Bitmap image;
    private ArrayList<Tag> tags;

    public Photo(Bitmap image) {
        this.image = image;
        this.tags = new ArrayList<Tag>();
    }

    public Bitmap getBitmap() {
        return this.image;
    }


    public ArrayList<Tag> getTags() { return this.tags; }
}

/**
 * Photo Albums Class
 * Displays Thumbnail of Photos in an album
 * Available options include: add photo,
 * delete and rename album
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 *
 */
public class PhotosActivity extends AppCompatActivity {

    private static final int dialog_alert = 10;
    public static final String PHOTO_INDEX = "photoIndex";
    public static final String PHOTO_NAME = "photoName";
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String CURRENT_ALBUM = "albumName";
    public static final String EDIT_OR_DELETE = "editOrDelete";

    public static final int RENAME_ALBUM_CODE = 1;
    public static final int ADD_PHOTO_CODE = 2;
    public static final int SHOW_PHOTO_CODE = 3;

    private int albumIndex, editOrDelete;   // delete = 0, edit = 1
    ArrayList<Album> albumList;
    ArrayList<Photo> photos;
    private String albumName;
    Album currentAlbum;

    GridView gridView;
    ListView album_list;

    Button editBtn;
    Button deleteBtn;
    Button backAlbum;
    Button addPhotoBtn;
    EditText albumInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);

        Bundle bundle = getIntent().getExtras();
        albumName = bundle.getString(Albums.SELECTED_ALBUM);

        setTitle(albumName);

        albumList = Albums.getAlbumList();


        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getName().equals(albumName)) {
                currentAlbum = albumList.get(i);
                photos = albumList.get(i).getPhotos();
                break;
            }
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new PhotoAdapter(this, R.layout.grid_view_items, photos));

        //gridView.setAdapter(new PhotoAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPhoto(position);
            }
        });

    }

    public void addPhotoBtn(View c) {
        //Bundle bundle = new Bundle();
        //bundle.putString(CURRENT_ALBUM, albumName);

        Intent g = new Intent (this, AddPhotoActivity.class);
        //g.putExtras(bundle);
        startActivityForResult(g, ADD_PHOTO_CODE);
    }

    /**
     * Back to Album List
     */

    public void backAlbum(View c) {
        setResult(RESULT_OK);
        finish();
    }

    public void deleteAlbum(View c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this album?");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("CANCEL", null);

        AlertDialog alert = builder.create();
        alert.show();

        Button yes = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        yes.setOnClickListener(v -> {
            for (int i = 0; i < albumList.size(); i++) {
                if (albumList.get(i).getName().equals(currentAlbum.getName())) {
                    albumList.remove(i);
                    break;
                }
            }

            setResult(RESULT_OK);
            finish();

        });

        return;
    }

    public void showPhoto(int position) {
        Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
        i.putExtra("id", position);
        i.putExtra(CURRENT_ALBUM, albumName);
        startActivityForResult(i, SHOW_PHOTO_CODE);
    }

    public void renameAlbum(View c) {
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_ALBUM, albumName);
        bundle.putInt(ALBUM_INDEX, currentAlbum.index);

        Intent intent = new Intent(this, AddAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, RENAME_ALBUM_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (intent == null ) {
            gridView.setAdapter(new PhotoAdapter(this, R.layout.grid_view_items, photos));
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        if (requestCode == RENAME_ALBUM_CODE) {
            String newName = bundle.getString(AddAlbum.ALBUM_NAME);
            for (int i = 0; i < albumList.size(); i++) {
                if (albumList.get(i).getName().equals(currentAlbum.getName())) {
                    albumList.get(i).setName(newName);
                }
            }

            setTitle(newName);
        }

        if (requestCode == ADD_PHOTO_CODE) {
            String uriString = bundle.getString(AddPhotoActivity.PHOTO_URI);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uriString));
                currentAlbum.photos.add(new Photo(bitmap));
            } catch (Exception ex) {
                //
            }
        }

        gridView.setAdapter(new PhotoAdapter(this, R.layout.grid_view_items, photos));
    }

    /**
     * Saves Albums to "albums.dat"
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ObjectOutputStream oos;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.openFileOutput("albums.dat", Context.MODE_PRIVATE));
            objectOutputStream.writeObject(albumList);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onSaveInstanceState(outState);
    }
}
