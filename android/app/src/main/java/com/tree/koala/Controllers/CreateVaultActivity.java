package com.tree.koala.Controllers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.tree.koala.R;
import com.tree.koala.utils.Constants;
import com.tree.koala.utils.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
  private Location mVaultLocation;
  private TextView mAddressView;
  private EditText mVaultText;
  public static final int PERMISSION_REQ_CODE = 0;

  private LocationServices mLocationServices;

  public static final String TAG = CreateVaultActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MapboxAccountManager.start(this, Constants.mapboxToken);
    setContentView(R.layout.activity_create_vault);


    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("Create Vault");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    } else {
      Log.d(TAG, "Support Action Bar is null");
    }

    mLocationServices = LocationServices.getLocationServices(this);
    mVaultText = (EditText) findViewById(R.id.vault_name_text);
    mAddressView = (TextView) findViewById(R.id.address_text);
    mMapView = (MapView) findViewById(R.id.create_vault_map);
    mMapView.onCreate(savedInstanceState);
    mMapView.getMapAsync(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.create_vault_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_submit: {
        String vaultName = mVaultText.getText().toString();
        Location location = Constants.userLocation;
        final String username = "jashans";
        byte[] data = new byte[1]; // empty byte[] denotes data for vault
        JsonUtils.insertRecord(username, "*" + vaultName, location, data, "");
      }
    }
    return true;
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
    Location lastLocation = mLocationServices.getLastLocation();
    updateMap(lastLocation);
    // TODO: make network calls to the API to create the vault
  }

  private void updateMap(Location location) {
    if (location != null) {
      mMapboxMap.setCameraPosition(new CameraPosition.Builder()
              .target(new LatLng(location))
              .zoom(16)
              .build());

      mMapboxMap.addMarker(new MarkerOptions().position(new LatLng(location))
              .title("Vault Location"));

      Geocoder geocoder = new Geocoder(this, Locale.getDefault());
      try {
        List<Address> addressList =
                geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if (addressList != null) {
          Address address = addressList.get(0);
          mAddressView.setText(address.getAddressLine(0));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    mVaultLocation = location;
  }



}
