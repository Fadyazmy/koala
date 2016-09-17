package com.tree.koala.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.constants.Style;
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

public class CreateVaultActivity extends AppCompatActivity {
  public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
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




}
