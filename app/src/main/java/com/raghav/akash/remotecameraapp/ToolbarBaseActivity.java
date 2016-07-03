package com.raghav.akash.remotecameraapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;

public class ToolbarBaseActivity extends AppCompatActivity {

    View container;

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
