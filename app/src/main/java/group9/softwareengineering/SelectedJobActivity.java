package group9.softwareengineering;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelectedJobActivity extends AppCompatActivity {

    private String usersID;
    private String documentId;
    private Posting posting;
    private ArrayList<String> petsID = new ArrayList<>();
    private ArrayList<Pet> pets = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView postedBy, startDate, endDate, description, location;
    private ImageView star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_job);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.usersID = getIntent().getStringExtra("usersID");
        this.petsID = getIntent().getStringArrayListExtra("usersPetIds");
        this.documentId = getIntent().getStringExtra("ids");
        fetchFromDatabasePosting();
        fetchFromDatabasePets();
        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.jobRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        postedBy = findViewById(R.id.jobPostedBy);
        startDate = findViewById(R.id.jobStartTime);
        endDate = findViewById(R.id.jobEndTime);
        description = findViewById(R.id.jobDescription);
        location = findViewById(R.id.jobLocation);
        star = findViewById(R.id.starImage);
        star.setTag(R.drawable.star_empty);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) star.getTag();
                if(tag == R.drawable.star_empty){
                    star.setImageResource(R.drawable.star_fill);
                    star.setTag(R.drawable.star_fill);
                } else {
                    star.setImageResource(R.drawable.star_empty);
                    star.setTag(R.drawable.star_empty);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    private void fetchFromDatabasePosting() {
        db.collection("postings").document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        posting = document.toObject(Posting.class);
                    }
                    postedBy.append(posting.getPoster());
                    startDate.append(posting.getStart_time().toString());
                    endDate.append(posting.getEnd_time().toString());
                    description.setText(posting.getDescription());
                    location.append(posting.getLocation().toString());
                }
            }
        });
    }

    private void fetchFromDatabasePets() {
        for (int i = 0; i < this.petsID.size(); i++) {
            db.collection("pets").document(this.petsID.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Pet tempPet = document.toObject(Pet.class);
                            pets.add(tempPet);
                        }
                        adapter = new SelectedJobAdapter(pets, getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                }
            });
        }
    }
}

