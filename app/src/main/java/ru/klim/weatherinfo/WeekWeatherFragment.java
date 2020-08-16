package ru.klim.weatherinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Администратор on 10.07.2016.
 */
public class WeekWeatherFragment extends Fragment {

    WeatherAdapter weatherAdapter;
    String press, temperature;
    private long day;
    private int imegeView;
    private RestClient rc = new RestClient();
    private MainActivity mainActivity;

    private ContentLoadingProgressBar progress;
    private TextView error;
    private ListView listview;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekweather_fragment,container,false);

        progress = view.findViewById(R.id.progress);
        error = view.findViewById(R.id.error);
        listview = view.findViewById(R.id.adapter_container);

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
                        Log.d("WeekWeatherFragment", "onSuccess() -> location: " + location);
                        new Loader(location).execute();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("WeekWeatherFragment", "onFailure() -> Exception: " + e);
                        progress.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                });

        return view;
    }

    private class Loader extends AsyncTask<Void, Void, ArrayList<Data>> {

        private Location mLocation;

        public Loader(Location location) {
            mLocation = location;
        }

        @Override
        protected ArrayList<Data> doInBackground(Void... params) {
            return loadWeather(mLocation);
        }

        @Override
        protected void onPostExecute(ArrayList<Data> data) {
            progress.setVisibility(View.GONE);
            if(data == null || data.isEmpty()) {
                error.setVisibility(View.VISIBLE);
                return;
            }
            weatherAdapter = new WeatherAdapter(getActivity(), data);
            listview.setAdapter(weatherAdapter);
        }
    }

    private ArrayList<Data> loadWeather(Location location) {
        ArrayList<Data> data = new ArrayList<>();
        try {
            String resp = rc.getListWeather(location);
            JSONObject object = new JSONObject(resp);
            JSONArray array = object.getJSONArray("list");
            for(int i=0; i<array.length(); i++){
                JSONObject ob = (JSONObject) array.get(i);
                imegeView = R.drawable.snow;
                press = ob.getString("pressure");
                JSONObject temp = ob.getJSONObject("temp");
                temperature = temp.getString("day");
                day = ob.getLong("dt");
                JSONArray weather = ob.getJSONArray("weather");
                String icon = weather.getJSONObject(0).getString("icon");
                Data d = new Data(press, day, temperature, imegeView, icon);
                data.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}