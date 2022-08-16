package tv.batman.digitoon.android.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import tv.batman.digitoon.android.ApplicationLoader;
import tv.batman.digitoon.android.R;
import tv.batman.digitoon.android.Session.Session;

// created by morti

public class Theme {
  // shape style
  public static final int NONE = 0;
  public static final int RECT = 1;
  public static final int OVAL = 2;
  // none shape
  public static final int TOP = 1;
  public static final int BOTTOM = 2;
  public static final int LEFT = 4;
  public static final int RIGHT = 8;

  public static final String key_app_primary = "app_primary";
  public static final String key_app_primary_dark = "app_primary_dark";
  public static final String key_app_accent = "app_accent";
  public static final String key_action_bar_default = "app_action_bar_default";
  public static final String key_app_background = "app_background";
  public static final String key_app_default = "app_default";
  public static final String key_app_default_2 = "app_default_2";
  public static final String key_app_default_3 = "app_default_3";
  public static final String key_app_card_row_background = "app_card_row_background";
  public static final String key_app_toast = "app_toast";
  public static final String key_app_toast_text = "app_toast_text";
  public static final String key_app_shadow = "app_shadow";
  public static final String key_app_shadow_2 = "app_shadow_2";
  public static final String key_app_shadow_3 = "app_shadow_3";
  public static final String key_app_shadow_4 = "app_shadow_4";
  public static final String key_app_selector = "app_selector";
  public static final String key_app_selector_white = "app_selector_white";
  public static final String key_app_text = "app_text";
  public static final String key_app_text2 = "app_text2";
  public static final String key_app_text3 = "app_text3";
  public static final String key_app_text4 = "app_text4";
  public static final String key_app_text5 = "app_text5";
  public static final String key_app_text6 = "app_text6";

  private static HashMap<String, Integer> colors = new HashMap<>();

  public static class ScaleSelector implements View.OnTouchListener {
    private View target;
    private AnimatorSet animatorSet;

    private ScaleSelector(View target) {
      this.target = target;
      target.setOnTouchListener(this);
    }

    public static void setTarget(View target) {
      new ScaleSelector(target);
    }

