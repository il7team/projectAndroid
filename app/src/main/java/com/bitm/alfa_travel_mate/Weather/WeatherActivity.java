package com.bitm.alfa_travel_mate.Weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bitm.alfa_travel_mate.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Bundle myBundle;
    Geocoder geocoder;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    List<Address> addresses;
    WeatherAPI weatherAPI;
    String location_city="Dhaka";
    Forcast forecast;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    String CURRENT_FULL_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20%3D%202487889&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    String BASE_URL="https://query.yahooapis.com/";


    CurrentWeather currentWeather;
    GifImageView gifImageView;
    ViewPager viewPager;
    TabLayout tabLayout;
    RelativeLayout relativeLayout;
    ImageView weather_condition_image;
    ViewPagerAdapter viewPagerAdapter;
    Toolbar toolbar;
    Context context;
    EditText editText;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editText = (EditText) findViewById(R.id.search_place_et);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);



        gifImageView = (GifImageView) findViewById(R.id.gifbackground);

        relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        weather_condition_image = (ImageView)findViewById(R.id.condition);

        myBundle=null;

        geocoder = new Geocoder(this);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);




        //getcurrentlocationweather();
        getdefaultweater(location_city);

    }

    private void updateviewpager(Bundle myBundle,List<Forecast> forecasts){

        viewPagerAdapter.fragments.clear();
        viewPagerAdapter.titles.clear();
        currentWeather = new CurrentWeather();
        currentWeather.setArguments(myBundle);
        forecast = new Forcast();
        forecast.setForecastItem(forecasts);
        viewPagerAdapter.getFragments(currentWeather,"Current");
        viewPagerAdapter.getFragments(forecast,"Forecast");
        viewPager.getAdapter().notifyDataSetChanged();



    }


    private void populateviewpager(Bundle myBundle,List<Forecast> forecasts) {



        currentWeather = new CurrentWeather();
        currentWeather.setArguments(myBundle);
        forecast = new Forcast();
        forecast.setForecastItem(forecasts);
        viewPager = (ViewPager)findViewById(R.id.ProgramsViewPager);
        tabLayout = (TabLayout) findViewById(R.id.ProgramsTabLayout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.getFragments(currentWeather,"Current");
        viewPagerAdapter.getFragments(forecast,"Forecast");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.weather_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.current_place:

                getcurrentlocationweather();
                break;

            case R.id.search_location:


                String city= editText.getText().toString();
                if(city.equals("")) {
                    Toast.makeText(WeatherActivity.this,"Enter a city name", Toast.LENGTH_SHORT).show();
                }
                else{
                    getweather(editText.getText().toString());
                }



                break;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        getdefaultweater("dhaka");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        getdefaultweater("dhaka");
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses.size()>0){

            location_city=addresses.get(0).getLocality().toString();
            getweather(location_city);

        }
        googleApiClient.disconnect();
    }

    public void getdefaultweater(String location) {

        String query1  = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""+location+"\")";
        String query2 = "json";
        retrofit2.Call<WeatherData> getWeatherData = weatherAPI.getWeatherData(query1,query2);

        getWeatherData.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(retrofit2.Call<WeatherData> call, Response<WeatherData> response) {

                WeatherData weatherdata = response.body();

                myBundle = new Bundle();
                myBundle .putString("location",weatherdata.getQuery().getResults().getChannel().getLocation().getCity());
                myBundle .putString("country",weatherdata.getQuery().getResults().getChannel().getLocation().getCountry());
                myBundle .putInt("code", Integer.parseInt(weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getCode()));
                myBundle .putString("date", weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getDate().toString());
                myBundle .putString("temp", weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getTemp().toString());
                myBundle .putString("condition", weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getText().toString());
                myBundle .putString("day", weatherdata.getQuery().getResults().getChannel().getItem().getForecast().get(0).getDay().toString());
                myBundle .putString("high", weatherdata.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh().toString());
                myBundle .putString("low", weatherdata.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow().toString());
                myBundle .putString("humidity", weatherdata.getQuery().getResults().getChannel().getAtmosphere().getHumidity());
                myBundle .putString("sunrise", weatherdata.getQuery().getResults().getChannel().getAstronomy().getSunrise().toString());
                myBundle .putString("sunset", weatherdata.getQuery().getResults().getChannel().getAstronomy().getSunset().toString());
                List<Forecast> forecasts;
                forecasts =  weatherdata.getQuery().getResults().getChannel().getItem().getForecast();

                for(int i = 0; i<forecasts.size();i++){
                    forecasts.get(i).setHumidity(Integer.parseInt(weatherdata.getQuery().getResults().getChannel().getAtmosphere().getHumidity()));
                }

                populateviewpager(myBundle,forecasts);


            }

            @Override
            public void onFailure(retrofit2.Call<WeatherData> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "not responding", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void getweather(String location) {

        String query1  = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""+location+"\")";
        String query2 = "json";
        retrofit2.Call<WeatherData> getWeatherData = weatherAPI.getWeatherData(query1,query2);

        getWeatherData.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(retrofit2.Call<WeatherData> call, Response<WeatherData> response) {

                WeatherData weatherdata = response.body();

                if(weatherdata.getQuery().getCount()!=0){
                    myBundle = new Bundle();
                    myBundle .putString("location",weatherdata.getQuery().getResults().getChannel().getLocation().getCity());
                    myBundle .putString("country",weatherdata.getQuery().getResults().getChannel().getLocation().getCountry());
                    myBundle .putInt("code", Integer.parseInt(weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getCode()));
                    myBundle .putString("date", weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getDate().toString());
                    myBundle .putString("temp", weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getTemp().toString());
                    myBundle .putString("condition", weatherdata.getQuery().getResults().getChannel().getItem().getCondition().getText().toString());
                    myBundle .putString("day", weatherdata.getQuery().getResults().getChannel().getItem().getForecast().get(0).getDay().toString());
                    myBundle .putString("high", weatherdata.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh().toString());
                    myBundle .putString("low", weatherdata.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow().toString());
                    myBundle .putString("humidity", weatherdata.getQuery().getResults().getChannel().getAtmosphere().getHumidity());
                    myBundle .putString("sunrise", weatherdata.getQuery().getResults().getChannel().getAstronomy().getSunrise().toString());
                    myBundle .putString("sunset", weatherdata.getQuery().getResults().getChannel().getAstronomy().getSunset().toString());
                    List<Forecast> forecasts;
                    forecasts =  weatherdata.getQuery().getResults().getChannel().getItem().getForecast();

                    for(int i = 0; i<forecasts.size();i++){
                        forecasts.get(i).setHumidity(Integer.parseInt(weatherdata.getQuery().getResults().getChannel().getAtmosphere().getHumidity()));
                    }

                    updateviewpager(myBundle,forecasts);
                }
                else {
                    Toast.makeText(WeatherActivity.this, "No such city available", Toast.LENGTH_SHORT).show();
                }
                


            }

            @Override
            public void onFailure(retrofit2.Call<WeatherData> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "not responding", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void getcurrentlocationweather(){


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{mPermission}, REQUEST_CODE_PERMISSION);

                Toast.makeText(this, "location permission denied", Toast.LENGTH_SHORT).show();
                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
            else {
                googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addOnConnectionFailedListener(this)
                        .addConnectionCallbacks(this)
                        .build();
                googleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
