package com.grepx.realmsharedpreferences;

import io.realm.RealmObject;

public class RealmSharedPref extends RealmObject {

  public RealmSharedPref() {
  }

  public RealmSharedPref(String key, String valueString) {
    this.key = key;
    this.valueString = valueString;
  }

  public RealmSharedPref(String key, long valueInt) {
    this.key = key;
    this.valueInt = valueInt;
  }

  public RealmSharedPref(String key, float valueFloat) {
    this.key = key;
    this.valueFloat = valueFloat;
  }

  String key;
  String valueString;
  long valueInt;
  float valueFloat;
}
