package group9.softwareengineering;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class UsersProfile extends AppCompatActivity {
    private Button button; // button to change acitivies through intent

// Links This page to the xml page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile); // links the page to XML
       // Links the edit button on users pfoile to go to the edit page.
        button= (Button) findViewById(R.id.Edit_Profile_Btn); // finds the edit button with its ID
        button.setOnClickListener(new View.OnClickListener() { // When user clicks it should change activities
            @Override
            public void onClick(View v) {
                openEditUserProfileActivity(); // opens this page
            }
    });

    }
    public void  openEditUserProfileActivity(){
        Intent intent = new Intent(this, EditUserProfileActivity.class); // creates an intent to make the change possible
        startActivity(intent); // passes the intent to the activity
    }


}