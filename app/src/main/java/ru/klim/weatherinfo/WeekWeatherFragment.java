package ru.klim.weatherinfo;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Администратор on 10.07.2016.
 */
public class WeekWeatherFragment extends Fragment {
    ArrayList<Data> data = new ArrayList<Data>();
    WeatherAdapter weatherAdapter;
    String press, temperature;
    private long day;
    private int imegeView;
    private RestClient rc;
    private Location mLocation;
    private ListView listview;
    private MainActivity mainAcivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.weekweather_fragment,container,false);

        listview = (ListView)v.findViewById(R.id.adapter_container);
        mLocation = mainAcivity.getlocation();
        rc = new RestClient();
        getWeather();
        return v;
    }

    public void getLocation(Location location){
        mLocation = location;
        getWeather();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainAcivity = (MainActivity) context;
    }

    private void getWeather() {
        new Loader().execute();
    }

    private void getData(){
        try {
            String resp = rc.getListWeather(mLocation);
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private class Loader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("DENISYK", "Data size = " + data.size());
            weatherAdapter= new WeatherAdapter(getActivity(), data);
            listview.setAdapter(weatherAdapter);
        }
    }
}