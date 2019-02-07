package com.example.mdjahirulislam.doobbi.view.offers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;

public class MyOfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_offer );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent( this, HomeActivity.class );
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( intent );

            finish();
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected( item );
    }
}
