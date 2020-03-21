package com.example.shelfstack.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageGrabber extends AsyncTask<String, Integer, Bitmap> {
    private Book inputBook;

    public ImageGrabber(Book input){
        inputBook = input;
    }

    public ImageGrabber(ImageListener listener){
        this.imageListener = listener;
    }

    public interface ImageListener {
        public void onFinished(Bitmap image);
    }

    private ImageListener imageListener;

    public Bitmap doInBackground(String... src) {

        try {
            URL url = new URL(src[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public void onPostExecute(Bitmap result){
        imageListener.onFinished(result);
    }
}
