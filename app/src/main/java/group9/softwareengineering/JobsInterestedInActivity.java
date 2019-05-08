package group9.softwareengineering;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class JobsInterestedInActivity extends AppCompatActivity implements HomeAdapter.onClickListener {

//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;
//
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private FirebaseUser currentUser;
//    private FirestoreRecyclerAdapter adapter;
//    private Query query;
//
//    private SelectedJobActivity selected

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
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
        setContentView(R.layout.activity_jobs_interested_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        pay = getString(R.string.string_pay);
        posted = getString(R.string.string_posted);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        fetchesFromDatabase();
    }


    private void fetchesFromDatabase() {
        db.collection("postings")
                .whereArrayContains("sitters_interested", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Posting> tempPostings = new ArrayList<>();
                            ArrayList<String> tempids = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                tempPostings.add(posting);
                                tempids.add(document.getId());
                            }
                            postings = tempPostings;
                            ids = tempids;
                            adapter = new HomeAdapter(postings, pay, posted, getApplicationContext(), JobsInterestedInActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SelectedJobActivity.class);
        intent.putExtra("ids", this.ids.get(position));
        intent.putStringArrayListExtra("usersPetIds" , this.postings.get(position).getPetIDs());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        postings.clear();
        ids.clear();
        fetchesFromDatabase();
    }
}
//        recyclerView = (RecyclerView) findViewById(R.id.jobs_interested_in_recycler_view);
//
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        query = db.collection("postings").whereEqualTo("sitters_interested",currentUser.getUid());
//
//        final FirestoreRecyclerOptions<Posting> response = new FirestoreRecyclerOptions.Builder<Posting>()
//                .setQuery(query, Posting.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<Posting, PostingHolder>(response) {
//            @Override
//            protected void onBindViewHolder(PostingHolder holder, int position, final Posting posting) {
//                holder.setData(posting, getApplicationContext());
//                holder.setOnClickListener(new PostingHolder.ClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        //TODO: go to Job info activity
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, final int position) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(JobsInterestedInActivity.this);
//
//                        builder.setTitle(getString(R.string.delete_posting_dialog_title));
//                        builder.setMessage(getString(R.string.delete_posting_dialog_message));
//
//                        builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                selected.deleteFromDatabase();
//                                dialogInterface.dismiss();
//                            }
//                        });
//                        builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        });
//                        builder.create().show();
//                    }
//                });
//
//            }
//
//            @NonNull
//            @Override
//            public PostingHolder onCreateViewHolder(ViewGroup group, int i) {
//                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.recycler_view_item_my_postings, group, false);
//
//                return new PostingHolder(view);
//            }
//        };
//            recyclerView.setAdapter(adapter);


//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }



