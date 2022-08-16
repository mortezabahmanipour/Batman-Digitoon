package tv.batman.digitoon.android.Customs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.Theme;

import androidx.annotation.Keep;

// created by morti

public class CircleProgressbar extends View {
  private Paint baseBackgroundPaint;
  private Paint backPaint;
  private Paint forePaint;
  private RectF rectF = new RectF();
  private int backWidth = AndroidUtilities.dp(2);
  private int foreWidth = AndroidUtilities.dp(2);
  private int baseBackgroundColor = 0x00000000;
  private int backColor = Theme.getColor(Theme.key_app_shadow_2);
  private int foreColor = Theme.getColor(Theme.key_app_accent);
  private float progress = 0;
  private float queueProgress = -1;
  private float startAngle = -90;
  private float sweepAngle = 0;
  private float maxProgress = 100;
  private long lastUpdateTime;
  private static final float rotationTime = 1800;
  private boolean clockWise;
  private boolean indeterminate;
  private ObjectAnimator objectAnimator;
  private static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();

  public CircleProgressbar(Context context) {
    this(context, null);
  }

  public CircleProgressbar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircleProgressbar(Context context, AttributeSet attrs, int i) {
    super(context, attrs, i);
    baseBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    baseBackgroundPaint.setColor(baseBackgroundColor);

    backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    backPaint.setStyle(Paint.Style.STROKE);
    backPaint.setStrokeWidth(backWidth);
    backPaint.setColor(backColor);

    forePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    forePaint.setStyle(Paint.Style.STROKE);
    forePaint.setStrokeCap(Paint.Cap.ROUND);
    forePaint.setStrokeWidth(foreWidth);
    forePaint.setColor(foreColor);

    setProgress(progress);
    setClockwise(clockWise);
    setIndeterminate(true);
  }

  public void setClockwise(boolean value) {
    clockWise = value;
    updateAngle();
  }

  public void setIndeterminate(boolean value) {
    if (indeterminate = value) {
      setProgress(progress == 0 ? 70 : progress);
    }
  }

  public void setBackWidth(int width) {
    backWidth = width;
    backPaint.setStrokeWidth(backWidth);
  }

  public void setForeWidth(int width) {
    foreWidth = width;
    forePaint.setStrokeWidth(foreWidth);
  }

  public void setBaseBackgroundColor(int color) {
    baseBackgroundColor = color;
    baseBackgroundPaint.setColor(color);
  }

  public void setBackColor(int color) {
    backColor = color;
    backPaint.setColor(color);
  }

  public void setForeColor(int color) {
    foreColor = color;
    forePaint.setColor(color);
  }

  public boolean isClockWise() {
    return clockWise;
  }

  public void setMaxProgress(int value) {
    maxProgress = value;
    setProgress(getProgress());
  }

  public boolean isIndeterminate() {
    return indeterminate;
  }

  public float getProgress() {
    return progress;
  }

  public float getMaxProgress() {
    return maxProgress;
  }

  public void setProgress(float progress) {
    if (objectAnimator != null && objectAnimator.isRunning()) {
      objectAnimator.cancel();
    }
    setFinalProgress(progress);
  }

  @Keep
  private void setFinalProgress(float progress) {
    this.progress = Math.min(progress, maxProgress);
    sweepAngle = (360 * progress / maxProgress);
    updateAngle();
  }

  private void updateAngle() {
    sweepAngle = Math.abs(sweepAngle);
    sweepAngle = clockWise ? -sweepAngle : sweepAngle;
  }

  private void updateAnimation() {
    if (indeterminate) {
      long newTime = System.currentTimeMillis();
      long dt = newTime - lastUpdateTime;
      if (dt > 17) {
        dt = 17;
      }
      lastUpdateTime = newTime;

      startAngle += 360 * dt / rotationTime;
      int count = (int) (startAngle / 360);
      startAngle -= count * 360;
    } else {
      lastUpdateTime = 0;
      startAngle = -90;
    }
    invalidate();
  }

  public void setProgressWithAnimation(float progress) {
    if (progress > maxProgress) {
      progress = maxProgress;
    }
    if (progress < 0) {
      progress = 0;
    }
    if (objectAnimator != null && objectAnimator.isRunning()) {
      queueProgress = progress;
      return;
    }
    objectAnimator = ObjectAnimator.ofFloat(this, "finalProgress", getProgress(), progress);
    objectAnimator.setDuration(300);
    objectAnimator.setInterpolator(decelerateInterpolator);
    objectAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animator) {
        objectAnimator = null;
        if (queueProgress == getProgress()) {
          queueProgress = -1;
        } else if (queueProgress != -1) {
          setProgressWithAnimation(queueProgress);
        }
      }
    });
    objectAnimator.start();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int size = Math.min(getWidth(), getHeight());
    float stroke = Math.max(backWidth, foreWidth) / 2f;
    rectF.set(stroke + getPaddingLeft(), stroke + getPaddingTop(), size - stroke - getPaddingRight(), size - stroke - getPaddingBottom());
    if (baseBackgroundPaint.getColor() != 0x00000000) {
      canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, size / 2f, baseBackgroundPaint);
    }
    canvas.drawArc(rectF, 0, 360, false, backPaint);
    canvas.drawArc(rectF, startAngle, sweepAngle, false, forePaint);
    updateAnimation();
  }
}