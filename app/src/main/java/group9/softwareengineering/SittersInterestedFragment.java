package group9.softwareengineering;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
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


public class SittersInterestedFragment extends Fragment implements SittersAdapter.onClickListener{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private FirebaseUser currentUser;
    private LinkedHashMap<Posting , String>postings = new LinkedHashMap<>();
    private LinkedHashMap<String, Profile> users = new LinkedHashMap<>();
    private SittersAdapter adapter;
    private ArrayList<String> users2 = new ArrayList<>();
    private LinkedListMultimap<Posting , Profile> postingsAndProfiles = LinkedListMultimap.create();



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
                                if(document.get("sitter_found") == "") {
                                    //Log.d("das", document.getId() + " => " + document.getData());
                                    Posting posting = document.toObject(Posting.class);
                                    postings.put(posting, document.getId());
                                }
                            }
                            firestoreCallback2.onCallback(postings);

                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void loadUsers(final FirestoreCallback firestoreCallback) {
                db.collection("profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Profile profile = document.toObject(Profile.class);
                                users.put(document.getId(), profile);
                            }
                            firestoreCallback.onCallback(users);
                        }
                    }
                });
    }


    public void setUpRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        for (Posting posting : postings.keySet()) {
            for (String id : posting.getSitters_interested()) {
                if(users.containsKey(id)) {
                    postingsAndProfiles.put(posting ,users.get(id));
                    users2.add(id);
                    String pay = String.valueOf(users.get(id).getFee_ph());
                    String posted = users.get(id).getName();
                    Log.i("MPENI DAME" , "EMPIKE " + postingsAndProfiles.size());
                    adapter = new SittersAdapter(postings, postingsAndProfiles, pay, posted, getContext(), SittersInterestedFragment.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext() , ViewSittersProfileActivity.class);
        intent.putExtra("profile" ,users.get(users2.get(position)));
        startActivity(intent);
    }


    private interface FirestoreCallback {
        void onCallback(LinkedHashMap<String, Profile> hashMap);
    }

    private interface FirestoreCallback2 {
        void onCallback(LinkedHashMap<Posting , String>hashMap);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        postings.clear();
        users.clear();
        postingsAndProfiles.clear();
        loadPostings(new FirestoreCallback2() {
            @Override
            public void onCallback(LinkedHashMap<Posting , String> hashMap) {
                postings = hashMap;
                loadUsers(new FirestoreCallback() {
                    @Override
                    public void onCallback(LinkedHashMap<String, Profile> hashMap2) {
                        users = hashMap2;
                        setUpRecycler();
                    }
                });
            }
        });
    }

    public void setupRecycler() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                if (dX > 0) {
                    // Set your color for positive displacement
                    paint.setARGB(255, 0, 255, 0);
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), paint);
                } else {
                    //Set  color for negative displacement
                    paint.setARGB(255, 255, 0, 0);
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
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
                                        postings.clear();
                                        users.clear();
                                        loadPostings(new FirestoreCallback2() {
                                            @Override
                                            public void onCallback(LinkedHashMap<Posting,String> hashMap) {
                                                loadUsers(new FirestoreCallback() {
                                                    @Override
                                                    public void onCallback(LinkedHashMap<String, Profile> hashMap) {
                                                        final int size = postingsAndProfiles.size();
                                                        postingsAndProfiles.clear();
                                                        adapter.notifyItemRangeRemoved(0, size);
                                                        setUpRecycler();
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



