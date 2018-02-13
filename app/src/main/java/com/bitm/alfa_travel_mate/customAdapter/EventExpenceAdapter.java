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
import com.bitm.alfa_travel_mate.model.EventExpence;


import java.util.ArrayList;


/**
 * Created by Jibunnisa on 5/7/2017.
 */

public class EventExpenceAdapter extends ArrayAdapter<EventExpence> {

    Context context;
    ArrayList<EventExpence> eventExpences;
    public EventExpenceAdapter(@NonNull Context context, ArrayList<EventExpence> eventExpences) {
        super(context, R.layout.event_expence_list_row,eventExpences);
        this.context = context;
        this.eventExpences = eventExpences;
    }
    private class ViewHolder {
        TextView dateTv;
        TextView timeTv;
        TextView detailsTv;
        TextView amountTv;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_expence_list_row, parent, false);
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.dateTv);
            viewHolder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            viewHolder.detailsTv = (TextView)convertView.findViewById(R.id.detailsTv);
            viewHolder.amountTv = (TextView)convertView.findViewById(R.id.amountTv);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.dateTv.setText(eventExpences.get(position).getDate());
        viewHolder.timeTv.setText(eventExpences.get(position).getTime());
        viewHolder.detailsTv.setText(eventExpences.get(position).getDetails());
        viewHolder.amountTv.setText(eventExpences.get(position).getAmount());
        return convertView;
    }
}
