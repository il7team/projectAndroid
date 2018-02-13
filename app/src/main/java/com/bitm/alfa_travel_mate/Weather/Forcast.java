package com.bitm.alfa_travel_mate.Weather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitm.alfa_travel_mate.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Forcast extends Fragment {


    RecyclerView recyclerView;
    ForcastAdapter forcastAdapter;


    List<Forecast> forecasts;

    public Forcast() {
        // Required empty public constructor
    }

    public void setForecastItem(List<Forecast>forecasts){

            this.forecasts = forecasts;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forcast, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.forecast_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            forcastAdapter = new ForcastAdapter(forecasts,getActivity());




        recyclerView.setAdapter(forcastAdapter);
    }
}
