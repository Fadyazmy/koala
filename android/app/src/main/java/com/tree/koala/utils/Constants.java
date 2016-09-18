package com.tree.koala.utils;

import android.location.Location;

import com.tree.koala.Models.Vault;
import com.tree.koala.Models.VaultFile;

import java.util.ArrayList;

/**
 * Created by Elad
 */
public class Constants {
  public static String mapboxToken = "pk.eyJ1IjoiamFzaGFuczk4IiwiYSI6ImNpdDc4YXJ1YTA3YW8yenAxZDRiM3I1dWIifQ.9cdUHw7KF4w06f2c0VFs7w";
  public static Location userLocation = null;

  public static ArrayList<Vault> getVaultList() {
    Location vaultLoc = new Location("");
    vaultLoc.setLatitude(43.464258);
    vaultLoc.setLongitude(-80.520410);

    Vault v = new Vault("Home Vault", vaultLoc, new ArrayList<VaultFile>(), "id");
    ArrayList<Vault> list = new ArrayList<>();
    list.add(v);
    return list;
  }
}
