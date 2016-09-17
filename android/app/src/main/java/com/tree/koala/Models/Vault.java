package com.tree.koala.models;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Jashan Shewakramani
 * Description: Class for handling and storing vault data
 */
public class Vault {
  private String mVaultName; // the vault name
  private Location mLocation;
  private ArrayList<VaultFile> mVaultFiles;
  private String mVaultId;


  public Vault(String mVaultName, Location mLocation, ArrayList<VaultFile> mVaultFiles, String mVaultId) {
    this.mVaultName = mVaultName;
    this.mLocation = mLocation;
    this.mVaultFiles = mVaultFiles;
    this.mVaultId = mVaultId;
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
}
