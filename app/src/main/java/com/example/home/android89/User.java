package com.example.home.android89;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Users Albums Object Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    ArrayList<Album> albums;
    public static String fileName = "albums.dat";

    public User(ArrayList<Album> albums) {
        this.albums = albums;
    }

}