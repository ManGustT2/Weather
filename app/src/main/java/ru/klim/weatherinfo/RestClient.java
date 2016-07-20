package ru.klim.weatherinfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Администратор on 17.07.2016.
 */
public class RestClient  {

        private static final String WEATHER_KEY = "&appid=7d261ea6e556c0f4f08906ae8353f2bd";
        private static final String LINK = "http://api.openweathermap.org/data/2.5/weather?q=";

        public String getUrl(String url) throws IOException {
            return new String(getUrlBytes(url));
        }

        private byte[] getUrlBytes(String _url) throws IOException {
            URL url = new URL(LINK+"Kiev"+WEATHER_KEY);
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

