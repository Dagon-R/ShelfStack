package com.example.shelfstack.ui.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shelfstack.R;
import com.example.shelfstack.data.Book;
import com.example.shelfstack.data.MediaElement;

import java.util.ArrayList;
import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyViewHolder> {
    private List<Book> mDataset;
    private List<Integer> selectedPos = new ArrayList<>();
    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView author;
        public ImageView thumbnail;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            author = v.findViewById(R.id.authors);
            thumbnail = v.findViewById(R.id.thumbnail);
            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    Integer pos = Integer.valueOf(getLayoutPosition());
                    if(selectedPos.contains(pos)){
                        selectedPos.remove(pos);
                    }
                    else{
                        selectedPos.add(pos);
                    }
                    notifyItemChanged(pos);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LibraryAdapter(List<Book> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LibraryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v;
        if(viewType == TYPE_INACTIVE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_element_hlist_view, parent, false);
        }
        else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_element_hlist_view_selected, parent, false);
        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MediaElement currentElement = mDataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(currentElement.getName());
        String authors_csv = "";
        for(String author : currentElement.getAuthors()){
            authors_csv += (author + ", ");
        }
        if(authors_csv.length() > 2){
            authors_csv = authors_csv.substring(0, authors_csv.length()-2);
        }
        holder.author.setText(authors_csv);
        holder.thumbnail.setImageBitmap(currentElement.getThumbnail());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position){
        Integer pos = Integer.valueOf(position);
        if(selectedPos.contains(pos)){
            return TYPE_ACTIVE;
        }
        else{
            return TYPE_INACTIVE;
        }
    }

    public List<Book> getSelectedBooks(){
        ArrayList<Book> selectedBooks = new ArrayList<>();
        for(int n : selectedPos){
            selectedBooks.add(mDataset.get(n));
        }
        return selectedBooks;
    }
}
