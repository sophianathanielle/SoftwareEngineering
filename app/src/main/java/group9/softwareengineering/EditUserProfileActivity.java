package group9.softwareengineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditUserProfileActivity extends AppCompatActivity {


    //private String name, password, phone, address, bio;
    private TextView editInfo, editPicture, editPets;
    private EditText editName, editPassword, editPhone, editAddress, editBio, editPetName;
    private ImageButton profilePicture, petPicture;
    private FloatingActionButton fabUpdate;
    private Profile user;
    private Pet pet;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Pet> petsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editInfo = (TextView) findViewById(R.id.editInfo);
        editPicture = (TextView) findViewById(R.id.editPicture);
        editPets = (TextView) findViewById(R.id.editPets);

        editName = (EditText) findViewById(R.id.editName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPhone = (EditText) findViewById(R.id.editPassword);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editBio = (EditText) findViewById(R.id.editBio);
        editPetName = (EditText) findViewById(R.id.editPetName);

        profilePicture = (ImageButton) findViewById(R.id.userProfile);
        //petPicture = (ImageButton) findViewById(R.id.)

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        fabUpdate = (FloatingActionButton) findViewById(R.id.update);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatesDatabase();
                //Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                //startActivity(intent);
            }
        });



    }



    private void updatesDatabase() {
        db.collection("profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            user.setName(editName.toString());
                            user.setPassword(editPassword.toString());
                            user.setPhone_number(editPhone.toString());
                            //user.setLocation(editAddress.toString());
                            user.setBio(editBio.toString());
                            //user.setPicture(editPicture);
                            pet.setName(editPetName.toString());
                            //pet.setPicture(editPetPicture);
                        } else {
                            Log.d("FB", "Error updating your info: ", task.getException());
                        }
                    }
                });
    }


}
