package com.tree.koala.Controllers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tree.koala.Models.FileItem;
import com.tree.koala.Models.VaultFile;
import com.tree.koala.R;
import com.tree.koala.utils.Constants;
import com.tree.koala.utils.JsonUtils;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BrowseVaultActivity extends AppCompatActivity {

  FloatingActionButton mFloatingActionButton;
  public static final int FILE_SELECT_CODE = 4;
  private String vaultTitle;
  private FastItemAdapter mAdapter;
  private RecyclerView mFilesList;

  public static final String TAG = BrowseVaultActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse_vault);

    vaultTitle = getIntent().getStringExtra("vault_title");
    if (getSupportActionBar() != null)
      getSupportActionBar().setTitle(vaultTitle);
    mFloatingActionButton = (FloatingActionButton) findViewById(R.id.upload_file_button);

    mFilesList = (RecyclerView) findViewById(R.id.browse_files_recyclerview);
    mAdapter = new FastItemAdapter();
    mFilesList.setLayoutManager(new LinearLayoutManager(this));
    mFilesList.setAdapter(mAdapter);

    mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
          startActivityForResult(Intent.createChooser(intent, "Select a file to uplaod"),
                  FILE_SELECT_CODE);
        } catch (ActivityNotFoundException e) {
          e.printStackTrace();
        }
      }
    });

    setupList();
  }

  private void setupList() {
    OkHttpClient client = new OkHttpClient();

    final ArrayList<FileItem> vaultFiles = new ArrayList<>();

    final Request request = new Request.Builder()
            .url(Constants.FETCH_DATA_ENDPOINT)
            .header("username", "jashans")
            .build();

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Request request, IOException e) {
        Log.d(TAG, e.getMessage());
      }

      @Override
      public void onResponse(Response response) throws IOException {
        if (response.isSuccessful())
          Log.d(TAG, "success!");
        BasicDBList list = (BasicDBList) JSON.parse(response.body().string());
        for (int i = 0; i < list.size(); i++) {
          BasicDBObject entry = (BasicDBObject) list.get(i);
          if (entry.getString("filename") != null && entry.getString("filename").charAt(0) != '*')
            vaultFiles.add(entryToVaultFile(entry));
        }

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mAdapter.add(vaultFiles);
          }
        });
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case FILE_SELECT_CODE:
        if (resultCode == RESULT_OK) {
          Uri uri = data.getData();
          uploadFileToVault(uri);
        }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void uploadFileToVault(Uri uri) {
    String filename = getFilename(uri);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    FileInputStream fis;
    try {
      fis = new FileInputStream(new File(uri.getPath()));
      byte[] buf = new byte[1024];
      int n;
      while (-1 != (n = fis.read(buf)))
        baos.write(buf, 0, n);
    } catch (Exception e) {
      e.printStackTrace();
    }
    byte[] fileBytes = baos.toByteArray();

    JsonUtils.insertRecord("jashans", filename, Constants.userLocation, fileBytes, vaultTitle);
  }

  public String getFilename(Uri uri) {
    String fileName = null;
    if (uri.getScheme().equals("file")) {
      fileName = uri.getLastPathSegment();
    } else {
      Cursor cursor = null;
      try {
        cursor = getContentResolver().query(uri, new String[]{
                MediaStore.Images.ImageColumns.DISPLAY_NAME
        }, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
          fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
          Log.d(TAG, "name is " + fileName);
        }
      } finally {

        if (cursor != null) {
          cursor.close();
        }
      }
    }
    return fileName;
  }

  public FileItem entryToVaultFile(BasicDBObject entry) {
    FileItem item = new FileItem(entry.getString("filename"), entry.getString("data"));
    return item;
  }
}
