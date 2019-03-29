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


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ExampleViewHolder> {
    private ArrayList<Posting> postings;
    private String pay , posted;
    private Context context;


    public HomeAdapter(ArrayList<Posting> postings ,String pay ,String posted, Context context){
        this.postings = postings;
        this.pay = pay;
        this.posted = posted;
        this.context = context;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recyclerview_item , viewGroup , false);
        ExampleViewHolder example = new ExampleViewHolder(view);
        return example;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int i) {
        Posting posting = postings.get(i);
        holder.payment.setText(pay+posting.getPayment());
        holder.description.setText(posting.getDescription());
        holder.posted.setText(posted+posting.getPoster());
        //Picasso.with(context).load(posting.getPhotoURL()).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return postings.size();
    }



    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView payment;
        public TextView posted;
        public ImageView imageView;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            payment = itemView.findViewById(R.id.payment);
            posted = itemView.findViewById(R.id.posted);
            imageView = itemView.findViewById(R.id.image);

        }
    }

}
