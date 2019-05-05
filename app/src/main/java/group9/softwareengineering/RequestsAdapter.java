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


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ExampleViewHolder> {
    private LinkedHashMap<String,Posting> postings;
    private onClickListener onClickListener;
    private Context context;


    public RequestsAdapter(LinkedHashMap<String,Posting> postings , Context context, onClickListener onClickListener){
        this.postings = postings;
        this.context = context;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.requests_recyclerview_item , viewGroup , false);
        ExampleViewHolder example = new ExampleViewHolder(view, onClickListener);
        return example;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int i) {
        ArrayList<Posting> posts = new ArrayList<>(postings.values());
        Posting posting = posts.get(i);
        holder.description.setText(posting.getDescription());
        Picasso.with(context).load(posting.getPhotoURL()).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return postings.size();
    }



    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView description;
        TextView posted;
        ImageView imageView;
        onClickListener onClickListener;

        public ExampleViewHolder(@NonNull View itemView, onClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;
            description = itemView.findViewById(R.id.description);
            posted = itemView.findViewById(R.id.posted);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface onClickListener{
        void onItemClick(int position);
    }

    public  Posting getPostingAt(int position){
        return new ArrayList<Posting>(postings.values()).get(position);

    }

    public String getDocumentAt(int position){
        return new ArrayList<String>(postings.keySet()).get(position);
    }


}
