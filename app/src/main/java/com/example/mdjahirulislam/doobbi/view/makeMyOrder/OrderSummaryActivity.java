package com.example.mdjahirulislam.doobbi.view.makeMyOrder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mdjahirulislam.doobbi.R;

public class OrderSummaryActivity extends AppCompatActivity {

    private ImageView productIconIV;
    private ImageView closeItemIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_summary );
        productIconIV = findViewById( R.id.singleOrderItemIconIV );
        closeItemIV = findViewById( R.id.singleOrderItemCloseIV );



        productIconIV.setVisibility( View.VISIBLE );
        closeItemIV.setVisibility( View.VISIBLE );
    }
}
