package com.tree.koala.Controllers;

import android.content.Intent;
import android.location.Location;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tree.koala.Models.Vault;
import com.tree.koala.Models.VaultAdapter;
import com.tree.koala.R;
import com.tree.koala.utils.Constants;
import com.tree.koala.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;

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

        setupViews(savedInstanceState);


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
//        testAdapter();
    }

    private void testAdapter() {

        Location currLocation = new Location("");//provider name is unecessary
        currLocation.setLatitude(-118.24233);//your coords of course
        currLocation.setLongitude( 34.05332);
    }

    @Override
    protected void onPause() {
//        mAdapter.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null)
            mAdapter.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mAdapter != null)
            mAdapter.onLowMemory();
    }

    public void setupViews(final Bundle savedInstance) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(Constants.FETCH_DATA_ENDPOINT)
                .header("username", "jashans")
                .build();

        final ArrayList<Vault> vaults = new ArrayList<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.getMessage();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                Toast.makeText(VaultListActivity.this, "got response", Toast.LENGTH_SHORT).show();
                BasicDBList list = (BasicDBList) JSON.parse(response.body().string());
                for (int i = 0; i < list.size(); i++) {
                    BasicDBObject entry = (BasicDBObject) list.get(i);
                    if (entry.getString("filename") != null && entry.getString("filename").charAt(0) == '*') {
                        vaults.add(JsonUtils.entryToVault(entry));
                    }
                }

                mAdapter = new VaultAdapter(VaultListActivity.this, vaults);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(VaultListActivity.this));
                        mAdapter.onCreate(savedInstance);
                        mAdapter.onResume();

                    }
                });

            }
        });
    }
}
