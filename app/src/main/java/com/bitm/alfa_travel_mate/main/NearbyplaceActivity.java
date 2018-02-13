package com.bitm.alfa_travel_mate.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bitm.alfa_travel_mate.R;


public class NearbyplaceActivity extends AppCompatActivity {


    Toolbar toolbar;
    MapsActivity mapsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearbyplace);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mapsActivity = new MapsActivity();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //fragmentTransaction.add(R.id.container,mapsActivity,"mm");


        //  fragmentTransaction.add(R.id.toppan,blankFragment,"mm");
        //  fragmentTransaction.add(R.id.bottompan,blankFragment2,"nn");
        fragmentTransaction.commit();


    }


}
