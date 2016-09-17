package com.tree.koala.Controllers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.tree.koala.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateVaultActivity extends AppCompatActivity implements OnMapReadyCallback {
  public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private MapView mMapView;
  private MapboxMap mMapboxMap;

  public static final int PERMISSION_REQ_CODE = 0;

  private LocationServices mLocationServices;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MapboxAccountManager.start(this, "pk.eyJ1IjoiamFzaGFuczk4IiwiYSI6ImNpdDc4YXJ1YTA3YW8yenAxZDRiM3I1dWIifQ.9cdUHw7KF4w06f2c0VFs7w");
    setContentView(R.layout.activity_create_vault);

    mLocationServices = LocationServices.getLocationServices(this);

    mMapView = (MapView) findViewById(R.id.create_vault_map);
    mMapView.onCreate(savedInstanceState);
    mMapView.getMapAsync(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mMapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mMapView.onPause();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mMapView.onDestroy();
  }

  public String postVault(String url, String json) throws IOException {

    OkHttpClient client = new OkHttpClient();

      RequestBody body = RequestBody.create(JSON, json);
      Request request = new Request.Builder()
          .url(url)
          .post(body)
          .build();
      Response response = client.newCall(request).execute();
      return response.body().string();
  }


  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    mMapboxMap = mapboxMap;
    mMapboxMap.setStyleUrl(Style.LIGHT);
    // handle location data (request permissions if not granted already)
    if (!mLocationServices.areLocationPermissionsGranted()) {
      ActivityCompat.requestPermissions(this,
              new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                      Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQ_CODE);
    } else {
      registerVault();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQ_CODE: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
          registerVault();
        else
          new AlertDialog.Builder(this).setTitle("Error creating vault")
                  .setMessage("You must grant location permissions")
                  .create().show();
      }
    }
  }

  private void registerVault() {
    mLocationServices.addLocationListener(new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        if (location != null) {
          mMapboxMap.setCameraPosition(new CameraPosition.Builder()
                  .target(new LatLng(location))
                  .zoom(16)
                  .build());

          mMapboxMap.addMarker(new MarkerOptions().position(new LatLng(location))
                  .title("Vault Location"));
        }
      }
    });
  }
}
