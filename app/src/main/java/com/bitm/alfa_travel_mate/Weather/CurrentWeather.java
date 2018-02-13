package com.bitm.alfa_travel_mate.Weather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitm.alfa_travel_mate.R;

import pl.droidsonroids.gif.GifImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeather extends Fragment{


    TextView location_tv,day_tv,date_tv,sunrise_tv,sunset_tv,high_temp_tv,low_temp_tv,temperature_tv,condition_tv,humidity_tv;
    GifImageView condition_iv;
    GifImageView gifbackgroundiv;
    WeatherData weatherData;
    RelativeLayout relativeLayout;
    Bundle bundle;
    int code;


    public CurrentWeather() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = getArguments();

        location_tv = (TextView)getActivity().findViewById(R.id.location_tv);
        condition_iv = (GifImageView) getActivity().findViewById(R.id.condition);
        temperature_tv = (TextView) getActivity().findViewById(R.id.temperature_tv);
        date_tv = (TextView) getActivity().findViewById(R.id.date_tv);
        day_tv = (TextView) getActivity().findViewById(R.id.day_tv);
        sunrise_tv = (TextView) getActivity().findViewById(R.id.sunrise_tv);
        sunset_tv = (TextView) getActivity().findViewById(R.id.sunset_tv);
        high_temp_tv = (TextView) getActivity().findViewById(R.id.high_temp_tv);
        low_temp_tv = (TextView) getActivity().findViewById(R.id.low_temp_tv);
        condition_tv = (TextView) getActivity().findViewById(R.id.condition_tv);
        humidity_tv = (TextView) getActivity().findViewById(R.id.humidity_tv);
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.main_layout);


        condition_iv = (GifImageView) getActivity().findViewById(R.id.condition_iv);
        gifbackgroundiv = (GifImageView)getActivity().findViewById(R.id.gifbackground);

        Animation fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        relativeLayout.startAnimation(fadeout);

        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                relativeLayout.startAnimation(fadeIn);
                if(getArguments()!=null){
                    code=getArguments().getInt("code",444);
                    SetbackAndCon setbackAndCon=new SetbackAndCon();
                    setbackAndCon.setBackgroundAndCondition(code,gifbackgroundiv,condition_iv);
                    int high = (int) ((Double.parseDouble(bundle.getString("high"))-32)*0.55555);
                    int low  = (int) ((Double.parseDouble(bundle.getString("low"))-32)*0.55555);
                    int temp = (int) ((Double.parseDouble(bundle.getString("temp"))-32)*0.55555);
                    date_tv.setText(bundle.getString("date"));
                    day_tv.setText(bundle.getString("day"));
                    location_tv.setText(bundle.getString("location")+" , "+bundle.getString("country"));
                    sunrise_tv.setText(bundle.getString("sunrise"));
                    sunset_tv.setText(bundle.getString("sunset"));
                    high_temp_tv.setText(String .valueOf(high)+""+(char) 0x00B0+"C");
                    low_temp_tv.setText(String .valueOf(low)+""+(char)0x00B0 +"C");
                    temperature_tv.setText(String .valueOf(temp)+"" +(char)0x00B0+"C");
                    condition_tv.setText(bundle.getString("condition"));
                    humidity_tv.setText(bundle.getString("humidity"));

                }



            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });




    }






}
