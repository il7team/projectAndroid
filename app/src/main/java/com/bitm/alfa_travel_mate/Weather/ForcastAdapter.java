package com.bitm.alfa_travel_mate.Weather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bitm.alfa_travel_mate.R;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Jibunnisa on 12/4/2016.
 */

public class ForcastAdapter extends RecyclerView.Adapter<ForcastAdapter.DerpHolder> {

    List<Forecast> data ;
    LayoutInflater inflater;
    ClickedListener clickedListener;
    Context context;
    public ForcastAdapter(List<Forecast> data, Context context){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }
    public void setClickedListener(ClickedListener clickedListener){
        this.clickedListener = clickedListener;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.forecast_single_row,parent,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder holder, final int position) {

        final Forecast item = data.get(position);
        holder.date.setText(item.getDate());
        holder.day.setText(item.getDay());
        holder.condition.setText(item.getText());
        int high = (int) ((Double.parseDouble(item.getHigh())-32)*0.55555);
        int low  = (int) ((Double.parseDouble(item.getLow())-32)*0.55555);

        holder.high.setText(high+""+(char) 0x00B0+"C");
        holder.low.setText(low+""+(char) 0x00B0+"C");
        holder.humidity.setText(item.getHumidity()+" ");
        int code= Integer.parseInt(item.getCode());

        SetbackAndCon setbackAndCon = new SetbackAndCon();
        setbackAndCon.setCondition(code,holder.picture);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView date;
        TextView day;
        TextView condition;
        TextView humidity;
        TextView high;
        TextView low;
        GifImageView picture;
        View view;

        public DerpHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date_tv);
            day = (TextView) itemView.findViewById(R.id.day_tv);
            condition = (TextView) itemView.findViewById(R.id.condition_tv);
            humidity = (TextView) itemView.findViewById(R.id.humidity_tv);
            high = (TextView) itemView.findViewById(R.id.high_temp_tv);
            low = (TextView) itemView.findViewById(R.id.low_temp_tv);
            picture = (GifImageView) itemView.findViewById(R.id.imageView_condition);
            view = itemView.findViewById(R.id.forecast_recycler_view_single_row);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(clickedListener!=null){
                clickedListener.itemclick(v,getPosition());
            }
        }
    }
    public interface ClickedListener{
        public void itemclick(View view, int position);
    }
}
