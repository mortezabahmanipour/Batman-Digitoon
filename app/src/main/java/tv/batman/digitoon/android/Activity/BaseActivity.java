package tv.batman.digitoon.android.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.RequestHelper;
import tv.batman.digitoon.android.Utils.Theme;

public class BaseActivity extends AppCompatActivity {
  protected Activity activity;
  protected int tag = AndroidUtilities.random.nextInt();
  protected int[] requestsId = new int[100];

  protected void setStatusBarTheme() {
    Window window = getWindow();
    if(window != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        int color = Theme.getColor(Theme.key_action_bar_default);
        int flags = window.getDecorView().getSystemUiVisibility();
        if (color == 0xFFFFFFFF) {
          flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
          flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        window.getDecorView().setSystemUiVisibility(flags);
      }
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Theme.getColor(Theme.key_app_primary_dark));
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setStatusBarTheme();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    RequestHelper.getInstance().cancel(tag);
  }
}
