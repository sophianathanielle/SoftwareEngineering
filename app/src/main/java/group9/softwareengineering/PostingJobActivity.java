package group9.softwareengineering;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PostingJobActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int DATE_DIALOG = 0;
    int TIME_DIALOG = 0;
    private TextView startDate;
    private TextView startTime;
    private TextView endDate;
    private TextView endTime;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_job);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inflate TextViews
        TextView startDate = (TextView) findViewById(R.id.startDate);
        TextView startTime = (TextView) findViewById(R.id.startTime);
        TextView endDate = (TextView) findViewById(R.id.endDate);
        TextView endTime = (TextView) findViewById(R.id.endTime);
    }

    public void clickSD(View view) {
        Calendar c = Calendar.getInstance();
        DATE_DIALOG = 0;
        datePickerDialog = new DatePickerDialog(PostingJobActivity.this, this, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void clickST(View view) {
        Calendar c = Calendar.getInstance();
        TIME_DIALOG = 0;
        timePickerDialog = new TimePickerDialog(PostingJobActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, this, c.get(c.HOUR), c.get(c.MINUTE), false);
        timePickerDialog.show();
    }

    public void clickED(View view) {
        Calendar c = Calendar.getInstance();
        c.add(c.DAY_OF_MONTH, 7);
        DATE_DIALOG = 1;
        datePickerDialog = new DatePickerDialog(PostingJobActivity.this, this, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void clickET(View view) {
        Calendar c = Calendar.getInstance();
        TIME_DIALOG = 1;
        timePickerDialog = new TimePickerDialog(PostingJobActivity.this, TimePickerDialog.THEME_HOLO_LIGHT,this, c.get(c.HOUR), c.get(c.MINUTE), false);
        timePickerDialog.show();
    }

//    public void clickLocation(View view) {
//        Intent intent = new Intent(this, MapsActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEEMMMMdyyyy");
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        if (DATE_DIALOG == 0) {
            TextView startDate = (TextView) PostingJobActivity.this.findViewById(R.id.startDate);
            startDate.setText(format.format(c.getTime()));
        }
        if (DATE_DIALOG == 1) {
            TextView endDate = (TextView) PostingJobActivity.this.findViewById(R.id.endDate);
            endDate.setText(format.format(c.getTime()));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (TIME_DIALOG == 0) {
            TextView startTime = (TextView) PostingJobActivity.this.findViewById(R.id.startTime);
            startTime.setText(hourOfDay + ": " + minute);
        }
        if (TIME_DIALOG == 1) {
            TextView endTime = (TextView) PostingJobActivity.this.findViewById(R.id.endTime);
            endTime.setText(hourOfDay + ": " + minute);
        }
    }
}
