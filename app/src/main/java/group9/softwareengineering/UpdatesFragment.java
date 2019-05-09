package group9.softwareengineering;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatesFragment extends Fragment implements UpdatesAdapter.onClickListener {

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Posting> postings = new ArrayList<>();
    private UpdatesAdapter adapter;
    private RecyclerView recyclerView;


    public UpdatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_updates, container, false);
        recyclerView = view.findViewById(R.id.updateRecycler);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        postings.clear();
        loadPostings(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<Posting> tempPostings) {
                setupRecycler();
            }
        });

        return view;
    }

    private void loadPostings(final UpdatesFragment.FirestoreCallback firestoreCallback) {
        db.collection("postings").whereEqualTo("poster_id", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("location_updates") != null || document.get("photo_updates") != null) {
                                    Posting posting = document.toObject(Posting.class);
                                    postings.add(posting);
                                }
                            }
                            firestoreCallback.onCallback(postings);

                        }
                    }
                });
    }

    private void setupRecycler(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new UpdatesAdapter(postings, getContext(), UpdatesFragment.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private interface FirestoreCallback {
        void onCallback(ArrayList<Posting> tempPostings );
    }

    @Override
    public void onItemClick(final int position) {
        if (postings.get(position).getPhoto_updates().size() > 0 && postings.get(position).getLocation_updates().size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("What Updates would you like to see?");
            builder.setItems(new CharSequence[]
                            {"View Photo Updates", "View Location Updates"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(getContext(), RequestedPhotosViewActivity.class);
                                    intent.putStringArrayListExtra("requested_photos", postings.get(position).getPhoto_updates());
                                    startActivity(intent);
                                    break;
                                case 1:
                                    Intent intent2 = new Intent(getContext(), RequestedLocationsViewActivity.class);
                                    intent2.putExtra("posting" , postings.get(position));
                                    startActivity(intent2);
                                    break;
                            }
                        }
                    });
            builder.create().show();
        } else if (postings.get(position).getPhoto_updates().size() > 0 && postings.get(position).getLocation_updates().size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("What Updates would you like to see?");
            builder.setItems(new CharSequence[]
                            {"View Photo Updates"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(getContext(), RequestedPhotosViewActivity.class);
                                    intent.putStringArrayListExtra("requested_photos", postings.get(position).getPhoto_updates());
                                    startActivity(intent);
                                    break;
                            }
                        }
                    });
            builder.create().show();
        } else  if (postings.get(position).getPhoto_updates().size() == 0 && postings.get(position).getLocation_updates().size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("What Updates would you like to see?");
            builder.setItems(new CharSequence[]
                            {"View Location Updates"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent2 = new Intent(getContext(), RequestedLocationsViewActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("requested_locations", postings.get(position).getLocation_updates());
                                    intent2.putExtras(bundle);
                                    startActivity(intent2);
                                    break;
                            }
                        }
                    });
            builder.create().show();
        }
    }

}
