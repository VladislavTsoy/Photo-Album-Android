package com.example.home.android89;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Search Photos Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

public class Search extends AppCompatActivity {

    private String tagType, tagValue;
    private EditText typeText, valueText;
    public static Album searchAlbum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        setTitle("Search Photos");

        typeText = findViewById(R.id.type_search);
        valueText = findViewById(R.id.value_search);
    }

    public void performSearch(View v) {
        tagType = typeText.getText().toString().trim();
        tagValue = valueText.getText().toString().trim();

        if (!tagType.equalsIgnoreCase("Person") && !tagType.equalsIgnoreCase("Place")) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                    "Please enter \"Person\" or \"Place\" for tag type.");

            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "error");

            return;
        }

        if (tagType.isEmpty() || tagType == null || tagValue.isEmpty() || tagValue == null) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                    "Please fill in all the fields.");

            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "error");

            return;
        }

        // perform search
        searchAlbum = new Album(null);
        for (int i = 0; i < Albums.getAlbumList().size(); i++) {
            Album currentA = Albums.getAlbumList().get(i);
            for (int j = 0; j < currentA.getPhotos().size(); j++) {
                Photo currentP = currentA.getPhotos().get(j);
                for (int k = 0; k < currentP.getTags().size(); k++) {
                    Tag currentT = currentP.getTags().get(k);
                    if (currentT.getType().equalsIgnoreCase(tagType) && currentT.getValue().equalsIgnoreCase(tagValue)) {
                        searchAlbum.getPhotos().add(currentP);
                        continue;
                    }
                }
            }
        }

        if (searchAlbum.getPhotos().size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                    "No photos match the search.");

            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "error");

            return;
        } else {
            Intent intent = new Intent(this, SearchResults.class);
            startActivity(intent);
        }
    }

    /**
     * Executes Cancel Button
     */

    public void cancelSearch(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }


}
