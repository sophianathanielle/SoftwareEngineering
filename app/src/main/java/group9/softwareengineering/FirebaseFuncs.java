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
    // get DocumentReference of a Profile
    private DocumentReference getProfileDR (String uid) {
        return db.collection("profiles").document(uid);
    }

    public Profile getCurrentProfile() {
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

    public Profile getProfile(String uid) {
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
    public void updateProfile(Profile profile) {
        getProfileDR(currentUser.getUid()).set(profile);
    }

    // POSTINGS
    private DocumentReference getPostingDR(String posting_id) {
        return db.collection("postings").document(posting_id);
    }

    // Postings I have created
    public List<Posting> getMyPostings() {
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

    public void updatePosting(Posting posting) {
        getPostingDR(posting.getID()).set(posting);
    }

    public void createPosting(Posting posting) {
        db.collection("postings").document().set(posting); }

    public void deletePosting(String id) {
        getPostingDR(id).delete();
    }

    public List<Posting> getOtherPostings() {
        final ArrayList<Posting> postings = new ArrayList<>();

        db.collection("postings")
                .whereEqualTo("completed", false)
                .whereEqualTo("sitter_found", null)
                .whereGreaterThan("poster_id", currentUser.getUid())
                .whereLessThan("poster_id",currentUser.getUid())
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

    // REQUESTS
    private DocumentReference getRequestDR(String posting_id, String uid) {
        return db.collection("postings").document(posting_id).collection("requests").document(uid);
    }

    //  - sitter
    private DocumentReference getHistoryDR(String posting_id, String uid) {
        return db.collection("profiles").document(uid).collection("history").document(posting_id);
    }

    public List<Posting> getRequestedJobs() {
        final ArrayList<Posting> postings = new ArrayList<>();

        db.collection("profiles")
                .document(currentUser.getUid())
                .collection("history")
                .whereEqualTo("accepted", false)
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

    // perhaps in future, instead of creating docs with this preset data, create Cloud Funcs to populate these docs on creation
    public void createRequest(Posting posting) {
        Map<String, Object> data = new HashMap<>();
        data.put("accepted", false);
        data.put("declined", false);
        Map<String, Object> histdata = new HashMap<>();
        histdata.put("accepted", false);
        histdata.put("reviewed", false);
        getHistoryDR(posting.getID(), currentUser.getUid()).set(histdata);
        getRequestDR(posting.getID(), currentUser.getUid()).set(data);
    }

    public void cancelRequest(Posting posting) {
        getRequestDR(posting.getID(), currentUser.getUid()).delete();
        getHistoryDR(posting.getID(), currentUser.getUid()).delete();
    }

    // - owner/poster
    public Map<String, Profile> getRequestsForPosting (Posting posting) {
        final HashMap<String, Profile> profileIDs = new HashMap<>();

        db.collection("postings")
                .document(posting.getID())
                .collection("requests")
                .whereEqualTo("accepted", false)
                .whereEqualTo("declined", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getId();
                                profileIDs.put(uid, getProfile(uid));
                            }
                        } else {
                            Log.d("FB", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return profileIDs;
    }

    public void acceptRequest(Posting posting, String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("accepted", true);
        Map<String, Object> data1 = new HashMap<>();
        data1.put("sitter_found", uid);
        getPostingDR(posting.getID()).set(data1);
        getRequestDR(posting.getID(), uid).set(data);
        getHistoryDR(posting.getID(), uid).set(data);
    }

    public void declineRequest(Posting posting, String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("declined", true);
        getRequestDR(posting.getID(), uid).set(data);
        getHistoryDR(posting.getID(), uid).delete();
    }

    // PETS
    // used to get pet info from ID
    public Pet getPet(String petID) {
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

    private void createPet(Pet pet){
        db.collection("pets").add(pet);
    }

    private DocumentReference getPetDR(String pet_id) {
        return db.collection("pets").document(pet_id);
    }

    private void updatePet(Pet pet){
        getPetDR(pet.getID()).set(pet);
    }

    private void deletePet(Pet pet) {
        getPetDR(pet.getID()).delete();
    }

    // REVIEW
    // used to get review info from ID
    private Review getReview (String Uid) {
        final Review[] review = new Review[1];
        DocumentReference reviewRef = db.collection("reviews").document(Uid);

        reviewRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                review[1] = documentSnapshot.toObject(Review.class);
            }
        });

        return review[1];
    }

    private void createReview(Review review){
        db.collection("reviews").add(review);
    }
    private DocumentReference getReviewDR(String review_id) {
        return db.collection("reviews").document(review_id);
    }

    private void updateReview(Review review){
        getReviewDR(currentUser.getUid()).set(review);
    }

    private void deleteReview(Review review){
        getReviewDR(currentUser.getUid()).delete();
    }
}
