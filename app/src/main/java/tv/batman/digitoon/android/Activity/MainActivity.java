package tv.batman.digitoon.android.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tv.batman.digitoon.android.API;
import tv.batman.digitoon.android.ApplicationLoader;
import tv.batman.digitoon.android.Customs.SwipeRefreshLayout;
import tv.batman.digitoon.android.Customs.ImageView;
import tv.batman.digitoon.android.Customs.RecyclerListView;
import tv.batman.digitoon.android.Customs.Row.BaseCardRow;
import tv.batman.digitoon.android.Customs.Row.SpaceRow;
import tv.batman.digitoon.android.Customs.TextView;
import tv.batman.digitoon.android.Fragments.FragmentController;
import tv.batman.digitoon.android.Fragments.VideoInfoFragment;
import tv.batman.digitoon.android.Listeners.FragmentNavigation;
import tv.batman.digitoon.android.Models.SpaceModel;
import tv.batman.digitoon.android.Models.VideoModel;
import tv.batman.digitoon.android.R;
import tv.batman.digitoon.android.Session.Database;
import tv.batman.digitoon.android.Session.Session;
import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.AppLog;
import tv.batman.digitoon.android.Utils.DispatchQueue;
import tv.batman.digitoon.android.Utils.LayoutHelper;
import tv.batman.digitoon.android.Utils.RequestHelper;
import tv.batman.digitoon.android.Utils.Theme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

// created by morti

@SuppressLint("NotifyDataSetChanged")
public class MainActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecyclerListView.RecyclerListViewClickListener, RecyclerListView.RecyclerListViewListener, FragmentNavigation {

  private static volatile DispatchQueue queue = new DispatchQueue("mainActivityQueue");
  private ViewGroup baseLayout;
  private ViewGroup toolbar;
  private View shadowToolbar;
  private ImageView ivDarkMode;
  private ImageView ivListMode;
  private TextView tvTitle;
  private TextView tvMessage;
  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerListView recyclerListView;
  private VideosAdapter adapter;
  private GridLayoutManager layoutManager;
  private List<RecyclerListView.BaseModel> videos = new ArrayList<>();
  private int listMode = 0;
  private boolean spacePositionVisible;
  private boolean isTopShadow;
  private ObjectAnimator objectAnimator;
  private FragmentController fragmentController;

  private class VideosAdapter extends RecyclerListView.BaseAdapter {

    public VideosAdapter(Context context, List<RecyclerListView.BaseModel> list) {
      super(context, list);
    }

