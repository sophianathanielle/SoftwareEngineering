package group9.softwareengineering;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PostingJobActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback{

    int DATE_DIALOG = 0;
    int TIME_DIALOG = 0;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Pet> mPets = new ArrayList<Pet>();

    // Default location is Guildford-ish
    private double mLatitude = 51.24;
    private double mLongitude = -0.59;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;


    // TODO
    // Get FirebaseUser working.
    // Get RecyclerView working probably with the help of Theo.
    // Make location update on the fly.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mLatitude = extras.getDouble("Lat");
            mLongitude = extras.getDouble("Long");
        }

        setContentView(R.layout.activity_posting_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLite);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView) findViewById(R.id.petRecyclerView);
        layoutManager = new LinearLayoutManager(this);

        fetchPets();
        Log.v("faggotry mpets", Integer.toString(mPets.size()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng location = new LatLng(mLatitude, mLongitude);
        googleMap.addMarker(new MarkerOptions().position(location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    public void clickSD(View view) {
        Calendar c = Calendar.getInstance();
        DATE_DIALOG = 0;
        datePickerDialog = new DatePickerDialog(PostingJobActivity.this, this, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void clickST(View view) {
        Calendar c = Calendar.getInstance();
        TIME_DIALOG = 0;
        timePickerDialog = new TimePickerDialog(PostingJobActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, this, c.get(c.HOUR), c.get(c.MINUTE), false);
        timePickerDialog.show();
    }

    public void clickED(View view) {
        Calendar c = Calendar.getInstance();
        c.add(c.DAY_OF_MONTH, 7);
        DATE_DIALOG = 1;
        datePickerDialog = new DatePickerDialog(PostingJobActivity.this, this, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void clickET(View view) {
        Calendar c = Calendar.getInstance();
        TIME_DIALOG = 1;
        timePickerDialog = new TimePickerDialog(PostingJobActivity.this, TimePickerDialog.THEME_HOLO_LIGHT,this, c.get(c.HOUR), c.get(c.MINUTE), false);
        timePickerDialog.show();
    }

    public void clickLocation(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        if (DATE_DIALOG == 0) {
            TextView startDate = (TextView) PostingJobActivity.this.findViewById(R.id.startDate);
            startDate.setText(format.format(c.getTime()));
        }
        if (DATE_DIALOG == 1) {
            TextView endDate = (TextView) PostingJobActivity.this.findViewById(R.id.endDate);
            endDate.setText(format.format(c.getTime()));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        if (TIME_DIALOG == 0) {
            TextView startTime = (TextView) PostingJobActivity.this.findViewById(R.id.startTime);
            startTime.setText(format.format(c.getTime()));
        }
        if (TIME_DIALOG == 1) {
            TextView endTime = (TextView) PostingJobActivity.this.findViewById(R.id.endTime);
            endTime.setText(format.format(c.getTime()));
        }
    }

    private void fetchPets() {
        db.collection("pets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Pet> pets = new ArrayList<Pet>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.v("successful get", document.getId() + " => " + document.getData());
                        // USE THE USER'S ID IM USING SOPHIA'S HERE BECAUSE SHES THE ONLY ONE WITH A PET
                        if (document.getData().get("owner_id").equals("7Xx3slfuH6Tx7N0DMvbHnHlMry32")) {
                            String name = (String) document.getData().get("name");
                            String photoRef = (String) document.getData().get("photo_reference");
                            Pet newPet = new Pet(name, 0, "", "", "", photoRef, document.getId());
                            pets.add(newPet);
                        }
                    }
                    setPets(pets);
                    adapter = new PostingJobAdapter(mPets);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.v("dickhead", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void update(View view) {
        // Find everything by ID
        TextView startDate = (TextView) this.findViewById(R.id.startDate);
        TextView endDate = (TextView) this.findViewById(R.id.endDate);
        TextView startTime = (TextView) this.findViewById(R.id.startTime);
        TextView endTime = (TextView) this.findViewById(R.id.endTime);
        EditText description = (EditText) this.findViewById(R.id.descText);
        EditText payString = (EditText) this.findViewById(R.id.paymentText);

        // Get start and end dates
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm");
        Date start = new Date();
        Date end = new Date();
        try {
            start = format.parse(startDate.getText().toString()+"T"+startTime.getText().toString());
            end = format.parse(endDate.getText().toString()+"T"+endTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get location
        GeoPoint location = new GeoPoint(mLatitude, mLongitude);
        int payment = Integer.parseInt(payString.getText().toString());

        Toast.makeText(this, "Username: " + currentUser.getDisplayName() + "\r\n" +
                "User ID: " + currentUser.getUid() + "\r\n" +
                "Start: " + start.toString() + "\r\n" +
                "End: " + end.toString() + "\r\n" +
                "Location: " + mLatitude + "//" + mLongitude + "\r\n" +
                "Desc: " + description.getText().toString() + "\r\n" +
                "Payment: " + payString, Toast.LENGTH_SHORT).show();

        // Posting posting = new Posting(currentUser.getDisplayName(), currentUser.getUid(), start, end, location, description.getText().toString(), 10, );

    }

    private void setPets(List<Pet> pets) {
        Log.v("faggotry setting pets", Integer.toString(pets.size()));
        this.mPets = pets;
        Log.v("faggotry testfaggotcunt", Integer.toString(this.mPets.size()));
    }

}
