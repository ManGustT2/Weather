package ru.klim.weatherinfo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Администратор on 10.07.2016.
 */
public class TodayWeatherFragment extends Fragment {

    private ContentLoadingProgressBar progress;
    private TextView error;
    private ViewGroup contentVg;
    private TextView city;
    private TextView mJsonTempDayView;
    private TextView mJsonTempNightView;
    private TextView mJsonPressueView;
    private TextView mJsonHumidityView;
    private TextView mJsonWindView;
    private ImageView mImegeView;

    private RestClient rc = new RestClient();
    private MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_fragment, container, false);

        progress = view.findViewById(R.id.progress);
        error = view.findViewById(R.id.error);
        contentVg = view.findViewById(R.id.contentVg);
        city = view.findViewById(R.id.city);
        mJsonTempDayView = view.findViewById(R.id.dayTemperature);
        mJsonTempNightView = view.findViewById(R.id.nightTemperature);
        mJsonPressueView = view.findViewById(R.id.pressure);
        mJsonHumidityView = view.findViewById(R.id.humidity);
        mJsonWindView = view.findViewById(R.id.wind);
        mImegeView = view.findViewById(R.id.mImageView);

        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            progress.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
            return view;
        }
        mainActivity.fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("TodayWeatherFragment", "onSuccess() -> location: " + location);
                        new Loader(location).execute();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TodayWeatherFragment", "onFailure() -> Exception: " + e);
                        progress.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                });

        return view;

    }

    private class Loader extends AsyncTask<Void, Void, String> {

        private Location mLocation;

        public Loader(Location location) {
            mLocation = location;
        }

        @Override
        protected String doInBackground(Void... params) {
            return loadWeather(mLocation);
        }

        @Override
        protected void onPostExecute(String data) {
            progress.setVisibility(View.GONE);
            if(data == null || data.isEmpty()) {
                error.setVisibility(View.VISIBLE);
                return;
            }
            contentVg.setVisibility(View.VISIBLE);

            try {
                JSONObject object = new JSONObject(data);
                String name = object.getString("name");
                JSONObject main = object.getJSONObject("main");
                JSONObject jwind = object.getJSONObject("wind");
                JSONArray weather = object.getJSONArray("weather");
                String icon = weather.getJSONObject(0).getString("icon");

                city.setText(name);
                mJsonTempDayView.setText("День" + " " + main.getString("temp_max"));
                mJsonTempNightView.setText("Ночь" + " " + main.getString("temp_min"));
                mJsonPressueView.setText("Влажность" + " " + main.getString("humidity"));
                mJsonHumidityView.setText("Давление" + " " + main.getString("pressure"));
                mJsonWindView.setText("Ветер" + " " + jwind.getString("speed"));
                Glide.with(getContext()).load("http://api.openweathermap.org/img/w/" + icon + ".png")
                        .into(mImegeView);
            } catch (Exception e) {
                contentVg.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        }
    }

    private String loadWeather(Location location) {
        Log.d("TodayWeatherFragment", "loadWeather()");
        try {
            String str = rc.getUrl(location);

            Log.d("TodayWeatherFragment", "loadWeather() -> data: " + str);

            return str;

        } catch (Exception e) {
            Log.d("TodayWeatherFragment", "loadWeather() -> Exception: " + e);
            e.printStackTrace();
            return "";
        }
    }
}