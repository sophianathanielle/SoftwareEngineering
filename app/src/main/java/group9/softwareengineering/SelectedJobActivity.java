package group9.softwareengineering;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SelectedJobActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String documentId;
    private Posting posting;
    private ArrayList<String> petsID = new ArrayList<>();
    private ArrayList<Pet> pets = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView postedBy, startDate, endDate, description;
    private ImageView star;
    private FirebaseUser currentUser;
    private SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_job);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.petsID = getIntent().getStringArrayListExtra("usersPetIds");
        this.documentId = getIntent().getStringExtra("ids");
        star = findViewById(R.id.starImage);
        star.setTag(R.drawable.star_empty);
        fetchFromDatabasePosting(new FirestoreCallback() {
            @Override
            public void onCallback(Posting tempPsting) {
                posting = tempPsting;
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
            }
        });
        fetchFromDatabasePets();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.myJobPetsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        postedBy = findViewById(R.id.jobPostedBy);
        startDate = findViewById(R.id.jobStartTime);
        endDate = findViewById(R.id.jobEndTime);
        description = findViewById(R.id.jobDescription);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) star.getTag();
                if (tag == R.drawable.star_empty) {
                    star.setImageResource(R.drawable.star_fill);
                    star.setTag(R.drawable.star_fill);
                    insertToDatabase();
                } else {
                    star.setImageResource(R.drawable.star_empty);
                    star.setTag(R.drawable.star_empty);
                    deleteFromDatabase();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng location = new LatLng(posting.getLocation().getLatitude(), posting.getLocation().getLongitude());
        googleMap.addMarker(new MarkerOptions().position(location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    public void deleteFromDatabase() {
        db.collection("postings").document(documentId).update("sitters_interested", FieldValue.arrayRemove(currentUser.getUid()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.successRemoved, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void insertToDatabase() {
        db.collection("postings").document(documentId).update("sitters_interested", FieldValue.arrayUnion(currentUser.getUid()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void fetchFromDatabasePosting(final FirestoreCallback firestoreCallback) {
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
                    for (String i : posting.getSitters_interested()) {
                        if (i.equals(currentUser.getUid())) {
                            star.setTag(R.drawable.star_fill);
                            star.setImageResource(R.drawable.star_fill);
                        }
                    }
                firestoreCallback.onCallback(posting);
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

    private interface FirestoreCallback {
        void onCallback(Posting posting );
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

