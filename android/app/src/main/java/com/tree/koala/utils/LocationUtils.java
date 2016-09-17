package com.tree.koala.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Elad on 9/17/2016.
 */

public class LocationUtils {


  public static String getAddressFromLocation(Context context, LatLng latLng) throws IOException {
    Geocoder geocoder;
    List<Address> addresses;
    geocoder = new Geocoder(context, Locale.getDefault());

    addresses = geocoder.getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    String city = addresses.get(0).getLocality();
    String state = addresses.get(0).getAdminArea();
    String country = addresses.get(0).getCountryName();
    String postalCode = addresses.get(0).getPostalCode();
    String knownName = addresses.get(0).getFeatureName();

    return  address;
  }

}