    @Override
    public @NonNull
    RecyclerListView.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
      View view;
      switch (i) {
        case 1:
          view = new VideosRow(context, listMode);
          break;
        case -2:
          view = new SpaceRow(context);
          break;
        default:
          view = new View(context);
          view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
          break;
      }
      return new RecyclerListView.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListView.Holder holder, int position) {
      switch (holder.getItemViewType()) {
        case 1:
          ((VideosRow) holder.itemView).setModel((VideoModel) getItem(position));
          break;
        case -2:
          ((SpaceRow) holder.itemView).setModel((SpaceModel) getItem(position));
          break;
      }
    }
  }

  private static class VideosRow extends BaseCardRow<VideoModel> {

    private ImageView ivThumbnail;
    private ImageView ivVideo;
    private TextView tvTitle;
    private TextView tvYear;
    private TextView tvMoreInfo;
    private int layoutWidth = (AndroidUtilities.displaySize.x - AndroidUtilities.dp(30)) / 2;
    private int listMode;

    private void setModel(VideoModel model) {
      this.model = model;
      tvTitle.setText(model.title);
      tvYear.setText(model.year);
      if (listMode != 0) {
        tvTitle.setTextColor(Theme.getColor(Theme.key_app_text));
        tvYear.setTextColor(Theme.getColor(Theme.key_app_text3));
        tvMoreInfo.setTextColor(Theme.getColor(Theme.key_app_text2));
        tvMoreInfo.setDrawableTintColor(Theme.getColor(Theme.key_app_text4));
        tvMoreInfo.setBackground(Theme.createDrawable(Theme.getColor(Theme.key_app_shadow), 0, 0, Theme.RECT, AndroidUtilities.dp(3)));
      }
      Glide.with(context).asBitmap().load(model.poster).override(AndroidUtilities.dp(250)).into(ivThumbnail);
      setBackground(Theme.createDrawable(new Theme.SelectorBuilder().setDefaultColor(Theme.getColor(Theme.key_app_card_row_background)).pressedColor(Theme.getColor(Theme.key_app_selector)).style(Theme.RECT).radii(AndroidUtilities.dp(7.5f))));
    }

    @SuppressLint("SetTextI18n")
    public VideosRow(Context context, int listMode) {
      super(context);
      this.listMode = listMode;
      setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      setCardElevation(AndroidUtilities.dp(3));
      Theme.ScaleSelector.setTarget(this);

      ivThumbnail = new ImageView(context);
      ivThumbnail.setRadius(AndroidUtilities.dp(7));
      ivThumbnail.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
      if (listMode == 0) {
        int[] size = AndroidUtilities.getHeightWithParent(layoutWidth, AndroidUtilities.dp(300), AndroidUtilities.dp(444));
        addView(ivThumbnail, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, size[1]));
      } else {
        int[] size = AndroidUtilities.getHeightWithParent(AndroidUtilities.dp(100), AndroidUtilities.dp(300), AndroidUtilities.dp(444));
        addView(ivThumbnail, LayoutHelper.createFrame(AndroidUtilities.dp(100), size[1], 3, AndroidUtilities.dp(10), AndroidUtilities.dp(10), AndroidUtilities.dp(10), AndroidUtilities.dp(10)));
      }

      ivVideo = new ImageView(context);
      ivVideo.setImageDrawable(Theme.getDrawableColor(R.drawable.ic_play_arrow, 0));
      ivVideo.setPadding(AndroidUtilities.dp(2.5f), AndroidUtilities.dp(2.5f), AndroidUtilities.dp(2.5f), AndroidUtilities.dp(2.5f));
      ivVideo.setBackground(Theme.createRadialGradientDrawable(new int[] {0x90000000, 0x00000000}, AndroidUtilities.dp(12.5f)));
      if (listMode == 0) {
        addView(ivVideo, LayoutHelper.createFrame(AndroidUtilities.dp(25), AndroidUtilities.dp(25), 5, 0, AndroidUtilities.dp(10), AndroidUtilities.dp(10), 0));
      } else {
        addView(ivVideo, LayoutHelper.createFrame(AndroidUtilities.dp(25), AndroidUtilities.dp(25), 3, AndroidUtilities.dp(80), AndroidUtilities.dp(15), AndroidUtilities.dp(15), 0));
      }

      tvTitle = new TextView(context);
      tvTitle.setTextSize(12);
      tvTitle.setTypefaceStyle(Typeface.BOLD);
      tvTitle.setTextColor(0xFFFFFFFF);
      tvTitle.setGravity(3);
      tvTitle.setMaxLines(2);
      if (listMode == 0) {
        tvTitle.setPadding(AndroidUtilities.dp(10), AndroidUtilities.dp(30), AndroidUtilities.dp(10), 0);
        tvTitle.setBackground(Theme.createShadowGradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x00000000, 0xC1000000, 0xFF000000}, 0, 0, AndroidUtilities.dp(6), AndroidUtilities.dp(6)));
        addView(tvTitle, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, AndroidUtilities.dp(108), Gravity.BOTTOM, 0, 0, 0, 0));
      } else {
        addView(tvTitle, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, AndroidUtilities.dp(120), AndroidUtilities.dp(25), AndroidUtilities.dp(10), 0));
      }

      tvYear = new TextView(context);
      tvYear.setTextSize(12);
      tvYear.setGravity(3);
      if (listMode == 0) {
        tvYear.setTextColor(0xFFC1BEBE);
        tvYear.setDrawableTintColor(0xFFC1BEBE);
        tvYear.setDrawableSize(AndroidUtilities.dp(18));
        tvYear.setCompoundDrawables(null, null, Theme.getDrawableColor(R.drawable.ic_arrow_right, 0), null);
        addView(tvYear, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | 3, AndroidUtilities.dp(10), 0, AndroidUtilities.dp(7), AndroidUtilities.dp(10)));
      } else {
        addView(tvYear, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, AndroidUtilities.dp(120), AndroidUtilities.dp(85), 0, 0));
      }

      if (listMode != 0) {
        tvMoreInfo = new TextView(context);
        tvMoreInfo.setTextSize(12);
        tvMoreInfo.setGravity(Gravity.CENTER);
        tvMoreInfo.setText("More Info");
        tvMoreInfo.setPadding(AndroidUtilities.dp(10), 0, AndroidUtilities.dp(5), 0);
        tvMoreInfo.setCompoundDrawablePadding(AndroidUtilities.dp(5));
        tvMoreInfo.setDrawableSize(AndroidUtilities.dp(22));
        tvMoreInfo.setCompoundDrawables(null, null, Theme.getDrawableColor(R.drawable.ic_arrow_right, 0), null);
        addView(tvMoreInfo, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, AndroidUtilities.dp(30), Gravity.BOTTOM | 3, AndroidUtilities.dp(120), 0, 0, AndroidUtilities.dp(20)));
      }
    }
  }

  private void initializeViews() {
    baseLayout = findViewById(R.id.baseLayout);
    toolbar = findViewById(R.id.toolbar);
    shadowToolbar = findViewById(R.id.shadowToolbar);
    ivDarkMode = findViewById(R.id.ivDarkMode);
    ivListMode = findViewById(R.id.ivListMode);
    tvTitle = findViewById(R.id.tvTitle);
    tvMessage = findViewById(R.id.tvMessage);
    swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    recyclerListView = findViewById(R.id.recyclerListView);
  }

  private void initializeDefaults() {
    fragmentController = FragmentController.newInstance(getSupportFragmentManager());
    updateTheme();
    tvTitle.setTypefaceStyle(Typeface.BOLD);
    swipeRefreshLayout.setTargetSpace(AndroidUtilities.toolbarHeight);
    recyclerListView.setAdapter(adapter = new VideosAdapter(activity, videos));
    listMode = Session.getInstance().getInt("list_mode", 0);
    recyclerListView.setLayoutManager(layoutManager = new GridLayoutManager(activity, listMode == 0 ? 2 : 1));
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        return position == videos.size() || position == 0 ? layoutManager.getSpanCount() : 1;
      }
    });
    recyclerListView.addItemDecoration(new RecyclerListView.Decoration(AndroidUtilities.dp(10)) {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state, int position, int count) {
        if (listMode == 1) {
          if (position != 0) {
            outRect.left = space;
            outRect.right = space;
          }
          outRect.bottom = space;
          return;
        }
        if (position != 0) {
          position--;
          if ((position + 1) <= 2) {
            outRect.top = space;
          }
          if ((position + 1) % 2 == 0) {
            outRect.right = space;
            outRect.left = space / 2;
          } else {
            outRect.right = space / 2;
            outRect.left = space;
          }
          outRect.bottom = space;
        }
      }
    });
  }

  private void initializeListeners() {
    ivDarkMode.setOnClickListener(this);
    ivListMode.setOnClickListener(this);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerListView.setRecyclerListViewClickListener(this);
    recyclerListView.setRecyclerListViewListener(this);
  }

  private void loadVideosFromCache() {
    swipeRefreshLayout.setRefreshing(true);
    AndroidUtilities.setVisibility(tvMessage, View.GONE);
    videos.clear();
    adapter.notifyDataSetChanged();
    queue.postRunnable(() -> {
      videos.addAll(Database.getInstance().getVideos());
      for (RecyclerListView.BaseModel videoModel : videos) {
        videoModel._type = 1;
      }
      if (!videos.isEmpty()) {
        videos.add(0, new SpaceModel(AndroidUtilities.toolbarHeight));
      }
      AndroidUtilities.runOnUIThread(() -> {
        adapter.notifyDataSetChanged();
        loadVideosFromServer();
      });
    });
  }

  private void loadVideosFromServer() {
    swipeRefreshLayout.setRefreshing(true);
    AndroidUtilities.setVisibility(tvMessage, View.GONE);
    requestsId[0] = RequestHelper.getInstance().addToRequestQueue(tag, API.BASE + "&s=batman", null, (isSuccessful, response) -> {
      if (!isSuccessful) {
        swipeRefreshLayout.setRefreshing(false);
        if (videos.isEmpty()) {
          AndroidUtilities.setVisibility(tvMessage, View.VISIBLE);
          tvMessage.setText("داده ای برای نمایش وجود ندارد");
        }
        if (ApplicationLoader.isNetworkOnline()) {
          AndroidUtilities.toast(activity, "مشکل در ارتباط با سرور");
        } else {
          AndroidUtilities.toast(activity, "دستگاه شما به اینترنت متصل نیست");
        }
        return;
      }
      queue.postRunnable(() -> {
        List<VideoModel> videos = new ArrayList<>();
        try {
          JSONObject baseJSONObject = (JSONObject) new JSONTokener(response).nextValue();
          if (!baseJSONObject.isNull("Search")) {
            JSONArray videosJSONArray = baseJSONObject.getJSONArray("Search");
            for (int i=0; i < videosJSONArray.length(); i++) {
              JSONObject rowJSONObject = videosJSONArray.getJSONObject(i);
              VideoModel video = new VideoModel();
              video._type = 1;
              video.imdb_id = rowJSONObject.getString("imdbID");
              video.title = rowJSONObject.getString("Title");
              video.year = rowJSONObject.getString("Year");
              video.type = rowJSONObject.getString("Type");
              video.poster = rowJSONObject.getString("Poster");
              videos.add(video);
            }
          }
        } catch (Exception e) {
          AppLog.e(MainActivity.class, e);
        }
        this.videos.clear();
        if (!videos.isEmpty()) {
          this.videos.add(0, new SpaceModel(AndroidUtilities.toolbarHeight));
        }
        this.videos.addAll(videos);
        Database.getInstance().insertVideos(videos);
        AndroidUtilities.runOnUIThread(() -> {
          swipeRefreshLayout.setRefreshing(false);
          if (this.videos.isEmpty()) {
            AndroidUtilities.setVisibility(tvMessage, View.VISIBLE);
            tvMessage.setText("داده ای برای نمایش وجود ندارد");
          }
          adapter.notifyDataSetChanged();
        });
      });
    });
  }

  private void animationTopShadow(boolean show) {
    if (isTopShadow == show) {
      return;
    }
    isTopShadow = show;
    if (objectAnimator != null && objectAnimator.isRunning()) {
      objectAnimator.cancel();
    }
    objectAnimator = ObjectAnimator.ofFloat(shadowToolbar, View.ALPHA, isTopShadow ? 1.0f : 0.0f);
    objectAnimator.setDuration(150);
    objectAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
    objectAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        objectAnimator = null;
      }
    });
    objectAnimator.start();
  }

  private void updateTheme() {
    baseLayout.setBackgroundColor(Theme.getColor(Theme.key_app_background));
    toolbar.setBackgroundColor(Theme.getColor(Theme.key_app_primary));
    tvMessage.setTextColor(Theme.getColor(Theme.key_app_text2));
    shadowToolbar.setBackground(Theme.createShadowGradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x15000000, 0x00000000}, 0.0f));
    ivDarkMode.setBackground(Theme.createSelectorDrawable(new Theme.SelectorBuilder().pressedColor(Theme.getColor(Theme.key_app_selector)).style(Theme.OVAL)));
    ivDarkMode.setImageDrawable(Theme.getDrawableColor(Session.getInstance().getTheme() == 0 ? R.drawable.ic_dark_mode : R.drawable.ic_light_mode, Theme.getColor(Theme.key_app_text2)));
    ivListMode.setBackground(Theme.createSelectorDrawable(new Theme.SelectorBuilder().pressedColor(Theme.getColor(Theme.key_app_selector)).style(Theme.OVAL)));
    ivListMode.setImageDrawable(Theme.getDrawableColor(listMode == 0 ? R.drawable.ic_list_mode_list : R.drawable.ic_list_mode_grid, Theme.getColor(Theme.key_app_text2)));
  }

  private void actionDarkMode() {
    ivDarkMode.setEnabled(false);
    AndroidUtilities.runOnUIThread(() -> ivDarkMode.setEnabled(true), 500);
    queue.postRunnable(() -> {
      int theme = Session.getInstance().getTheme();
      Session.getInstance().setTheme(theme == 0 ? 1 : 0);
      Theme.applyTheme();
      AndroidUtilities.runOnUIThread(() -> {
        updateTheme();
        setStatusBarTheme();
        adapter.notifyDataSetChanged();
      });
    });
  }

  private void actionListMode() {
    int currentPosition = 0;
    if (recyclerListView.getChildCount() > 0) {
      View firstChild = recyclerListView.getChildAt(0);
      currentPosition = recyclerListView.getChildAdapterPosition(firstChild);
    }
    if (listMode == 0) {
      listMode = 1;
      layoutManager.setSpanCount(1);
    } else {
      listMode = 0;
      layoutManager.setSpanCount(2);
    }
    ivListMode.setImageDrawable(Theme.getDrawableColor(listMode == 0 ? R.drawable.ic_list_mode_list : R.drawable.ic_list_mode_grid, Theme.getColor(Theme.key_app_text2)));
    Session.getInstance().setInt("list_mode", listMode);
    recyclerListView.setAdapter(adapter);
    recyclerListView.scrollToPosition(currentPosition);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initializeViews();
    initializeDefaults();
    initializeListeners();
    loadVideosFromCache();
  }

  @Override
  public void onClick(View v) {
    if (v.equals(ivDarkMode)) {
      actionDarkMode();
    } else if (v.equals(ivListMode)) {
      actionListMode();
    }
  }

  @Override
  public void onRefresh() {
    toolbar.setTranslationY(0.0f);
    swipeRefreshLayout.setTranslationY(0.0f);
    shadowToolbar.setTranslationY(0.0f);
    RequestHelper.getInstance().cancelId(requestsId[0]);
    loadVideosFromServer();
  }

  @Override
  public void showFragment(DialogFragment fragment) {
    fragmentController.showFragment(fragment);
  }

  @Override
  public void onRecyclerListViewPositionClicked(RecyclerView recyclerView, int position) {
    if (position < 0 || position >= videos.size()) {
      return;
    }
    RecyclerListView.BaseModel base = videos.get(position);
    if (base._type == 1) {
      VideoModel video = (VideoModel) base;
      showFragment(VideoInfoFragment.newInstance().setVideo(video));
    }
  }

  @Override
  public void onRecyclerListViewPositionLongClicked(RecyclerView recyclerView, int position) {

  }

  @Override
  public void onRecyclerListViewPointPosition(RecyclerView recyclerView, int position, boolean up) {

  }

  @Override
  public void onRecyclerListViewPositionChanged(RecyclerView recyclerView, int firstPosition, int lastPosition) {
    spacePositionVisible = firstPosition == 0 || firstPosition == -1;
  }

  @Override
  public void onRecyclerListViewScrollValueChanged(RecyclerView recyclerView, boolean value) {

  }

  @Override
  public void onRecyclerListViewScrollChanged(RecyclerView recyclerView, int currentScroll, int dx, int dy) {
    int layoutReviewsHeight = toolbar.getMeasuredHeight();
    float layoutReviewsY = toolbar.getTranslationY();
    if (dy <= 0) {
      if (layoutReviewsY < 0) {
        toolbar.setTranslationY(Math.min(0, layoutReviewsY - dy));
        shadowToolbar.setTranslationY(Math.min(0, layoutReviewsY - dy));
        swipeRefreshLayout.setTargetSpace((int) (AndroidUtilities.toolbarHeight + Math.min(0, layoutReviewsY - dy)));
      }
    } else {
      if (layoutReviewsY > -layoutReviewsHeight) {
        toolbar.setTranslationY(Math.max(-layoutReviewsHeight, layoutReviewsY - dy));
        shadowToolbar.setTranslationY(Math.max(-layoutReviewsHeight, layoutReviewsY - dy));
        swipeRefreshLayout.setTargetSpace((int) (AndroidUtilities.toolbarHeight + Math.max(-layoutReviewsHeight, layoutReviewsY - dy)));
      }
    }
    boolean showTopShadow = true;
    if (spacePositionVisible) {
      RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
      if (layoutManager != null) {
        View view = layoutManager.findViewByPosition(0);
        if (view == null || view.getBottom() > 0 && (view.getBottom() >= view.getMeasuredHeight() || !isTopShadow)) {
          showTopShadow = false;
        }
      }
    }
    animationTopShadow(showTopShadow);
  }

  @Override
  public void onRecyclerListViewScrollStateChanged(RecyclerView recyclerView, int newState) {

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (objectAnimator != null && objectAnimator.isRunning()) {
      objectAnimator.cancel();
    }
  }
}