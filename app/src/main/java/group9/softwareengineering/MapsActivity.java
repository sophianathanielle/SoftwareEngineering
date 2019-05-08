package group9.softwareengineering;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mLocation = new LatLng(51.24, -0.59);
    private boolean flag;
    private Button locationOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationOn = (Button) findViewById(R.id.getLocation);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        flag = false;
        this.mMap = googleMap;
        googleMap.setMinZoomPreference(5);
        googleMap.setMaxZoomPreference(20);
        googleMap.addMarker(new MarkerOptions().position(mLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng point)
            {
                android.util.Log.v("onMapClick", "Lat: " + point.latitude + ", Lon: " + point.longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                mLocation = point;
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION_INTENT");
        this.registerReceiver(new LocationReceiver(), filter);
    }

    public void getLocation(View view) {
        if (!flag) {
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
            flag = true;
            locationOn.setText("Location Fetching Off");
        } else {
            Intent intent = new Intent(this, LocationService.class);
            stopService(intent);
            flag = false;
            locationOn.setText("Location Fetching On");
        }
    }

    public void saveLocation(View view) {
            Intent intent = new Intent(this, PostingJobActivity.class);
            intent.putExtra("Lat", mLocation.latitude);
            intent.putExtra("Long", mLocation.longitude);
            startActivity(intent);
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
            if (flag) {
                mLocation = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(mLocation));
            }
        }
    }
}