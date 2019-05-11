package group9.softwareengineering;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class AgreedJobInfoActivity extends AppCompatActivity {


    private TextView jobDescription, jobStartTime, jobEndTime, jobPrice, petOwner, petSitter, sitterPhone , ownerPhone;
    private Button complete;
    private Posting posting;
    private ArrayList<String> petsID = new ArrayList<>();
    private ArrayList<Pet> pets = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String id;
    private Profile sitter;
    private boolean flag = true;
    private SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreed_job_info);

        jobDescription = findViewById(R.id.jobDescription);
        jobStartTime = findViewById(R.id.jobStartTime);
        jobEndTime = findViewById(R.id.jobEndTime);
        jobPrice = findViewById(R.id.jobPrice);
        petOwner = findViewById(R.id.petOwner);
        petSitter = findViewById(R.id.petSitter);
        sitterPhone  =findViewById(R.id.sitterPhone);
        ownerPhone =findViewById(R.id.ownerPhone);
        complete=findViewById(R.id.completeJobButton);

        id = getIntent().getStringExtra("id");
        flag = getIntent().getBooleanExtra("flag" , true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.myJobPetsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        loadPosting(new FirestoreCallback() {
            @Override
            public void onCallback(Posting tempPosting) {
                posting = tempPosting;
                jobDescription.setText(posting.getDescription());
                jobStartTime.setText(posting.getStart_time().toString());
                jobEndTime.setText(posting.getEnd_time().toString());
                jobPrice.setText(String.valueOf(posting.getPayment()));
                petOwner.setText(posting.getPoster());
                petSitter.setText(posting.getSitter_found());
                ownerPhone.setText(currentUser.getPhoneNumber());
                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLite);
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        LatLng location = new LatLng(posting.getLocation().getLatitude(), posting.getLocation().getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(location));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                        mapFragment.getView().setClickable(false);
                    }
                });
                loadProfileSitter(new FirestoreCallback3() {
                    @Override
                    public void onCallback(Profile tempSitter) {
                        sitter = tempSitter;
                        sitterPhone.setText(sitter.getPhone_number());
                        petSitter.setText(sitter.getName());
                    }
                });
                loadPets(new FirestoreCallback2() {
                    @Override
                    public void onCallback(ArrayList<Pet> tempPets) {
                        pets = tempPets;
                        setupRecycler();
                    }
                });
                if(flag) {
                    complete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.collection("postings").document(id).update("completed", true);
                            Toast.makeText(getApplicationContext(), getString(R.string.job_complete), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    complete.setVisibility(View.INVISIBLE);
                    complete.setEnabled(false);
                }
            }

            private void loadProfileSitter(final FirestoreCallback3 firestoreCallback3) {
                db.collection("profile").document(posting.getSitter_found()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Profile profile = task.getResult().toObject(Profile.class);
                            sitter = profile;
                            firestoreCallback3.onCallback(sitter);
                        }
                    }
                });

                db.collection("profile").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Profile profile = task.getResult().toObject(Profile.class);
                            ownerPhone.setText(profile.getPhone_number());
                        }
                    }
                });
            }
        });



    }

    private void loadPosting(final FirestoreCallback firestoreCallback) {
        db.collection("postings").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    posting = task.getResult().toObject(Posting.class);
                    firestoreCallback.onCallback(posting);
                }
            }
        });
    }

    private void loadPets(final FirestoreCallback2 firestoreCallback2) {
        db.collection("pets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        for (String petId : posting.getPetIDs()) {
                            if (document.getId().equals(petId)){
                                Pet pet = document.toObject(Pet.class);
                                pets.add(pet);
                            }
                        }
                    }
                    firestoreCallback2.onCallback(pets);
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onCallback(Posting tempPosting);
    }

    private interface FirestoreCallback2 {
        void onCallback(ArrayList<Pet> tempPets);
    }

    private interface FirestoreCallback3 {
        void onCallback(Profile sitter);
    }

    private void setupRecycler(){
        adapter = new PetAdapter(pets, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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