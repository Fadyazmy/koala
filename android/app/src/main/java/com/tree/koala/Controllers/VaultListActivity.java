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
import com.tree.koala.R;

public class VaultListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FloatingActionButton mCreateVaultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, "pk.eyJ1IjoiamFzaGFuczk4IiwiYSI6ImNpdDc4YXJ1YTA3YW8yenAxZDRiM3I1dWIifQ.9cdUHw7KF4w06f2c0VFs7w");
        setContentView(R.layout.activity_vault_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.vault_recycler_view);
        FastItemAdapter fastItemAdapter = new FastItemAdapter();
        mRecyclerView.setAdapter(fastItemAdapter);

        mCreateVaultButton = (FloatingActionButton) findViewById(R.id.create_vault_button);
        mCreateVaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to create vault activity
                Intent intent = new Intent(VaultListActivity.this, CreateVaultActivity.class);
                startActivity(intent);
            }
        });

    }
}
