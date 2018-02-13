package com.bitm.alfa_travel_mate.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.model.EventExpence;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;

public class AddExpence extends AppCompatActivity {

    Button saveBtn,deleteBtn;
    Toolbar toolbar;
    TextView dateTv;
    TextView timeTv;
    TextView detailsTv;
    EventExpence eventExpence;
    TextView amountTv;
    String date,time,details,event_id,expenseId;
    String amount;
    java.util.Calendar calendar;
    DatabaseReference myRef;
    java.text.SimpleDateFormat simpledateformat,simpledateformat1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adde);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Expense Add");


        myRef= FirebaseDatabase.getInstance().getReference().child(Utils.FIRE_EXPENSES);
        dateTv = (TextView) findViewById(R.id.dateTv);
        timeTv = (TextView) findViewById(R.id.timeTv);
        detailsTv = (TextView) findViewById(R.id.detailsEt);
        amountTv = (TextView) findViewById(R.id.amountEt);
        saveBtn = (Button)findViewById(R.id.btn_save);
        deleteBtn = (Button)findViewById(R.id.btn_delete);
        deleteBtn.setVisibility(View.GONE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpence();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(expenseId)){
                    deleteExpense();
                }

            }
        });
        Intent intent = getIntent();
        eventExpence = (EventExpence)intent.getSerializableExtra("expense");
        if(eventExpence != null){
            expenseId = eventExpence.getExpenceId();
            event_id= eventExpence.getEventId();
            detailsTv.setText(eventExpence.getDetails());
            amountTv.setText(eventExpence.getAmount());
            dateTv.setText(eventExpence.getDate());
            timeTv.setText(eventExpence.getTime());
            deleteBtn.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Expense Update");
            saveBtn.setText("Update");
        }
        else {
            event_id= intent.getStringExtra("event_id");
        }

        simpledateformat = new java.text.SimpleDateFormat("HH:mm a");
        simpledateformat1 = new java.text.SimpleDateFormat("dd/MM/yyyy");

        calendar = java.util.Calendar.getInstance(Locale.getDefault());

        date= simpledateformat1.format(calendar.getTime());

        time = simpledateformat.format(calendar.getTime());

        dateTv.setText(date);
        timeTv.setText(time);





    }

    private void deleteExpense() {

        AlertDialog.Builder alert = new AlertDialog.Builder(AddExpence.this);
        alert.setTitle("Delete Event???");
        alert.setMessage("Are you sure to delete the expence? ");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Query eventQuery = myRef.orderByChild("expenceId").equalTo(expenseId);

                eventQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                            eventSnapshot.getRef().removeValue();
                        }
                        startActivity(new Intent(AddExpence.this,EventDescription.class).putExtra("event_id",event_id).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        alert.setNegativeButton("Cancel",null);
        alert.show();
    }

    public void saveExpence() {
        if(!TextUtils.isEmpty(expenseId)){
            updateExpense();
        }
        else{
            details = detailsTv.getText().toString();
            amount = amountTv.getText().toString();

            if(TextUtils.isEmpty(details)){
                Toast.makeText(AddExpence.this, "details information is required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(amount)){
                Toast.makeText(AddExpence.this, "Amount is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            expenseId = myRef.push().getKey();
            eventExpence = new EventExpence(expenseId,event_id,details,amount,date,time);
            myRef.child(expenseId).setValue(eventExpence);
            startActivity(new Intent(AddExpence.this,EventDescription.class)
                    .putExtra("event_id",event_id).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }


      //  Toast.makeText(AddExpence.this, " "+date+"   "+time+"  "+getIntent().getStringExtra("event_id"), Toast.LENGTH_SHORT).show();


    }

    private void updateExpense() {
        details = detailsTv.getText().toString();
        amount = amountTv.getText().toString();

        if(TextUtils.isEmpty(details)){
            Toast.makeText(AddExpence.this, "details information is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(amount)){
            Toast.makeText(AddExpence.this, "Amount is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        eventExpence = new EventExpence(expenseId,event_id,details,amount,date,time);
        myRef.child(expenseId).setValue(eventExpence);
        startActivity(new Intent(AddExpence.this,EventDescription.class)
                .putExtra("event_id",event_id).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
              onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddExpence.this,EventDescription.class).putExtra("event_id",event_id).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();

    }
}
