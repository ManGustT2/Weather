package ru.klim.weatherinfo;

import android.net.ParseException;
import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Администратор on 10.07.2016.
 */
public class Data {
    private String pressure;
    private long day;
    private String temperature;
    private int imageView;
    private String icon;

    Data(String _press, long _day, String _temperature, int _imageView, String icon){
        pressure = _press;
        imageView = _imageView;
        day = _day;
        temperature = _temperature;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPressure() {
        return pressure;
    }

    public String getTimestamp() {
        Date d = getDate(day);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(d.getTime());
        String date = DateFormat.format("EEEE", cal).toString();
        return date;
    }

    private Date getDate(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public int getImageView(){
        return imageView;
    }

    public String getDay(){
        return getTimestamp();
    }

    public String getTemperature(){
        return temperature;
    }

}