package com.tree.koala.Models;

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

    public Vault(String vaultName, Location location,
                 ArrayList<VaultFile> vaultFiles, String vaultId) {
        mVaultName = vaultName;
        mLocation = location;
        mVaultFiles = vaultFiles;
        mVaultId = vaultId;
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
