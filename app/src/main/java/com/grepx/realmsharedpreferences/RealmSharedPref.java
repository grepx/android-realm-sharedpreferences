package com.grepx.realmsharedpreferences;

import io.realm.RealmObject;

public class RealmSharedPref extends RealmObject {
  public String key;
  public String valueString;
  public long valueInt;
  public float valueFloat;
}
