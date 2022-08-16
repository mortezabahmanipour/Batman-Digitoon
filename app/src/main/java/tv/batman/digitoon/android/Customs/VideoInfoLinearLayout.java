package tv.batman.digitoon.android.Customs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.Theme;

public class VideoInfoLinearLayout extends LinearLayout {

  private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

  private void initialize() {
    paint.setColor(Theme.getColor(Theme.key_app_primary));
    setWillNotDraw(false);
  }

  public VideoInfoLinearLayout(Context context) {
    super(context);
    initialize();
  }

  public VideoInfoLinearLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public VideoInfoLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    float width = getWidth();
    float height = getHeight();
    canvas.drawArc(-AndroidUtilities.dp(135), AndroidUtilities.dp(-135), width + AndroidUtilities.dp(135), AndroidUtilities.dp(135), 0, 180, false, paint);
  }
}
