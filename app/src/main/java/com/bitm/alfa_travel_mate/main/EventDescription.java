package com.bitm.alfa_travel_mate.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.customAdapter.CustomViewPagerAdapter;
import com.bitm.alfa_travel_mate.customAdapter.EventAdapter;
import com.bitm.alfa_travel_mate.fragment.ExpenceList;
import com.bitm.alfa_travel_mate.fragment.MomentList;
import com.bitm.alfa_travel_mate.model.TravelEvent;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventDescription extends AppCompatActivity {



    Toolbar toolbar;
    TextView destinationTv;
    TextView fromTv;
    TextView toTv;
    TextView budgetTv;
    String eventid;
    String destination;

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    ViewPager viewPager;
    TabLayout tabLayout;
    CustomViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);


        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Description");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabview);

        viewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.getFragments(new ExpenceList(),"expense list");
        viewPagerAdapter.getFragments(new MomentList(),"moments");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



        Intent intent = getIntent();
        eventid=intent.getStringExtra("event_id");


        destinationTv = (TextView) findViewById(R.id.travelDestTv);
        fromTv = (TextView) findViewById(R.id.fromDateTv);
        toTv = (TextView) findViewById(R.id.toDateTv);
        budgetTv = (TextView) findViewById(R.id.estBudgetTv);

        firebaseAuth = FirebaseAuth.getInstance();
        final String userId  = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference(Utils.FIRE_EVENTS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TravelEvent travelEvent =  new TravelEvent();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    travelEvent = snapshot.getValue(TravelEvent.class);
                    if(travelEvent.getEventId().equals(eventid)){
                       destinationTv.setText(travelEvent.getTravelDestination());
                        fromTv.setText("From:\n"+travelEvent.getFromDate());
                        toTv.setText("To:\n"+travelEvent.getToDate());
                        budgetTv.setText("Est. Budget:\n"+travelEvent.getEstimateBudget());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
               onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EventDescription.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
}
