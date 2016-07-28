package ru.klim.weatherinfo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Администратор on 10.07.2016.
 */
public class WeatherAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Data> object;

    WeatherAdapter(Context context,ArrayList<Data> date){
        ctx = context;
        object = date;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return object.size();
    }

    @Override
    public Object getItem(int position) {
        return object.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view==null){
            view = lInflater.inflate(R.layout.week_adapter, parent, false);
        }
        Data d = getData(position);

        TextView tvPressure = (TextView) view.findViewById(R.id.pressure);
        tvPressure.setText("Давление:" + " " + d.getPressure());
        TextView tvDay = (TextView) view.findViewById(R.id.textViewDay);
        tvDay.setText("День недели:"+" "+ d.getDay());
        TextView tvTemperature = (TextView) view.findViewById(R.id.textViewTemoerature);
        tvTemperature.setText("Температура:"+" "+d.getTemperature());
        ImageView ivIcon = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(ctx).load("http://api.openweathermap.org/img/w/"+d.getIcon()+".png")
                .into(ivIcon);

        return view;
    }

    Data getData(int position) {
        return ((Data) getItem(position));
    }

    ArrayList<Data> getData() {
        ArrayList<Data> box = new ArrayList<Data>();
        for (Data p : object) {
            box.add(p);
        }
        return box;
    }

}