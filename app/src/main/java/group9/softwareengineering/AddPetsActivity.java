package group9.softwareengineering;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;

public class AddPetsActivity extends AppCompatActivity {

    private EditText age, bio, breed, name, species;
    private Button saveBtn, uploadBtn;
    private ImageView petPhoto;
    private int ageInt = 0;
    private String bioString , breedString, nameString , speciesString, photoURL;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private Uri uri;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pets);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        age = findViewById(R.id.petAge);
        bio = findViewById(R.id.petBio);
        breed = findViewById(R.id.petBreed);
        name = findViewById(R.id.petName);
        species = findViewById(R.id.petSpecies);
        saveBtn = findViewById(R.id.save);
        petPhoto = findViewById(R.id.petPhoto);
        uploadBtn = findViewById(R.id.upload);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        buttonListener();

    }


    private void buttonListener() {
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(bio.getText().toString()) && !TextUtils.isEmpty(breed.getText().toString()) && !TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(species.getText().toString()) && petPhoto.getDrawable() != null) {
                    bioString = bio.getText().toString();
                    breedString = breed.getText().toString();
                    nameString = name.getText().toString();
                    speciesString = species.getText().toString();
                    ageInt = Integer.parseInt(age.getText().toString());
                        if (ageInt != 0 && bioString != null && breedString != null && nameString != null && speciesString != null && petPhoto.getDrawable() != null) {
                            uploadImage(new FirestoreCallback() {
                                @Override
                                public void onCallback(String photoUrl) {
                                    Pet pet = new Pet(nameString, ageInt, bioString, speciesString, breedString, photoURL, currentUser.getUid());
                                    db.collection("pets").add(pet);
                                    Toast.makeText(AddPetsActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),ViewMyProfileActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(AddPetsActivity.this, getString(R.string.invalid_fields), Toast.LENGTH_SHORT).show();
                        }
                } else  Toast.makeText(AddPetsActivity.this, getString(R.string.invalid_fields), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            petPhoto.setImageBitmap(photo);
            uri = getImageUri(getApplicationContext() , photo);
        }
    }

    private String getFileExternsion(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage(final FirestoreCallback firestoreCallback) {
        if (uri != null) {
            StorageReference fileReference = storageReference.child(String.valueOf(System.currentTimeMillis()) + "." + getFileExternsion(uri));
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },3000);
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                photoURL = task.getResult().toString();
                                firestoreCallback.onCallback(photoURL);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPetsActivity.this, getString(R.string.error_uploading), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
    }

    private Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(AddPetsActivity.this.getContentResolver(), photo, "Pet", null);
        return Uri.parse(path);
    }

    private interface FirestoreCallback {
        void onCallback(String photoUrl );
    }
}
