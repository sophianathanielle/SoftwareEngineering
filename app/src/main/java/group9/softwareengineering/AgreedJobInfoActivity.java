package group9.softwareengineering;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AgreedJobInfoActivity extends AppCompatActivity {


    private TextView jobDescription, jobLocation, jobStartTime, jobEndTime, jobPrice, petOwner, petSitter;
    private ImageView imageOwner, imageSitter;
    private String usersID, documentId;
    private Posting posting;
    private Profile profile;
    private ArrayList<String> petsID = new ArrayList<>();
    private ArrayList<Pet> pets = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreed_job_info);

        jobDescription = findViewById(R.id.jobDescription);
        jobLocation = findViewById(R.id.jobLocation);
        jobStartTime = findViewById(R.id.jobStartTime);
        jobEndTime = findViewById(R.id.jobEndTime);
        jobPrice = findViewById(R.id.jobPrice);
        petOwner = findViewById(R.id.petOwner);
        petSitter = findViewById(R.id.petSitter);

        imageOwner = findViewById(R.id.imageOwner);
        imageSitter = findViewById(R.id.imageSitter);
       // imageOwner.setOnClickListener(new View.OnClickListener() {
       //     @Override
        //    public void onClick(View view) {
         //       Intent intent = new Intent(view.getContext(), UsersProfile.class);
          //      startActivity(intent);
           // }
        //});
        //imageSitter.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View view) {
           //     Intent intent = new Intent(view.getContext(), UsersProfile.class);
            //    startActivity(intent);
           // }
        //});

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fetchFromDatabasePosting();
        fetchFromDatabaseProfile();
        fetchFromDatabasePets();

        this.usersID = getIntent().getStringExtra("usersID");
        this.petsID = getIntent().getStringArrayListExtra("usersPetIds");
        this.documentId = getIntent().getStringExtra("ids");

        recyclerView = findViewById(R.id.myJobPetsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

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
                    jobDescription.setText(posting.getDescription());
                    jobLocation.append(posting.getLocation().toString());
                    jobStartTime.append(posting.getStart_time().toString());
                    jobEndTime.append(posting.getEnd_time().toString());
                    //jobPrice.append(posting.getPayment().toString());
                    petOwner.append(posting.getPoster());
                    petSitter.append((posting.getSitter_found()));
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


    private void fetchFromDatabaseProfile() {
        db.collection("profile").document(usersID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        profile = document.toObject(Profile.class);
                    }
                    //methods to get profile images
                    //imageOwner.getImage();
                    //imageSitter.getImage();
                }

            }

        });
    }

}