
package group9.softwareengineering;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;




public class ViewMyProfileActivity extends AppCompatActivity {

    private TextView name , payment , bio , phone , rating;
    private ImageView image_name , image_phone , image_bio , image_payment;
    private Button saveBtn , addPetBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private String bioString,nameString,phoneString;
    private int paymentInt;
    private Context context = this;
    private RecyclerView recyclerView;
    private ArrayList<Pet> pets = new ArrayList<>();
    private PetAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_user_profile);
        name = findViewById(R.id.name);
        payment = findViewById(R.id.payment);
        bio = findViewById(R.id.bio);
        phone = findViewById(R.id.phone);
        rating = findViewById(R.id.rating);
        image_name = findViewById(R.id.image_name);
        image_phone = findViewById(R.id.image_phone);
        image_bio = findViewById(R.id.image_bio);
        image_payment = findViewById(R.id.image_payment);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        loadProfile();
        imageListeners();
        saveBtn = findViewById(R.id.save);
        addPetBtn = findViewById(R.id.addPet);
        recyclerView = findViewById(R.id.recyclerView);
        setupRecyclerView();
        buttonListeners();
    }

    private void setupRecyclerView() {
        loadPets(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<Pet> tempPets) {
                pets = tempPets;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                adapter = new PetAdapter(pets ,getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void loadPets(final FirestoreCallback firestoreCallback) {
        db.collection("pets").whereEqualTo("owner_id" , currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Pet pet = document.toObject(Pet.class);
                        pets.add(pet);
                    }
                    firestoreCallback.onCallback(pets);

                }
                }
            });
        }

    private void buttonListeners() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("profile").document(currentUser.getUid())
                        .update("bio", bioString , "fee_ph" , paymentInt , "name" , nameString , "phone_number" , phoneString)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context,getString(R.string.successfully_updated),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , AddPetsActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void imageListeners() {
        image_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Name");
                final EditText input = new EditText(getApplicationContext());
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText(input.getText().toString());
                        nameString = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        image_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Payment");
                final EditText input = new EditText(getApplicationContext());
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("[0-9]+")) {
                            payment.setText(input.getText().toString());
                            paymentInt = Integer.parseInt(input.getText().toString());
                        } else Toast.makeText(context,getString(R.string.invalid_payment),Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        image_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Bio");
                final EditText input = new EditText(getApplicationContext());
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bio.setText(input.getText().toString());
                        bioString = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        image_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Phone");
                final EditText input = new EditText(getApplicationContext());
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("[0-9]+")) {
                            phone.setText(input.getText().toString());
                            phoneString = input.getText().toString();
                        } else Toast.makeText(context,getString(R.string.invalid_phone),Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void loadProfile() {
        db.collection("profile").document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                Profile user = document.toObject(Profile.class);
                name.setText(user.getName());
                nameString = user.getName();
                payment.setText(String.valueOf(user.getFee_ph()));
                paymentInt = user.getFee_ph();
                if(user.getBio() != null){
                    bio.setText(user.getBio());
                    bioString = user.getBio();
                }
                phone.setText(user.getPhone_number());
                phoneString = user.getPhone_number();
                rating.setText(String.valueOf(user.getRating()));
            }
        });
    }

    private interface FirestoreCallback {
        void onCallback(ArrayList<Pet> tempPets );
    }


}


























