package group9.softwareengineering;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ExampleViewHolder> {
    private ArrayList<String> photos;
    private Context context;


    public PhotosAdapter(ArrayList<String> postings, Context context) {
        this.photos = postings;
        this.context = context;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_layout, viewGroup, false);
        ExampleViewHolder example = new ExampleViewHolder(view);
        return example;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int i) {
        String photo = photos.get(i);
        Picasso.with(context).load(photo).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo);
        }


    }
}

