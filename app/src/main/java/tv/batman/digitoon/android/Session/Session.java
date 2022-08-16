package tv.batman.digitoon.android.Session;

import android.content.Context;
import android.content.SharedPreferences;

import tv.batman.digitoon.android.ApplicationLoader;

// created by morti

public class Session {
  private static volatile Session Instance;

  public static synchronized Session getInstance() {
    Session localInstance = Instance;
    if (localInstance == null) {
      synchronized (Session.class) {
        localInstance = Instance;
        if (localInstance == null) {
          Instance = localInstance = new Session();
        }
      }
    }
    return localInstance;
  }

  private static final String SHARED_PREFERENCE = "shared_preference";
  private static final String THEME = "theme";
  private final SharedPreferences sharedPreferences;

  private Session() {
    sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
  }

  public void setTheme(int i) {
    sharedPreferences.edit().putInt(THEME, i).apply();
  }

  public int getTheme() {
    return sharedPreferences.getInt(THEME , 1);
  }

  public String getString(String key, String def) {
    return sharedPreferences.getString(key, def);
  }

  public void setString(String key, String str) {
    sharedPreferences.edit().putString(key, str).apply();
  }

  public int getInt(String key, int def) {
    return sharedPreferences.getInt(key, def);
  }

  public void setInt(String key, int i) {
    sharedPreferences.edit().putInt(key, i).apply();
  }

  public void setBoolean(String key, boolean value) {
    sharedPreferences.edit().putBoolean(key, value).apply();
  }

  public boolean isBoolean(String key, boolean def) {
    return sharedPreferences.getBoolean(key, def);
  }
}