    private void action(boolean value) {
      if (animatorSet != null && animatorSet.isRunning()) {
        animatorSet.cancel();
      }
      float size = Math.max(target.getMeasuredHeight(), target.getMeasuredWidth());
      float currentScale = target.getScaleX();
      float nextScale = value ? (size - AndroidUtilities.dp(8)) / size : 1.0f;
      animatorSet = new AnimatorSet();
      animatorSet.playTogether(ObjectAnimator.ofFloat(target, View.SCALE_X, currentScale, nextScale), ObjectAnimator.ofFloat(target, View.SCALE_Y, currentScale, nextScale));
      animatorSet.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          animatorSet = null;
        }
      });
      animatorSet.setDuration(100);
      animatorSet.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          action(true);
          return true;
        case MotionEvent.ACTION_UP:
          action(false);
          target.performClick();
          return true;
        case MotionEvent.ACTION_OUTSIDE:
        case MotionEvent.ACTION_CANCEL:
          action(false);
          return true;
      }
      return false;
    }
  }

  static {
    applyTheme();
  }

  public static void applyTheme() {
    int theme = Session.getInstance().getTheme();
    colors.clear();
    Resources resources = ApplicationLoader.applicationContext.getResources();
    colors.put(key_app_default, resources.getColor(R.color.color_default));
    colors.put(key_app_default_2, resources.getColor(R.color.color_default_2));
    colors.put(key_app_default_3, resources.getColor(R.color.color_default_3));
    colors.put(key_app_selector_white, 0xA0FFFFFF);
    if (theme == 0) {
//      colors.put(key_app_primary, 0xFF018786);
      colors.put(key_app_primary, 0xFFFFFFFF);
      colors.put(key_app_primary_dark, 0xFFFFFFFF);
      colors.put(key_app_accent, 0xFF018786);
      colors.put(key_action_bar_default, 0xFFFFFFFF);
      colors.put(key_app_background, 0xFFEFEEEE);
      colors.put(key_app_card_row_background, 0xFFF7F7F7);
      colors.put(key_app_toast, 0xBF000000);
      colors.put(key_app_toast_text, 0xFFF3F3F3);
      colors.put(key_app_shadow_2, 0x10000000);
      colors.put(key_app_shadow_3, 0x15000000);
      colors.put(key_app_shadow, 0x08000000);
      colors.put(key_app_shadow_4, 0x20000000);
      colors.put(key_app_selector, 0x10000000);
      colors.put(key_app_text, 0xFF030A16);
      colors.put(key_app_text2, 0xFF424750);
      colors.put(key_app_text3, 0xFF81848A);
      colors.put(key_app_text4, 0xFFA2A4A9);
      colors.put(key_app_text5, 0xFFA1A3A8);
      colors.put(key_app_text6, 0xFFC8CACF);
    } else if (theme == 1) {
      colors.put(key_app_primary, 0xFF454545);
      colors.put(key_app_primary_dark, 0xFF454545);
      colors.put(key_app_accent, 0xFF018786);
      colors.put(key_action_bar_default, 0xFF000000);
      colors.put(key_app_background, 0xFF212121);
      colors.put(key_app_card_row_background, 0xFF303030);
      colors.put(key_app_toast, 0xBFFFFFFF);
      colors.put(key_app_toast_text, 0xFF030A16);
      colors.put(key_app_shadow, 0x08FFFFFF);
      colors.put(key_app_shadow_2, 0x10FFFFFF);
      colors.put(key_app_shadow_3, 0x15FFFFFF);
      colors.put(key_app_shadow_4, 0x20FFFFFF);
      colors.put(key_app_selector, 0x10FFFFFF);
      colors.put(key_app_text, 0xFFF3F3F3);
      colors.put(key_app_text2, 0xFFC1BEBE);
      colors.put(key_app_text3, 0xFF858585);
      colors.put(key_app_text4, 0xFF777676);
      colors.put(key_app_text5, 0xFF606060);
      colors.put(key_app_text6, 0xFF494949);
    }
  }

  public static int getColor(String key) {
    Integer value = colors.get(key);
    return value != null ? value : 0xFF000000;
  }

  public static void setDrawableColor(Drawable drawable, int color) {
    if (drawable == null) {
      return;
    }
    if (drawable instanceof ShapeDrawable) {
      ((ShapeDrawable) drawable).getPaint().setColor(color);
    } else {
      drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
    }
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  public static Drawable getDrawableColor(int res, int color) {
    Drawable drawable = ApplicationLoader.applicationContext.getResources().getDrawable(res).mutate();
    if (color != 0) {
      setDrawableColor(drawable, color);
    }
    return drawable;
  }

  public static Drawable createDrawable(@NonNull SelectorBuilder builder) {
    return createDrawable(builder.defaultColor, builder.defaultStrokeColor, builder.defaultStrokeWidth, builder.style, builder.radii);
  }

  public static Drawable createDrawable(int color, int strokeColor, int strokeWidth, int style, float... radii) {
    GradientDrawable gradientDrawable = new GradientDrawable();
    if (style == RECT) {
      gradientDrawable.setShape(GradientDrawable.RECTANGLE);
      if (radii != null) {
        gradientDrawable.setCornerRadii(radii.length == 8 ? radii : radii.length == 4 ? new float[]{radii[0], radii[0], radii[1], radii[1], radii[2], radii[2], radii[3], radii[3]} : new float[]{radii[0], radii[0], radii[0], radii[0], radii[0], radii[0], radii[0], radii[0]});
      }
    } else if (style == OVAL) {
      gradientDrawable.setShape(GradientDrawable.OVAL);
    }
    if (color != 0) {
      gradientDrawable.setColor(color);
    }
    if (strokeWidth > 0) {
      gradientDrawable.setStroke(strokeWidth, strokeColor);
    }
    return gradientDrawable;
  }

  public static Drawable createSelectorDrawable(SelectorBuilder builder) {
    return createSelectorDrawable(builder, false);
  }

  public static Drawable createSelectorDrawable(SelectorBuilder builder, boolean foreground) {
    if (builder == null) {
      return null;
    }
    Drawable defaultDrawable = createDrawable(foreground ? 0 : builder.defaultColor, foreground ? 0 : builder.defaultStrokeColor, foreground ? 0 : builder.defaultStrokeWidth, builder.style, builder.radii);
    if (!builder.selectable) {
      return defaultDrawable;
    }
    if (!builder.simple) {
      Drawable pressedDrawable = createDrawable(0xffffffff, 0, 0, builder.style, builder.radii);
      ColorStateList colorStateList = new ColorStateList(
        new int[][]{StateSet.WILD_CARD},
        new int[]{builder.pressedColor}
      );
      return new RippleDrawable(colorStateList, defaultDrawable, pressedDrawable);
    } else {
      int color = builder.defaultColor == 0 ? ColorUtils.setAlphaComponent(builder.pressedColor, 125) : builder.pressedColor;
      Drawable pressedDrawable = createDrawable(color, builder.pressedStrokeColor, builder.pressedStrokeWidth, builder.pressedStyle, builder.pressedRadii);
      StateListDrawable stateListDrawable = new StateListDrawable();
      stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
      stateListDrawable.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
      stateListDrawable.addState(StateSet.WILD_CARD, defaultDrawable);
      return stateListDrawable;
    }
  }

  public static Drawable createRadialGradientDrawable(int[] colors, float radius) {
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColors(colors);
    gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
    gradientDrawable.setGradientRadius(radius);
    gradientDrawable.setShape(GradientDrawable.RECTANGLE);
    return gradientDrawable;
  }

  public static Drawable createShadowGradientDrawable(GradientDrawable.Orientation orientation, int[] colors, float... radii) {
    GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
    gradientDrawable.setCornerRadii(radii.length == 8 ? radii : radii.length == 4 ? new float[] {radii[0], radii[0], radii[1], radii[1], radii[2], radii[2], radii[3], radii[3]} : new float[]{radii[0], radii[0], radii[0], radii[0], radii[0], radii[0], radii[0], radii[0]});
    return gradientDrawable;
  }

  public static void setSelectorView(@NonNull View view, SelectorBuilder builder) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      view.setBackground(builder == null ? null : Theme.createDrawable(builder));
      view.setForeground(builder == null ? null : Theme.createSelectorDrawable(builder, true));
    } else {
      view.setBackground(builder == null ? null : Theme.createSelectorDrawable(builder));
    }
  }

  public static void setBackgroundColorWithTag(ViewGroup viewGroup, int color, String tag) {
    for (int i=0; i < viewGroup.getChildCount(); i++) {
      View view = viewGroup.getChildAt(i);
      if (view instanceof ViewGroup) {
        setBackgroundColorWithTag((ViewGroup) view, color, tag);
      } else {
        if (tag.equals(view.getTag())) {
          view.setBackgroundColor(color);
        }
      }
    }
  }

  public static void disableShape(float[] floats, int flag) {
    if (floats == null || floats.length != 8) {
      return;
    }
    switch (flag) {
      case 1:
        floats[0] = floats[1] = floats[2] = floats[3] = 0;
        break;
      case 2:
        floats[4] = floats[5] = floats[6] = floats[7] = 0;
        break;
      case 4:
        floats[0] = floats[1] = floats[6] = floats[7] = 0;
        break;
      case 8:
        floats[2] = floats[3] = floats[4] = floats[5] = 0;
        break;
      case 3:
        floats[0] = floats[1] = floats[2] = floats[3] = floats[4] = floats[5] = floats[6] = floats[7] = 0;
        break;
      case 5:
        floats[0] = floats[1] = floats[2] = floats[3] = floats[6] = floats[7] = 0;
        break;
      case 9:
        floats[0] = floats[1] = floats[2] = floats[3] = floats[4] = floats[5] = 0;
        break;
      case 6:
        floats[0] = floats[1] = floats[4] = floats[5] = floats[6] = floats[7] = 0;
        break;
      case 10:
        floats[2] = floats[3] = floats[4] = floats[5] = floats[6] = floats[7] = 0;
        break;
      case 12:
        floats[0] = floats[1] = floats[2] = floats[3] = floats[4] = floats[5] = floats[6] = floats[7] = 0;
        break;
    }
  }

  public static class SelectorBuilder {
    private boolean simple;
    private boolean selectable = true;
    private int defaultColor;
    private int defaultStrokeColor;
    private int defaultStrokeWidth;
    private int style;
    private float[] radii;
    private int pressedColor;
    private int pressedStrokeColor;
    private int pressedStrokeWidth;
    private int pressedStyle;
    private float[] pressedRadii;

    public SelectorBuilder setSimple(boolean simple) {
      this.simple = simple;
      return this;
    }

    public SelectorBuilder setSelectable(boolean selectable) {
      this.selectable = selectable;
      return this;
    }

    public SelectorBuilder setDefaultColor(int defaultColor) {
      this.defaultColor = defaultColor;
      this.pressedColor = defaultColor;
      return this;
    }

    public SelectorBuilder setDefaultStrokeColor(int defaultStrokeColor) {
      this.defaultStrokeColor = defaultStrokeColor;
      this.pressedStrokeColor = defaultStrokeColor;
      return this;
    }

    public SelectorBuilder setDefaultStrokeWidth(int defaultStrokeWidth) {
      this.defaultStrokeWidth = defaultStrokeWidth;
      this.pressedStrokeWidth = defaultStrokeWidth;
      return this;
    }

    public SelectorBuilder style(int style) {
      this.style = style;
      if (this.pressedStyle == NONE) {
        this.pressedStyle = style;
      }
      return this;
    }

    public SelectorBuilder radii(float ...radii) {
      this.radii = radii.length == 8 ? radii : radii.length == 4 ? new float[] {radii[0], radii[0], radii[1], radii[1], radii[2], radii[2], radii[3], radii[3]} : new float[]{radii[0], radii[0], radii[0], radii[0], radii[0], radii[0], radii[0], radii[0]};
      if (this.pressedRadii == null || pressedRadii.length == 0) {
        this.pressedRadii = this.radii;
      }
      return this;
    }

    public SelectorBuilder pressedColor(int pressedColor) {
      this.pressedColor = pressedColor;
      return this;
    }

    public SelectorBuilder setPressedStrokeColor(int pressedStrokeColor) {
      this.pressedStrokeColor = pressedStrokeColor;
      return this;
    }

    public SelectorBuilder setPressedStrokeWidth(int pressedStrokeWidth) {
      this.pressedStrokeWidth = pressedStrokeWidth;
      return this;
    }

    public SelectorBuilder pressedStyle(int pressedStyle) {
      this.pressedStyle = pressedStyle;
      return this;
    }

    public SelectorBuilder setPressedRadii(float ...pressedRadii) {
      this.pressedRadii = pressedRadii.length == 8 ? pressedRadii : new float[]{pressedRadii[0], pressedRadii[0], pressedRadii[0], pressedRadii[0], pressedRadii[0], pressedRadii[0], pressedRadii[0], pressedRadii[0]};
      return this;
    }

    public SelectorBuilder setNoneShape(int noneShape) {
      disableShape(radii, noneShape);
      disableShape(pressedRadii, noneShape);
      return this;
    }
  }
}
