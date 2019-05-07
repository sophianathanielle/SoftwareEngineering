package group9.softwareengineering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class sendphoto extends AppCompatActivity {

    private Button mUploadBtn;
    private ImageView mImageView;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendphoto);
        mStorage = FirebaseStorage.getInstance().getReference(); // storage for firebase


    mUploadBtn =(Button) findViewById(R.id.Upload);
    mImageView = (ImageView) findViewById(R.id.imageView);
    mProgress = new ProgressDialog(this);

    mUploadBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);

             }
         });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            mProgress.setMessage("Uploading Image ...");
            mProgress.show();

            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("photo").child(uri.getLastPathSegment()); // This is the file name and then the file path.

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();


// get download URL////

                    Toast.makeText(sendphoto.this, "Uploading Finished ...", Toast.LENGTH_LONG).show();

                }
            });


        }
    }
}