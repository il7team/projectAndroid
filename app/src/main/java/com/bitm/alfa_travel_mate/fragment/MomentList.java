package com.bitm.alfa_travel_mate.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.customAdapter.EventExpenceAdapter;
import com.bitm.alfa_travel_mate.customAdapter.MomentAdapter;
import com.bitm.alfa_travel_mate.databinding.FragmentMomentList2Binding;
import com.bitm.alfa_travel_mate.main.AddMomentActivity;
import com.bitm.alfa_travel_mate.main.EventDescription;
import com.bitm.alfa_travel_mate.main.MomentActivity;
import com.bitm.alfa_travel_mate.model.EventExpence;
import com.bitm.alfa_travel_mate.model.TravelMoment;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MomentList extends Fragment {




    FragmentMomentList2Binding binding;
    private ArrayList<TravelMoment> moments;
    private MomentAdapter adapter;
    DatabaseReference dbRef;
    private TravelMoment moment;
    String eventid;
    DatabaseReference databaseReference;
    String eventName;
    public MomentList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_moment_list2, container, false);
        View view = binding.getRoot();
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        binding.momentListRV.setHasFixedSize(true);
        binding.momentListRV.setLayoutManager(new GridLayoutManager(getActivity(),2));
        Intent intent = getActivity().getIntent();
        eventid=intent.getStringExtra("event_id");
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
                    adapter = new MomentAdapter(getActivity(),moments);
                    binding.momentListRV.setAdapter(adapter);
                    adapter.setListener(new MomentAdapter.Listener() {
                        @Override
                        public void onClick(int position) {
                            moment = moments.get(position);
                            showUpdateDeleteDialog();
                            /*Intent momentIntent = new Intent(getApplicationContext(),AddMomentActivity.class);
                            momentIntent.putExtra("moment",moment);
                            startActivity(momentIntent);*/
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        binding.fabexpence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addMoment();
            }
        });
    }

    public void addMoment() {
        Intent momentIntent = new Intent(getContext(),AddMomentActivity.class);
        momentIntent.putExtra("event_id",eventid).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(momentIntent);

    }

    public void showMoment() {
        Intent momentListIntent = new Intent(getContext(),MomentActivity.class);
        momentListIntent.putExtra("event_id",eventid);
        startActivity(momentListIntent);

    }

    private void showUpdateDeleteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.moment_update_dialog, null);
        dialogBuilder.setView(dialogView);

        final ImageView momentImage = (ImageView) dialogView.findViewById(R.id.imageView);
        final EditText descriptionET = (EditText) dialogView.findViewById(R.id.descriptionET);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.momentUpdateBtn);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.momentDeleteBtn);
        if(moment != null){

            eventid= moment.getEventId();
            descriptionET.setText(moment.getDescription());
            Picasso.with(getActivity()).load(moment.getImageUrl()).fit().centerCrop().into(momentImage);

        }

        dialogBuilder.setTitle("Moment Modification");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = descriptionET.getText().toString().trim();
                moment.setDescription(description);
                if (!TextUtils.isEmpty(description)) {
                    updateMoment(moment);
                    b.dismiss();
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMoment(moment);
                b.dismiss();
            }
        });
    }
    private void deleteMoment(TravelMoment moment) {
        DatabaseReference momentItemDb = dbRef.child(moment.getMomentId());
        momentItemDb.removeValue();
        Toast.makeText(getActivity(), "Moment Deleted", Toast.LENGTH_LONG).show();
    }

    private void updateMoment(TravelMoment moment) {
        DatabaseReference momentItemDb = dbRef.child(moment.getMomentId());
        momentItemDb.setValue(moment);
        Toast.makeText(getActivity(), "Moment Updated", Toast.LENGTH_LONG).show();
    }

}
