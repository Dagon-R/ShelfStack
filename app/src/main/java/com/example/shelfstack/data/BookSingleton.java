package com.example.shelfstack.data;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookSingleton {
    private List<Book> bookList;

    private static final BookSingleton ourInstance = new BookSingleton();

    public static BookSingleton getInstance() {
        return ourInstance;
    }

    private BookSingleton() {
        bookList = new ArrayList<>();
    }

    public boolean LoadBook(){

        return true;
    }

    public List<Book> getBookList(){
        return bookList;
    }
}
