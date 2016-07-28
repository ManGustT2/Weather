package ru.klim.weatherinfo;

import android.location.Location;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Администратор on 17.07.2016.
 */
public class RestClient {

    private static final String WEATHER_KEY = "&appid=7d261ea6e556c0f4f08906ae8353f2bd&units=metric";
    private static final String LINK = "http://api.openweathermap.org/data/2.5/";

    public String getUrl(String url) throws IOException {
        return new String(getUrlBytes(LINK+"weather?q=Kiev"+WEATHER_KEY));
    }

    public String getListWeather(Location location) throws IOException {
        String url = LINK+"forecast/daily?&lat="+location.getLatitude()+"&lon="+location.getLongitude()+WEATHER_KEY+"&cnt=7";
        return new String(getUrlBytes(url));
    }

    public String getUrl(Location location) throws IOException {
        String url = LINK+"weather?&lat="+location.getLatitude()+"&lon="+location.getLongitude()+WEATHER_KEY;
        return new String(getUrlBytes(url));
    }

    private byte[] getUrlBytes(String _url) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream is = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer)) > 0){
                bos.write(buffer, 0, bytesRead);
            }
            bos.close();
            return bos.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
}
