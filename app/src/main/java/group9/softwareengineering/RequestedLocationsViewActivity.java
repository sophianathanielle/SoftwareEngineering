package group9.softwareengineering;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class RequestedLocationsViewActivity extends AppCompatActivity {

    private ArrayList<GeoPoint> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_locations_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        locations = (ArrayList<GeoPoint>) bundle.getSerializable("requested_locations");

    }

}
