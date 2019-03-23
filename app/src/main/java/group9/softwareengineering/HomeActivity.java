package group9.softwareengineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class HomeActivity extends AppCompatActivity {
    private com.getbase.floatingactionbutton.FloatingActionButton starredFab, postedFab, newJobsFab, upcomingFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionsMenu expandFab = (FloatingActionsMenu) findViewById(R.id.expand);
        starredFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.starred);
        postedFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.posted);
        newJobsFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.newJobs);
        upcomingFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.upcoming);
        starredFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StarredJobActivity.class);
                startActivity(intent);
            }
        });
        postedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostedJobActivity.class);
                startActivity(intent);
            }
        });
        newJobsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewJobActivity.class);
                startActivity(intent);
            }
        });
        upcomingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpcomingJobActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
                return true;
            case R.id.userProfile:
                Intent intent2 = new Intent(getApplicationContext(), EditUserProfileActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
