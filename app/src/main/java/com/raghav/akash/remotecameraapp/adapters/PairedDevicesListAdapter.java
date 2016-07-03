package com.raghav.akash.remotecameraapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.raghav.akash.remotecameraapp.R;

import java.util.ArrayList;

/**
 * Created by akash on 3/7/16.
 */
public class PairedDevicesListAdapter extends BaseAdapter {

    ArrayList bluetoothDevicesData;
    Context context;

    @Override
    public int getCount() {
        return bluetoothDevicesData.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothDevicesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_paired_devices, parent, false);
        }
        return convertView;
    }
}
