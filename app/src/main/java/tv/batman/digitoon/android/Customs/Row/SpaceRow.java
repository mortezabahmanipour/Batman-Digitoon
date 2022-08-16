package tv.batman.digitoon.android.Customs.Row;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import tv.batman.digitoon.android.Models.SpaceModel;

public class SpaceRow extends View {
  private SpaceModel space;

  public SpaceRow(@NonNull Context context) {
    super(context);
  }

  public void setModel(SpaceModel space) {
    this.space = space;
    requestLayout();
  }

  public void setModel(int space) {
    this.space = new SpaceModel(space);
    requestLayout();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, space != null && space.space > 0 ? MeasureSpec.makeMeasureSpec(space.space, MeasureSpec.EXACTLY) : heightMeasureSpec);
  }
}
