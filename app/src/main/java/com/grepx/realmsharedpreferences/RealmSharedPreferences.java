package com.grepx.realmsharedpreferences;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class RealmSharedPreferences implements SharedPreferences {

  private final Realm realm;

  public RealmSharedPreferences(String name) {
    RealmConfiguration realmConfiguration =
        new RealmConfiguration.Builder()
            .name(name)
            .deleteRealmIfMigrationNeeded() // todo: remove
            .build();
    realm = Realm.getInstance(realmConfiguration);
  }

  @Override public Map<String, ?> getAll() {
    return null;
  }

  @Nullable @Override public String getString(String key, @Nullable String defValue) {
    RealmSharedPref realmSharedPref = getSharedPref(key);
    return realmSharedPref != null ? realmSharedPref.valueString : defValue;
  }

  @Nullable @Override public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
    // todo
    return new HashSet<>();
  }

  @Override public int getInt(String key, int defValue) {
    RealmSharedPref realmSharedPref = getSharedPref(key);
    return realmSharedPref != null ? (int) realmSharedPref.valueLong : defValue;
  }

  @Override public long getLong(String key, long defValue) {
    RealmSharedPref realmSharedPref = getSharedPref(key);
    return realmSharedPref != null ? realmSharedPref.valueLong : defValue;
  }

  @Override public float getFloat(String key, float defValue) {
    RealmSharedPref realmSharedPref = getSharedPref(key);
    return realmSharedPref != null ?
           Float.intBitsToFloat((int) realmSharedPref.valueLong) :
           defValue;
  }

  @Override public boolean getBoolean(String key, boolean defValue) {
    RealmSharedPref realmSharedPref = getSharedPref(key);
    return realmSharedPref != null ?
           realmSharedPref.valueLong == 1l :
           defValue;
  }

  private RealmSharedPref getSharedPref(String key) {
    return realm.where(RealmSharedPref.class).equalTo("key", key).findFirst();
  }

  @Override public boolean contains(String key) {
    return realm.where(RealmSharedPref.class).equalTo("key", key).findFirst() != null;
  }

  @Override public Editor edit() {
    return new RealmSharedPreferenceEditor();
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
      // todo
      return this;
    }

    @Override public Editor putInt(final String key, final int value) {
      sharedPreferenceOperations.add(new Runnable() {
        @Override public void run() {
          realm.copyToRealmOrUpdate(new RealmSharedPref(key, value));
        }
      });
      return this;
    }

    @Override public Editor putLong(final String key, final long value) {
      sharedPreferenceOperations.add(new Runnable() {
        @Override public void run() {
          realm.copyToRealmOrUpdate(new RealmSharedPref(key, value));
        }
      });
      return this;
    }

    @Override public Editor putFloat(final String key, final float value) {
      sharedPreferenceOperations.add(new Runnable() {
        @Override public void run() {
          realm.copyToRealmOrUpdate(new RealmSharedPref(key, Float.floatToIntBits(value)));
        }
      });
      return this;
    }

    @Override public Editor putBoolean(final String key, final boolean value) {
      sharedPreferenceOperations.add(new Runnable() {
        @Override public void run() {
          realm.copyToRealmOrUpdate(new RealmSharedPref(key, value ? 1l : 0l));
        }
      });
      return this;
    }

    @Override public Editor remove(final String key) {
      sharedPreferenceOperations.add(new Runnable() {
        @Override public void run() {
          realm.where(RealmSharedPref.class).equalTo("key", key).findFirst().deleteFromRealm();
        }
      });
      return this;
    }

    @Override public Editor clear() {
      sharedPreferenceOperations.add(new Runnable() {
        @Override public void run() {
          realm.deleteAll();
        }
      });
      return this;
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
