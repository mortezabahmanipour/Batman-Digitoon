package tv.batman.digitoon.android.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

import androidx.annotation.StringRes;
import tv.batman.digitoon.android.ApplicationLoader;
import tv.batman.digitoon.android.Customs.TextView;
import tv.batman.digitoon.android.R;

// created by morti

public class AndroidUtilities {

  public static Random random = new Random();
  public static Point displaySize = new Point();
  public static Typeface IRANSans_FaNum;
  public static int statusBarHeight = 0;
  public static int navigationBarHeight = 0;
  public static int toolbarHeight = 0;
  public static float density = 1;

  static {
    updateDisplaySize(ApplicationLoader.applicationContext);
    initializeFonts();
  }

  public static void updateDisplaySize(Context context) {
    try {
      Resources resources = context.getResources();
      int statusBarResourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
      if (statusBarResourceId > 0) {
        statusBarHeight = resources.getDimensionPixelSize(statusBarResourceId);
      }
      int navigationBarResourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
      boolean hasOnScreenNavBar = navigationBarResourceId > 0 && resources.getBoolean(navigationBarResourceId);
      navigationBarResourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
      if (hasOnScreenNavBar && navigationBarResourceId > 0) {
        navigationBarHeight = resources.getDimensionPixelSize(navigationBarResourceId);
      }
      toolbarHeight = resources.getDimensionPixelSize(R.dimen.toolbar_height);
      WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      if (windowManager != null) {
        Display display = windowManager.getDefaultDisplay();
        if (display != null) {
          display.getSize(displaySize);
        }
      }
      density = resources.getDisplayMetrics().density;
    } catch (Exception e) {
      AppLog.e(AndroidUtilities.class, e);
    }
  }

  public static void initializeFonts() {
    IRANSans_FaNum = Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), "fonts/IRANSans_FaNum.ttf");
  }

  public static void runOnUIThread(Runnable runnable) {
    runOnUIThread(runnable, 0);
  }

  public static void runOnUIThread(Runnable runnable, long delay) {
    if (delay == 0) {
      ApplicationLoader.applicationHandler.post(runnable);
    } else {
      ApplicationLoader.applicationHandler.postDelayed(runnable, delay);
    }
  }

  public static void cancelRunOnUIThread(Runnable runnable) {
    ApplicationLoader.applicationHandler.removeCallbacks(runnable);
  }

  public static int dp(float value) {
    if (value == 0) {
      return 0;
    }
    return (int) Math.ceil(density * value);
  }

  public static int dp2(float value) {
    if (value == 0) {
      return 0;
    }
    return (int) Math.floor(density * value);
  }

  public static float dpf2(float value) {
    if (value == 0) {
      return 0;
    }
    return density * value;
  }

  public static float pix(float dp) {
    if (dp == 0) {
      return 0;
    }
    return dp / density;
  }

  public static int int_pix(float dp) {
    if (dp == 0) {
      return 0;
    }
    return (int)(dp / density);
  }

  public static void setVisibility(View view, int visibility) {
    if (view == null || view.getVisibility() == visibility) {
      return;
    }
    view.setVisibility(visibility);
  }

  public static void toast(Context context, @StringRes int res) {
    toast(context, context.getString(res));
  }

  public static void toast(Context context, String str) {
    runOnUIThread(() -> {
      try {
        Toast toast = new Toast(context);
        TextView text = new TextView(context);
        text.setBackground(Theme.createDrawable(Theme.getColor(Theme.key_app_toast),0 , 0, Theme.RECT, AndroidUtilities.dp(7)));
        text.setPadding(AndroidUtilities.dp(10), AndroidUtilities.dp(10), AndroidUtilities.dp(10), AndroidUtilities.dp(10));
        text.setTextColor(Theme.getColor(Theme.key_app_toast_text));
        text.setTextSize(14);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setText(str);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(text);
        toast.show();
      } catch (Exception e) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
      }
    });
  }

  public static Bitmap convertDrawableToBitmap(Drawable drawable) {
    if(drawable != null) {
      if(drawable instanceof BitmapDrawable) {
        return ((BitmapDrawable) drawable).getBitmap();
      }
      int width = drawable.getIntrinsicWidth();
      int height = drawable.getIntrinsicHeight();
      if (width > 0 && height  > 0) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
      }
    }
    return null;
  }

  public static int[] getHeightWithParent(int parent, int width, int height) {
    if (parent > width) {
      int sub = parent - width;
      float percent = sub * 100f / width;
      return new int[] {parent, height + (int) (percent / 100f * height)};
    } else {
      int sub = width - parent;
      float percent = sub * 100f / width;
      return new int[] {width, height - (int) (percent / 100f * height)};
    }
  }
}
