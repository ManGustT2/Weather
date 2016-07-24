
package ru.klim.weatherinfo;

import android.Manifest;
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
public class TodayWeatherFragment extends Fragment implements LocationListener {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viev = inflater.inflate(R.layout.current_fragment, container, false);

        mJsonTempDayView = (TextView) viev.findViewById(R.id.dayTemperature);
        mJsonTempNightView = (TextView) viev.findViewById(R.id.nightTemperature);
        mJsonPressueView = (TextView) viev.findViewById(R.id.pressure);
        mJsonHumidityView = (TextView) viev.findViewById(R.id.humidity);
        mJsonWindView = (TextView) viev.findViewById(R.id.wind);

        rc = new RestClient();
        getWeather();

        return viev;

    }

    private void getWeather() {
        if (mLocation != null) {
            new Loader().execute();
        } else {
            initLocation();
        }
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

    private void initLocation() {
        mManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mProvider = mManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = mManager.getLastKnownLocation(mProvider);

        boolean enable = mManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enable) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        getWeather();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mManager.requestLocationUpdates(mProvider, 400, 1, this);
    }

    @Override
    public void onPause()

    {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mManager.removeUpdates(this);
    }

    class Loader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (mLocation != null) {
                setData();
            } else {
                initLocation();
            }
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