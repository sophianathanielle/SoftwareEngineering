package group9.softwareengineering;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ViewSittersProfileActivity extends AppCompatActivity {


    private TextView name , bio , rating;
    private Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sitters_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.name);
        bio = findViewById(R.id.bio);
        rating = findViewById(R.id.rating);
        if(getIntent().getExtras() != null){
            profile = getIntent().getParcelableExtra("profile");
        }
        name.setText(profile.getName());
        bio.setText(profile.getBio());
        rating.setText(String.valueOf(profile.getRating()));
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
