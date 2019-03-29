package group9.softwareengineering;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText inputEmail;
    Button reset;
    FirebaseAuth mAuth;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        inputEmail =(EditText)findViewById(R.id.emailForgot);
        reset = (Button) findViewById(R.id.reset_button);
        title = (TextView) findViewById(R.id.title);

        final String email= inputEmail.getText().toString();
         reset.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mAuth.sendPasswordResetEmail(email)
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(ForgotPasswordActivity.this,"Check your email for a reset link.",Toast.LENGTH_LONG).show();
                                 }else{
                                     Toast.makeText(ForgotPasswordActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                 }
                             }
                         });
             }
         });
    }
}
