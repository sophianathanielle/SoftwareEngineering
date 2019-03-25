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
import java.util.List;

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

    private void updateProfile(Profile profile) {
        db.collection("profiles").document(currentUser.getUid()).set(profile);
    }

    private List<Posting> getPostings() {
        final ArrayList<Posting> postings = new ArrayList<>();

        db.collection("postings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Posting posting = document.toObject(Posting.class);
                                if (!posting.getOwner_id().equals(currentUser.getUid())) {
                                    for (String petID: posting.getPetIDs()) {
                                        Pet pet = getPet(petID);
                                        posting.addPet(pet);
                                    }
                                    postings.add(posting);
                                }
                            }
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return postings;
    }

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
