package group9.softwareengineering;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class AgreedJobInfoActivity extends AppCompatActivity {


    private TextView jobDescription,descLabel, jobLocation,locationLabel, jobStartTime,startLabel, jobEndTime,endLabel, jobPrice,priceLabel, petOwner,ownerLabel, petSitter,sitterLabel,sitterPhone,ownerPhone;
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
        descLabel =findViewById(R.id.descLabel);
        locationLabel =findViewById(R.id.locationLabel);
        startLabel =findViewById(R.id.startLabel);
        endLabel =findViewById(R.id.endLabel);
        priceLabel  =findViewById(R.id.priceLabel);
        ownerLabel =findViewById(R.id.ownerLabel);
        sitterLabel =findViewById(R.id.sitterLabel);
        sitterPhone  =findViewById(R.id.sitterPhone);
        ownerPhone =findViewById(R.id.ownerPhone);
        fetchFromDatabasePets();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.posting = getIntent().getParcelableExtra("posting");
        double longitude = getIntent().getDoubleExtra("longitude",0);
        double latitude = getIntent().getDoubleExtra("latitude",0);
        GeoPoint location = new GeoPoint(latitude,longitude);
        String start = getIntent().getStringExtra("start_time");
        String start_time = new String(start);
        String end = getIntent().getStringExtra("end_time");
        String end_time = new String(end);

        recyclerView = findViewById(R.id.myJobPetsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        jobDescription.setText(posting.getDescription());
        jobLocation.setText(location.toString());
        jobStartTime.setText(start_time);
        jobEndTime.setText(end_time);
        jobPrice.setText(String.valueOf(posting.getPayment()));
        petOwner.setText(posting.getPoster());
        petSitter.setText(posting.getSitter_found());

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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}