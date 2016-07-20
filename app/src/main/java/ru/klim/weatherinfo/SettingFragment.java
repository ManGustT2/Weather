package ru.klim.weatherinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

/**
 * Created by Администратор on 10.07.2016.
 */
public class SettingFragment extends Fragment {
    Switch mWifiSwitch, mStartSwitch, mAskToEnterSwitch;
    public static final String APP_PREFERENCES = "my_settings";
    public static final String APP_PREFERENCES_WIFI = "wi-fi";
    public static final String APP_PREFERENCES_START = "load_by_start";
    public static final String APP_PREFERENCES_ASKBYEXIT = "ask_by_exit";
    private SharedPreferences mSettings;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editorWiFi = mSettings.edit();
        editorWiFi.putBoolean(APP_PREFERENCES_WIFI, mWifiSwitch.isChecked());
        SharedPreferences.Editor editorStart = mSettings.edit();
        editorStart.putBoolean(APP_PREFERENCES_START, mStartSwitch.isChecked());
        SharedPreferences.Editor editorAsk = mSettings.edit();
        editorAsk.putBoolean(APP_PREFERENCES_ASKBYEXIT, mAskToEnterSwitch.isChecked());
        editorWiFi.apply();
        editorStart.apply();
        editorAsk.apply();
    }

     public void getPreference(){
        mWifiSwitch.setChecked(mSettings.getBoolean(APP_PREFERENCES_WIFI, false));
       mStartSwitch.setChecked(mSettings.getBoolean(APP_PREFERENCES_START, false));
       mAskToEnterSwitch.setChecked(mSettings.getBoolean(APP_PREFERENCES_ASKBYEXIT, false));
     }


    private void findUI(View v) {
        mWifiSwitch = (Switch) v.findViewById(R.id.wifiSwitch);
        mStartSwitch = (Switch) v.findViewById(R.id.startSwitch);
        mAskToEnterSwitch = (Switch) v.findViewById(R.id.askToEnterSwitch);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View viev = inflater.inflate(R.layout.settings_fragment, container, false);
        findUI(viev);
        getPreference();
        return viev;
    }


}
