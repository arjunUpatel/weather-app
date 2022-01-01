package com.example.arjunpatelweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView currentTemperature;
    TextView currentWeather;
    ImageView currentWeatherImage;
    TextView currentDate;
    TextView currentTime;
    Button search;
    Button fahrenheit;
    Button celcius;

    URL currentURL;
    URLConnection currentConnection;
    InputStream currentStream;
    BufferedReader currentReader;
    String currentStuff;

    URL forecastURL;
    URLConnection forecastConnection;
    InputStream forecastStream;
    BufferedReader forecastReader;
    String forecastStuff;

    JSONObject currentData;
    JSONObject forecastData;

    TextView threeHourTemperature;
    ImageView threeHourImage;
    TextView threeHourDate;
    TextView threeHourTime;

    TextView sixHourTemperature;
    ImageView sixHourImage;
    TextView sixHourDate;
    TextView sixHourTime;

    TextView nineHourTemperature;
    ImageView nineHourImage;
    TextView nineHourDate;
    TextView nineHourTime;

    TextView twelveHourTemperature;
    ImageView twelveHourImage;
    TextView twelveHourDate;
    TextView twelveHourTime;

    TextView fifteenHourTemperature;
    ImageView fifteenHourImage;
    TextView fifteenHourDate;
    TextView fifteenHourTime;
    int go;

    TextView quoteText;
    ArrayList<String> quotes;
    String zipCode;
    boolean celsiusIsClicked;
    boolean fahrenheitIsClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celsiusIsClicked=false;
        fahrenheitIsClicked=true;
        quoteText=findViewById(R.id.quoteText);
        search=findViewById(R.id.searchButton);
        editText=findViewById(R.id.editText);
        fahrenheit=findViewById(R.id.fahrenheitButton);
        celcius=findViewById(R.id.celciusButton);
        currentTemperature=findViewById(R.id.currentTemperatureText);
        currentWeather=findViewById(R.id.currentWeatherText);
        currentWeatherImage=findViewById(R.id.currentWeatherImage);
        currentDate=findViewById(R.id.currentDateText);
        currentTime=findViewById(R.id.currentTimeText);

        threeHourTemperature=findViewById(R.id.threeHourTemperature);
        threeHourImage=findViewById(R.id.threeHourImage);
        threeHourDate=findViewById(R.id.threeHourDate);
        threeHourTime=findViewById(R.id.threeHourTime);

        sixHourTemperature=findViewById(R.id.sixHourTemperature);
        sixHourImage=findViewById(R.id.sixHourImage);
        sixHourDate=findViewById(R.id.sixHourDate);
        sixHourTime=findViewById(R.id.sixHourTime);

        nineHourTemperature=findViewById(R.id.nineHourTemperature);
        nineHourImage=findViewById(R.id.nineHourImage);
        nineHourDate=findViewById(R.id.nineHourDate);
        nineHourTime=findViewById(R.id.nineHourTime);

        twelveHourTemperature=findViewById(R.id.twelveHourTemperature);
        twelveHourImage=findViewById(R.id.twelveHourImage);
        twelveHourDate=findViewById(R.id.twelveHourDate);
        twelveHourTime=findViewById(R.id.twelveHourTime);

        fifteenHourTemperature=findViewById(R.id.fifteenHourTemperature);
        fifteenHourImage=findViewById(R.id.fifteenHourImage);
        fifteenHourDate=findViewById(R.id.fifteenHourDate);
        fifteenHourTime=findViewById(R.id.fifteenHourTime);

        quotes=new ArrayList();
        quotes.add("I am detecting precipitative activity");
        quotes.add("There are no clouds, only clear skies");
        quotes.add("I am a creature of the mist");
        quotes.add("Thunderstorm!");
        quotes.add("Turn up the snowstorm");
        quotes.add("Where will I make it rain!");
        quotes.add("Yo, the clouds are in charge!");
        quotes.add("Move those feet to the raindrop beat");
        quotes.add("The clouds speak to me");
        quotes.add("Sub-optimal temperature");
        zipCode=editText.getText().toString();//sent to async task
        new ZipCoder().execute(zipCode);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                zipCode=s.toString();
                Log.d("Tag",""+zipCode);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zipCode.length()==5) {
                    go=0;
                    new ZipCoder().execute(zipCode);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid zipcode", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class ZipCoder extends AsyncTask<String, Void, Void>{


        @Override
        protected Void doInBackground(String... strings) {

            String theZip=strings[0];
            try {
                currentURL= new URL("https://api.openweathermap.org/data/2.5/weather?zip="+theZip+"&appid=2a5b14a12cf980560efa1d69fbd24284");
                currentConnection = currentURL.openConnection();
                currentStream = currentConnection.getInputStream();
                currentReader=new BufferedReader(new InputStreamReader(currentStream));
                currentStuff = currentReader.readLine();

                forecastURL = new URL("https://api.openweathermap.org/data/2.5/forecast?zip="+theZip+"&appid=2a5b14a12cf980560efa1d69fbd24284");
                forecastConnection = forecastURL.openConnection();
                forecastStream = forecastConnection.getInputStream();
                forecastReader=new BufferedReader(new InputStreamReader(forecastStream));
                forecastStuff = forecastReader.readLine();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try{
                currentData = new JSONObject(currentStuff);
                forecastData = new JSONObject(forecastStuff);
            }catch(Exception e){

            }
            while(go==0){
                    try {
                        currentTemperature.setText(kelvinToFahrenheit(currentData.getJSONObject("main").get("temp").toString()));
                        currentWeatherImage.setImageResource(findAppropriateImage(currentData.getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        currentDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(currentData.get("dt").toString())))).substring(0, 10));
                        currentWeather.setText(currentData.getJSONArray("weather").getJSONObject(0).get("description").toString());
                        currentTime.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(currentData.get("dt").toString())))).substring(11, 22));

                        threeHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(0).getJSONObject("main").get("temp").toString()));
                        threeHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        threeHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(0).get("dt").toString())))).substring(0, 5));
                        threeHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(0).get("dt").toString())))).substring(11, 22)));

                        sixHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(1).getJSONObject("main").get("temp").toString()));
                        sixHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        sixHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(1).get("dt").toString())))).substring(0, 5));
                        sixHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(1).get("dt").toString())))).substring(11, 22)));

                        nineHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(2).getJSONObject("main").get("temp").toString()));
                        nineHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        nineHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(2).get("dt").toString())))).substring(0, 5));
                        nineHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(2).get("dt").toString())))).substring(11, 22)));

                        twelveHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(3).getJSONObject("main").get("temp").toString()));
                        twelveHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(3).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        twelveHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(3).get("dt").toString())))).substring(0, 5));
                        twelveHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(3).get("dt").toString())))).substring(11, 22)));

                        fifteenHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(4).getJSONObject("main").get("temp").toString()));
                        fifteenHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(4).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        fifteenHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(4).get("dt").toString())))).substring(0, 5));
                        fifteenHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(4).get("dt").toString())))).substring(11, 22)));

                        quoteText.setText(printAppropriateQuote(currentData.getJSONArray("weather").getJSONObject(0).get("icon").toString(), quotes));
                        go = 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        go = 0;
                }
                    try {
                        currentTemperature.setText(kelvinToFahrenheit(currentData.getJSONObject("main").get("temp").toString()));
                        currentWeatherImage.setImageResource(findAppropriateImage(currentData.getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        currentDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(currentData.get("dt").toString())))).substring(0, 10));
                        currentWeather.setText(currentData.getJSONArray("weather").getJSONObject(0).get("description").toString());
                        currentTime.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(currentData.get("dt").toString())))).substring(11, 22));

                        threeHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(0).getJSONObject("main").get("temp").toString()));
                        threeHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        threeHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(0).get("dt").toString())))).substring(0, 5));
                        threeHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(0).get("dt").toString())))).substring(11, 22)));

                        sixHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(1).getJSONObject("main").get("temp").toString()));
                        sixHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        sixHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(1).get("dt").toString())))).substring(0, 5));
                        sixHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(1).get("dt").toString())))).substring(11, 22)));

                        nineHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(2).getJSONObject("main").get("temp").toString()));
                        nineHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        nineHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(2).get("dt").toString())))).substring(0, 5));
                        nineHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(2).get("dt").toString())))).substring(11, 22)));

                        twelveHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(3).getJSONObject("main").get("temp").toString()));
                        twelveHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(3).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        twelveHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(3).get("dt").toString())))).substring(0, 5));
                        twelveHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(3).get("dt").toString())))).substring(11, 22)));

                        fifteenHourTemperature.setText(kelvinToFahrenheit(forecastData.getJSONArray("list").getJSONObject(4).getJSONObject("main").get("temp").toString()));
                        fifteenHourImage.setImageResource(findAppropriateImage(forecastData.getJSONArray("list").getJSONObject(4).getJSONArray("weather").getJSONObject(0).get("icon").toString()));
                        fifteenHourDate.setText(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(4).get("dt").toString())))).substring(0, 5));
                        fifteenHourTime.setText(timeBeautify(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa").format(new java.util.Date(1000 * (Long.parseLong(forecastData.getJSONArray("list").getJSONObject(4).get("dt").toString())))).substring(11, 22)));

                        quoteText.setText(printAppropriateQuote(currentData.getJSONArray("weather").getJSONObject(0).get("icon").toString(), quotes));
                        go = 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        go = 0;
                    }
            }
            super.onPostExecute(aVoid);
        }
    }

    public static String kelvinToFahrenheit(String temperature){

        temperature=(""+Math.round(((Double.parseDouble(temperature))-273.15)*1.8+32));
        return temperature+"°";
    }

    public static String kelvinToCelcius(String temperature){

        temperature=(""+Math.round((Double.parseDouble(temperature))-273.15));
        return temperature+"°";
    }

    public static int findAppropriateImage(String icon){

        if(icon.equals("01d"))
            return R.drawable.sunny;
        if(icon.equals("01n"))
            return R.drawable.clearnight;
        if(icon.equals("02d")||icon.equals("04d"))
            return R.drawable.partlycloudy;
        if(icon.equals("02n")||icon.equals("04n"))
            return R.drawable.partlycloudynight;
        if(icon.equals("03d")||icon.equals("03n"))
            return R.drawable.cloudy;
        if(icon.equals("09d")||icon.equals("10d"))
            return R.drawable.rain;
        if(icon.equals("09n")||icon.equals("10n"))
            return R.drawable.rainynight;
        if(icon.equals("11d"))
            return R.drawable.thunderstorm;
        if(icon.equals("11n"))
            return R.drawable.thunderstormnight;
        if(icon.equals("13d")||icon.equals("13n"))
            return R.drawable.snow;
        if(icon.equals("50d")||icon.equals("50n"))
            return R.drawable.mist;
        return R.drawable.ic_launcher_background;
    }

    public static String timeBeautify(String time){

        return time.substring(0,2)+time.substring(8);
    }

    public static String printAppropriateQuote(String icon, ArrayList<String> quotes){
        if(icon.equals("01d")||icon.equals("01n"))
            return quotes.get(1);
        if(icon.equals("02d")||icon.equals("04d")||icon.equals("02n")||icon.equals("04n"))
            return quotes.get(8);
        if(icon.equals("03d")||icon.equals("03n"))
            return quotes.get(6);
        if(icon.equals("09d")||icon.equals("10d")||icon.equals("09n")||icon.equals("10n")) {
            int rand = (int) (Math.random() * 3);
            if (rand == 0)
                return quotes.get(0);
            if (rand == 1)
                return quotes.get(5);
            if (rand == 2)
                return quotes.get(7);
        }
        if(icon.equals("11d")||icon.equals("11n"))
            return quotes.get(3);
        if(icon.equals("13d")||icon.equals("13n")) {
            int rand = (int) (Math.random() * 2);
            if(rand==0)
                return quotes.get(4);
            if(rand==1)
                return quotes.get(9);
        }
        if(icon.equals("50d")||icon.equals("50n"))
            return quotes.get(2);
        return "just have a good day";
    }
}
