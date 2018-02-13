package com.bitm.alfa_travel_mate.main;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.Weather.WeatherActivity;
import com.bitm.alfa_travel_mate.customAdapter.EventAdapter;

import com.bitm.alfa_travel_mate.model.TravelEvent;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    Toolbar toolbar;
    ListView eventListView;
    private ArrayList<TravelEvent> travelEvents;
    private TravelEvent travelEvent;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private EventAdapter eventAdapter;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    String  fromdate;
    String  todate;
    Calendar calendar;
    int year,month,day;
    TextView empty;
    SimpleDateFormat sdf;
    Button fromDateBtn;
    Button toDateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event List");

        eventListView = (ListView) findViewById(R.id.eventListView);
        registerForContextMenu(eventListView);
        empty = (TextView) findViewById(R.id.empty_view);

        firebaseAuth = FirebaseAuth.getInstance();
        final String userId  = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference(Utils.FIRE_EVENTS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                travelEvents =  new ArrayList<TravelEvent>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    travelEvent = snapshot.getValue(TravelEvent.class);
                    if(travelEvent.getUserId().equals(userId)){
                        travelEvents.add(travelEvent);
                    }
                }
                eventAdapter = new EventAdapter(HomeActivity.this, travelEvents);
                eventListView.setAdapter(eventAdapter);
                eventListView.setEmptyView(empty);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                startActivity(new Intent(HomeActivity.this,EventDescription.class)
                        .putExtra("event_id",travelEvents.get(position).getEventId()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                return false;
            }
        });



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,AddEventActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Weather:
                startActivity(new Intent(HomeActivity.this,WeatherActivity.class));
                break;
            case R.id.Nearby:
                startActivity(new Intent(HomeActivity.this,MapsActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.eventListView) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.contextmenu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:
                deleteEvent(info.position);
                return true;
            case R.id.update:
                 updateEvent(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateEvent(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_event, null);

        dialogBuilder.setView(dialogView);

        final EditText travelDestET = (EditText) dialogView.findViewById(R.id.travelDestET);
        final EditText estBudgetET = (EditText) dialogView.findViewById(R.id.estBudgetET);
        fromDateBtn = (Button) dialogView.findViewById(R.id.fromDate);
        toDateBtn = (Button) dialogView.findViewById(R.id.toDate);
        ImageView cancelbtn = (ImageView) dialogView.findViewById(R.id.cancel_button);
        final Button updateEventBtn = (Button) dialogView.findViewById(R.id.updateEventBtn);


        travelEvent = travelEvents.get(position);
        if(travelEvent != null){

            travelDestET.setText(travelEvent.getTravelDestination());
            estBudgetET.setText(travelEvent.getEstimateBudget());
            fromDateBtn.setText(travelEvent.getFromDate());
            toDateBtn.setText(travelEvent.getToDate());

        }
        final AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        b.show();

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.dismiss();
            }
        });

        fromDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectfromdate();
            }


        });

        toDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecttodate();
            }


        });

        updateEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = travelDestET.getText().toString();
                String budget =estBudgetET.getText().toString();
                fromdate = fromDateBtn.getText().toString();
                todate = toDateBtn.getText().toString();
                if(TextUtils.isEmpty(destination)){
                    Toast.makeText(getApplicationContext(), "Destination is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(budget)){
                    Toast.makeText(getApplicationContext(), "Estimated Budget is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fromdate)){
                    Toast.makeText(getApplicationContext(), "From date is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(todate)){
                    Toast.makeText(getApplicationContext(), "To date is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String eventId = travelEvent.getEventId().toString();
                TravelEvent objEvent = new TravelEvent(eventId,travelEvent.getUserId(),destination,budget,fromdate,todate);
                DatabaseReference eventItemDb = databaseReference.child(eventId);
                eventItemDb.setValue(objEvent);
                Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
                b.dismiss();

            }
        });
    }

    private void selectfromdate() {
        calendar = Calendar.getInstance(Locale.getDefault());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(HomeActivity.this,fromdateSet,year,month,day);
        dateDialog.show();

    }
    private DatePickerDialog.OnDateSetListener todateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            calendar.set(year,month,dayOfMonth);
            String newDate = sdf.format(calendar.getTime());
            todate = newDate;
            toDateBtn.setText(newDate);

        }
    };
    private DatePickerDialog.OnDateSetListener fromdateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            calendar.set(year,month,dayOfMonth);
            String newDate = sdf.format(calendar.getTime());
            fromdate = newDate;
            fromDateBtn.setText(newDate);

        }
    };
    private void selecttodate() {
        calendar = Calendar.getInstance(Locale.getDefault());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(HomeActivity.this,todateSet,year,month,day);
        dateDialog.show();
    }
    public void deleteEvent(int position){
        final String id = travelEvents.get(position).getEventId().toString();
        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setTitle("Delete Event???");
        alert.setMessage("Are you sure to delete the event? ");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final Query eventQuery = ref.child(Utils.FIRE_EVENTS).orderByChild("eventId").equalTo(id);
                Query eventQuery1 = ref.child(Utils.FIRE_EXPENSES).orderByChild("eventId").equalTo(id);
                Query eventQuery2 = ref.child(Utils.FIRE_MOMENTS).orderByChild("eventId").equalTo(id);

                eventQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                            eventSnapshot.getRef().removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                eventQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot expenseSnapshot: dataSnapshot.getChildren()) {
                            expenseSnapshot.getRef().removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                eventQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot momentSnapshot: dataSnapshot.getChildren()) {
                            momentSnapshot.getRef().removeValue();
                        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
