package ru.klim.weatherinfo;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
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
    private ImageView mImegeView;

    private RestClient rc;
    private Location mLocation;
    private String day, night, humidity, pressure, speed, icon;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_fragment, container, false);

        mJsonTempDayView = view.findViewById(R.id.dayTemperature);
        mJsonTempNightView = view.findViewById(R.id.nightTemperature);
        mJsonPressueView = view.findViewById(R.id.pressure);
        mJsonHumidityView = view.findViewById(R.id.humidity);
        mJsonWindView = view.findViewById(R.id.wind);
        mImegeView = view.findViewById(R.id.mImageView);
        mLocation = mainActivity.getlocation();

        rc = new RestClient();
        if(mLocation != null)
            getWeather();
        return view;

    }

    public void getLocation(Location location){
        mLocation = location;
        getWeather();
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
            JSONArray weather = object.getJSONArray("weather");
            icon = weather.getJSONObject(0).getString("icon");

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
            mJsonTempDayView.setText("День"+" " + day);
            mJsonTempNightView.setText("Ночь"+" "+ night);
            mJsonPressueView.setText("Влажность"+" "+humidity);
            mJsonHumidityView.setText("Давление"+" "+pressure);
            mJsonWindView.setText("Ветер" + " " + speed);
            Glide.with(getContext()).load("http://api.openweathermap.org/img/w/"+icon+".png")
                    .into(mImegeView);


        }
    }
}