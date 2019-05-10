package group9.softwareengineering;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HistoryFragment extends Fragment implements HistoryAdapter.onClickListener{

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Posting> postings = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.historyRecycler);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        postings.clear();
        loadPostings(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<Posting> tempPostings , ArrayList<String> tempIds) {
                postings = tempPostings;
                ids = tempIds;
                setupRecycler();
            }
        });

        return view;
    }

    private void loadPostings(final FirestoreCallback firestoreCallback) {
        postings.clear();
        ids.clear();
        db.collection("postings").whereEqualTo("poster_id", currentUser.getUid()).whereEqualTo("completed" , true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                postings.add(posting);
                                ids.add(document.getId());
                            }
                            firestoreCallback.onCallback(postings , ids);

                        }
                    }
                });
    }

    private void setupRecycler(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new HistoryAdapter(postings, getContext(), HistoryFragment.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private interface FirestoreCallback {
        void onCallback(ArrayList<Posting> tempPostings, ArrayList<String> tempIds);
    }

    @Override
    public void onItemClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("What rating would you like to give to the Pet Sitter?");
        builder.setItems(new CharSequence[]
                        {"Rate 1 star", "Rate 2 stars" , "Rate 3 stars", "Rate 4 stars" , "Rate 5 stars"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                db.collection("profile").document(postings.get(position).getSitter_found()).update("ratingsOf1", FieldValue.increment(1));
                                db.collection("postings").document(ids.get(position)).delete();
                                loadPostings(new FirestoreCallback() {
                                    @Override
                                    public void onCallback(ArrayList<Posting> tempPostings, ArrayList<String> tempIds) {
                                        postings = tempPostings;
                                        ids = tempIds;
                                        setupRecycler();
                                    }
                                });
                                Toast.makeText(getContext(),getString(R.string.thanks),Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                db.collection("profile").document(postings.get(position).getSitter_found()).update("ratingsOf2", FieldValue.increment(1));
                                db.collection("postings").document(ids.get(position)).delete();
                                loadPostings(new FirestoreCallback() {
                                    @Override
                                    public void onCallback(ArrayList<Posting> tempPostings, ArrayList<String> tempIds) {
                                        postings = tempPostings;
                                        ids = tempIds;
                                        setupRecycler();
                                    }
                                });
                                Toast.makeText(getContext(),getString(R.string.thanks),Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                db.collection("profile").document(postings.get(position).getSitter_found()).update("ratingsOf3", FieldValue.increment(1));
                                db.collection("postings").document(ids.get(position)).delete();
                                loadPostings(new FirestoreCallback() {
                                    @Override
                                    public void onCallback(ArrayList<Posting> tempPostings, ArrayList<String> tempIds) {
                                        postings = tempPostings;
                                        ids = tempIds;
                                        setupRecycler();
                                    }
                                });
                                Toast.makeText(getContext(),getString(R.string.thanks),Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                db.collection("profile").document(postings.get(position).getSitter_found()).update("ratingsOf4", FieldValue.increment(1));
                                db.collection("postings").document(ids.get(position)).delete();
                                loadPostings(new FirestoreCallback() {
                                    @Override
                                    public void onCallback(ArrayList<Posting> tempPostings, ArrayList<String> tempIds) {
                                        postings = tempPostings;
                                        ids = tempIds;
                                        setupRecycler();
                                    }
                                });
                                Toast.makeText(getContext(),getString(R.string.thanks),Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                db.collection("profile").document(postings.get(position).getSitter_found()).update("ratingsOf5", FieldValue.increment(1));
                                db.collection("postings").document(ids.get(position)).delete();
                                loadPostings(new FirestoreCallback() {
                                    @Override
                                    public void onCallback(ArrayList<Posting> tempPostings, ArrayList<String> tempIds) {
                                        postings = tempPostings;
                                        ids = tempIds;
                                        setupRecycler();
                                    }
                                });
                                Toast.makeText(getContext(),getString(R.string.thanks),Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        builder.create().show();    }

}
