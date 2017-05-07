package com.grepx.realmsharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesPerformanceTest {

  private static final String TAG = SharedPreferencesPerformanceTest.class.getSimpleName();

  SharedPreferences sharedPreferences;
  int readWriteType;
  int dataLocality;
  int recordCount;

  final String SHARED_PREF_NAME = "SharedPreferences";
  final String REALM_PREF_NAME = "RealmSharedPreferences";

  final int TYPE_READ = 0;
  final int TYPE_WRITE = 1;
  final int TYPE_READ_WRITE = 2;

  final int LOCALITY_LOW = 0;
  final int LOCALITY_HIGH = 1;

  final int RECORD_COUNT_LOW = 50;
  final int RECORD_COUNT_MEDIUM = 250;
  final int RECORD_COUNT_LARGE = 1000;

  @Test
  public void sharedPreferencesReadHighLocalityTest() throws Exception {
    Context context = InstrumentationRegistry.getTargetContext();
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

    // mixed read/write with high data locality test
    long startTime = System.nanoTime();
    for (int testStep = 0; testStep < 50; testStep++) {
      sharedPreferences.edit()
                       .putString(getKey(testStep), getValueString(testStep))
                       .apply();
    }
    printElapsedTime("SharedPreferences Read HighLocality - ", startTime);
  }

  private void printElapsedTime(String configuration, long startTime) {
    Log.d(TAG, configuration + (System.nanoTime() - startTime) / 1000000l + "ms");
  }

  private void writeString(int testStep) {
    //sharedPreferences.edit()
    //                 .putString()
  }

  private String getKey(int testStep) {
    return "test_key" + testStep;
  }

  private String getValueString(int testStep) {
    return "test_value" + testStep;
  }
}
