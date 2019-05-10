package group9.softwareengineering;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SendRequestedPhotoActivity extends AppCompatActivity {

    private Button mUploadBtn , mCaptureBtn;
    private ImageView mImageView;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    private String id , photoURL;
    private Uri uri;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendphoto);
        mStorage = FirebaseStorage.getInstance().getReference(); // storage for firebase
        id = getIntent().getStringExtra("id");
        mUploadBtn = (Button) findViewById(R.id.upload);
        mCaptureBtn = (Button) findViewById(R.id.capture);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(new FirestoreCallback() {
                    @Override
                    public void onCallback(String photoUrl) {
                        photoURL = photoUrl;
                        db.collection("postings").document(id).update("photo_updates" , FieldValue.arrayUnion(photoUrl) , "photo_request" , false).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);
            uri = getImageUri(getApplicationContext() , photo);
            mUploadBtn.setEnabled(true);
            mUploadBtn.setVisibility(View.VISIBLE);
        }
    }

    private String getFileExternsion(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage(final SendRequestedPhotoActivity.FirestoreCallback firestoreCallback) {
        if (uri != null) {
            StorageReference fileReference = mStorage.child(String.valueOf(System.currentTimeMillis()) + "." + getFileExternsion(uri));
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
                    Toast.makeText(SendRequestedPhotoActivity.this, getString(R.string.error_uploading), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(SendRequestedPhotoActivity.this.getContentResolver(), photo, "Pet", null);
        return Uri.parse(path);
    }

    private interface FirestoreCallback {
        void onCallback(String photoUrl );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}