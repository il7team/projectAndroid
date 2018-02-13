package com.bitm.alfa_travel_mate.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.customAdapter.MomentAdapter;
import com.bitm.alfa_travel_mate.model.TravelMoment;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MomentActivity extends AppCompatActivity {

    private ArrayList<TravelMoment> moments;
    private MomentAdapter adapter;
    RecyclerView momentListRV;
    DatabaseReference dbRef;
    private TravelMoment moment;
    String eventid;
    String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);
        momentListRV = (RecyclerView) findViewById(R.id.momentListRV);
        momentListRV.setHasFixedSize(true);
        momentListRV.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        eventid=intent.getStringExtra("event_id");
        eventName = intent.getStringExtra("event_name");
        dbRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIRE_MOMENTS);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                moments = new ArrayList<TravelMoment>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    moment = snapshot.getValue(TravelMoment.class);
                    if(moment.getEventId().equals(eventid)) {
                        moments.add(moment);
                    };
                    adapter = new MomentAdapter(MomentActivity.this,moments);
                    momentListRV.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.moment_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.action_add){
            Intent momentIntent = new Intent(MomentActivity.this,AddMomentActivity.class);
            momentIntent.putExtra("event_id",eventid);
            momentIntent.putExtra("event_name",eventName);
            startActivity(momentIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
