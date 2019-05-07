package group9.softwareengineering;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class NotificationActivity extends AppCompatActivity {

    private SittersInterestedFragment sittersInterestedFragment;
    private JobsAgreedFragment jobsAgreedFragment;
    private RequestsFragment requestsFragment;
    private UpdatesFragment updatesFragment;
    private HistoryFragment historyFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sitters:
                    setFragment(sittersInterestedFragment);
                    return true;
                case R.id.navigation_agreedJobs:
                    setFragment(jobsAgreedFragment);
                    return true;
                case R.id.navigation_requests:
                    setFragment(requestsFragment);
                    return true;
                case R.id.navigation_updates:
                    setFragment(updatesFragment);
                    return true;
                case R.id.navigation_history:
                    setFragment(historyFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        sittersInterestedFragment = new SittersInterestedFragment();
        jobsAgreedFragment = new JobsAgreedFragment();
        requestsFragment = new RequestsFragment();
        updatesFragment = new UpdatesFragment();
        historyFragment = new HistoryFragment();
        setFragment(sittersInterestedFragment);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout , fragment);
        fragmentTransaction.commit();
    }

}
