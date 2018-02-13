package com.bitm.alfa_travel_mate.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.databinding.ActivityLoginBinding;
import com.bitm.alfa_travel_mate.databinding.AddEventBinding;
import com.bitm.alfa_travel_mate.model.TravelEvent;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddEventActivity extends AppCompatActivity {

    AddEventBinding binding;

    DatabaseReference myRef;
    TravelEvent travelEvent;
    Calendar calendar;
    int year,month,day;
    SimpleDateFormat sdf;
    String  fromdate;
    String  todate;
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(AddEventActivity.this,R.layout.add_event);


        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Event");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        firebaseAuth = FirebaseAuth.getInstance();
        binding.saveEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String destination = binding.travelDestET.getText().toString();
                String budget = binding.estBudgetET.getText().toString();

                if(TextUtils.isEmpty(destination)){
                    Toast.makeText(AddEventActivity.this, "Destination is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(budget)){
                    Toast.makeText(AddEventActivity.this, "Estimated Budget is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fromdate)){
                    Toast.makeText(AddEventActivity.this, "From date is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(todate)){
                    Toast.makeText(AddEventActivity.this, "To date is required!", Toast.LENGTH_SHORT).show();
                    return;
                }


                String userId  = firebaseAuth.getCurrentUser().getUid();
                myRef= FirebaseDatabase.getInstance().getReference(Utils.FIRE_EVENTS);
                String eventId = myRef.push().getKey();
                travelEvent =new TravelEvent(eventId,userId,binding.travelDestET.getText().toString(),
                        binding.estBudgetET.getText().toString(),
                        fromdate,
                        todate);
                /*String accessKey = myRef.push().getKey();
                travelEvent.setAccessKey(accessKey);*/

                myRef.child(eventId).setValue(travelEvent);
                //myRef.push().setValue(travelEvent);
                Toast.makeText(AddEventActivity.this, "save successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddEventActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        startActivity(new Intent(AddEventActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }


    private DatePickerDialog.OnDateSetListener fromdateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            calendar.set(year,month,dayOfMonth);
            String newDate = sdf.format(calendar.getTime());
            fromdate = newDate;
            binding.fromDate.setText(newDate);
        }
    };
    private DatePickerDialog.OnDateSetListener todateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            calendar.set(year,month,dayOfMonth);
            String newDate = sdf.format(calendar.getTime());
            todate = newDate;
            binding.toDate.setText(newDate);
        }
    };

    public void selecttodate(View view) {
        calendar = Calendar.getInstance(Locale.getDefault());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(AddEventActivity.this,todateSet,year,month,day);
        dateDialog.show();
    }

    public void selectfromdate(View view) {
        calendar = Calendar.getInstance(Locale.getDefault());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(AddEventActivity.this,fromdateSet,year,month,day);
        dateDialog.show();
    }
}
