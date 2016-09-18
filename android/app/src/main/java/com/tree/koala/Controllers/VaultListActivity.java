package com.tree.koala.Controllers;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.tree.koala.Models.VaultAdapter;
import com.tree.koala.R;
import com.tree.koala.utils.Constants;

public class VaultListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FloatingActionButton mCreateVaultButton;
    private VaultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, Constants.mapboxToken);
        setContentView(R.layout.activity_vault_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.vault_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VaultAdapter(this, Constants.getVaultList());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.onCreate(savedInstanceState);

        mCreateVaultButton = (FloatingActionButton) findViewById(R.id.create_vault_button);
        mCreateVaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to create vault activity
                Intent intent = new Intent(VaultListActivity.this, CreateVaultActivity.class);
                startActivity(intent);
            }
        });

        // important variable; will need to be accessed in other activities too
        Constants.userLocation = LocationServices.getLocationServices(this).getLastLocation();
        testAdapter();
    }

    private void testAdapter() {

        Location currLocation = new Location("");//provider name is unecessary
        currLocation.setLatitude(-118.24233);//your coords of course
        currLocation.setLongitude( 34.05332);
    }

    @Override
    protected void onPause() {
        mAdapter.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAdapter.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mAdapter.onLowMemory();
    }
}
