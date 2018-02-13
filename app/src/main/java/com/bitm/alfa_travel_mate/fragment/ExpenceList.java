package com.bitm.alfa_travel_mate.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.customAdapter.EventExpenceAdapter;
import com.bitm.alfa_travel_mate.databinding.FragmentExpenceListBinding;
import com.bitm.alfa_travel_mate.main.AddExpence;
import com.bitm.alfa_travel_mate.main.EventDescription;
import com.bitm.alfa_travel_mate.model.EventExpence;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenceList extends Fragment {


    FragmentExpenceListBinding binding;
    EventExpence eventExpence;

    EventExpenceAdapter eventExpenceAdapter;
    ArrayList<EventExpence> eventExpences;
    DatabaseReference databaseReference;
    String event_id,destination,from,to,budget;
    public ExpenceList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_expence_list, container, false);
        View view = binding.getRoot();
        return view;



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        event_id = getActivity().getIntent().getStringExtra("event_id");

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.FIRE_EXPENSES);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                eventExpences = new ArrayList<EventExpence>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    eventExpence = snapshot.getValue(EventExpence.class);
                    if(eventExpence.getEventId().equals(event_id)){
                        eventExpences.add(eventExpence);
                    }

                }
                eventExpenceAdapter = new EventExpenceAdapter(getActivity(),eventExpences);

                binding.expenseLv.setAdapter(eventExpenceAdapter);
                binding.expenseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        eventExpence = eventExpences.get(position);
                        Intent expenseIntent = new Intent(getActivity(),AddExpence.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        expenseIntent.putExtra("expense",eventExpence);
                            startActivity(expenseIntent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        binding.fabexpence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpence();
            }
        });

    }


    public void addExpence() {

        startActivity(new Intent(getContext(),AddExpence.class).putExtra("event_id",event_id));
    }
    public void showExpence(View view) {
        startActivity(new Intent(getContext(), ExpenceList.class).putExtra("event_id",event_id));
    }
}
