package com.bitm.alfa_travel_mate.customAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.model.TravelMoment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nexttel_1 on 5/10/2017.
 */

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentViewHolder> {

    private TravelMoment objMoment;
    private Context context;
    private ArrayList<TravelMoment>moments;
    Listener mListener;
    public MomentAdapter(Context context, ArrayList<TravelMoment> moments){
this.context = context;
        this.moments = moments;
    }
    public static interface Listener{
        public void onClick(int position);
    }
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public MomentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.moment_list_row,parent,false);
        return new MomentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MomentViewHolder holder, final int position) {
        objMoment = moments.get(position);
        Picasso.with(context).load(objMoment.getImageUrl()).into(holder.momentIV);
        //holder.momentIV.setImageResource(R.mipmap.ic_launcher);
        holder.dateTV.setText(objMoment.getDate());
        holder.timeTV.setText(objMoment.getTime());
        holder.descriptionTV.setText(objMoment.getDescription());
        CardView cardView = holder.mCardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener != null){
                    mListener.onClick(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return moments.size();
    }

    public class MomentViewHolder extends RecyclerView.ViewHolder {
        private ImageView momentIV;
        private TextView descriptionTV;
        private TextView dateTV;
        private TextView timeTV;
        CardView mCardView;
        public MomentViewHolder(View itemView) {
            super(itemView);
            momentIV = (ImageView) itemView.findViewById(R.id.momentIV);
            descriptionTV = (TextView) itemView.findViewById(R.id.descrptionTV);
            dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            timeTV = (TextView) itemView.findViewById(R.id.timeTV);
            mCardView = (CardView) itemView;

        }
    }
}
