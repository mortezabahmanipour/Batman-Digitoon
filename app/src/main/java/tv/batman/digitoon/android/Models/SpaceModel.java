package tv.batman.digitoon.android.Models;

import tv.batman.digitoon.android.Customs.RecyclerListView;

public class SpaceModel extends RecyclerListView.BaseModel {
  public int space;

  public SpaceModel() {
    this(0);
  }

  public SpaceModel(int space) {
    this._type = -2;
    this.space = space;
  }
}
