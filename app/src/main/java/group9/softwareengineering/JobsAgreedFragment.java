package group9.softwareengineering;


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
public class JobsAgreedFragment extends Fragment implements SumbitRequestsPhotoAdapter.onClickListener , SumbitRequestsLocationAdapter.onClickListener{

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Posting> postingsLocation = new ArrayList<>();
    private ArrayList<Posting> postingsPhoto = new ArrayList<>();
    private ArrayList<String> idsPhoto = new ArrayList<>();
    private ArrayList<String> idsLocation = new ArrayList<>();

    private SumbitRequestsPhotoAdapter adapterPhoto ;
    private SumbitRequestsLocationAdapter adapterLocation;
    private RecyclerView photoRecyclerView , locationRecyclerView;

    public JobsAgreedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs_agreed, container, false);
        photoRecyclerView = view.findViewById(R.id.photoRecycler);
        locationRecyclerView = view.findViewById(R.id.locationRecycler);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        postingsLocation.clear();
        postingsPhoto.clear();

        return view;
    }

    private void loadPostings(final JobsAgreedFragment.FirestoreCallback firestoreCallback) {
        db.collection("postings").whereEqualTo("sitter_found", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                if(posting.getPhoto_request() == null){
                                    posting.setPhoto_request(false);
                                }
                                if(posting.getLocation_request() == null){
                                    posting.setLocation_request(false);
                                }
                                if (posting.getPhoto_request()) {
                                    postingsPhoto.add(posting);
                                    idsPhoto.add(document.getId());
                                }
                                if(posting.getLocation_request()){
                                    postingsLocation.add(posting);
                                    idsLocation.add(document.getId());
                                }
                            }
                            firestoreCallback.onCallback(postingsPhoto , postingsLocation);

                        }
                    }
                });
    }

    private void setupRecyclers() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        adapterPhoto = new SumbitRequestsPhotoAdapter(postingsPhoto, getContext(), JobsAgreedFragment.this);
        adapterLocation = new SumbitRequestsLocationAdapter(postingsLocation, getContext(), JobsAgreedFragment.this);
        photoRecyclerView.setLayoutManager(layoutManager);
        locationRecyclerView.setLayoutManager(layoutManager2);
        photoRecyclerView.setAdapter(adapterPhoto);
        locationRecyclerView.setAdapter(adapterLocation);

    }


    private interface FirestoreCallback {
        void onCallback(ArrayList<Posting> tempPostingsPhoto , ArrayList<Posting> tempPostingLocation);
    }

    //On item click for the first recycler (location)
    @Override
    public void onItemClick(final int position) {
        Intent intent = new Intent(getContext(), SendRequestedLocationActivity.class);
        intent.putExtra("id" , idsLocation.get(position));
        startActivity(intent);
    }

    //On item click for the second recycler (photo)
    @Override
    public void onItemClick2(final int position) {
        Intent intent = new Intent(getContext(), SendRequestedPhotoActivity.class);
        intent.putExtra("id" , idsPhoto.get(position));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        postingsLocation.clear();
        postingsPhoto.clear();
        loadPostings(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<Posting> tempPostingsPhoto , ArrayList<Posting> tempPostingsLocation) {
                postingsLocation = tempPostingsLocation;
                postingsPhoto = tempPostingsPhoto;
                setupRecyclers();
            }
        });

    }
}
