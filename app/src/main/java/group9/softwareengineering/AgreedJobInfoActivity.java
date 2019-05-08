package group9.softwareengineering;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AgreedJobInfoActivity extends AppCompatActivity {


    private TextView jobDescription, jobLocation, jobStartTime, jobEndTime, jobPrice, petOwner, petSitter;
    private ImageView imageOwner, imageSitter;
    private String usersID, documentId;
    private Posting posting;
    private Profile profile;
    private ArrayList<String> petsID = new ArrayList<>();
    private ArrayList<Pet> pets = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreed_job_info);

        jobDescription = findViewById(R.id.jobDescription);
        jobLocation = findViewById(R.id.jobLocation);
        jobStartTime = findViewById(R.id.jobStartTime);
        jobEndTime = findViewById(R.id.jobEndTime);
        jobPrice = findViewById(R.id.jobPrice);
        petOwner = findViewById(R.id.petOwner);
        petSitter = findViewById(R.id.petSitter);

        imageOwner = findViewById(R.id.imageOwner);
        imageSitter = findViewById(R.id.imageSitter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        this.posting = getIntent().getParcelableExtra("posting");


        recyclerView = findViewById(R.id.myJobPetsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

    }


}