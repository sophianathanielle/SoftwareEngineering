package group9.softwareengineering;

import android.content.Context;
import android.os.storage.OnObbStateChangeListener;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ExampleViewHolder> implements Filterable {
    private ArrayList<Posting> postings;
    private ArrayList<Posting> postingsFilters;
    private onClickListener onClickListener;
    private String pay , posted;
    private Context context;

    public HomeAdapter(ArrayList<Posting> postings ,String pay ,String posted, Context context, onClickListener onClickListener){
        this.postings = postings;
        this.pay = pay;
        this.posted = posted;
        this.context = context;
        this.onClickListener = onClickListener;
        postingsFilters = new ArrayList<>(postings);
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
        Posting posting = postings.get(i);
        holder.payment.setText(pay+posting.getPayment() +" per Hour");
        holder.description.setText(posting.getDescription());
        holder.posted.setText(posted+posting.getPoster());
        Picasso.with(context).load(posting.getPhotoURL()).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return postings.size();
    }

    @Override
    public Filter getFilter() {
         Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Posting> filteredPostings = new ArrayList<>();
                if(charSequence == null || charSequence.length() == 0){
                    filteredPostings.addAll(postingsFilters);
                } else {
                    String filterString = charSequence.toString().toLowerCase().trim();
                    for(Posting posting : postingsFilters){
                        if(posting.getDescription().toLowerCase().contains(filterString)){
                            filteredPostings.add(posting);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredPostings;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                postings.clear();
                postings.addAll((ArrayList)filterResults.values);
                notifyDataSetChanged();
            }
        };
        return filter;
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

}
