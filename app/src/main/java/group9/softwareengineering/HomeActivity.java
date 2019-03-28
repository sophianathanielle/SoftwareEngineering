package group9.softwareengineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private com.getbase.floatingactionbutton.FloatingActionButton starredFab, postedFab, newJobsFab, upcomingFab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Posting>  postings = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button find;
    private String pay;
    private String posted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fetchesFromDatabase();
        pay = getString(R.string.pay);
        posted = getString(R.string.posted);
        FloatingActionsMenu expandFab = (FloatingActionsMenu) findViewById(R.id.expand);
        starredFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.starred);
        postedFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.posted);
        newJobsFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.newJobs);
        upcomingFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.upcoming);
        find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("das" , String.valueOf(postings.get(0).getPayment()));
                fetchesFromDatabase();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
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

    private void fetchesFromDatabase() {
        db.collection("postings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Posting> tempPostings = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("das", document.getId() + " => " + document.getData());
                                Posting posting = document.toObject(Posting.class);
                                tempPostings.add(posting);
                                Log.i("postings", Integer.toString(postings.size()));
                            }
                            postings = tempPostings;
                            adapter = new HomeAdapter(postings , pay , posted);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void example() {
        Posting posting = new Posting();
        posting.setDescription("asdsad");
        posting.setPayment(213);
        postings.add(posting);
        postings.add(posting);
        postings.add(posting);

        Log.i("das", Integer.toString(postings.size()));

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
