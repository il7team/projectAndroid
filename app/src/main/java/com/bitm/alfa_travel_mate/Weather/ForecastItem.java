package com.bitm.alfa_travel_mate.Weather;



import com.bitm.alfa_travel_mate.R;

import java.util.ArrayList;

/**
 * Created by Jibunnisa on 12/4/2016.
 */

public class ForecastItem {

    private String date;

    private String day;

    private String condition;

    private String high;

    private String low;

    private String humidity;

    private int Image;

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public ArrayList<ForecastItem> getForecastItems(){



        ArrayList<ForecastItem> items = new ArrayList<>();

        String []date = {"22 june 2017","23 june 2017","24 june 2017","25 june 2017",};
        String []day = {"monday","tuesday","wednesday","thrusday"};
        String []condition = {"sunny","rainy","foggy","snow"};
        String []humidity = {"45","33","44","55"};
        String []high = {"45","33","44","55"};
        String []low = {"45","33","44","55"};
        int []image = {R.drawable.cloudy_condition,R.drawable.rain_sunny_condition,R.drawable.lightening_rain_condition,R.drawable.snowy_condition};


        for(int i=0;i<date.length;i++){
            ForecastItem forecastItem = new ForecastItem();
            forecastItem.setDate(date[i]);
            forecastItem.setDay(day[i]);
            forecastItem.setHumidity(humidity[i]);
            forecastItem.setHigh(high[i]);
            forecastItem.setCondition(condition[i]);
            forecastItem.setLow(low[i]);
            forecastItem.setImage(image[i]);

            items.add(i,forecastItem);
        }


        return  items;
    }



}
