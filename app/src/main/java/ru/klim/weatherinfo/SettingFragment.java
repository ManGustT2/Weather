package ru.klim.weatherinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * Created by Администратор on 10.07.2016.
 */
public class SettingFragment extends Fragment {

    private ContentLoadingProgressBar progress;
    private TextView error;
    private ListView listview;
    private AppCompatActivity mainActivity;
    private WifiListAdapter adapter = new WifiListAdapter();

    WifiManager wifiManager;

    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                // scan failure handling
                scanFailure();
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        Log.d("SettingFragment", "scanSuccess() -> results: " + results);
        progress.setVisibility(View.GONE);
        adapter.setData(results);
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        List<ScanResult> results = wifiManager.getScanResults();
        Log.d("SettingFragment", "scanFailure() -> results: " + results);
        progress.setVisibility(View.GONE);
        adapter.setData(results);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiManager = (WifiManager) mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        findUI(view);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mainActivity.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            scanFailure();
        }

        return view;
    }

    private void findUI(View view) {
        progress = view.findViewById(R.id.progress);
        error = view.findViewById(R.id.error);
        listview = view.findViewById(R.id.adapter_container);

        listview.setAdapter(adapter);
    }


}
