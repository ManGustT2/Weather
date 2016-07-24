
package ru.klim.weatherinfo;

        import android.Manifest;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.graphics.drawable.Drawable;
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
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.ArrayList;

/**
 * Created by Администратор on 10.07.2016.
 */
public class WeekWeatherFragment extends Fragment implements LocationListener{
    ArrayList<Data> data = new ArrayList<Data>();
    WeatherAdapter weatherAdapter;
    String press,day,temperature;
    private int imegeView;
    private TextView mTextView;
    private RestClient rc;
    private Location mLocation;
    private LocationManager mManager;
    private String mProvider;
    private ListView listview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rc = new RestClient();
// rc.getListWeather()

// ArrayList<Integer> drawable = new ArrayList<Integer>();
// drawable.ensureCapacity(7);
// drawable.add(R.drawable.snow);
// drawable.add(R.drawable.overcast);
// drawable.add(R.drawable.storm);
// drawable.add(R.drawable.sunny);
// drawable.add(R.drawable.overcast);
// drawable.add(R.drawable.sunny);
// drawable.add(R.drawable.sunny);
//
// ArrayList<String> weekDay = new ArrayList<String>();
// weekDay.ensureCapacity(7);
// weekDay.add("Понедельник");
// weekDay.add("Вторник");
// weekDay.add("Среда");
// weekDay.add("Четверг");
// weekDay.add("Пятница");
// weekDay.add("Суббота");
// weekDay.add("Воскресенье");
// for(int i=0; i<7; i++){
//
// imegeView = drawable.get(i);
// press = "Давление";
// day = weekDay.get(i);
//
// //всі поля тут у тебе пусті
// //тому коли ти в адаптері сетиш картинку то отримуєш помилку
// // тут тобі потрібно проініціалізувати всі значення press,day,temperature,imegView
// // тоді все буде працювати норм
//
//
// Data d = new Data(press,day,temperature,imegeView);
// data.add(d);
// }

        getWeather();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//return super.onCreateView(inflater, container, savedInstanceState);
        View v =inflater.inflate(R.layout.weekweather_fragment,container,false);

        listview = (ListView)v.findViewById(R.id.adapter_container);

//В активності для передачі контексту ми використовуємо this
//в фрагменті ми використовуємо getActivity();
// weatherAdapter= new WeatherAdapter(getActivity(), data);
//
// // присваиваем адаптер списку
// listView.setAdapter(weatherAdapter);

        return v;
    }

    private void getWeather() {
        if (mLocation != null) {
            new Loader().execute();
        } else {
            initLocation();
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
            Intent intent = new
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mManager.removeUpdates(this);
    }

    private void getData(){
        try {
            String resp = rc.getListWeather(mLocation);
            JSONObject object = new JSONObject(resp);
            JSONArray array = object.getJSONArray("list");
            for(int i=0; i<array.length(); i++){
                JSONObject ob = (JSONObject) array.get(i);
                imegeView = R.drawable.snow;
                JSONObject main = ob.getJSONObject("main");
                press = main.getString("pressure");
                temperature = main.getString("temp");
                day = ob.getString("dt");
                Data d = new Data(press, day, temperature, imegeView);
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
            if (mLocation != null) {
                getData();
            } else {
                initLocation();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("DENISYK", "Data size = " + data.size());
            weatherAdapter= new WeatherAdapter(getActivity(), data);

// присваиваем адаптер списку
            listview.setAdapter(weatherAdapter);
        }
    }
}