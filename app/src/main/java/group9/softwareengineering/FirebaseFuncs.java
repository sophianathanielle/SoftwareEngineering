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
    private static final FirebaseFuncs ourInstance = new FirebaseFuncs();
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public static FirebaseFuncs getInstance() {
        return ourInstance;
    }

    private FirebaseFuncs() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("atestuser@test.com", "password"); // test user
        currentUser = mAuth.getCurrentUser();
    }

    // PROFILE
    private Profile getCurrentProfile() {
        final Profile[] profile = new Profile[1];

        DocumentReference profileRef = db.collection("profiles").document(currentUser.getUid());

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

        DocumentReference profileRef = db.collection("profiles").document(uid);

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
        db.collection("profiles").document(currentUser.getUid()).set(profile);
    }

    // POSTINGS
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
                                for (String petID: posting.getPetIDs()) {
                                    Pet pet = getPet(petID);
                                    posting.addPet(pet);
                                }
                                postings.add(posting);
                            }
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return postings;
    }


    private List<Posting> getOtherPostings() {
        final ArrayList<Posting> postings = new ArrayList<>();

        db.collection("postings")
                .whereEqualTo("in_progress", "0")
                .whereGreaterThan("poster_id", currentUser.getUid())
                .whereLessThan("poster_id",currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                for (String petID: posting.getPetIDs()) {
                                    Pet pet = getPet(petID);
                                    posting.addPet(pet);
                                }
                                postings.add(posting);
                            }
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return postings;
    }

    private void updatePosting(Posting posting) {
        db.collection("postings").document(posting.getID()).set(posting);
    }

    private void createPosting(Posting posting) {
        db.collection("postings").add(posting);
    }

    private void deletePosting(Posting posting) {
        db.collection("postings").document(posting.getID()).delete();
    }

    // REQUESTS
    private void createRequest(Posting posting) {
        Map<String, Object> data = new HashMap<>();
        data.put("accepted", false);
        db.collection("postings").document(posting.getID()).collection("reviews").document(currentUser.getUid()).set(data);
    }

    // PETS
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
