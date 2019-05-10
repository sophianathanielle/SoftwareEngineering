package group9.softwareengineering;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeAdapter.onClickListener {
    private com.getbase.floatingactionbutton.FloatingActionButton starredFab, postedFab, myJobsFab, upcomingFab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Posting> postings = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<Posting> usersPostings = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SwipeRefreshLayout swipeRefreshLayout;

    private SearchView findSearch;
    private String pay;
    private String posted;
    private FirebaseUser currentUser;
    private MenuItem notificationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fetchesFromDatabase();
        pay = getString(R.string.string_pay);
        posted = getString(R.string.string_posted);
        FloatingActionsMenu expandFab = (FloatingActionsMenu) findViewById(R.id.expand);
        starredFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.starred);
        myJobsFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.posted);
        postedFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.newJobs);
        upcomingFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.upcoming);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        setSwipeRefresh();
        findSearch = findViewById(R.id.find);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        starredFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JobsInterestedInActivity.class);
                startActivity(intent);
            }
        });
        postedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostingJobActivity.class);
                startActivity(intent);
            }
        });
        myJobsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPostingsActivity.class);
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

    private void setSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchesFromDatabase();
            }
        });
    }



    private void fetchesFromDatabase() {
        db.collection("postings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Posting> tempPostings = new ArrayList<>();
                            ArrayList<Posting> tempUsersPostings = new ArrayList<>();
                            ArrayList<String> tempIds = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("das", document.getId() + " => " + document.getData());
                                if (!document.get("poster_id").equals(currentUser.getUid())) {
                                    Posting posting = document.toObject(Posting.class);
                                    tempPostings.add(posting);
                                    tempIds.add(document.getId());
                                    Log.i("postings", Integer.toString(postings.size()));
                                } else {
                                    Posting posting = document.toObject(Posting.class);
                                    tempUsersPostings.add(posting);
                                }
                            }
                            usersPostings = tempUsersPostings;
                            postings = tempPostings;
                            ids = tempIds;
                            adapter = new HomeAdapter(postings, pay, posted, getApplicationContext(), HomeActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            swipeRefreshLayout.setRefreshing(false);
                            for (Posting posting : usersPostings) {
                                if (posting.getLocation_request() || posting.getPhoto_request() || posting.getSitters_interested().size() > 0) {
                                    notificationIcon.setIcon(R.drawable.notification_bell_active_con);
                                }
                            }
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepage, menu);
        notificationIcon = menu.findItem(R.id.notification);
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
                Intent intent2 = new Intent(getApplicationContext(), ViewMyProfileActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SelectedJobActivity.class);
        intent.putExtra("ids", this.ids.get(position));
        intent.putStringArrayListExtra("usersPetIds", this.postings.get(position).getPetIDs());
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(notificationIcon != null){
            notificationIcon.setIcon(R.drawable.notification_bell_icon);
        }
        postings.clear();
        ids.clear();
        usersPostings.clear();
        fetchesFromDatabase();
    }
}
