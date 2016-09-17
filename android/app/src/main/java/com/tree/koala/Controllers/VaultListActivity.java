package com.tree.koala.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.tree.koala.R;

public class VaultListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO insert MapBox access token
        MapboxAccountManager.start(this, "");
        setContentView(R.layout.activity_vault_list);
    }
}
