package group9.softwareengineering;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SendRequestedLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mId;
    private LatLng mLocation = new LatLng(51.24, -0.59);
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getStringExtra("id");
        setContentView(R.layout.activity_send_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mButton = (Button) findViewById(R.id.confirmLocation);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        googleMap.setMinZoomPreference(5);
        googleMap.setMaxZoomPreference(40);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 5));
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION_INTENT");
        this.registerReceiver(new SendRequestedLocationActivity.LocationReceiver(), filter);
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    private void findPosting() {
        GeoPoint gpLocation = new GeoPoint(mLocation.latitude, mLocation.longitude);
        db.collection("postings").document(mId).update("location_updates" , FieldValue.arrayUnion(gpLocation) , "location_request" , false);
    }

    public void confirmLocation(View view) {
        findPosting();
        Intent myService = new Intent(this, LocationService.class);
        stopService(myService);
        this.finish();
    }

    public class LocationReceiver extends BroadcastReceiver {
        // Default constructor.
        public LocationReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            double latitude = bundle.getDouble("latitude");
            double longitude = bundle.getDouble("longitude");
            mLocation = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(mLocation));
            if (mButton != null) {
                mButton.setEnabled(true);
            }
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
