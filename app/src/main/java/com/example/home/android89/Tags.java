package com.example.home.android89;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Tag Object Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */
class Tag {
    static final long serialVersionUID = 1L;
    String type;
    String value;

    public Tag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return value + "\n" + type;
    }

}

/**
 * Add Tag(s) Activity
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */
public class Tags extends AppCompatActivity {

    private EditText tagPlaceText, tagPersonText;
    private String album;
    private int photoIndex;
    private Album currentAlbum;
    private Photo currentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tag);
        setTitle("Add Tag(s)");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            album = bundle.getString(FullImageActivity.CURRENT_ALBUM);
            photoIndex = bundle.getInt(FullImageActivity.CURRENT_PHOTO_INDEX);
        }

        for (int i = 0; i < Albums.getAlbumList().size(); i++) {
            if (Albums.getAlbumList().get(i).getName().equalsIgnoreCase(album)) {
                currentAlbum = Albums.getAlbumList().get(i);
                currentPhoto = currentAlbum.getPhotos().get(photoIndex);
                break;
            }
        }

        tagPlaceText = findViewById(R.id.tagPlaceText);
        tagPersonText = findViewById(R.id.tagPersonText);
    }

    /**
     * Saves Tags
     */

    public void saveTag(View v) {
        String place = tagPlaceText.getText().toString().trim();
        String person = tagPersonText.getText().toString().trim();

        if ((place.isEmpty() || place == null) && (person.isEmpty() || person == null)) {
            cancelTag(v);
        }

        if (!place.isEmpty()) {
            Tag tag = new Tag("Place", place);
            for (int i = 0; i < currentPhoto.getTags().size(); i++){
                if (currentPhoto.getTags().get(i).getType().equals("Place")) {
                    if (currentPhoto.getTags().get(i).getValue().equals(place)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                                "Tag already exists for this photo.");

                        DialogFragment newFragment = new AlbumDialogFragment();
                        newFragment.setArguments(bundle);
                        newFragment.show(getFragmentManager(), "error");

                        return;
                    }
                }
            }
            currentPhoto.getTags().add(tag);
        }

        if (!person.isEmpty()) {
            Tag tag = new Tag("Person", person);
            for (int i = 0; i < currentPhoto.getTags().size(); i++){
                if (currentPhoto.getTags().get(i).getType().equals("Person")) {
                    if (currentPhoto.getTags().get(i).getValue().equals(person)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                                "Tag already exists for this photo.");

                        DialogFragment newFragment = new AlbumDialogFragment();
                        newFragment.setArguments(bundle);
                        newFragment.show(getFragmentManager(), "error");

                        return;
                    }
                }
            }
            currentPhoto.getTags().add(tag);
        }

        setResult(RESULT_OK);
        finish();
    }

    /**
     * Executes Cancel Button
     */

    public void cancelTag(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

}
