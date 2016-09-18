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
import android.view.View;

import com.tree.koala.R;
import com.tree.koala.utils.Constants;
import com.tree.koala.utils.JsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class BrowseVaultActivity extends AppCompatActivity {

  FloatingActionButton mFloatingActionButton;
  public static final int FILE_SELECT_CODE = 4;
  private String vaultTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse_vault);

    vaultTitle = getIntent().getStringExtra("vault_title");
    if (getSupportActionBar() != null)
      getSupportActionBar().setTitle(vaultTitle);
    mFloatingActionButton = (FloatingActionButton) findViewById(R.id.upload_file_button);

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
    Context context=getApplicationContext();
    String scheme = uri.getScheme();
    if (scheme.equals("file")) {
      fileName = uri.getLastPathSegment();
    }
    else if (scheme.equals("content")) {
      String[] proj = { MediaStore.Video.Media.TITLE };
      Uri contentUri = null;
      Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
      if (cursor != null && cursor.getCount() != 0) {
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
        cursor.moveToFirst();
        fileName = cursor.getString(columnIndex);
      }
    }
    return fileName;
  }
}
