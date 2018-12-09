package com.example.mdjahirulislam.doobbi.view.schedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.authentication.RegistrationActivity;

import java.util.Calendar;

public class ScheduleSummaryActivity extends AppCompatActivity {

    private TextView dateTV;
    private TextView dayTV;
    private TextView timeTV;

    private long dateTimeMills;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_schedule_summary );

        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dateTV = findViewById( R.id.summaryScheduleDateTV );
        dayTV = findViewById( R.id.summaryScheduleDayTV );
        timeTV = findViewById( R.id.summaryScheduleTimeTV );


        dateTimeMills = getIntent().getLongExtra( "dateTimeMills", Calendar.getInstance().getTimeInMillis() );
        time = getIntent().getStringExtra( "time" );

        dateTV.setText( Functions.convertDate( dateTimeMills ) );
        dayTV.setText( Functions.convertDayFromMills( dateTimeMills ) );
        timeTV.setText( time );

    }

    public void goToScheduleListActivityFromScheduleSummary(View view) {
//        startActivity( new Intent( this, RegistrationActivity.class ));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ScheduleCalendarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this,ScheduleCalendarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();

//            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
