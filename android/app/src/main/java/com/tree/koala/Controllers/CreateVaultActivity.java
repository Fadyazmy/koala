package com.tree.koala.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.tree.koala.R;

public class CreateVaultActivity extends AppCompatActivity {

  MapView mMapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MapboxAccountManager.start(this, "pk.eyJ1IjoiamFzaGFuczk4IiwiYSI6ImNpdDc4YXJ1YTA3YW8yenAxZDRiM3I1dWIifQ.9cdUHw7KF4w06f2c0VFs7w");
    setContentView(R.layout.activity_create_vault);

    mMapView = (MapView) findViewById(R.id.create_vault_map);
    mMapView.onCreate(savedInstanceState);
    mMapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        mapboxMap.setStyleUrl(Style.LIGHT);
      }
    });
  }
}
