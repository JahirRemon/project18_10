package com.example.mdjahirulislam.doobbi.view.schedule;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;

import java.util.Calendar;

public class ScheduleCalendarActivity extends AppCompatActivity {

    private static final String TAG = ScheduleCalendarActivity.class.getSimpleName();


    private CalendarView scheduleCalendarCV;
    private TextView selectedDateTV;
    private TextView selectedTimeTV;
    private TextView scheduleDoneTV;
    private Calendar calendar;
    private long nextMonth;
    private long selectedTimeInMills;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_schedule_calendar );

//        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        scheduleCalendarCV = findViewById( R.id.scheduleCalendarCV );
        selectedDateTV = findViewById( R.id.selectedDateTV );
        selectedTimeTV = findViewById( R.id.selectedTimeTV );
        scheduleDoneTV = findViewById( R.id.scheduleDoneTV );

        calendar = Calendar.getInstance();
        nextMonth = 60 * 60 * 24 * 60;

        selectedDateTV.setText( calendar.get( Calendar.DAY_OF_MONTH ) + "." + (calendar.get( Calendar.MONTH ) + 1) + "." + calendar.get( Calendar.YEAR ) );
        selectedTimeInMills = calendar.getTimeInMillis();
        Log.d( TAG, "onCreate: " + selectedTimeInMills );
        scheduleCalendarCV.setMaxDate( calendar.getTimeInMillis() + nextMonth * 1000 );
        scheduleCalendarCV.setMinDate( calendar.getTimeInMillis() );
        scheduleCalendarCV.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                selectedDateTV.setText( i2 + "." + (i1 + 1) + "." + i );
//                Toast.makeText( ScheduleCalendarActivity.this, "" + calendarView.getDate(), Toast.LENGTH_SHORT ).show();
                Log.d( TAG, "onSelectedDayChange: " + calendarView.getDate() );
                selectedTimeInMills = Functions.convertMillsFromDMY( String.valueOf( selectedDateTV.getText() ) );
            }
        } );


    }

    public void goToScheduleTimeActivity(View view) {

        if (selectedTimeTV.getText().equals( "Select Time" )) {

            timePickerDialog();
        } else {
            startActivity( new Intent( this, ScheduleSummaryActivity.class )
                    .putExtra( "dateTimeMills", selectedTimeInMills )
                    .putExtra("time",selectedTimeTV.getText()) );
        }


    }

    public void selectTime(View view) {
        timePickerDialog();
    }

    private void timePickerDialog() {


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
        int minute = mcurrentTime.get( Calendar.MINUTE );
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                if (selectedHour < 12) {
                    selectedTimeTV.setText( selectedHour + ":" + selectedMinute + " AM" );
                } else if (selectedHour <= 12) {
                    selectedTimeTV.setText( selectedHour + ":" + selectedMinute + " PM" );
                } else {
                    selectedTimeTV.setText( selectedHour - 12 + ":" + selectedMinute + " PM" );
                }
                scheduleDoneTV.setText( "Done" );
            }
        }, hour, minute, false );//Yes 24 hour time
        mTimePicker.setTitle( "Select Time" );
        mTimePicker.show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected(item);
    }

}
