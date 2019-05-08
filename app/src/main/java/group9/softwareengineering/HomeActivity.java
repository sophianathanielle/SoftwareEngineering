package group9.softwareengineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeAdapter.onClickListener{
    private com.getbase.floatingactionbutton.FloatingActionButton starredFab, postedFab, myJobsFab, upcomingFab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Posting>  postings = new ArrayList<>();
    private ArrayList<String>  ids = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private Button find;
    private String pay;
    private String posted;
    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fetchesFromDatabase();
       // mUserId =mAuth.getCurrentUser().getUid();
        pay = getString(R.string.string_pay);
        posted = getString(R.string.string_posted);
        FloatingActionsMenu expandFab = (FloatingActionsMenu) findViewById(R.id.expand);
        starredFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.starred);
        myJobsFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.posted);
        postedFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.newJobs);
        upcomingFab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.upcoming);
        find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchesFromDatabase();
            }
        });
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

    private void fetchesFromDatabase() {
        db.collection("postings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Posting> tempPostings = new ArrayList<>();
                            ArrayList<String> tempIds = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("das", document.getId() + " => " + document.getData());
                                Posting posting = document.toObject(Posting.class);
                                tempPostings.add(posting);
                                tempIds.add(document.getId());
                                Log.i("postings", Integer.toString(postings.size()));
                            }
                            postings = tempPostings;
                            ids = tempIds;
                            adapter = new HomeAdapter(postings , pay , posted, getApplicationContext(), HomeActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
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
        intent.putStringArrayListExtra("usersPetIds" , this.postings.get(position).getPetIDs());
        startActivity(intent);
    }
}
