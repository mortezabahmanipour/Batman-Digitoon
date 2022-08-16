package tv.batman.digitoon.android.Customs.Row;

import android.content.Context;
import android.widget.FrameLayout;

import tv.batman.digitoon.android.Customs.RecyclerListView;

// created by morti

public abstract class BaseRow<T extends RecyclerListView.BaseModel> extends FrameLayout {
    protected Context context;
    protected boolean isAttachedToWindow;
    public T model;

    public BaseRow(Context context) {
        super(context);
        this.context = context;
        setMotionEventSplittingEnabled(false);
    }

    public void notifyChanged() {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        if (model != null) {
            model._row = this;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        if (model != null && model._row != null && model._row.equals(this)) {
            model._row = null;
        }
    }
}
