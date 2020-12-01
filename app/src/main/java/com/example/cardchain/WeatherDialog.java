package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDialog extends Dialog{

    private TextView greeting;
    private TextView currentWeather;
    private TextView currentTemperature;
    private TextView highLow;
    private ImageView timePic;
    private ConstraintLayout lay;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    public WeatherDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_dialog);

        greeting = findViewById(R.id.greeting);
        currentWeather = findViewById(R.id.currentWeather);
        currentTemperature = findViewById(R.id.currentTempreture);
        highLow = findViewById(R.id.highLow);
        timePic = findViewById(R.id.timePic);
        lay= findViewById(R.id.weatherDia);
        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Date currentTime = Calendar.getInstance().getTime();
        if (4 <= currentTime.getHours() && currentTime.getHours() < 12) {
            greeting.setText("Good Morning");
            timePic.setImageResource(R.drawable.morning);
        } else if (12 <= currentTime.getHours() && currentTime.getHours() < 8) {
            greeting.setText("Good Afternoon");
            timePic.setImageResource(R.drawable.afternoon);
        } else {
            greeting.setText("Good Evening");
            timePic.setImageResource(R.drawable.evening);
        }
        new GetWeather("waterloo").execute("this will go to background");;
    }

    class GetWeather extends AsyncTask<String, Integer, String> {
        private String currentTemp;
        private String minTemp;
        private String maxTemp;
        private String weather;
        protected String city;

        GetWeather(String city) {
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            Log.i("onPreExecute", " is called");
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("doInBackground", " is called");
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + this.city + ",ca&APPID=79cecf493cb6e52d25bb7b7050ff723c&mode=xml&units=metric");
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setReadTimeout(10000);
                httpsURLConnection.setConnectTimeout(15000);
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.connect();
                InputStream in = httpsURLConnection.getInputStream();
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);
                    int type;
                    while ((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("temperature")) {
                                currentTemp = parser.getAttributeValue(null, "value");
                                minTemp = parser.getAttributeValue(null, "min");
                                maxTemp = parser.getAttributeValue(null, "max");
                            } else if (parser.getName().equals("weather")) {
                                weather = parser.getAttributeValue(null, "value");

                            }
                        }
                        parser.next();
                    }
                } finally {
                    httpsURLConnection.disconnect();
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return " do background ended";
        }


        @Override
        protected void onPostExecute(String a) {
            currentWeather.setText(weather);
            currentTemperature.setText(currentTemp + "°");
            highLow.setText("H: " + maxTemp + "°    L: "+ minTemp +"°");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}

