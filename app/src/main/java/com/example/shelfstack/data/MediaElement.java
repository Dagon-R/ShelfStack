package com.example.shelfstack.data;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.List;

public interface MediaElement {
    public String getName();
    public String getISBN();
    public Bitmap getThumbnail();
    public List<String> getAuthors();
}
