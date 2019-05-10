package group9.softwareengineering;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditMyPostedJob extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView jobDescription, jobLocation, startDate,startTime,endDate,endTime, jobPrice;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String documentId;
    private Posting posting;
    private ArrayList<Pet> pets;

    int DATE_DIALOG = 0;
    int TIME_DIALOG = 0;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_posted_job);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        posting = getIntent().getParcelableExtra("posting");

        jobDescription = findViewById(R.id.jobDescription);
        jobLocation = findViewById(R.id.jobLocation);
        jobPrice = findViewById(R.id.jobPrice);
        TextView startDate = (TextView) findViewById(R.id.startDate);
        TextView startTime = (TextView) findViewById(R.id.startTime);
        TextView endDate = (TextView) findViewById(R.id.endDate);
        TextView endTime = (TextView) findViewById(R.id.endTime);
        this.documentId = getIntent().getStringExtra("ids");
        jobDescription.setText(posting.getDescription());
        jobPrice.setText(String.valueOf(posting.getPayment()));
        jobLocation.setText(posting.getLocation().toString());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatesDatabase();

            }
        });
    }

    private void updatesDatabase(){
        db.collection("postings").document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm");
                    Date start = new Date();
                    Date end = new Date();
                    try {
                        start = format.parse(startDate.getText().toString()+"T"+startTime.getText().toString());
                        end = format.parse(endDate.getText().toString()+"T"+endTime.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    posting.setDescription(jobDescription.toString());
                    posting.setStart_time(start);
                    posting.setEnd_time(end);
                    posting.setPayment(Integer.parseInt(jobPrice.toString()));
                    jobLocation.setText(posting.getLocation().toString());
                } else {
                    Log.d("FB", "Error updating your info: ", task.getException());
                }
            }

        });
    }
    public void clickSD(View view) {
        Calendar c = Calendar.getInstance();
        DATE_DIALOG = 0;
        datePickerDialog = new DatePickerDialog(EditMyPostedJob.this, this, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void clickST(View view) {
        Calendar c = Calendar.getInstance();
        TIME_DIALOG = 0;
        timePickerDialog = new TimePickerDialog(EditMyPostedJob.this, TimePickerDialog.THEME_HOLO_LIGHT, this, c.get(c.HOUR), c.get(c.MINUTE), false);
        timePickerDialog.show();
    }

    public void clickED(View view) {
        Calendar c = Calendar.getInstance();
        c.add(c.DAY_OF_MONTH, 7);
        DATE_DIALOG = 1;
        datePickerDialog = new DatePickerDialog(EditMyPostedJob.this, this, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void clickET(View view) {
        Calendar c = Calendar.getInstance();
        TIME_DIALOG = 1;
        timePickerDialog = new TimePickerDialog(EditMyPostedJob.this, TimePickerDialog.THEME_HOLO_LIGHT,this, c.get(c.HOUR), c.get(c.MINUTE), false);
        timePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEEMMMMdyyyy");
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        if (DATE_DIALOG == 0) {
            TextView startDate = (TextView) EditMyPostedJob.this.findViewById(R.id.startDate);
            startDate.setText(format.format(c.getTime()));

        }
        if (DATE_DIALOG == 1) {
            TextView endDate = (TextView) EditMyPostedJob.this.findViewById(R.id.endDate);
            endDate.setText(format.format(c.getTime()));

        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (TIME_DIALOG == 0) {
            TextView startTime = (TextView) EditMyPostedJob.this.findViewById(R.id.startTime);
            startTime.setText(hourOfDay + ": " + minute);
        }
        if (TIME_DIALOG == 1) {
            TextView endTime = (TextView) EditMyPostedJob.this.findViewById(R.id.endTime);
            endTime.setText(hourOfDay + ": " + minute);
        }
    }

}
