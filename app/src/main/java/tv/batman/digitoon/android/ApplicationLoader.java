package tv.batman.digitoon.android;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import tv.batman.digitoon.android.Utils.AppLog;

// created by morti

public class ApplicationLoader extends Application {
  public static volatile Context applicationContext;
  public static volatile Handler applicationHandler;

  @Override
  public void onCreate() {
    try {
      applicationContext = getApplicationContext();
    } catch (Throwable ignore) {

    }

    super.onCreate();

    if (applicationContext == null) {
      applicationContext = getApplicationContext();
    }

    applicationHandler = new Handler(applicationContext.getMainLooper());
  }

  public static boolean isNetworkOnline() {
    try {
      ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationLoader.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
      if (netInfo != null && (netInfo.isConnectedOrConnecting() || netInfo.isAvailable())) {
        return true;
      }
      netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        return true;
      } else {
        netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
          return true;
        }
      }
    } catch (Exception e) {
      AppLog.e(ApplicationLoader.class, e);
      return true;
    }
    return false;
  }
}
