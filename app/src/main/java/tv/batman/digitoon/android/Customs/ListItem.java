package tv.batman.digitoon.android.Customs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.LayoutHelper;
import tv.batman.digitoon.android.Utils.Theme;

public class ListItem extends FrameLayout {
  private Context context;
  private int itemTextSize = 12;
  private List<String> items = new ArrayList<>();

  private class Item extends TextView implements OnClickListener {

    private int getTagWidth() {
      return getWidthTextView();
    }

    private Item(Context context, String str) {
      super(context);
      setTextSize(itemTextSize);
      setTextColor(Theme.getColor(Theme.key_app_text3));
      setText(str);
      setGravity(Gravity.CENTER);
      setSingleLine();
      setBackground(Theme.createDrawable(new Theme.SelectorBuilder().setDefaultColor(Theme.getColor(Theme.key_app_shadow)).style(Theme.RECT).radii(AndroidUtilities.dp(7))));
      setPadding(AndroidUtilities.dp(10), 0, AndroidUtilities.dp(10), 0);
      setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    }
  }

  public void setItemTextSize(int value) {
    itemTextSize = value;
    for (int i=0; i < getChildCount(); i++) {
      View view = getChildAt(i);
      if (view instanceof Item) {
        ((Item) view).setTextSize(itemTextSize);
      }
    }
  }

  public void setItems(List<String> items) {
    this.items.clear();
    this.items.addAll(items);
    int width = AndroidUtilities.displaySize.x - (getPaddingLeft() + getPaddingRight());
    int totalWidth = 0;
    int line = 1;
    for(int i = 0; i < items.size(); i++) {
      String str = items.get(i);
      Item tag = new Item(context, str);
      int left;
      if (totalWidth + tag.getTagWidth() <= width) {
        left = totalWidth;
      } else {
        line++;
        left = 0;
        totalWidth = 0;
      }
      totalWidth += tag.getTagWidth() + AndroidUtilities.dp(10);
      int top = ((line - 1) * AndroidUtilities.dp(35)) + (AndroidUtilities.dp(10) * Math.max(0, line - 1));
//      top += AndroidUtilities.dp(20);
      addView(tag, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, AndroidUtilities.dp(35), 3, left, top, AndroidUtilities.dp(0), AndroidUtilities.dp(0)));
    }
  }

  public void clear() {
    items.clear();
    removeAllViews();
  }

  public ListItem(Context context) {
    this(context, null);
  }

  public ListItem(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ListItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    setMotionEventSplittingEnabled(false);
  }
}
