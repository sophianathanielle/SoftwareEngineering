package group9.softwareengineering;


import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.firebase.ui.auth.AuthUI.TAG;

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


}
