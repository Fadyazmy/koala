package com.tree.koala.utils;

import android.location.Location;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tree.koala.Controllers.CreateVaultActivity;
import com.tree.koala.Controllers.VaultListActivity;
import com.tree.koala.Models.Vault;
import com.tree.koala.Models.VaultFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Elad on 9/17/2016.
 */


public class JsonUtils {

  public static final MediaType JSON_TYPE
          = MediaType.parse("application/json; charset=utf-8");

  public static final String TAG = JsonUtils.class.getSimpleName();


  public static String getJsonFromVaultObj(Vault v) throws JSONException {

    JSONObject jsonObject = new JSONObject();
    jsonObject.accumulate("id", v.getVaultId());
    jsonObject.accumulate("name", v.getVaultName());
    jsonObject.accumulate("location", v.getLocation());

    return jsonObject.toString();
  }

  public static boolean insertRecord(String username, String fileName,
                                     Location location, byte[] fileData, String type) {
    BasicDBObject jsonObject = new BasicDBObject();
    try {
      jsonObject.put("username", username);
      jsonObject.put("filename", fileName);
      jsonObject.put("location", location.getLatitude() + "|" + location.getLongitude());
      jsonObject.put("url", fileData);
      jsonObject.put("data", type);

      OkHttpClient client = new OkHttpClient();
      RequestBody requestBody = RequestBody.create(JSON_TYPE, jsonObject.toString());
      Log.d(TAG, jsonObject.toString());

      Request request = new Request.Builder()
              .url(Constants.INSERT_VAULT_ENDPOINT)
              .post(requestBody)
              .header("Content-Type", "application/json")
              .build();
      client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
          Log.d(TAG, "Error");
        }

        @Override
        public void onResponse(Response response) throws IOException {
          if (response.isSuccessful())
            Log.d(TAG, "Successful response");
          else
            Log.d(TAG, response.body().string());
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public static Vault entryToVault(BasicDBObject entry) {
    Location vaultLocation = new Location("");
    vaultLocation.setLatitude(Double.parseDouble(entry.getString("location").split("\\|")[0]));
    vaultLocation.setLongitude(Double.parseDouble(entry.getString("location").split("\\|")[1]));

    return new Vault(entry.getString("filename").substring(1),
            vaultLocation, new ArrayList<VaultFile>(), "");
  }

}
