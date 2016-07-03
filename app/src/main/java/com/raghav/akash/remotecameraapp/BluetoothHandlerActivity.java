package com.raghav.akash.remotecameraapp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

public class BluetoothHandlerActivity extends ToolbarBaseActivity {

    private static final int REQUEST_ENABLE_BT = 101;
    private TextView bluetoothStateText;
    BroadcastReceiver bluetoothReciever = new BroadcastReceiver() {

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
    private Button tunOnOffBtn;
    private Button discoverableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_bluetooth_handler);
        setupToolbar(getString(R.string.title_bluetooth_handler_activity));
        init();
        enableBluetooth();
        registerReceiver(bluetoothReciever, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    private void init() {
        bluetoothStateText = (TextView) findViewById(R.id.bluetoothStateTxt);
        tunOnOffBtn = (Button) findViewById(R.id.turn_on_off_btn);
        assert tunOnOffBtn != null;
        tunOnOffBtn.setEnabled(false);
        tunOnOffBtn.setText(R.string.string_enable_bluetooth);
        discoverableBtn = (Button) findViewById(R.id.discoverable_btn);
        assert discoverableBtn != null;
        discoverableBtn.setEnabled(false);
        discoverableBtn.setText(R.string.string_set_discoverable);
        discoverableBtn.setText(R.string.string_set_undiscoverable);
    }

    /**
     * detect and enable bluetooth
     */
    public void enableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) { // check if bluetooth is supported
            Snackbar.make(getParentView(), R.string.string_bluetooth_unsupported, Snackbar.LENGTH_LONG).show();
            bluetoothStateText.setText(R.string.string_bluetooth_unsupported);
        } else {
            Snackbar.make(getParentView(), R.string.string_bluetooth_supported, Snackbar.LENGTH_SHORT).show();
            tunOnOffBtn.setEnabled(true);
            if (!mBluetoothAdapter.isEnabled()) { // check if bluetooth is enabled
                tunOnOffBtn.setText(R.string.string_enable_bluetooth);
                bluetoothStateText.setText(R.string.string_bluetooth_off);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.string_enable_bluetooth);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendToBluetoothSettings();
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
    public void sendToBluetoothSettings() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                discoverableBtn.setEnabled(true);
                Snackbar.make(getParentView(), "bluetooth enabled", Snackbar.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(getParentView(), "bluetooth enabling failed", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void setBluetoothState(int state) {
        switch (state) {
            case 1:
                bluetoothStateText.setText(R.string.string_bluetooth_off);
                break;
            case 2:
                bluetoothStateText.setText(R.string.string_bluetooth_turning_off);
                break;
            case 3:
                bluetoothStateText.setText(R.string.string_bluetooth_on);
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
        unregisterReceiver(bluetoothReciever);
    }

}
