package com.tree.koala.Controllers;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.tree.koala.Models.Vault;
import com.tree.koala.Models.VaultAdapter;
import com.tree.koala.R;
import com.tree.koala.utils.Constants;

public class VaultListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FloatingActionButton mCreateVaultButton;
    private FastItemAdapter<VaultAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, Constants.MapboxToken);
        setContentView(R.layout.activity_vault_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.vault_recycler_view);
        adapter = new FastItemAdapter<VaultAdapter>();
        mRecyclerView.setAdapter(adapter);



        mCreateVaultButton = (FloatingActionButton) findViewById(R.id.create_vault_button);
        mCreateVaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to create vault activity
                Intent intent = new Intent(VaultListActivity.this, CreateVaultActivity.class);
                startActivity(intent);
            }
        });


        testAdapter();
    }

    private void testAdapter() {

        Location currLocation = new Location("");//provider name is unecessary
        currLocation.setLatitude(-118.24233);//your coords of course
        currLocation.setLongitude( 34.05332);

        adapter.add(new VaultAdapter(new Vault("Home", currLocation, null, "123"),this));
    }
}
