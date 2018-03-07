package com.example.home.android89;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Photo Adapter Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

public class PhotoAdapter extends ArrayAdapter<Photo> {

    Context context;
    int id;
    ArrayList<Photo> photos;

    public PhotoAdapter(Context context, int textViewResourceId, ArrayList<Photo> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.id = textViewResourceId;
        this.photos = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ImageView imageView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (v == null) {
            v = inflater.inflate(R.layout.grid_view_items, null);
            imageView = (ImageView) v.findViewById(R.id.gridImg);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(240, 240));
        imageView.setImageBitmap(photos.get(position).getBitmap());
        //Uri uri = Uri.parse(photos.get(position).path);
        //imageView.setImageURI(uri);
        return v;
    }
}
