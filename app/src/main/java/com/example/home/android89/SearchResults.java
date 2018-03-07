package com.example.home.android89;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Photo Object Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

public class SearchResults extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        setTitle("Search Results");

        gridView = (GridView) findViewById(R.id.searchGrid);
        gridView.setAdapter(new PhotoAdapter(this, R.layout.grid_view_items, Search.searchAlbum.getPhotos()));

    }
}
