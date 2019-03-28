package group9.softwareengineering;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseFuncs {
    private static FirebaseFuncs ourInstance;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ArrayList<Posting> postings = new ArrayList<>();


    public static FirebaseFuncs getInstance() {
        if (ourInstance == null) {
            ourInstance = new FirebaseFuncs();
        }
        return ourInstance;
    }

    private FirebaseFuncs() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("atestuser@test.com", "password"); // test user
        currentUser = mAuth.getCurrentUser();
    }

    // PROFILE
    // get DocumentReference of a Profile
    private DocumentReference getProfileDR (String uid) {
        return db.collection("profiles").document(uid);
    }

    private Profile getCurrentProfile() {
        final Profile[] profile = new Profile[1];

        DocumentReference profileRef = getProfileDR(currentUser.getUid());

        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profile[0] = documentSnapshot.toObject(Profile.class);
            }
        });

        return profile[0];
    }

    private Profile getProfile(String uid) {
        final Profile[] profile = new Profile[1];

        DocumentReference profileRef = getProfileDR(uid);

        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profile[0] = documentSnapshot.toObject(Profile.class);
            }
        });

        return profile[0];
    }

    // used to create and update
    private void updateProfile(Profile profile) {
        getProfileDR(currentUser.getUid()).set(profile);
    }

    // POSTINGS
    private DocumentReference getPostingDR(String posting_id) {
        return db.collection("postings").document(posting_id);
    }

    private List<Posting> getMyPostings() {
        final ArrayList<Posting> postings = new ArrayList<>();

        db.collection("postings")
                .whereEqualTo("poster_id", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                postings.add(posting);
                            }
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return postings;
    }


    public ArrayList<Posting> getOtherPostings() {

        db.collection("postings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("das", document.getId() + " => " + document.getData());
                                Posting posting = document.toObject(Posting.class);
                                postings.add(posting);
                                Log.i("postings", Integer.toString(postings.size()));
                            }
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.i("postings", Integer.toString(postings.size()));
        return postings;
    }

    private void updatePosting(Posting posting) {
        getPostingDR(posting.getID()).set(posting);
    }

    private void createPosting(Posting posting) {
        db.collection("postings").add(posting);
    }

    private void deletePosting(Posting posting) {
        getPostingDR(posting.getID()).delete();
    }

    // REQUESTS
    private DocumentReference getRequestDR(String posting_id, String uid) {
        return db.collection("postings").document(posting_id).collection("requests").document(uid);
    }

    //  - sitter
    private void createRequest(Posting posting) {
        Map<String, Object> data = new HashMap<>();
        data.put("accepted", false);
        getRequestDR(posting.getID(), currentUser.getUid()).set(data);
    }

    private void deleteRequest(Posting posting) {
        getRequestDR(posting.getID(), currentUser.getUid()).delete();
    }

    // - owner/poster
    private void acceptRequest(Posting posting, String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("accepted", true);
        getRequestDR(posting.getID(), uid).set(data);
    }

    private void declineRequest(Posting posting, String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("declined", true);
        getRequestDR(posting.getID(), uid).set(data);
    }

    // PETS
    // used to get pet info from ID
    private Pet getPet(String petID) {
        final Pet[] pet = new Pet[1];

        DocumentReference petRef = db.collection("pets").document(petID);

        petRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pet[0] = documentSnapshot.toObject(Pet.class);
            }
        });

        return pet[0];
    }
}
