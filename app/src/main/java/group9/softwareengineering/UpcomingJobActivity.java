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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpcomingJobActivity extends AppCompatActivity implements HomeAdapter.onClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Posting> postings = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String pay;
    private String posted;
    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ArrayList mDocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fetchesFromDatabase();
        //mUserId =mAuth.getCurrentUser().getUid();
        pay = getString(R.string.string_pay);
        posted = getString(R.string.string_posted);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

    }

    private void fetchesFromDatabase() {
        db.collection("postings")
             .whereEqualTo("sitter_found",userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> documentIds=new ArrayList<>();
                            ArrayList<Posting> tempPostings = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                tempPostings.add(posting);
                                documentIds.add(document.getId());
                            }


                            postings = tempPostings;
                            adapter = new HomeAdapter(postings , pay , posted, getApplicationContext(), UpcomingJobActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        }


    private void funct(){


    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SelectedJobActivity.class);
        startActivity(intent);
    }
}
