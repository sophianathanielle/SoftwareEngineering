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

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.SelectedViewHolder> {

    private ArrayList<Pet> pets = new ArrayList<>();
    private Context context;

    public PetAdapter(ArrayList<Pet> pets, Context context) {
        this.pets = pets;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_job_recycler_item, viewGroup, false);
        SelectedViewHolder selectedViewHolder = new SelectedViewHolder(view);
        return selectedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectedViewHolder selectedViewHolder,final int i) {
        Pet pet = this.pets.get(i);
        selectedViewHolder.breed.append(pet.getBreed());
        selectedViewHolder.age.append(String.valueOf(pet.getAge()));
        selectedViewHolder.name.append(pet.getName());
        selectedViewHolder.bio.append(pet.getBio());
        selectedViewHolder.species.append(pet.getSpecies());
        Picasso.with(context).load(pet.getPhoto_reference()).fit().centerCrop().into(selectedViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.pets.size();
    }

    public static class SelectedViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView age;
        TextView breed;
        TextView bio;
        TextView species;
        ImageView imageView;

        public SelectedViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.petName);
            age = itemView.findViewById(R.id.petAge);
            breed = itemView.findViewById(R.id.petBreed);
            imageView = itemView.findViewById(R.id.petImage);
            bio = itemView.findViewById(R.id.petBio);
            species = itemView.findViewById(R.id.petSpecies);
        }
    }
}
