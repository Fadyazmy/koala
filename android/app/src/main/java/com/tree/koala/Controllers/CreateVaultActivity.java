package com.tree.koala.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.tree.koala.R;

public class CreateVaultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MapboxAccountManager.start(this, "pk.eyJ1IjoiamFzaGFuczk4IiwiYSI6ImNpdDc4YXJ1YTA3YW8yenAxZDRiM3I1dWIifQ.9cdUHw7KF4w06f2c0VFs7w");

    setContentView(R.layout.activity_create_vault);
  }
}
