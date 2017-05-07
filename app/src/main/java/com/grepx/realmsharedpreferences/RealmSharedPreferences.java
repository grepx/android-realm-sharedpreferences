package com.grepx.realmsharedpreferences;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class RealmSharedPreferences implements SharedPreferences {

  private final Realm realm;

  public RealmSharedPreferences(String name) {
    RealmConfiguration realmConfiguration =
        new RealmConfiguration.Builder()
            .name(name)
            .build();
    realm = Realm.getInstance(realmConfiguration);
  }

  @Override public Map<String, ?> getAll() {
    return null;
  }

  @Nullable @Override public String getString(String key, @Nullable String defValue) {
    RealmSharedPref realmSharedPref = realm.where(RealmSharedPref.class).findFirst();
    return realmSharedPref != null ? realmSharedPref.valueString : null;
  }

  @Nullable @Override public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
    return null;
  }

  @Override public int getInt(String key, int defValue) {
    return 0;
  }

  @Override public long getLong(String key, long defValue) {
    return 0;
  }

  @Override public float getFloat(String key, float defValue) {
    return 0;
  }

  @Override public boolean getBoolean(String key, boolean defValue) {
    return false;
  }

  @Override public boolean contains(String key) {
    return false;
  }

  @Override public Editor edit() {
    return null;
  }

  @Override public void registerOnSharedPreferenceChangeListener(
      OnSharedPreferenceChangeListener listener) {

  }

  @Override public void unregisterOnSharedPreferenceChangeListener(
      OnSharedPreferenceChangeListener listener) {

  }

  public class RealmSharedPreferenceEditor implements Editor {
    // todo: this could be made more efficient using a handler
    LinkedList<Runnable> sharedPreferenceOperations = new LinkedList<>();

    @Override public Editor putString(final String key, @Nullable final String value) {
      sharedPreferenceOperations.add(new Runnable() {
        @Override public void run() {
          realm.copyToRealmOrUpdate(new RealmSharedPref(key, value));
        }
      });
      return this;
    }

    @Override public Editor putStringSet(String key, @Nullable Set<String> values) {
      return null;
    }

    @Override public Editor putInt(String key, int value) {
      return null;
    }

    @Override public Editor putLong(String key, long value) {
      return null;
    }

    @Override public Editor putFloat(String key, float value) {
      return null;
    }

    @Override public Editor putBoolean(String key, boolean value) {
      return null;
    }

    @Override public Editor remove(String key) {
      return null;
    }

    @Override public Editor clear() {
      return null;
    }

    @Override public boolean commit() {
      // Not supported since I can't force Realm to write data to the file system synchronously
      // and therefore cannot provide the same contract. apply() is the preferred choice
      // in the vast majority of apps anyway.
      throw new UnsupportedOperationException();
    }

    @Override public void apply() {
      realm.beginTransaction();
      for (Runnable sharedPreferenceOperation : sharedPreferenceOperations) {
        sharedPreferenceOperation.run();
      }
      realm.commitTransaction();
    }
  }
}
