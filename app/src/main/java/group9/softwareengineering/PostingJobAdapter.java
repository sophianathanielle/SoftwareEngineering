package group9.softwareengineering;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostingJobAdapter extends RecyclerView.Adapter<PostingJobAdapter.ViewHolder> {

    private List<Pet> pets;
    private Context context;
    private View.OnClickListener mOnClickListener;

    public PostingJobAdapter(List<Pet> pets) {
        this.pets = pets;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_pet_item, viewGroup, false);
        v.setOnClickListener(mOnClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Pet pet = pets.get(i);
        Picasso.with(context).load(pet.getPhoto_reference()).fit().centerCrop().into(viewHolder.imageView);
        viewHolder.textView.setText(pet.getName());
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recyclerViewPetName);
            imageView = (ImageView) itemView.findViewById(R.id.recyclerViewPetImage);
        }
    }
}
