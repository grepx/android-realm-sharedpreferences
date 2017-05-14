package com.grepx.realmsharedpreferences;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmSharedPref extends RealmObject {

  public static final String KEY = "key";

  public RealmSharedPref() {
  }

  public RealmSharedPref(String key, String valueString) {
    this.key = key;
    this.valueString = valueString;
  }

  public RealmSharedPref(String key, long valueLong) {
    this.key = key;
    this.valueLong = valueLong;
  }

  @PrimaryKey String key;
  String valueString;
  long valueLong;
}
