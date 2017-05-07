package com.grepx.realmsharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SharedPreferences sharedPreferences =
        getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

    // todo: put tests in a connected android test


    // very simple library to test this idea, comes with a set of performance tests to compare with
    // a traditional native shared preferences

    // todo: implementation based on snappyDB

    // mixed read/write with low data locality performance test

    // read with high data locality performance test

    // read with low data locality performance test

    // write with high data locality performance test

    // write with low data locality performance test

    // mixed read/write big string values performance test

    // file size/start speed tests for large datasets
  }
}
