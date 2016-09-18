package com.tree.koala.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

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

  /*
 * Calculate distance between two points in latitude and longitude taking
 * into account height difference. If you are not interested in height
 * difference pass 0.0. Uses Haversine method as its base.
 *
 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
 * el2 End altitude in meters
 * @returns Distance in Meters
 */
  private static double distance(double lat1, double lat2, double lon1,
                                double lon2, double el1, double el2) {

    final int R = 6371; // Radius of the earth

    Double latDistance = Math.toRadians(lat2 - lat1);
    Double lonDistance = Math.toRadians(lon2 - lon1);
    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = el1 - el2;

    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
  }

  public static double distanceBetween(Location l1, Location l2) {
    return distance(l1.getLatitude(), l2.getLatitude(),
            l1.getLongitude(), l2.getLongitude(), 0, 0);
  }

  public static Location midpoint(Location l1, Location l2) {
    Location result = new Location("");
    result.setLatitude((l1.getLatitude() + l2.getLatitude()) / 2);
    result.setLongitude((l1.getLongitude() + l2.getLongitude())/ 2);

    return result;
  }

}
