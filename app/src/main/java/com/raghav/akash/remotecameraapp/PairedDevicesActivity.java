package com.raghav.akash.remotecameraapp;

import android.os.Bundle;
import android.view.View;

public class PairedDevicesActivity extends ToolbarBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_paired_devices);
        setupToolbar("Paired Devices");
        setupFab(android.R.drawable.stat_sys_data_bluetooth, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to start bluetooth or connect devices
            }
        });
    }
}
