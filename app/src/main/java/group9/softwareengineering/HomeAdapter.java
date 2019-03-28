package group9.softwareengineering;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ExampleViewHolder> {
    private ArrayList<Posting> postings;
    private String pay , posted;
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recyclerview_item , viewGroup , false);
        ExampleViewHolder example = new ExampleViewHolder(view);
        return example;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        Posting posting = postings.get(i);
        exampleViewHolder.payment.setText(pay+posting.getPayment());
        exampleViewHolder.description.setText(posting.getDescription());
        exampleViewHolder.posted.setText(posted+posting.getPoster());

    }

    @Override
    public int getItemCount() {
        return postings.size();
    }

    public HomeAdapter(ArrayList<Posting> postings ,String pay ,String posted){
        this.postings = postings;
        this.pay = pay;
        this.posted = posted;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView payment;
        public TextView posted;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            payment = itemView.findViewById(R.id.payment);
            posted = itemView.findViewById(R.id.posted);


        }
    }

}
