package com.example.home.android89;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.app.DialogFragment;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Add or Edit Albums
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

public class AddAlbum extends AppCompatActivity {

    public static final String ALBUM_INDEX = "albumIndex";
    public static final String ALBUM_NAME = "albumName";
    public static String ALBUM_SIZE = "albumSize";
    // start and end dates?

    private int albumIndex, albumSize;
    private EditText albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);

        // get field
        albumName = (EditText) findViewById(R.id.album_name);

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            albumName.setText(bundle.getString(ALBUM_NAME));
        }
    }

    /**
     * Displays next Photo in the slide show
     */

    public void save(View view) {
        String name = albumName.getText().toString().trim();
        if (name == null || name.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                    "Album name cannot be empty.");

            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missingFields");

            return;   // does not quit activity, just returns from method
        }

        ArrayList<Album> list = Albums.getAlbumList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(name)) {
                Bundle bundle = new Bundle();
                bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                        "An album with that name already exists");

                DialogFragment newFragment = new AlbumDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "error");

                return;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString(ALBUM_NAME, name);
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putInt(ALBUM_SIZE, albumSize);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);

        finish();
    }
    /**
     * Executes Cancel Button
     */
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

}