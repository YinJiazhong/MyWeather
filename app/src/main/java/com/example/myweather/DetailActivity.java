package com.example.myweather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweather.dao.CityWeatherRepository;
import com.example.myweather.entity.CityWeather;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static android.media.AudioTrack.ERROR;
import static android.view.PixelCopy.SUCCESS;

/**
 * DetailActivity about request the weather info form web api
 */
public class DetailActivity extends AppCompatActivity {


    //WEATHER_API
    private final static String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather";
    //WEATHER_API_ID
    private final static String WEATHER_API_ID = "cd7eb4457bde6f1c682e265ee8f8f014";
    ImageView imageView;
    //the city more info TextView
    TextView maxTemperatureTv, minTemperatureTv, currentTemperatureTv, feelTemperatureTv, cityNameTv;
    String cityName;
    /**
     * deal with the weather from web
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        CityWeather cityWeather = new CityWeather((String) msg.obj);
                        CityWeatherRepository.getInstance().getCityWeatherMap().put(cityWeather.getCityName(), cityWeather);
                        initUI(cityWeather);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ERROR:
                    break;
            }
            return false;
        }
    });

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView = findViewById(R.id.weather_status_imageView);
        maxTemperatureTv = findViewById(R.id.max_temperature_tv);
        minTemperatureTv = findViewById(R.id.min_temperature_tv);
        currentTemperatureTv = findViewById(R.id.current_temperature_tv);
        feelTemperatureTv = findViewById(R.id.feel_temperature_tv);
        cityNameTv = findViewById(R.id.city_name_tv);


        cityName = getIntent().getStringExtra("cityName");
        setTitle(cityName);

        //before request the city info ,check the city whether in the local
        if (CityWeatherRepository.getInstance().getCityWeatherMap().containsKey(cityName)) {
            CityWeather cityWeather = CityWeatherRepository.getInstance().getCityWeatherMap().get(cityName);
            //whether is over a hour
            if (cityWeather != null && !cityWeather.isOverHour()) {
                initUI(cityWeather);
            } else {
                sendGetWeatherRequest(cityName);
            }
        } else {
            sendGetWeatherRequest(cityName);
        }

    }

    /**
     * according cityWeather to updateUI
     *
     * @param cityWeather the info about city
     */
    private void initUI(CityWeather cityWeather) {
        cityNameTv.setText(cityWeather.getCityName());
        currentTemperatureTv.setText(cityWeather.getCurrentTemp() + "℃");
        feelTemperatureTv.setText("fells like " + cityWeather.getFeelLikeTemp() + "℃");
        maxTemperatureTv.setText("Min. " + cityWeather.getMaxTemp() + "℃");
        minTemperatureTv.setText("Max. " + cityWeather.getMinTemp() + "℃");
        Class drawable = R.drawable.class;
        try {
            Field field = drawable.getField(cityWeather.getIconName());
            int res_ID = field.getInt(field.getName());
            imageView.setImageResource(res_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * according cityNam to request the city weather
     *
     * @param cityName the cityName
     */
    private void sendGetWeatherRequest(String cityName) {
        final String path = WEATHER_API + "?q=" + cityName + "&units=metric&appid=" + WEATHER_API_ID;
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamUtils.readStream(is);
                        Message msg = Message.obtain();
                        msg.obj = result;
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }

        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            //confirm whether delete the city from List
            new AlertDialog.Builder(DetailActivity.this).setTitle("Warning!").setMessage("Are you sure you want to delete this city form your favorites?")
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Set<String> stringSet = sharedPreferences.getStringSet("city_set", new HashSet<String>());
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    stringSet.remove(cityName);
                    editor.putStringSet("city_set", stringSet).commit();
                    editor.apply();
                    finish();
                }
            }).create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}