package com.tree.koala.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tree.koala.R;


public class BrowseVaultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse_vault);

    Button btn = new Button(this);

    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });

  }
}
