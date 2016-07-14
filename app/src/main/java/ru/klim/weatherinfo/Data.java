package ru.klim.weatherinfo;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Администратор on 10.07.2016.
 */
public class Data {
    private String pressure;
    private String day;
    private String temperature;
    private int imageView;


    Data(String _press,String _day, String _temperature, int _imageView){
        pressure = _press;
        imageView = _imageView;
        day = _day;
        temperature = _temperature;

    }



    public String getPressure() {
        return pressure;
    }

    public void setPressure(String name) {
        this.pressure = pressure;
    }
    public int getImageView(){
        return imageView;
    }
    public void setImageView(int imageView){
        this.imageView = imageView;
    }
    public String getDay(){
        return day;
    }
    public void setDay(String day){
        this.day = day;
    }
    public String getTemperature(){
        return temperature;
    }
    public void setTemperature(String temperature){
        this.temperature = temperature;
    }


}
