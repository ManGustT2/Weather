package ru.klim.weatherinfo;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WifiListAdapter extends BaseAdapter {

    private List<ScanResult> results = new ArrayList<>();

    public void setData(List<ScanResult> results) {
        this.results.clear();
        this.results.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_wifi, parent, false);

        ScanResult result = results.get(position);
        ((TextView)view.findViewById(R.id.SSID)).setText(result.SSID);
        ((TextView)view.findViewById(R.id.BSSID)).setText(result.BSSID);

        return view;
    }
}
