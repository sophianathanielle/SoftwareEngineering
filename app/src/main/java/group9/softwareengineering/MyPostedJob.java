package group9.softwareengineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyPostedJob extends AppCompatActivity {


    private TextView jobDescription, jobLocation, jobStartTime, jobEndTime, jobPrice;
    private Button buttonSittersInterested;
    private String usersID;
    private String documentId;
    private Posting posting;
    private ArrayList<String> petsID = new ArrayList<>();
    private ArrayList<Pet> pets = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_job);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        jobDescription = findViewById(R.id.jobDescription);
        jobLocation = findViewById(R.id.jobLocation);
        jobStartTime = findViewById(R.id.jobStartTime);
        jobEndTime = findViewById(R.id.jobEndTime);
        jobPrice = findViewById(R.id.jobPrice);

        recyclerView = findViewById(R.id.myJobPetsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        this.usersID = getIntent().getStringExtra("usersID");
        this.petsID = getIntent().getStringArrayListExtra("usersPetIds");
        this.documentId = getIntent().getStringExtra("ids");
        fetchFromDatabasePosting();
        fetchFromDatabasePets();

        buttonSittersInterested = findViewById(R.id.SeeSittersInterested);
        buttonSittersInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostedJob.this, SittersInterestedFragment.class);
                startActivity(intent);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab_edit_job);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPostedJob.this,EditMyPostedJob.class);
                intent.putExtra(documentId,"ids");
                intent.putExtra("posting", posting);
                startActivity(intent);
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
                    jobStartTime.append(posting.getStart_time().toString());
                    jobEndTime.append(posting.getEnd_time().toString());
                    jobDescription.setText(posting.getDescription());
                    jobLocation.append(posting.getLocation().toString());
                    //jobPrice.append(posting.getPrice().toString());
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
                        adapter = new PetAdapter(pets, getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                }
            });
        }
    }


}
