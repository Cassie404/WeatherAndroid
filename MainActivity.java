package com.example.weatherinformationapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherinformationapp.Func.HomepageWeatherInfo;
import com.example.weatherinformationapp.R;
import com.example.weatherinformationapp.Func.HomepageJsonHandlerThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationListener {
    Button button_location;
    TextView textView_location;
    LocationManager locationManager;
    List<Address> addresses;
    public static String latitude = "22.316668";
    public static String longitude = "114.183334";
    ImageView weather_icon;
    private String TAG = "MainActivity";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //location
        textView_location = findViewById(R.id.current_location);
        button_location = findViewById(R.id.button_location);
            //Runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create method
                getLocation();
            }
        });

        //HomepageWeatherInfo, time&date
        LinearLayout itemView = findViewById(R.id.homepage);

        weather_icon = findViewById(R.id.weathericon);

        TextView today = itemView.findViewById(R.id.today);
        TextView time = itemView.findViewById(R.id.time);

        TextView instantTemp_avg = itemView.findViewById(R.id.instant_temp_avg);
        TextView todayhumidity = itemView.findViewById(R.id.humidity);
        TextView todaygust = itemView.findViewById(R.id.gust);
        TextView todayprecipitation = itemView.findViewById(R.id.precipitation);
        TextView instantTemp_up = itemView.findViewById(R.id.instant_temp_up);
        TextView instantTemp_down = itemView.findViewById(R.id.instant_temp_down);

        HomepageJsonHandlerThread jsonHandlerThread = new HomepageJsonHandlerThread();

        jsonHandlerThread.start();
        try {
            jsonHandlerThread.join();
//            Log.i(TAG, HomepageWeatherInfo.dataList.toString());
//            Log.i(TAG, HomepageWeatherInfo.dataList.get(0).values().toString());

            HashMap<String, String> home_data = HomepageWeatherInfo.dataList.get(0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentDateAndTime = sdf.format(new Date());
            today.setText(currentDateAndTime.split(" ")[0]);
            time.setText(currentDateAndTime.split(" ")[1]);
            instantTemp_avg.setText(home_data.get("temp_avg")+"°C");
            todayhumidity.setText(home_data.get("humidity").replaceAll("[\\[\\]\\(\\)]","") + "%");
            todaygust.setText(home_data.get("gust").replaceAll("[\\[\\]\\(\\)]","") + "km/h");
            todayprecipitation.setText(home_data.get("precipitation").replaceAll("[\\[\\]\\(\\)]","") + "mm");
            instantTemp_up.setText(home_data.get("temp_up").replaceAll("[\\[\\]\\(\\)]","") + "°C");
            instantTemp_down.setText(home_data.get("temp_down").replaceAll("[\\[\\]\\(\\)]","") + "°C");

        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e.getMessage());
        }


    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            String[] buff = address.split(",");
            textView_location.setText(buff[buff.length-3] + ',' + buff[buff.length-1]);
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            Log.i( TAG ,latitude + longitude);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }
    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


}