package group9.softwareengineering;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyPostingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Posting> postings = new ArrayList<>();

    private FirebaseUser currentUser;

    private FirestoreRecyclerAdapter adapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_postings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_postings_recycler_view);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        query = db.collection("postings").whereEqualTo("poster_id", currentUser.getUid());

        FirestoreRecyclerOptions<Posting> response = new FirestoreRecyclerOptions.Builder<Posting>()
                .setQuery(query, Posting.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Posting, PostingHolder>(response) {
            @Override
            public void onBindViewHolder(PostingHolder holder, int position, Posting posting) {
                holder.setData(posting, getApplicationContext());
            }

            @Override
            public PostingHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.recycler_view_item_my_postings, group, false);
                return new PostingHolder(view);
            }
        };

        /**
         * test code
         *
        FirebaseAuth.getInstance().signInWithEmailAndPassword("sm01562@surrey.ac.uk", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                query = db.collection("postings").whereEqualTo("poster_id", currentUser.getUid());

                FirestoreRecyclerOptions<Posting> response = new FirestoreRecyclerOptions.Builder<Posting>()
                        .setQuery(query, Posting.class)
                        .build();

                adapter = new FirestoreRecyclerAdapter<Posting, PostingHolder>(response) {
                    @Override
                    public void onBindViewHolder(PostingHolder holder, int position, Posting posting) {
                        holder.setData(posting, getApplicationContext());
                    }

                    @Override
                    public PostingHolder onCreateViewHolder(ViewGroup group, int i) {
                        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.recycler_view_item_my_postings, group, false);
                        return new PostingHolder(view);
                    }
                };

                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });

         **/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPostingsActivity.this, NewJobActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}


class PostingHolder extends RecyclerView.ViewHolder {
    private View view;

    PostingHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    void setData(Posting posting, Context context) {
        TextView when = view.findViewById(R.id.my_postings_when);
        TextView description = view.findViewById(R.id.my_postings_description);
        ImageView imageView = view.findViewById(R.id.my_postings_image);

        // format dates
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
        sdf.applyPattern("EEE, d MMM HH:mm");
        String date1 = sdf.format(posting.getStart_time());
        String date2 = sdf.format(posting.getEnd_time());

        when.setText(context.getString(R.string.job_duration, date1, date2));
        description.setText(posting.getDescription());
        Picasso.with(context).load(posting.getPhotoURL()).fit().centerCrop().into(imageView);
    }
}