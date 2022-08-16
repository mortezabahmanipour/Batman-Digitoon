package tv.batman.digitoon.android.Customs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.Theme;

public class SwipeRefreshLayout extends androidx.swiperefreshlayout.widget.SwipeRefreshLayout {

  public View target;

  public void setTargetSpace(int space) {
    target.setTranslationY(space);
  }

  public SwipeRefreshLayout(@NonNull Context context) {
    this(context, null);
  }

  public SwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize();
    if (getChildCount() > 0) {
      target = getChildAt(0);
      final int mTop = target.getTop();
      target.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> AndroidUtilities.setVisibility(target, top <= mTop ? View.GONE : View.VISIBLE));
    }
  }

  private void initialize() {
    setColorSchemeColors(Theme.getColor(Theme.key_app_accent));
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    View view = getChildAt(1);
    if (view != null && view.getId() == -1) {
      view.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30), MeasureSpec.EXACTLY));
    }
  }
}
