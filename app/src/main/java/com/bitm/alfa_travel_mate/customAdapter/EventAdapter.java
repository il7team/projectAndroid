package com.bitm.alfa_travel_mate.customAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.model.TravelEvent;

import java.util.ArrayList;


/**
 * Created by Jibunnisa on 5/7/2017.
 */

public class EventAdapter extends ArrayAdapter<TravelEvent> {

    Context context;
    ArrayList<TravelEvent> travelEvents;
    public EventAdapter(@NonNull Context context, ArrayList<TravelEvent> travelEvents) {
        super(context, R.layout.event_row_item, travelEvents);
        this.context = context;
        this.travelEvents = travelEvents;
    }
    private class ViewHolder {
        TextView destinationTv;
        TextView fromTv;
        TextView toTv;
        TextView remTv;
        TextView budgetTv;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_row_item, parent, false);
            viewHolder.destinationTv = (TextView) convertView.findViewById(R.id.travelDestTv);
            viewHolder.fromTv = (TextView) convertView.findViewById(R.id.fromDateTv);
            viewHolder.toTv = (TextView)convertView.findViewById(R.id.toDateTv);
            viewHolder.remTv = (TextView) convertView.findViewById(R.id.remBudgetTv);
            viewHolder.budgetTv = (TextView)convertView.findViewById(R.id.estBudgetTv);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.destinationTv.setText("Destination: "+travelEvents.get(position).getTravelDestination());
        viewHolder.fromTv.setText("From:\n"+travelEvents.get(position).getFromDate());
        viewHolder.toTv.setText("To:\n"+travelEvents.get(position).getToDate());
        viewHolder.budgetTv.setText("Est. Budget:\n"+travelEvents.get(position).getEstimateBudget()+" tk");
        viewHolder.remTv.setText("Remaining:\n"+travelEvents.get(position).getEstimateBudget()+" tk");
        return convertView;
    }
}
