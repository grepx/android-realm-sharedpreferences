package com.grepx.realmsharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import java.util.HashSet;
import java.util.Random;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesPerformanceTest {

  private static final String TAG = SharedPreferencesPerformanceTest.class.getSimpleName();

  String testConfigurationName;
  SharedPreferences sharedPreferences;
  int readWriteType;
  int dataLocality;
  int recordCount;
  Random random;

  final String SHARED_PREFERENCES_NAME = "SharedPreferences";
  final String REALM_PREFERENCES_NAME = "RealmSharedPreferences";

  final int IMPL_SHARED_PREFERENCES = 0;
  final int IMPL_REALM = 1;

  final int TYPE_READ = 10;
  final int TYPE_WRITE = 11;
  final int TYPE_READ_WRITE = 12;

  final int LOCALITY_LOW = 20;
  final int LOCALITY_HIGH = 21;

  final int RECORD_COUNT_LOW = 50;
  final int RECORD_COUNT_MEDIUM = 250;
  final int RECORD_COUNT_HIGH = 1000;

  @Test
  public void sharedPreferencesReadHighLocalityTest() throws Exception {
    testConfiguration(IMPL_SHARED_PREFERENCES, TYPE_READ_WRITE, LOCALITY_HIGH, RECORD_COUNT_LOW);
    testConfiguration(IMPL_SHARED_PREFERENCES, TYPE_READ_WRITE, LOCALITY_HIGH, RECORD_COUNT_MEDIUM);
    testConfiguration(IMPL_SHARED_PREFERENCES, TYPE_READ_WRITE, LOCALITY_HIGH, RECORD_COUNT_HIGH);

    //testConfiguration(IMPL_REALM, TYPE_READ_WRITE, LOCALITY_HIGH, RECORD_COUNT_LOW);
    //testConfiguration(IMPL_REALM, TYPE_READ_WRITE, LOCALITY_HIGH, RECORD_COUNT_MEDIUM);
    //testConfiguration(IMPL_REALM, TYPE_READ_WRITE, LOCALITY_HIGH, RECORD_COUNT_HIGH);
  }

  private void testConfiguration(int implementation, int readWriteType,
                                 int dataLocality, int recordCount) {
    testConfigurationName =
        getTestConfigurationName(implementation, readWriteType, dataLocality, recordCount);
    this.readWriteType = readWriteType;
    this.dataLocality = dataLocality;
    this.recordCount = recordCount;
    // arbitrary seed to create a deterministic random number generator
    random = new Random(8123576229384l);

    switch (implementation) {
      case IMPL_SHARED_PREFERENCES:
        sharedPreferences =
            InstrumentationRegistry.getTargetContext()
                                   .getSharedPreferences(SHARED_PREFERENCES_NAME,
                                                         Context.MODE_PRIVATE);
        break;
      case IMPL_REALM:
        sharedPreferences = new RealmSharedPreferences(REALM_PREFERENCES_NAME);
        break;
      default:
        throw new RuntimeException();
    }

    initPreferences();
    testPreferences();
  }

  private void initPreferences() {
    for (int i = 0; i < recordCount; i++) {
      writeRandomValue(i);
    }
  }

  private void testPreferences() {
    long startTime = System.nanoTime();
    for (int i = 0; i < recordCount; i++) {
      readValue(i);
    }
    for (int i = 0; i < recordCount; i++) {
      writeRandomValue(i);
    }
    for (int i = 0; i < recordCount; i++) {
      readValue(i);
    }
    printElapsedTime(testConfigurationName, startTime);
  }

  private void writeRandomValue(int prefIndex) {
    int prefType = prefIndex % 6;
    switch (prefType) {
      case 0:
        sharedPreferences.edit()
                         .putBoolean(getKey(prefIndex), random.nextBoolean())
                         .apply();
        break;
      case 1:
        sharedPreferences.edit()
                         .putFloat(getKey(prefIndex), random.nextFloat())
                         .apply();
        break;
      case 2:
        sharedPreferences.edit()
                         .putInt(getKey(prefIndex), random.nextInt())
                         .apply();
        break;
      case 3:
        sharedPreferences.edit()
                         .putLong(getKey(prefIndex), random.nextLong())
                         .apply();
        break;
      case 4:
        sharedPreferences.edit()
                         .putString(getKey(prefIndex), "random string " + random.nextLong())
                         .apply();
        break;
      case 5:
        // todo
        sharedPreferences.edit()
                         .putStringSet(getKey(prefIndex), new HashSet<String>())
                         .apply();
        break;
    }
  }

  private void readValue(int prefIndex) {
    int prefType = prefIndex % 6;
    switch (prefType) {
      case 0:
        sharedPreferences.getBoolean(getKey(prefIndex), false);
        break;
      case 1:
        sharedPreferences.getFloat(getKey(prefIndex), 0f);
        break;
      case 2:
        sharedPreferences.getInt(getKey(prefIndex), 0);
        break;
      case 3:
        sharedPreferences.getLong(getKey(prefIndex), 0l);
        break;
      case 4:
        sharedPreferences.getString(getKey(prefIndex), "");
        break;
      case 5:
        sharedPreferences.getStringSet(getKey(prefIndex), new HashSet<String>());
        break;
    }
  }

  private String getKey(int prefIndex) {
    return "test_key_" + prefIndex;
  }

  private void printElapsedTime(String configuration, long startTime) {
    Log.d(TAG, configuration + ": " + (System.nanoTime() - startTime) / 1000000l + "ms");
  }

  private String getTestConfigurationName(int implementation, int readWriteType,
                                          int dataLocality, int recordCount) {
    String result = "";
    switch (implementation) {
      case IMPL_REALM:
        result += "REALM_";
        break;
      case IMPL_SHARED_PREFERENCES:
        result += "SHARED_PREFERENCES_";
        break;
    }
    switch (readWriteType) {
      case TYPE_READ:
        result += "READ_";
        break;
      case TYPE_WRITE:
        result += "WRITE_";
        break;
      case TYPE_READ_WRITE:
        result += "READ_WRITE_";
        break;
    }
    switch (dataLocality) {
      case LOCALITY_HIGH:
        result += "HIGH_LOCALITY_";
        break;
      case LOCALITY_LOW:
        result += "LOW_LOCALITY_";
        break;
    }
    result += recordCount;
    return result;
  }
}
