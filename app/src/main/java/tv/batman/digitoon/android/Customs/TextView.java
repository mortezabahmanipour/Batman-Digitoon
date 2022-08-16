package tv.batman.digitoon.android.Customs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.AppLog;
import tv.batman.digitoon.android.Utils.Theme;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

// created by morti

public class TextView extends AppCompatTextView {
    protected Context context;
    private AnimatorSet animatorSet;
    private boolean isSquare;
    private int drawableSize;
    private float drawableAlpha = 1.0f;
    private int drawableTintColor;
    private int faceStyle = Typeface.NORMAL;

    public TextView(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initialize();
    }

    protected void initialize() {
        setIncludeFontPadding(false);
        setTypeface(AndroidUtilities.IRANSans_FaNum, faceStyle);
        setDrawableSize();
        setText(getText());
    }

    protected void restViews() {
        requestLayout();
        invalidate();
    }

    private void setDrawableSize() {
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(getDrawableResize(drawables[0]), getDrawableResize(drawables[1]), getDrawableResize(drawables[2]), getDrawableResize(drawables[3]));
    }

    private Drawable getDrawableResize(Drawable drawable) {
        if (drawable == null) return null;
        try {
            drawable = drawable.mutate();
            drawable.setAlpha((int)(drawableAlpha * 255));
            if (drawableSize > 0) {
                drawable.setBounds(0, 0, drawableSize, drawableSize);
            }
            if (drawableTintColor != 0) {
                Theme.setDrawableColor(drawable, drawableTintColor);
            }
        } catch (Exception e) {
            AppLog.e(TextView.class, e.getMessage());
        }
        return drawable;
    }

    public void setSquare(boolean square) {
        this.isSquare = square;
        restViews();
    }

    public void setTypefaceStyle(int faceStyle) {
        this.faceStyle = faceStyle;
        setTypeface(getTypeface(), faceStyle);
        restViews();
    }

    public void setDrawableTintColor(@ColorInt int drawableTintColor) {
        this.drawableTintColor = drawableTintColor;
        setDrawableSize();
    }

    public void hide() {
        if (getVisibility() == View.INVISIBLE) return;

        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f, 0.0f));
        animatorSet.setDuration(200);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(View.INVISIBLE);

                animatorSet = null;
            }
        });
        animatorSet.start();
    }

    public void show() {
        if (getVisibility() == View.VISIBLE) return;

        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }

        setVisibility(View.VISIBLE);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f, 1.0f));
        animatorSet.setDuration(200);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                animatorSet = null;
            }
        });
        animatorSet.start();
    }

    public void show(boolean show) {
        if (show) {
            show();
        } else {
            hide();
        }
    }

    public void setDrawableSize(int drawableSize) {
        this.drawableSize = drawableSize;
        setDrawableSize();
    }

    public void setDrawableAlpha(float drawableAlpha) {
        this.drawableAlpha = drawableAlpha;
        setDrawableSize();
    }

    public int getDrawableSize() {
        return drawableSize;
    }

    public int getWidthTextView() {
        return getWidthTextView(getText().toString());
    }

    public int getWidthTextView(String str) {
        Drawable[] drawables = getCompoundDrawables();
        return (int) getPaint().measureText(str) + (drawables[0] != null ? drawableSize + getCompoundDrawablePadding() : 0) + (drawables[2] != null ? drawableSize + getCompoundDrawablePadding() : 0) + getPaddingLeft() + getPaddingRight();
    }

    public int getHeightTextView() {
        Drawable[] drawables = getCompoundDrawables();
        int lineCount = getLineCount();
        return (int) (getTextSize() * lineCount) + (int) (getLineSpacingExtra() * (lineCount - 1)) + (drawables[1] != null ? drawableSize + getCompoundDrawablePadding() : 0) + (drawables[2] != null ? drawableSize + getCompoundDrawablePadding() : 0) + getPaddingTop() + getPaddingBottom();
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(getDrawableResize(left), getDrawableResize(top), getDrawableResize(right), getDrawableResize(bottom));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isSquare) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
