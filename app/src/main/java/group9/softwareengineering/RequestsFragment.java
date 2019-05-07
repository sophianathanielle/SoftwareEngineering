package group9.softwareengineering;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class RequestsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private FirebaseUser currentUser;
    private LinkedHashMap<String,Posting> postings = new LinkedHashMap<>();
    private LinkedHashMap<String, Profile> users = new LinkedHashMap<>();
    private RequestsAdapter adapter;
    private ArrayList<String> users2 = new ArrayList<>();


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        postings.clear();
        users.clear();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.sittersRecycler);
        setupRecycler();
        loadPostings(new FirestoreCallback2() {
            @Override
            public void onCallback(LinkedHashMap<String,Posting> hashMap) {
                setUpAdapter();            }
        });
        return view;
    }

    public void loadPostings(final FirestoreCallback2 firestoreCallback2) {
        db.collection("postings").whereEqualTo("poster_id", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("sitter_found") != null) {
                                    Posting posting = document.toObject(Posting.class);
                                    postings.put(document.getId(), posting);
                                }
                            }
                            firestoreCallback2.onCallback(postings);
                            //adapter = new HomeAdapter(postings , pay , posted, getApplicationContext(), HomeActivity.this);
                            //recyclerView.setLayoutManager(layoutManager);
                            //recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void setUpAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                adapter = new RequestsAdapter(postings, getContext(), null);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
    }

    private interface FirestoreCallback2 {
        void onCallback(LinkedHashMap< String,Posting> hashMap );
    }

    public void setupRecycler(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if (i == ItemTouchHelper.LEFT) {

                    db.collection("postings").document(adapter.getDocumentAt(viewHolder.getAdapterPosition()))
                            .update("location_request", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setUpAdapter();
                                    Toast.makeText(getContext(),getString(R.string.request_location_toast),Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (i == ItemTouchHelper.RIGHT) {
                    db.collection("postings").document(adapter.getDocumentAt(viewHolder.getAdapterPosition()))
                            .update("photo_request", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setUpAdapter();
                                    Toast.makeText(getContext(),getString(R.string.request_photo_toast),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }).attachToRecyclerView(recyclerView);
    }
}



