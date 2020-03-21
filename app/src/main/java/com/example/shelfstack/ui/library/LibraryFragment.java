package com.example.shelfstack.ui.library;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shelfstack.R;
import com.example.shelfstack.data.Book;
import com.example.shelfstack.data.BookSingleton;
import com.example.shelfstack.data.ImageGrabber;
import com.example.shelfstack.data.MediaElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LibraryFragment extends Fragment {

    private View popupView;
    private List<Book> libraryList;
    private List<Book> selectionList;
    private RecyclerView libraryRecyclerView;
    private LibraryAdapter libraryAdapter;

    private RecyclerView selectionRecyclerView;
    private LibraryAdapter selectionAdapter;
    private Button searchButton;
    private EditText searchQuery;
    private Button addButton;
    private Button cancelButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_library, container, false);
        libraryRecyclerView = root.findViewById(R.id.libraryRecyclerView);
        List<Book> bookInput = BookSingleton.getInstance().getBookList();
        libraryAdapter = new LibraryAdapter(bookInput);
        libraryRecyclerView.setAdapter(libraryAdapter);
        libraryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchButton = root.findViewById(R.id.addMedia);
        searchQuery = root.findViewById(R.id.search_name);
        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search(searchQuery.getText().toString());
                    }
                }
        );
        popupView = inflater.inflate(R.layout.selection_popup, null);
        selectionRecyclerView = popupView.findViewById(R.id.selection_recycler);
        return root;
    }

    private class imageCallback implements ImageGrabber.ImageListener{
        private Book reference;
        private LibraryAdapter adapter;
        imageCallback(Book book, LibraryAdapter adapter){
            reference = book;
            this.adapter = adapter;
        }

        public void onFinished(Bitmap image){
            reference.setThumbnail(image);
            adapter.notifyDataSetChanged();
        }
    }

    public void search(String search) {
        final List<Book> newBookList = new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://www.googleapis.com/books/v1/volumes?q=" + search + "&key=" + getActivity().getString(R.string.book_api_key);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray possibleBooks = jsonObject.getJSONArray("items");
                            Integer length = possibleBooks.length();
                            if(length > 10){
                                length = 10;
                            }
                            selectionList = new LinkedList<Book>();
                            ImageGrabber imageGrabber;
                            ImageGrabber.ImageListener listener;
                            selectionAdapter = new LibraryAdapter(newBookList);
                            for(int i = 0; i < length; i++){
                                Book newBook = new Book();
                                JSONObject currentElement = possibleBooks.getJSONObject(i);
                                JSONObject currentBook = currentElement.getJSONObject("volumeInfo");
                                JSONObject currentImage = currentBook.getJSONObject("imageLinks");
                                String currentName = currentBook.getString("title");
                                JSONArray currentAuthors = currentBook.getJSONArray("authors");
                                String currentID = currentElement.getString("id");
                                String currentBitmapURL = currentImage.getString("thumbnail");
                                listener = new imageCallback(newBook, selectionAdapter);
                                imageGrabber = new ImageGrabber(listener);
                                imageGrabber.execute(currentBitmapURL);
                                newBook.setGoogleBooksID(currentID);
                                newBook.setName(currentName);
                                for(int j = 0; j < currentAuthors.length(); j++){
                                    newBook.getAuthors().add(currentAuthors.getString(j));
                                }
                                newBookList.add(newBook);
                            }
                            selectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            selectionRecyclerView.setAdapter(selectionAdapter);
                        }
                        catch(Exception e){
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        popupWindow.update(0, 0, width, height);
        cancelButton = popupView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        addButton = popupView.findViewById(R.id.confirm);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> newBooks = selectionAdapter.getSelectedBooks();
                BookSingleton.getInstance().getBookList().addAll(newBooks);
                libraryAdapter = new LibraryAdapter(BookSingleton.getInstance().getBookList());
                libraryRecyclerView.swapAdapter(libraryAdapter, true);
                popupWindow.dismiss();
            }
        });
        return;
    }
}