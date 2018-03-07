package com.example.home.android89;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Album Object Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */
class Album implements Serializable {
    static final long serialVersionUID = 1L;
    String name;
    int size;
    int index;
    int totalAlbums;
    ArrayList<Photo> photos;

    Album(String name) {
        this.name = name;
        this.index = totalAlbums++;
        this.photos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

    public int getSize() {
        return photos.size();
    }

    public String toString() {
        return this.name + "\n" + getSize() + " Photos";
    }
}

/**
 * This class is for displaying the albums list.
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */
public class Albums extends AppCompatActivity {

    private ListView listView;
    public static ArrayList<Album> albums;
    private String fileName = "albums.dat";
    private static User user;

    public static final String SELECTED_ALBUM = "albumName";
    public static final int ADD_ALBUM_CODE = 1;
    public static final int SHOW_ALBUM_CODE = 2;

    private String albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_list);
        setTitle("My Photo Albums");

        Bundle toMove = getIntent().getExtras();
        if(toMove == null) {
            albums = new ArrayList<>();
            try {
                ObjectInputStream ois = new ObjectInputStream(this.openFileInput(fileName));
                user = (User) ois.readObject();
                albums = user.albums;
                ois.close();
            } catch (Exception e) {
                user = new User(albums);
                Album stock = new Album("Stock");
                albums.add(stock);
                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
                    stock.photos.add(new Photo(bitmap));
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic2);
                    stock.photos.add(new Photo(bitmap));
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic3);
                    stock.photos.add(new Photo(bitmap));
                } catch (Exception ex) {
                    //
                }
            }
        }

        listView = findViewById(R.id.albums_list);
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album_text, albums));



        listView.setOnItemClickListener((p, V, pos, id) -> {
            if (toMove != null) {
                String old = toMove.getString(FullImageActivity.CURRENT_ALBUM);
                int index = toMove.getInt(FullImageActivity.CURRENT_PHOTO_INDEX);
                for (int i = 0; i < albums.size(); i++) {
                    if(albums.get(i).getName().equalsIgnoreCase(old)) {
                        Album oldAlbum = albums.get(i);
                        Photo oldPhoto = oldAlbum.getPhotos().get(index);
                        oldAlbum.getPhotos().remove(index);
                        albums.get(pos).getPhotos().add(oldPhoto);
                    }
                }
            }
            showAlbum(pos);
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ObjectOutputStream oos;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.openFileOutput(fileName, Context.MODE_PRIVATE));
            objectOutputStream.writeObject(user);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * executes create album
     */

    public void createAlbum(View view) {
        Intent intent = new Intent(this, AddAlbum.class);
        startActivityForResult(intent, ADD_ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ADD_ALBUM_CODE) {
            Bundle bundle = intent.getExtras();
            String name = bundle.getString(AddAlbum.ALBUM_NAME);
            albums.add(new Album(name));
        }

        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album_text, albums));
    }

    /**
     * executes Search Activity
     */

    public void searchPhotos(View view) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    /**
     * Displays albums list
     */

    public void showAlbum(int pos) {
        Bundle bundle = new Bundle();
        Album album = albums.get(pos);
        bundle.putString(SELECTED_ALBUM, album.getName());

        Intent intent = new Intent(this, PhotosActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SHOW_ALBUM_CODE);
    }

    /**
     * Constructor
     *
     * @return ArrayList<Album>
     */

    public static ArrayList<Album> getAlbumList() {
        return albums;
    }

}