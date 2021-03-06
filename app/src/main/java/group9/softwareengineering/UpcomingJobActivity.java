package group9.softwareengineering;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpcomingJobActivity extends AppCompatActivity implements HomeAdapter.onClickListener {
    private RecyclerView recyclerView;
    private Calendar calendar;
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Posting> postings = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String pay;
    private String posted;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        pay = getString(R.string.string_pay);
        posted = getString(R.string.string_posted);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LineDividerItemDecoration(this, R.drawable.line_divider));
        fetchesFromDatabase(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<Posting> tempPostings , ArrayList<String> tempIds) {
                postings = tempPostings;
                ids = tempIds;
                setRecyclerView();
            }
        });
    }


    private void fetchesFromDatabase(final FirestoreCallback firestoreCallback) {
        db.collection("postings")
             .whereEqualTo("sitter_found",currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i("MPENI" , "MPENI");

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                postings.add(posting);
                                ids.add(document.getId());
                            }
                            firestoreCallback.onCallback(postings , ids);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, AgreedJobInfoActivity.class);
        intent.putExtra("posting" , postings.get(position));
        intent.putExtra("id" , ids.get(position));
        intent.putExtra("flag" , false);
        startActivity(intent);
    }

    private interface FirestoreCallback {
        void onCallback(ArrayList<Posting> tempPostings , ArrayList<String> tempIds );
    }

    public void setRecyclerView(){
        layoutManager = new LinearLayoutManager(this);
        adapter = new HomeAdapter(postings , pay , posted, getApplicationContext(), UpcomingJobActivity.this);
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
