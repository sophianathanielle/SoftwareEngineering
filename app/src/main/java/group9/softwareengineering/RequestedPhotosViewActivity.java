package group9.softwareengineering;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;

public class RequestedPhotosViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> photoURLs = new ArrayList<>();
    private PhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_photos_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.photoUpdateRecycler);
        photoURLs = getIntent().getStringArrayListExtra("requested_photos");
        setupRecycler();
    }

    private void setupRecycler(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new PhotosAdapter(photoURLs, this);
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
