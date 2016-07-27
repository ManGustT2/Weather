package ru.klim.weatherinfo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
    private TextView mJsonTempDayView;
    private TextView mJsonTempNightView;
    private TextView mJsonPressueView;
    private TextView mJsonHumidityView;
    private TextView mJsonWindView;

    private RestClient rc;
    private Location mLocation;
    private LocationManager mManager;
    private String mProvider;
    private String day, night, humidity, pressure, speed;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity =(MainActivity)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viev = inflater.inflate(R.layout.current_fragment, container, false);

        mJsonTempDayView = (TextView) viev.findViewById(R.id.dayTemperature);
        mJsonTempNightView = (TextView) viev.findViewById(R.id.nightTemperature);
        mJsonPressueView = (TextView) viev.findViewById(R.id.pressure);
        mJsonHumidityView = (TextView) viev.findViewById(R.id.humidity);
        mJsonWindView = (TextView) viev.findViewById(R.id.wind);
        mLocation = mainActivity.getlocation();
        rc = new RestClient();
        getWeather();
        return viev;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void getWeather() {
        new Loader().execute();
    }

    private void setData() {
        try {
            String str = rc.getUrl(mLocation);
            Log.d("DENISYK", "String = " + str);
            JSONObject object = new JSONObject(str);
            String name = object.getString("name");
            JSONObject main = object.getJSONObject("main");
            day = main.getString("temp");
            night = main.getString("temp_min");
            humidity = main.getString("humidity");
            pressure = main.getString("pressure");
            JSONObject jwind = object.getJSONObject("wind");
            speed = jwind.getString("speed");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class Loader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            setData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mJsonTempDayView.setText(day);
            mJsonTempNightView.setText(night);
            mJsonPressueView.setText(humidity);
            mJsonHumidityView.setText(pressure);
            mJsonWindView.setText(speed);
        }
    }
}