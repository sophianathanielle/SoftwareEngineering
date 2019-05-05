
package group9.softwareengineering;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.w3c.dom.Text;
import java.util.ArrayList;




public class UserProfile extends AppCompatActivity {


    private ArrayList<Pet> petsArrayList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView UserName, UserSurname, UserEmail, UserPhoneNumber, UserAddress, UserBio, usersPets;
    private ImageView ProfilePicture;
    private RecyclerView UserPets;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FloatingActionButton fabUpdate;

    private Profile user;
    private Pet pet;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UserName = (TextView) findViewById(R.id.Users_Name);
        UserSurname = (TextView) findViewById(R.id.Users_Surname);
        UserEmail = (TextView) findViewById(R.id.Users_Email);
        UserPhoneNumber = (TextView) findViewById(R.id.Users_phoneNumber);
        UserAddress = (TextView) findViewById(R.id.Users_Address);
        UserBio = (TextView) findViewById(R.id.Users_bio);
        ProfilePicture = (ImageButton) findViewById(R.id.userProfile);
        usersPets = (TextView) findViewById(R.id.usersPets);


        fabUpdate = (FloatingActionButton) findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatesDatabase();
                Intent intent = new Intent(getApplicationContext(), UsersProfile.class);
                startActivity(intent);
            }
        });
    }


        private void updatesDatabase() {
            db.collection("profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        user.setName(UserName.toString());
                        user.setPhone_number(UserPhoneNumber.toString());
                        //user.setLocation(editAddress.toString());
                        user.setBio(UserBio.toString());
                        //user.setPicture(editPicture);
                        pet.setName(usersPets.toString());
                        //pet.setPicture(editPetPicture);
                    } else {
                        Log.d("FB", "Error updating your info: ", task.getException());
                    }
                }
            });

        }


    }


























