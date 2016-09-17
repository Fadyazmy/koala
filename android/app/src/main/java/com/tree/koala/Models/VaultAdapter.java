package com.tree.koala.Models;

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

import java.util.List;

/**
 * Created by Jashan Shewakramani
 * RecyclerView Adapter for displaying vault data
 */
public class VaultAdapter extends AbstractItem<VaultAdapter, VaultAdapter.ViewHolder> {
  private Vault vault;

  public VaultAdapter(Vault vault) {
    this.vault = vault;
  }

  public Vault getVault() {
    return vault;
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

    holder.vaultTitleText.setText(vault.getVaultName());
    holder.vaultDescriptionText.setText(String.format("%d files", vault.getVaultFiles().size()));

    holder.mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        // display the location of the vault on the map using a marker
        mapboxMap.addMarker(new MarkerOptions()
            .position(new LatLng(vault.getLocation().getLatitude(), vault.getLocation().getLongitude()))
            .title(vault.getVaultName()));
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


