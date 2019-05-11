package group9.softwareengineering;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Multimap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SittersAdapter extends RecyclerView.Adapter<SittersAdapter.ExampleViewHolder> {
    private LinkedHashMap<Posting , String> postings;
    private onClickListener onClickListener;
    private String pay , posted;
    private Context context;
    private Multimap<Posting , Profile> postingsAndProfiles;
    private ArrayList<Posting> posts = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();



    public SittersAdapter(LinkedHashMap<Posting , String> postings , Multimap<Posting , Profile> postingsAndProfiles , String pay , String posted, Context context, onClickListener onClickListener){
        this.postings = postings;
        this.pay = pay;
        this.posted = posted;
        this.context = context;
        this.onClickListener = onClickListener;
        this.postingsAndProfiles = postingsAndProfiles;
        for(Map.Entry<Posting , Profile> allKeys : postingsAndProfiles.entries()){
            posts.add(allKeys.getKey());
            for(Posting posting : postings.keySet()){
                if(posting.equals(allKeys.getKey())){
                    ids.add(postings.get(posting));
                }
            }
        }
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recyclerview_item , viewGroup , false);
        ExampleViewHolder example = new ExampleViewHolder(view, onClickListener);
        return example;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int i) {
        ArrayList<Profile> profiles = new ArrayList<>(postingsAndProfiles.values());
        Posting posting = posts.get(i);
        Profile profile = profiles.get(i);
        holder.payment.setText("£"+String.valueOf(posting.getPayment())+ "per hour");
        holder.description.setText(posting.getDescription());
        holder.posted.setText(profile.getName());
        Picasso.with(context).load(posting.getPhotoURL()).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return postingsAndProfiles.size();
    }



    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         TextView description;
         TextView payment;
         TextView posted;
         ImageView imageView;
         onClickListener onClickListener;

        public ExampleViewHolder(@NonNull View itemView, onClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;
            description = itemView.findViewById(R.id.description);
            payment = itemView.findViewById(R.id.payment);
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


    public String getDocumentAt(int position){
        return ids.get(position);
    }


}
