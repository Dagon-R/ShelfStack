package com.example.shelfstack.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Book implements MediaElement {
    private String ISBN;
    private String name;
    private Bitmap thumbnail;
    private String googleBooksID;
    private List<String> authors = new ArrayList<>();

    public Book(){};

    public Book(String name, String ISBN, String ID, Bitmap thumbnail){
        this.name = name;
        this.ISBN = ISBN;
        this.googleBooksID = ID;
        this.thumbnail = thumbnail;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getISBN(){
        return ISBN;
    }

    @Override
    public Bitmap getThumbnail(){
        return thumbnail;
    }

    public String getGoogleBooksID() {
        return googleBooksID;
    }

    public List<String> getAuthors(){
        return authors;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setGoogleBooksID(String googleBooksID) {
        this.googleBooksID = googleBooksID;
    }
}
