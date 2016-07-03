package com.raghav.akash.remotecameraapp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;

public class ToolbarBaseActivity extends AppCompatActivity {

    View container;
    BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        setBluetoothState(1);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        setBluetoothState(2);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        setBluetoothState(3);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        setBluetoothState(4);
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        setBluetoothState(5);
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        setBluetoothState(6);
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTED:
                        setBluetoothState(7);
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTING:
                        setBluetoothState(8);
                        break;
                    default:
                        setBluetoothState(state);
                        break;
                }
            }
        }
    };

    protected void setBluetoothState(int state) {
        //override method to listen to bluetooth state
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        container = findViewById(R.id.coordinator_layout);
    }

    /**
     * initialize view stub for child class layout
     *
     * @param layoutResourceId layout resource file id
     */
    protected void setLayout(int layoutResourceId) {
        ViewStub stub = (ViewStub) findViewById(R.id.viewStub);
        if (stub != null) {
            stub.setLayoutResource(layoutResourceId);
            stub.inflate();
        }
    }

    /**
     * Add toolbar to the ui
     *
     * @param title toolbar title
     */
    protected void setupToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
        }
    }

    /**
     * Add fab to ui
     *
     * @param iconResourceId  id of drawable resource icon to be displayed on fab, pass -1 to set default icon
     * @param onClickListener fab click event handler object
     */
    protected void setupFab(int iconResourceId, View.OnClickListener onClickListener) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        if (iconResourceId != -1) {
            fab.setImageResource(iconResourceId);
        } else {
            fab.setImageResource(android.R.drawable.sym_action_email);
        }
        fab.setOnClickListener(onClickListener);
    }

    protected View getParentView() {
        return container;
    }
}
