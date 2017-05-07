package com.grepx.realmsharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import java.util.HashSet;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesPerformanceTest {

  private static final String TAG = SharedPreferencesPerformanceTest.class.getSimpleName();

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
  final int RECORD_COUNT_LARGE = 1000;

  @Test
  public void sharedPreferencesReadHighLocalityTest() throws Exception {
    Context context = InstrumentationRegistry.getTargetContext();
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

    // mixed read/write with high data locality test
    long startTime = System.nanoTime();
    for (int testStep = 0; testStep < 50; testStep++) {
      sharedPreferences.edit()
                       .putString(getKey(testStep), getValueString(testStep))
                       .apply();
    }
    printElapsedTime("SharedPreferences Read HighLocality - ", startTime);
  }

  private void testConfiguration(String configName, int implementation, int readWriteType,
                                 int dataLocality, int recordCount) {
    this.readWriteType = readWriteType;
    this.dataLocality = dataLocality;
    this.recordCount = recordCount;
    // arbitrary seed to create a deterministic random number generator
    random = new Random(8123576229384l);

    SharedPreferences sharedPreferences;
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
  }

  private void initPreferences() {

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
        break;
      case 2:
        break;
      case 3:
        break;
      case 4:
        break;
      case 5:
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
    return "test_key" + prefIndex;
  }

  private void printElapsedTime(String configuration, long startTime) {
    Log.d(TAG, configuration + (System.nanoTime() - startTime) / 1000000l + "ms");
  }

  private void writeString(int testStep) {
    //sharedPreferences.edit()
    //                 .putString()
  }

  private String getValueString(int testStep) {
    return "test_value" + testStep;
  }
}
