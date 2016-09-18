package com.tree.koala.Models;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mapbox.directions.DirectionsCriteria;
import com.mapbox.directions.MapboxDirections;
import com.mapbox.directions.service.models.DirectionsResponse;
import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.tree.koala.R;
import com.tree.koala.utils.Constants;

import java.io.IOException;
import java.util.List;

import retrofit.Response;

/**
 * Created by Jashan Shewakramani
 * RecyclerView Adapter for displaying vault data
 */
public class VaultItem extends AbstractItem<VaultItem, VaultItem.ViewHolder> {
  private Vault vault;
  private MapboxMap oMap;
  public VaultItem(Vault vault) {
    this.vault = vault;
  }
  public Context context;
  public Vault getVault() {
    return vault;
  }


  public VaultItem(Vault vault, Context context) {
    this.vault = vault;
    this.context = context;
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
        oMap = mapboxMap;
        try {
          drawRouteToVault();
        } catch (IOException e) {
          e.printStackTrace();
        }

        mapboxMap.addMarker(new MarkerOptions()
            .position(new LatLng(vault.getLocation().getLatitude(), vault.getLocation().getLongitude()))
            .title(vault.getVaultName()));
      }
    });
  }

  private void drawRouteToVault() throws IOException {
    Location lastLocation = LocationServices.getLocationServices(context).getLastLocation();

    if(oMap != null) {

      Waypoint origin = new Waypoint(lastLocation.getLongitude(), lastLocation.getLatitude());

// Santa Monica Pier
      Waypoint destination = new Waypoint(vault.getLocation().getLongitude(), vault.getLocation().getLatitude());

// Build the client object
      MapboxDirections client = new MapboxDirections.Builder()
          .setAccessToken(Constants.mapboxToken)
          .setOrigin(origin)
          .setDestination(destination)
          .setProfile(DirectionsCriteria.PROFILE_DRIVING)
          .build();

// Execute the request
      Response<DirectionsResponse> response = client.execute();
      DirectionsRoute route = response.body().getRoutes().get(0);
      List<Waypoint> waypoints = route.getGeometry().getWaypoints();
      LatLng[] points = new LatLng[waypoints.size()];
      for (int i = 0; i < waypoints.size(); i++) {
        points[i] = new LatLng(
            waypoints.get(i).getLatitude(),
            waypoints.get(i).getLongitude());
      }


      oMap.addPolyline(new PolylineOptions()
          .add(points)
          .color(Color.parseColor("#3887be"))
          .width(5));

    }


  }

  protected static class ViewHolder extends RecyclerView.ViewHolder {
    protected MapView mapView;
    protected TextView vaultTitleText;
    protected TextView vaultDescriptionText;

    public ViewHolder(View view) {
      super(view);
      mapView = (MapView) view.findViewById(R.id.vaults_item_map);
      vaultTitleText = (TextView) view.findViewById(R.id.vault_title);
      vaultDescriptionText = (TextView) view.findViewById(R.id.vault_description);
    }
  }
}


