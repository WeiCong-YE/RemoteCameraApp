package com.raghav.akash.remotecameraapp;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raghav.akash.remotecameraapp.util.Constants;

public class BluetoothHandlerActivity extends ToolbarBaseActivity {

    private BluetoothAdapter bluetoothAdapter;
    private TextView bluetoothStateText;
    private Button tunOnOffBtn;
    private Button discoverableBtn;
    private Button pairedDevicesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_bluetooth_handler);
        setupToolbar(getString(R.string.title_bluetooth_handler_activity));
        init();
        enableBluetooth();
        setupListeners();
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    private void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothStateText = (TextView) findViewById(R.id.bluetoothStateTxt);
        tunOnOffBtn = (Button) findViewById(R.id.turn_on_off_btn);
        discoverableBtn = (Button) findViewById(R.id.discoverable_btn);
        pairedDevicesBtn = (Button) findViewById(R.id.show_paired_devices_btn);
    }

    private void setupListeners() {
        tunOnOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled()) {
                    setBluetoothDisabled();
                } else {
                    setBluetoothEnabled();
                }
            }
        });

        discoverableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled() && bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivityForResult(discoverableIntent, Constants.REQUEST_DISCOVERABLE_BT);
                }
            }
        });

        pairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BluetoothHandlerActivity.this, PairedDevicesActivity.class));
            }
        });
    }

    private void setBluetoothDisabled() {
        bluetoothAdapter.disable();
    }

    /**
     * detect and enable bluetooth
     */
    public void enableBluetooth() {
        if (bluetoothAdapter == null) { // check if bluetooth is supported
            Snackbar.make(getParentView(), R.string.string_bluetooth_unsupported, Snackbar.LENGTH_LONG).show();
            bluetoothStateText.setText(R.string.string_bluetooth_unsupported);
        } else {
            Snackbar.make(getParentView(), R.string.string_bluetooth_supported, Snackbar.LENGTH_SHORT).show();
            tunOnOffBtn.setEnabled(true);
            if (!bluetoothAdapter.isEnabled()) { // check if bluetooth is enabled
                tunOnOffBtn.setText(R.string.string_enable_bluetooth);
                bluetoothStateText.setText(R.string.string_bluetooth_off);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.string_enable_bluetooth);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setBluetoothEnabled();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                tunOnOffBtn.setEnabled(true);
                tunOnOffBtn.setText(R.string.string_disable_bluetooth);
                bluetoothStateText.setText(R.string.string_bluetooth_on);
                discoverableBtn.setEnabled(true);
            }
        }
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
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(getParentView(), "bluetooth enabling failed", Snackbar.LENGTH_SHORT).show();
            }
        }
        if (requestCode == Constants.REQUEST_DISCOVERABLE_BT) {
            if (resultCode == RESULT_CANCELED) {
                Snackbar.make(getParentView(), "bluetooth discovering permission denied", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(getParentView(), "bluetooth discoverable for " + resultCode + " seconds", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void setBluetoothState(int state) {
        switch (state) {
            case 1:
                bluetoothStateText.setText(R.string.string_bluetooth_off);
                tunOnOffBtn.setText(R.string.string_enable_bluetooth);
                break;
            case 2:
                bluetoothStateText.setText(R.string.string_bluetooth_turning_off);
                break;
            case 3:
                bluetoothStateText.setText(R.string.string_bluetooth_on);
                tunOnOffBtn.setText(R.string.string_disable_bluetooth);
                break;
            case 4:
                bluetoothStateText.setText(R.string.string_bluetooth_turning_on);
                break;
            case 5:
                bluetoothStateText.setText(R.string.string_bluetooth_connected);
                break;
            case 6:
                bluetoothStateText.setText(R.string.string_bluetooth_connecting);
                break;
            case 7:
                bluetoothStateText.setText(R.string.string_bluetooth_disconnected);
                break;
            case 8:
                bluetoothStateText.setText(R.string.string_bluetooth_disconnecting);
                break;
            default:
                bluetoothStateText.setText(R.string.string_bluetooth_state_error);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }

}
