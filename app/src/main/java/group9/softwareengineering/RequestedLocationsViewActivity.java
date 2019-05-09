package group9.softwareengineering;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class RequestedLocationsViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<GeoPoint> locations = new ArrayList<>();
    private ArrayList<Marker> mMarkers = new ArrayList<>();
    private ArrayList<String> latitudes = new ArrayList<>();
    private ArrayList<String> longitudes = new ArrayList<>();

    private GoogleMap mMap;
    private Posting posting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_locations_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        latitudes = getIntent().getStringArrayListExtra("latitudes");
        longitudes = getIntent().getStringArrayListExtra("longitudes");
        for(int i = 0; i < latitudes.size(); i++ ){
            double latitude = Double.parseDouble(latitudes.get(i));
            double longitude = Double.parseDouble(longitudes.get(i));
            GeoPoint location = new GeoPoint(latitude , longitude);
            locations.add(location);
        }
        Log.v("PostingLength", Integer.toString(locations.size()));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        googleMap.setMinZoomPreference(5);
        googleMap.setMaxZoomPreference(20);
        LatLng mLatLng;
        Marker marker;
        for (GeoPoint location : locations) {
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(mLatLng));
            mMarkers.add(marker);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker m : mMarkers) {
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
    }

    public void stopActivity(View view) {
        this.finish();
    }




}
