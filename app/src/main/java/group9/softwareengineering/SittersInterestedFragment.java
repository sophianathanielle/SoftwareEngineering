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


public class SittersInterestedFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private FirebaseUser currentUser;
    private LinkedHashMap<String,Posting> postings = new LinkedHashMap<>();
    private LinkedHashMap<String, Profile> users = new LinkedHashMap<>();
    private SittersAdapter adapter;
    private ArrayList<String> users2 = new ArrayList<>();


    public SittersInterestedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sitters_interested, container, false);
        postings.clear();
        users.clear();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.sittersRecycler);
        setupRecycler();
        loadPostings(new FirestoreCallback2() {
            @Override
            public void onCallback(LinkedHashMap<String,Posting> hashMap) {
                loadUsers(new FirestoreCallback() {
                    @Override
                    public void onCallback(LinkedHashMap<String, Profile> hashMap) {
                        methodname();
                    }
                });
            }
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
                                //Log.d("das", document.getId() + " => " + document.getData());
                                Posting posting = document.toObject(Posting.class);
                                postings.put(document.getId() , posting);
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

    public void loadUsers(final FirestoreCallback firestoreCallback) {
        for (Posting posting : postings.values()) {
            for (final String userID : posting.getSitters_interested()) {
                db.collection("profile").document(userID)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        Profile user = document.toObject(Profile.class);
                        users.put(userID, user);
                        firestoreCallback.onCallback(users);
                    }
                });
            }
        }
    }


    public void methodname() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        for (Posting posting : postings.values()) {
            for (String id : posting.getSitters_interested()) {
                users2.add(id);
                String pay = String.valueOf(users.get(id).getFee_ph());
                String posted = users.get(id).getName();
                adapter = new SittersAdapter(postings, pay, posted, getContext(), null);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        }
    }


    private interface FirestoreCallback {
        void onCallback(LinkedHashMap<String , Profile> hashMap );
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
                            .update("sitters_interested", FieldValue.arrayRemove(users2.get(viewHolder.getAdapterPosition())))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                }

                    if (i == ItemTouchHelper.RIGHT) {
                        db.collection("postings").document(adapter.getDocumentAt(viewHolder.getAdapterPosition()))
                                .update("sitters_interested", FieldValue.delete())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                        db.collection("postings").document(adapter.getDocumentAt(viewHolder.getAdapterPosition()))
                                .update("sitter_found", users2.get(viewHolder.getAdapterPosition()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadPostings(new FirestoreCallback2() {
                                            @Override
                                            public void onCallback(LinkedHashMap<String,Posting> hashMap) {
                                                loadUsers(new FirestoreCallback() {
                                                    @Override
                                                    public void onCallback(LinkedHashMap<String, Profile> hashMap) {
                                                        methodname();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                    }
            }
        }).attachToRecyclerView(recyclerView);
    }
}



