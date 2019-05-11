package group9.softwareengineering;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.Locale;
import java.util.Map;

public class PostingJobActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback {

    int DATE_DIALOG = 0;
    int TIME_DIALOG = 0;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Pet> mPets = new ArrayList<Pet>();
    private ArrayList<String> petsIds = new ArrayList<>();
    private ArrayList<String> petIdsSelected = new ArrayList<>();
    private List<Pet> mPetsSelected = new ArrayList<Pet>();
    private SupportMapFragment mapFragment;
    // Default location is Guildford-ish
    private double mLatitude;
    private double mLongitude;
    private boolean locationSet;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private TextView endTime , startTime , startDate, endDate;
    private long startTimeLong , endTimeLong;
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy" ,new Locale("en" , "GB"));
    private SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm" , new Locale("en" , "GB"));
    private EditText description , payString;
    private Posting posting;
    private String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_job);

        mLatitude = 51.24;
        mLongitude = -0.59;
        locationSet = false;
        endTime = (TextView) findViewById(R.id.endTime);
        startTime = (TextView) findViewById(R.id.startTime);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
         description = (EditText) findViewById(R.id.descText);
         payString = (EditText) findViewById(R.id.paymentText);
        if(getIntent().getExtras() != null){
            posting = getIntent().getParcelableExtra("posting");
            startTimeLong = getIntent().getLongExtra("start" , 0);
            endTimeLong = getIntent().getLongExtra("end" , 0);
            id = getIntent().getStringExtra("id");
            Date start = new Date();
            Date end = new Date();
            start.setTime(startTimeLong);
            end.setTime(endTimeLong);
            endTime.setText(formatTime.format(end));
            startTime.setText(formatTime.format(start));
            startDate.setText(formatDate.format(start));
            endDate.setText(formatDate.format(end));
            description.setText(posting.getDescription());
            payString.setText(String.valueOf(posting.getPayment()));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLite);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView) findViewById(R.id.petRecyclerView);
        layoutManager = new LinearLayoutManager(this);

        fetchPets();
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
        timePickerDialog = new TimePickerDialog(PostingJobActivity.this, this, c.get(c.HOUR), c.get(c.MINUTE), true);
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
        timePickerDialog = new TimePickerDialog(PostingJobActivity.this, this, c.get(c.HOUR), c.get(c.MINUTE), true);
        timePickerDialog.show();
    }

    public void clickLocation(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        if (DATE_DIALOG == 0) {
            startDate.setText(formatDate.format(c.getTime()));
        }
        if (DATE_DIALOG == 1) {
            endDate.setText(formatDate.format(c.getTime()));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        if (TIME_DIALOG == 0) {
            startTime.setText(formatTime.format(c.getTime()));
        }
        if (TIME_DIALOG == 1) {
            endTime.setText(formatTime.format(c.getTime()));
        }
    }

    private void fetchPets() {
        db.collection("pets").whereEqualTo("owner_id", currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Pet> pets = new ArrayList<Pet>();
                    ArrayList<String> tempIds = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Pet newPet = document.toObject(Pet.class);
                        pets.add(newPet);
                        tempIds.add(document.getId());
                    }
                    setPets(pets, tempIds);
                    adapter = new PostingJobAdapter(mPets, getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    public void update(View view) {

        // Get start and end dates
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm");
        Date start = new Date();
        Date end = new Date();
        try {
            start = format.parse(startDate.getText().toString() + "T" + startTime.getText().toString());
            end = format.parse(endDate.getText().toString() + "T" + endTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get location
        GeoPoint location = new GeoPoint(mLatitude, mLongitude);
        petIdsSelected.clear();
        mPetsSelected.clear();
        for (int i = 0; i < mPets.size(); i++) {
            if (mPets.get(i).isSelected()) {
                petIdsSelected.add(petsIds.get(i));
                mPetsSelected.add(mPets.get(i));
            }
        }
        if (!TextUtils.isEmpty(payString.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()) && !TextUtils.isEmpty(startDate.getText().toString()) && !TextUtils.isEmpty(endDate.getText().toString()) && !TextUtils.isEmpty(startTime.getText().toString()) && !TextUtils.isEmpty(endTime.getText().toString())) {
            if (petIdsSelected.size() > 0) {
                if (Integer.parseInt(payString.getText().toString()) > 0) {
                    if (locationSet) {
                        int payment = Integer.parseInt(payString.getText().toString());
                        Posting posting = new Posting(currentUser.getDisplayName(), currentUser.getUid(), start, end, location, description.getText().toString(), payment, petIdsSelected, false, false, mPetsSelected.get(0).getPhoto_reference());
                        if(getIntent().getExtras() != null){
                            db.collection("postings").document(id).update("start_time" , start , "end_time" , end , "location" , location , "petIDs" , petIdsSelected
                                    , "photoURL" , mPetsSelected.get(0).getPhoto_reference() , "description" , description.getText().toString() , "payment" , payment);
                            Toast.makeText(this, getString(R.string.successfully_updated_posting), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            db.collection("postings").add(posting);
                            Toast.makeText(this, getString(R.string.successfully_created), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else
                        Toast.makeText(this, getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, getString(R.string.invalid_fields), Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, getString(R.string.invalid_pet), Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, getString(R.string.invalid_fields), Toast.LENGTH_SHORT).show();
    }

    private void setPets(List<Pet> pets, ArrayList<String> ids) {
        this.mPets = pets;
        this.petsIds = ids;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            mLatitude = extras.getDouble("Lat");
            mLongitude = extras.getDouble("Long");
            locationSet = extras.getBoolean("set");
            mapFragment.getMapAsync(this);        }
    }
}
