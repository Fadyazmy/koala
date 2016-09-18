package com.tree.koala.Models;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.tree.koala.R;
import com.tree.koala.utils.Constants;
import com.tree.koala.utils.LocationUtils;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Jashan Shewakramani
 * Description: Vault adapter to show maps
 */
public class VaultAdapter extends RecyclerView.Adapter<VaultAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Vault> mVaults;
    private Bundle mSavedState;
    private ArrayList<ViewHolder> mViewHolders;

    public static final String TAG = VaultAdapter.class.getSimpleName();

    public VaultAdapter(Context context, ArrayList<Vault> vaults) {
        MapboxAccountManager.start(mContext, Constants.mapboxToken);
        mContext = context;
        mVaults = vaults;
        mVaults.sort(new Comparator<Vault>() {
            @Override
            public int compare(Vault v1, Vault v2) {
                return v1.getVaultName().compareTo(v2.getVaultName());
            }
        });
        mViewHolders = new ArrayList<>();
    }

    public VaultAdapter(Context context, ArrayList<Vault> vaults, Bundle bundle) {
        mSavedState = bundle;
        new VaultAdapter(context, vaults);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.vault_item, parent, false);

        ViewHolder holder = new ViewHolder(v);
        holder.mapView.onCreate(mSavedState);
        holder.mapView.onResume();
        mViewHolders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Vault item = mVaults.get(position);
        holder.titleText.setText(item.getVaultName());
        holder.vaultLoc = mVaults.get(position).getLocation();
        Constants.userLocation = LocationServices.getLocationServices(mContext).getLastLocation();
        if (Constants.userLocation == null) {
            holder.descriptionText.setText(String.format("%d files", item.getVaultFiles().size()));
        } else {
            double distanceToVault = LocationUtils.distanceBetween(Constants.userLocation,
                    item.getLocation());
            if (distanceToVault < 1000) {
                distanceToVault = Math.round(distanceToVault / 10) * 10;
                holder.descriptionText.setText(String.format("%s metres", distanceToVault));
            } else {
                distanceToVault = Math.round(distanceToVault / 100) * 100 / 1000;
                holder.descriptionText.setText(String.format("%s km", distanceToVault));
            }
        }

        // check if the holder map has loaded
        if (holder.mapBoxMap != null) {
            setupMap(item.getLocation(), holder.mapBoxMap);
        } else {
            Log.d(TAG, "MapBoxMap is null");
            // if not loaded, load the map asynchronously
            holder.mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap map) {
                    holder.mapBoxMap = map;
                    setupMap(item.getLocation(), map);
                }
            });
        }
    }

    /**
     * Sets up the map options for a list item given a location
     */
    private void setupMap(Location location, MapboxMap mapboxMap) {
        mapboxMap.setStyleUrl(Style.LIGHT);
        mapboxMap.setMyLocationEnabled(true);
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(location)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder()
                .include(new LatLng(location));

        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(location))
                .zoom(12.5)
                .build());

        if (Constants.userLocation != null) {
            builder.include(new LatLng(Constants.userLocation));
            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50), 1000);
        }


    }

    @Override
    public int getItemCount() {
        return mVaults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MapView mapView;
        public MapboxMap mapBoxMap;
        public TextView titleText;
        public TextView descriptionText;
        public Location vaultLoc;

        public ViewHolder(View itemView) {
            super(itemView);
            mapView = (MapView)itemView.findViewById(R.id.vaults_item_map);
            titleText = (TextView) itemView.findViewById(R.id.vault_title);
            descriptionText = (TextView) itemView.findViewById(R.id.vault_description);
        }

    }

    public void onResume() {
        for (int i = 0; i < mViewHolders.size(); i++) {
            mViewHolders.get(i).mapView.onResume();
        }
    }

    public void onPause() {
        for (int i = 0; i < mViewHolders.size(); i++) {
            mViewHolders.get(i).mapView.onPause();
        }
    }

    public void onDestroy() {
        for (int i = 0; i < mViewHolders.size(); i++) {
            mViewHolders.get(i).mapView.onDestroy();
        }
    }

    public void onLowMemory() {
        for (int i = 0; i < mViewHolders.size(); i++) {
            mViewHolders.get(i).mapView.onLowMemory();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        for (int i = 0; i < mViewHolders.size(); i++) {
            mViewHolders.get(i).mapView.onCreate(savedInstanceState);
        }
    }
}
