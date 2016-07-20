package ru.klim.weatherinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Администратор on 10.07.2016.
 */
public class TodayWeatherFragment extends Fragment {
    TextView JsonTempDayView;
    TextView JsonTempNightView;
    TextView JsonPressueView;
    TextView JsonHumidityView;
    TextView JsonWindView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, contaxiner, savedInstanceState);
        View viev =inflater.inflate(R.layout.current_fragment,container,false);

        JsonTempDayView = (TextView)viev.findViewById(R.id.dayTemperature);
        JsonTempNightView = (TextView)viev.findViewById(R.id.nightTemperature);
        JsonPressueView = (TextView)viev.findViewById(R.id.pressure);
        JsonHumidityView = (TextView)viev.findViewById(R.id.humidity);
        JsonWindView = (TextView)viev.findViewById(R.id.wind);




        final RestClient rc = new RestClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String str = rc.getUrl("sd");
                    Log.d("DENISYK", "String = " + str);
                    JSONObject object = new JSONObject(str);
                    String name = object.getString("name");
                    String dayTempersture = object.getString("temp");
                    String nightTemperature = object.getString("temp_min");
                    String humidity = object.getString("humidity");
                    String pressure = object.getString("pressure");
                    String wind = object.getString("wind");

                    JsonTempDayView.setText(dayTempersture);
                    JsonTempNightView.setText(nightTemperature);
                    JsonPressueView.setText(humidity);
                    JsonHumidityView.setText(pressure);
                    JsonWindView.setText(wind);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return viev;

    }


}
