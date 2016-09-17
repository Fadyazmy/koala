package com.tree.koala.utils;

import com.tree.koala.Models.Vault;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Elad on 9/17/2016.
 */


public class JsonUtils {


  public static String getJsonFromVaultObj(Vault v) throws JSONException {

    JSONObject jsonObject = new JSONObject();
    jsonObject.accumulate("id", v.getVaultId());
    jsonObject.accumulate("name", v.getVaultName());
    jsonObject.accumulate("location", v.getLocation());

    return jsonObject.toString();
  }

}
