package ru.klim.weatherinfo;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Администратор on 10.07.2016.
 */
public class WeekWeatherFragment extends Fragment {
    ArrayList<Data> data = new ArrayList<Data>();
    WeatherAdapter weatherAdapter;
    String press,day,temperature;
    private int imegeView;
    private TextView mTextView;







    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Integer> drawable = new ArrayList<Integer>();
        drawable.ensureCapacity(7);
        drawable.add(R.drawable.snow);
        drawable.add(R.drawable.overcast);
        drawable.add(R.drawable.storm);
        drawable.add(R.drawable.sunny);
        drawable.add(R.drawable.overcast);
        drawable.add(R.drawable.sunny);
        drawable.add(R.drawable.sunny);

        ArrayList<String> weekDay = new ArrayList<String>();
        weekDay.ensureCapacity(7);
        weekDay.add("Понедельник");
        weekDay.add("Вторник");
        weekDay.add("Среда");
        weekDay.add("Четверг");
        weekDay.add("Пятница");
        weekDay.add("Суббота");
        weekDay.add("Воскресенье");
        for(int i=0; i<7; i++){

            imegeView =  drawable.get(i);
            press = "Давление";
            day = weekDay.get(i);

            //всі поля тут у тебе пусті
            //тому коли ти в адаптері сетиш картинку то отримуєш помилку
            // тут тобі потрібно проініціалізувати всі значення press,day,temperature,imegView
            // тоді все буде працювати норм


            Data d = new Data(press,day,temperature,imegeView);
            data.add(d);
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v =inflater.inflate(R.layout.weekweather_fragment,container,false);


        ListView listView = (ListView)v.findViewById(R.id.adapter_container);

        //В активності для передачі контексту ми використовуємо this
        //в фрагменті ми використовуємо getActivity();
        weatherAdapter= new WeatherAdapter(getActivity(), data);

        // присваиваем адаптер списку
        listView.setAdapter(weatherAdapter);
        return v;
    }
}
