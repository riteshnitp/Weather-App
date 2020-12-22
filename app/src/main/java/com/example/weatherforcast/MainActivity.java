package com.example.weatherforcast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforcast.CallApi.ApiClient;
import com.example.weatherforcast.CallApi.ApiInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText textInputEditText;
    private Button materialButton;
    private TextView temperature, minTemperature, maxTemperature, windSpeed, humidity, sunset, sunrise, pressure;
    private TextView status, location, time, feelTemperature;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city_name = textInputEditText.getText().toString();
                if(city_name.isEmpty()){
                    Toast.makeText(MainActivity.this, "City Name is Required", Toast.LENGTH_SHORT).show();
                } else {
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> call = apiInterface.getWeatherData("306155ad9110595a00affda7158893bf", city_name, "metric");

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                setResult(response.body());
                                relativeLayout.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("Fail", "onResponse: " + "failed");

                        }
                    });
                }
            }
        });
    }

    public void init(){
        textInputEditText = findViewById(R.id.city_name);
        materialButton = findViewById(R.id.search_button);
        temperature = findViewById(R.id.temp);
        minTemperature = findViewById(R.id.temp_min);
        maxTemperature = findViewById(R.id.temp_max);
        feelTemperature = findViewById(R.id.temp_feel);
        windSpeed = findViewById(R.id.wind);
        humidity = findViewById(R.id.humidity);
        sunset = findViewById(R.id.sunset);
        sunrise = findViewById(R.id.sunrise);
        pressure = findViewById(R.id.pressure);
        status = findViewById(R.id.status);
        location = findViewById(R.id.address);
        time = findViewById(R.id.time_date);
        relativeLayout = findViewById(R.id.mainContainer);
    }
    public void setResult(JsonObject result){
        JsonObject sys = (JsonObject) result.get("sys");

        JsonObject main = (JsonObject) result.get("main");
        String setTemp = main.get("temp").toString();
        Double tempDVal = Double.valueOf(setTemp);
        int val = (int) Math.round(tempDVal);
        temperature.setText("" + val + "°C");

        String setMinTemp = main.get("temp_min").toString();
        Double tempMinDVal = Double.valueOf(setMinTemp);
        int minVal = (int) Math.round(tempMinDVal);
        minTemperature.setText("Min Temp: " + minVal + "°C");

        String setMaxTemp = main.get("temp_max").toString();
        Double tempMaxDVal = Double.valueOf(setMaxTemp);
        int maxVal = (int) Math.round(tempMaxDVal);
        maxTemperature.setText("Max Temp: " + maxVal + "°C");

        String setFeelTemp = main.get("feels_like").toString();
        Double tempFeelDVal = Double.valueOf(setFeelTemp);
        int feelVal = (int) Math.round(tempFeelDVal);
        feelTemperature.setText("Feel Temp: " + feelVal + "°C");

        String loc = result.get("name").toString();
        String country = sys.get("country").toString();
        location.setText( loc.substring(1,loc.length()-1) + ", " + country.substring(1,country.length()-1));

        humidity.setText(main.get("humidity").toString() + " %");
        pressure.setText(main.get("pressure".toString()) + " hPa");

        JsonObject wind = (JsonObject) result.get("wind");
        windSpeed.setText(wind.get("speed").toString() + " m/s");

        String currentTimeString = result.get("dt").toString();
        long currentTimeLong = Long.parseLong(currentTimeString);
        Date date = new Date(currentTimeLong * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy h:mm a");
        String formattedDate = sdf.format(date);
        time.setText(formattedDate);

        String sunriseTimeString = sys.get("sunrise").toString();
        sunrise.setText(DateConverter(sunriseTimeString));

        String sunsetTimeString = sys.get("sunset").toString();
        sunset.setText(DateConverter(sunsetTimeString));

        JsonArray jsonArray = (JsonArray) result.get("weather");
        JsonObject obj = (JsonObject) jsonArray.get(0);
        String weatherStatus = obj.get("main").toString();
        status.setText(weatherStatus.substring(1,weatherStatus.length()-1));
    }

    public String DateConverter(String str){
        long currentTimeLong = Long.parseLong(str);
        Date date = new Date(currentTimeLong * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}