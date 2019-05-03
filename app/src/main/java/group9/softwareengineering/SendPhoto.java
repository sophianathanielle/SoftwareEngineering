package group9.softwareengineering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class SendPhoto extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234 ;
    private ImageView imageView;
    private Button buttonChoose, buttonupload;

    private Uri filePath; // To store image we need a Uri object

   /////// private StorageReference storageReference;///////

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendphoto);

      /////// storageReference = FirebaseStorage.getInstance().getReference();///////
        imageView = (ImageView) findViewById(R.id.imageView); // finds layout and assigns imageview to variable name
        buttonChoose = (Button) findViewById(R.id.buttonChoose);// finds layout and assigns button to variable name
        buttonChoose = (Button) findViewById(R.id.buttonUpload);// finds layout and assigns button to variable name
        buttonChoose.setOnClickListener(this);
        buttonupload.setOnClickListener(this);
    }

    /**
     * This method is called a showFileChooser
     * Intent is used not to open another page but to open a file chooser
     * When user goes to send photo on the app and clicks on the "choose" button. It will use this method.
     * We need to get the result of this intent. Which is why below we have overided the method
     */
    private void showFileChooser(){ // fileChooser method
        Intent intent = new Intent(); // This intent opens the file chooser
        intent.setType("image/*"); // This means we can collect all image files
        intent.setAction(Intent.ACTION_GET_CONTENT); //
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST); // Gave this a value, illustarted above
    }

    private void uploadFile(){

        // Below is in case the user has not selected any file. We will need to check the filepath to see if the user has or has not selected a file.
        if (filePath != null) { // open if statement
            // If the filepath is fine then we are going to add a progess Dialog to know when it is complete, making the application more user friendly
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading ... ");



            StorageReference riversRef = storageReference.child("images/PetLink_Image.jpg");// folder name is images and name of picture is PetLink_Image.jpg

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        // When the file is uploaded sucesfully we will obtain this message below
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        // If on the other hand an error occurs then the onFailure method will be executed
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<uploadTast.TastSnapshot>() {
                        @Override
                        public void onProgress(UploadTast.TaskSnapshot taskSnapshot){
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalBytesCount();
                            progressDialog.setMessage(((int) progress) + "% Uploaded...");
                        }
                    });
            ;

        }// close if statement
        else{
            // if the filepath is null then we will display an error toast
        }
    }


    // This method will start when closing off the intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
        filePath = data.getData(); // storing the Uri here, this means we have the selected file
            /**
             * Below is where the image is going to be located within the ImageView
             * The ImageView is going to take a Bitmap object
             */

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap); // We can choose an image and the image will be displayed inside the image view
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // implements onclickListeners
    public void onClick(View view) {

        if(view == buttonChoose) {
            // This will open the file chooser

            showFileChooser(); // This is the method called showFileChooser with the Intents. Explained more within the method

        }
        else if (view == buttonupload ){
            // upload file to firebase storage
            uploadFile();
        }

        }
    }

