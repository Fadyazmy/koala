package com.tree.koala.Models;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.tree.koala.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jashan Shewakramani
 * Description: Class for handling and storing vault data
 */
public class Vault extends AbstractItem<Vault, Vault.ViewHolder>{
    private String mVaultName; // the vault name
    private Location mLocation;
    private ArrayList<VaultFile> mVaultFiles;
    private String mVaultId;

    public Vault(String vaultName, Location location,
                 ArrayList<VaultFile> vaultFiles, String vaultId) {
        mVaultName = vaultName;
        mLocation = location;
        mVaultFiles = vaultFiles;
        mVaultId = vaultId;
    }

    public String getVaultName() {
        return mVaultName;
    }

    public void setVaultName(String vaultName) {
        mVaultName = vaultName;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public ArrayList<VaultFile> getVaultFiles() {
        return mVaultFiles;
    }

    public void setVaultFiles(ArrayList<VaultFile> vaultFiles) {
        mVaultFiles = vaultFiles;
    }

    public String getVaultId() {
        return mVaultId;
    }

    public void setVaultId(String vaultId) {
        mVaultId = vaultId;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.vault_item;
    }

    @Override
    public void bindView(final ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        holder.vaultTitleText.setText(mVaultName);
        holder.vaultDescriptionText.setText(String.format("%d files", mVaultFiles.size()));

        holder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // display the location of the vault on the map using a marker
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                        .title(mVaultName));
            }
        });
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected MapView mapView;
        protected TextView vaultTitleText;
        protected TextView vaultDescriptionText;


        public ViewHolder(View view) {
            super(view);
            mapView = (MapView) view.findViewById(R.id.vault_map);
            vaultTitleText = (TextView) view.findViewById(R.id.vault_title);
            vaultDescriptionText = (TextView) view.findViewById(R.id.vault_description);
        }
    }
}
