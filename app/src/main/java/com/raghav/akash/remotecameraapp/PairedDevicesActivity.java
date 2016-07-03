package com.raghav.akash.remotecameraapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raghav.akash.remotecameraapp.util.Constants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class PairedDevicesActivity extends ToolbarBaseActivity {

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_paired_devices);
        setupToolbar("Paired Devices");
        init();
        if (checkBluetoothState()) {
            setupListView();
        }
        setupFab(android.R.drawable.stat_sys_data_bluetooth, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to start bluetooth or connect devices
            }
        });
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    private void setupListView() {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> deviceNames = new ArrayList<>();
        for (BluetoothDevice bluetoothDevice : devices) {
            deviceNames.add(bluetoothDevice.getName());
        }
        ListView listView = (ListView) findViewById(R.id.paired_devices_list);
        assert listView != null;
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNames));
    }

    private void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private boolean checkBluetoothState() {
        if (bluetoothAdapter == null) { // check if bluetooth is supported
            Snackbar.make(getParentView(), R.string.string_bluetooth_unsupported, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            Snackbar.make(getParentView(), R.string.string_bluetooth_supported, Snackbar.LENGTH_SHORT).show();
            if (!bluetoothAdapter.isEnabled()) { // check if bluetooth is enabled
                setBluetoothEnabled();
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * open bluetooth settings to enable bluetooth
     */
    public void setBluetoothEnabled() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(getParentView(), "bluetooth enabled", Snackbar.LENGTH_SHORT).show();
                setupListView();
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(getParentView(), "bluetooth enabling denied", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }
}
