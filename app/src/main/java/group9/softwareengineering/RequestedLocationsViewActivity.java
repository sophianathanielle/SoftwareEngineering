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
import com.google.android.gms.maps.SupportMapFragment;
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
    private ArrayList<LatLng> mLatLngs = new ArrayList<>();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_locations_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        latitudes = getIntent().getStringArrayListExtra("latitudes");
        longitudes = getIntent().getStringArrayListExtra("longitudes");
        for(int i = 0; i < latitudes.size(); i++ ){
            double latitude = Double.parseDouble(latitudes.get(i));
            double longitude = Double.parseDouble(longitudes.get(i));
            GeoPoint location = new GeoPoint(latitude, longitude);
            locations.add(location);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        googleMap.setMinZoomPreference(1);
        googleMap.setMaxZoomPreference(20);
        LatLng mLatLng;
        Marker marker;
        for (GeoPoint location : this.locations) {
            Log.v("ReqLocLocations", "Latitude: " + Double.toString(location.getLatitude()) + "Longitude: " + Double.toString(location.getLongitude()));
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mLatLngs.add(mLatLng);
            marker = mMap.addMarker(new MarkerOptions().position(mLatLng));
            mMarkers.add(marker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng mll : this.mLatLngs) {
            builder.include(mll);
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }

    public void stopActivity(View view) {
        this.finish();
    }

}
