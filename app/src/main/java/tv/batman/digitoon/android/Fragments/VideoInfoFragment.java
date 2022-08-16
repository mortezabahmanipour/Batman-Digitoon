package tv.batman.digitoon.android.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import tv.batman.digitoon.android.API;
import tv.batman.digitoon.android.Activity.MainActivity;
import tv.batman.digitoon.android.ApplicationLoader;
import tv.batman.digitoon.android.Customs.CircleProgressbar;
import tv.batman.digitoon.android.Customs.ImageView;
import tv.batman.digitoon.android.Customs.ListItem;
import tv.batman.digitoon.android.Customs.TextView;
import tv.batman.digitoon.android.Models.VideoModel;
import tv.batman.digitoon.android.R;
import tv.batman.digitoon.android.Session.Database;
import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.AppLog;
import tv.batman.digitoon.android.Utils.DispatchQueue;
import tv.batman.digitoon.android.Utils.RequestHelper;
import tv.batman.digitoon.android.Utils.SpanUtil;
import tv.batman.digitoon.android.Utils.Theme;

// created by morti

@SuppressLint("SetTextI18n")
public class VideoInfoFragment extends BaseDialogFragment implements View.OnClickListener, ViewTreeObserver.OnScrollChangedListener {

  public static VideoInfoFragment newInstance() {
    return new VideoInfoFragment();
  }

  private static volatile DispatchQueue queue = new DispatchQueue("videoInfoFragmentQueue");
  private ViewGroup baseLayout;
  private ViewGroup toolbar;
  private View shadowRatings;
  private View shadowToolbar;
  private NestedScrollView nestedScrollView;
  private LinearLayout videoContentLinearLayout;
  private ImageView ivBack;
  private ImageView ivThumbnail;
  private ImageView ivDownload;
  private TextView tvTitle;
  private TextView tvVideoTitle;
  private TextView tvVideoInfo;
  private TextView tvMessage;
  private TextView tvPlay;
  private TextView tvMoreParts;
  private TextView tvSynopsisName;
  private TextView tvConstructiveFactorsName;
  private TextView tvRatingsName;
  private TextView tvAboutName;
  private TextView tvSynopsis;
  private TextView tvConstructiveFactors;
  private TextView tvAbout;
  private ListItem listItemRatings;
  private CircleProgressbar pbLoading;
  private VideoModel video;
  private AnimatorSet animatorSet;
  private ObjectAnimator titleObjectAnimator;
  private boolean isTitleShow;

  public VideoInfoFragment setVideo(VideoModel video) {
    this.video = video;
    return this;
  }

  private void initialiseViews(View view) {
    baseLayout = view.findViewById(R.id.baseLayout);
    toolbar = view.findViewById(R.id.toolbar);
    shadowRatings = view.findViewById(R.id.shadowRatings);
    shadowToolbar = view.findViewById(R.id.shadowToolbar);
    nestedScrollView = view.findViewById(R.id.nestedScrollView);
    videoContentLinearLayout = view.findViewById(R.id.videoContentLinearLayout);
    ivBack = view.findViewById(R.id.ivBack);
    ivThumbnail = view.findViewById(R.id.ivThumbnail);
    ivDownload = view.findViewById(R.id.ivDownload);
    tvTitle = view.findViewById(R.id.tvTitle);
    tvVideoTitle = view.findViewById(R.id.tvVideoTitle);
    tvVideoInfo = view.findViewById(R.id.tvVideoInfo);
    tvMessage = view.findViewById(R.id.tvMessage);
    tvPlay = view.findViewById(R.id.tvPlay);
    tvMoreParts = view.findViewById(R.id.tvMoreParts);
    tvSynopsisName = view.findViewById(R.id.tvSynopsisName);
    tvConstructiveFactorsName = view.findViewById(R.id.tvConstructiveFactorsName);
    tvRatingsName = view.findViewById(R.id.tvRatingsName);
    tvAboutName = view.findViewById(R.id.tvAboutName);
    tvSynopsis = view.findViewById(R.id.tvSynopsis);
    tvConstructiveFactors = view.findViewById(R.id.tvConstructiveFactors);
    tvAbout = view.findViewById(R.id.tvAbout);
    listItemRatings = view.findViewById(R.id.listItemRatings);
    pbLoading = view.findViewById(R.id.pbLoading);
  }

  private void initialiseDefaults() {
    baseLayout.setBackgroundColor(Theme.getColor(Theme.key_app_background));
    toolbar.setBackground(Theme.createShadowGradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Theme.getColor(Theme.key_app_primary), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_app_primary), 0)}, 0.0f));
    shadowToolbar.setBackground(Theme.createShadowGradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x15000000, 0x00000000}, 0.0f));
    ivBack.setImageDrawable(Theme.getDrawableColor(R.drawable.ic_arrow_back, Theme.getColor(Theme.key_app_text2)));
    ivBack.setBackground(Theme.createSelectorDrawable(new Theme.SelectorBuilder().pressedColor(Theme.getColor(Theme.key_app_selector)).style(Theme.OVAL)));
    tvTitle.setTextColor(Theme.getColor(Theme.key_app_text));
    tvTitle.setTypefaceStyle(Typeface.BOLD);
    tvVideoTitle.setTextColor(Theme.getColor(Theme.key_app_text));
    tvVideoTitle.setTypefaceStyle(Typeface.BOLD);
    tvVideoInfo.setTextColor(Theme.getColor(Theme.key_app_text3));
    tvMessage.setTextColor(Theme.getColor(Theme.key_app_text2));
    tvMoreParts.setTextColor(Theme.getColor(Theme.key_app_text3));
    tvSynopsisName.setTextColor(Theme.getColor(Theme.key_app_text2));
    tvConstructiveFactorsName.setTextColor(Theme.getColor(Theme.key_app_text2));
    tvRatingsName.setTextColor(Theme.getColor(Theme.key_app_text2));
    tvAboutName.setTextColor(Theme.getColor(Theme.key_app_text2));
    tvSynopsisName.setTypefaceStyle(Typeface.BOLD);
    tvConstructiveFactorsName.setTypefaceStyle(Typeface.BOLD);
    tvRatingsName.setTypefaceStyle(Typeface.BOLD);
    tvAboutName.setTypefaceStyle(Typeface.BOLD);
    tvSynopsis.setTextColor(Theme.getColor(Theme.key_app_text3));
    tvConstructiveFactors.setTextColor(Theme.getColor(Theme.key_app_text3));
    tvAbout.setTextColor(Theme.getColor(Theme.key_app_text3));
    ivThumbnail.setRadius(AndroidUtilities.dp(7));
    tvMessage.setBackground(Theme.createSelectorDrawable(new Theme.SelectorBuilder().setDefaultColor(Theme.getColor(Theme.key_app_shadow)).pressedColor(Theme.getColor(Theme.key_app_selector)).style(Theme.RECT).radii(AndroidUtilities.dp(7))));
    tvPlay.setBackground(Theme.createSelectorDrawable(new Theme.SelectorBuilder().setDefaultColor(Theme.getColor(Theme.key_app_default_3)).pressedColor(Theme.getColor(Theme.key_app_selector_white)).style(Theme.RECT).radii(AndroidUtilities.dp(7))));
    tvPlay.setTypefaceStyle(Typeface.BOLD);
    tvMoreParts.setBackground(Theme.createSelectorDrawable(new Theme.SelectorBuilder().setDefaultColor(Theme.getColor(Theme.key_app_shadow)).pressedColor(Theme.getColor(Theme.key_app_selector)).style(Theme.RECT).radii(AndroidUtilities.dp(7))));

    ivDownload.setBackground(Theme.createSelectorDrawable(new Theme.SelectorBuilder().pressedColor(Theme.getColor(Theme.key_app_selector)).style(Theme.OVAL)));
    ivDownload.setImageDrawable(Theme.getDrawableColor(R.drawable.ic_file_download, Theme.getColor(Theme.key_app_text2)));

    Theme.setBackgroundColorWithTag(baseLayout, Theme.getColor(Theme.key_app_shadow_2), "shadow_center");

    Glide.with(activity).asBitmap().load(video.poster).override(AndroidUtilities.dp(300)).into(ivThumbnail);
    tvTitle.setText(video.title);
    tvVideoTitle.setText(video.title);
  }

  private void initialiseListeners() {
    ivBack.setOnClickListener(this);
    tvMessage.setOnClickListener(this);
    tvPlay.setOnClickListener(this);
    tvMoreParts.setOnClickListener(this);
    ivDownload.setOnClickListener(this);
    nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
  }

  private void loadInformationFromServer() {
    AndroidUtilities.setVisibility(tvVideoInfo, View.GONE);
    AndroidUtilities.setVisibility(tvMessage, View.GONE);
    AndroidUtilities.setVisibility(videoContentLinearLayout, View.GONE);
    AndroidUtilities.setVisibility(pbLoading, View.VISIBLE);
    requestsId[0] = RequestHelper.getInstance().addToRequestQueue(tag, API.BASE + "&i=" + video.imdb_id, null, (isSuccessful, response) -> {
      if (!isSuccessful) {
        loadVideosFromCache();
        if (ApplicationLoader.isNetworkOnline()) {
          AndroidUtilities.toast(activity, "مشکل در ارتباط با سرور");
        } else {
          AndroidUtilities.toast(activity, "دستگاه شما به اینترنت متصل نیست");
        }
        return;
      }
      queue.postRunnable(() -> {
        VideoModel video;
        try {
          video = new VideoModel();
          JSONObject baseJSONObject = (JSONObject) new JSONTokener(response).nextValue();
          video.imdb_id = baseJSONObject.getString("imdbID");
          video.title = baseJSONObject.getString("Title");
          video.year = baseJSONObject.getString("Year");
          video.type = baseJSONObject.getString("Type");
          video.poster = baseJSONObject.getString("Poster");
          video.rated = baseJSONObject.isNull("Rated") ? null : baseJSONObject.getString("Rated");
          video.released = baseJSONObject.isNull("Released") ? null : baseJSONObject.getString("Released");
          video.runtime = baseJSONObject.isNull("Runtime") ? null : baseJSONObject.getString("Runtime");
          video.genre = baseJSONObject.isNull("Genre") ? null : baseJSONObject.getString("Genre");
          video.director = baseJSONObject.isNull("Director") ? null : baseJSONObject.getString("Director");
          video.writer = baseJSONObject.isNull("Writer") ? null : baseJSONObject.getString("Writer");
          video.actors = baseJSONObject.isNull("Actors") ? null : baseJSONObject.getString("Actors");
          video.plot = baseJSONObject.isNull("Plot") ? null : baseJSONObject.getString("Plot");
          video.language = baseJSONObject.isNull("Language") ? null : baseJSONObject.getString("Language");
          video.country = baseJSONObject.isNull("Country") ? null : baseJSONObject.getString("Country");
          video.awards = baseJSONObject.isNull("Awards") ? null : baseJSONObject.getString("Awards");
          video.ratings =baseJSONObject.isNull("Ratings") ? null :  baseJSONObject.getString("Ratings");
          video.metascore = baseJSONObject.isNull("Metascore") ? null : baseJSONObject.getString("Metascore");
          video.imdb_rating = baseJSONObject.isNull("imdbRating") ? null : baseJSONObject.getString("imdbRating");
          video.imdb_votes = baseJSONObject.isNull("imdbVotes") ? null : baseJSONObject.getString("imdbVotes");
          video.dvd = baseJSONObject.isNull("DVD") ? null : baseJSONObject.getString("DVD");
          video.total_seasons = baseJSONObject.isNull("totalSeasons") ? 0 : baseJSONObject.getInt("totalSeasons");
          video.box_office = baseJSONObject.isNull("BoxOffice") ? null : baseJSONObject.getString("BoxOffice");
          video.production = baseJSONObject.isNull("Production") ? null : baseJSONObject.getString("Production");
          video.website = baseJSONObject.isNull("Website") ? null : baseJSONObject.getString("Website");
        } catch (Exception e) {
          AppLog.e(MainActivity.class, e);
          video = null;
        }
        if (video != null) {
          Database.getInstance().updateVideo(this.video = video);
        }
        AndroidUtilities.runOnUIThread(this::setInformation);
      });
    });
  }

  private void loadVideosFromCache() {
    queue.postRunnable(() -> {
      VideoModel video = Database.getInstance().getVideo(this.video.imdb_id);
      if (video != null) {
        this.video = video;
      }
      AndroidUtilities.runOnUIThread(this::setInformation);
    });
  }

  private void setInformation() {
    AndroidUtilities.setVisibility(pbLoading, View.GONE);
    if (video.actors == null && video.genre == null && video.country == null) {
      AndroidUtilities.setVisibility(tvMessage, View.VISIBLE);
      tvMessage.setText("مشکل در بارگذاری اطلاعات، برای تلاش دوباره اینجا کلیک کنید");
    } else {
      AndroidUtilities.setVisibility(tvVideoInfo, View.VISIBLE);
      AndroidUtilities.setVisibility(videoContentLinearLayout, View.VISIBLE);
      AndroidUtilities.setVisibility(tvMoreParts, "series".equals(video.type) ? View.VISIBLE : View.GONE);
      tvMoreParts.setText(activity.getText(R.string.more_parts) + " «" + video.total_seasons + "»");
      tvVideoInfo.setText(video.year + " • " + video.runtime);
      tvSynopsis.setText(video.plot);
      SpannableStringBuilder aboutBuilder = new SpannableStringBuilder();
      if (!TextUtils.isEmpty(video.imdb_rating)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("IMDb Rating: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false, 0, 0, 0, null, Theme.getDrawableColor(R.drawable.imdb_logo, 0), AndroidUtilities.dp(35), 0, 0, AndroidUtilities.dp(5), 0, 0));
        aboutBuilder.append(video.imdb_rating);
      }
      if (!TextUtils.isEmpty(video.imdb_votes)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("IMDb Votes: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.imdb_votes);
      }
      if (!TextUtils.isEmpty(video.genre)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("Genre: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.genre);
      }
      if (!TextUtils.isEmpty(video.country)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("Country: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.country);
      }
      if (!TextUtils.isEmpty(video.language)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("Language: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.language);
      }
      if (!TextUtils.isEmpty(video.released)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("Released: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.released);
      }
      if (!TextUtils.isEmpty(video.awards)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("Awards: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.awards);
      }
      if (!TextUtils.isEmpty(video.rated)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("Rated: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.rated);
      }
      if (!TextUtils.isEmpty(video.metascore)) {
        if (aboutBuilder.length() > 0) {
          aboutBuilder.append("\n");
        }
        aboutBuilder.append(SpanUtil.span(new SpannableStringBuilder("Metascore: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        aboutBuilder.append(video.metascore);
      }
      tvAbout.setText(aboutBuilder);
      SpannableStringBuilder constructiveFactorsBuilder = new SpannableStringBuilder();
      if (!TextUtils.isEmpty(video.actors)) {
        if (constructiveFactorsBuilder.length() > 0) {
          constructiveFactorsBuilder.append("\n");
        }
        constructiveFactorsBuilder.append(SpanUtil.span(new SpannableStringBuilder("Actors: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        constructiveFactorsBuilder.append(video.actors);
      }
      if (!TextUtils.isEmpty(video.director)) {
        if (constructiveFactorsBuilder.length() > 0) {
          constructiveFactorsBuilder.append("\n");
        }
        constructiveFactorsBuilder.append(SpanUtil.span(new SpannableStringBuilder("Director: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        constructiveFactorsBuilder.append(video.director);
      }
      if (!TextUtils.isEmpty(video.writer)) {
        if (constructiveFactorsBuilder.length() > 0) {
          constructiveFactorsBuilder.append("\n");
        }
        constructiveFactorsBuilder.append(SpanUtil.span(new SpannableStringBuilder("Writer: "), 0, -1, Theme.getColor(Theme.key_app_text5), -1, false));
        constructiveFactorsBuilder.append(video.writer);
      }
      tvConstructiveFactors.setText(constructiveFactorsBuilder);
      AndroidUtilities.setVisibility(shadowRatings, View.GONE);
      AndroidUtilities.setVisibility(tvRatingsName, View.GONE);
      AndroidUtilities.setVisibility(listItemRatings, View.GONE);
      try {
        listItemRatings.clear();
        if (!TextUtils.isEmpty(video.ratings)) {
          JSONArray ratingsArray = (JSONArray) new JSONTokener(video.ratings).nextValue();
          if (ratingsArray.length() > 0) {
            AndroidUtilities.setVisibility(shadowRatings, View.VISIBLE);
            AndroidUtilities.setVisibility(tvRatingsName, View.VISIBLE);
            AndroidUtilities.setVisibility(listItemRatings, View.VISIBLE);
            List<String> items = new ArrayList<>();
            for (int i=0; i < ratingsArray.length(); i++) {
              JSONObject object = ratingsArray.getJSONObject(i);
              String source = object.isNull("Source") ? null : object.getString("Source");
              String value = object.isNull("Value") ? null : object.getString("Value");
              items.add(value + " • " + source);
            }
            listItemRatings.setItems(items);
            AppLog.i(MainActivity.class, "items " + items.size());
          }
        }
      } catch (Exception e) {
        AppLog.e(MainActivity.class, e);
      }
      showContentWithAnimation();
    }
  }

  private void showContentWithAnimation() {
    if (animatorSet != null && animatorSet.isRunning()) {
      animatorSet.cancel();
    }
    animatorSet = new AnimatorSet();
    animatorSet.playTogether(ObjectAnimator.ofFloat(tvVideoInfo, "alpha", tvVideoInfo.getAlpha() != 1.0f ? tvVideoInfo.getAlpha() : 0.0f, 1.0f));
    animatorSet.playTogether(ObjectAnimator.ofFloat(videoContentLinearLayout, "alpha", videoContentLinearLayout.getAlpha() != 1.0f ? videoContentLinearLayout.getAlpha() : 0.0f, 1.0f));
    animatorSet.playTogether(ObjectAnimator.ofFloat(videoContentLinearLayout, "translationY", videoContentLinearLayout.getTranslationY() > 0 ? videoContentLinearLayout.getTranslationY() : AndroidUtilities.dp(25), 0.0f));
    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        if (animation.equals(animatorSet)) {
          animatorSet = null;
        }
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        if (animation.equals(animatorSet)) {
          animatorSet = null;
        }
      }
    });
    animatorSet.setDuration(250);
    animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
    animatorSet.start();
  }

  private void animateTitle(final boolean show) {
    if (isTitleShow == show) {
      return;
    }
    isTitleShow = show;
    if (titleObjectAnimator != null && titleObjectAnimator.isRunning()) {
      titleObjectAnimator.cancel();
    }
    if (show) {
      AndroidUtilities.setVisibility(tvTitle, View.VISIBLE);
    }
    titleObjectAnimator = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, show ? 1f : 0f);
    titleObjectAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        if (animation.equals(titleObjectAnimator)) {
          titleObjectAnimator = null;
        }
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        if (animation.equals(titleObjectAnimator)) {
          titleObjectAnimator = null;
          if (!show) {
            AndroidUtilities.setVisibility(tvTitle, View.GONE);
          }
        }
      }
    });
    titleObjectAnimator.setDuration(200);
    titleObjectAnimator.start();
  }

  private void actionBack() {
    dismiss();
  }

  private void actionMessage() {
    loadInformationFromServer();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_BatmanDigitoon);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_video_info, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    if (video == null || TextUtils.isEmpty(video.imdb_id)) {
      dismiss();
      return;
    }
    initialiseViews(view);
    initialiseDefaults();
    initialiseListeners();
    loadInformationFromServer();
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    if (v.equals(ivBack)) {
      actionBack();
    } else if (v.equals(tvMessage)) {
      actionMessage();
    }
  }

  @Override
  public void onScrollChanged() {
    if (isDestroy()) {
      return;
    }
    int currentScroll = nestedScrollView.getScrollY();
    int scrollSize = ivThumbnail.getBottom() - AndroidUtilities.toolbarHeight;
    float alpha = 0;
    if (currentScroll >= 0) {
      alpha = currentScroll <= scrollSize ? (currentScroll / (scrollSize / 100f)) / 100f : 1;
    }
    shadowToolbar.setAlpha(alpha);
    toolbar.setBackground(Theme.createShadowGradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Theme.getColor(Theme.key_app_primary), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_app_primary), (int) (alpha * 255))}, 0.0f));
    animateTitle((((View) tvVideoTitle.getParent()).getTop() + tvVideoTitle.getBottom() + AndroidUtilities.dp(-10)) < (currentScroll + AndroidUtilities.toolbarHeight));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (animatorSet != null && animatorSet.isRunning()) {
      animatorSet.cancel();
    }
    if (titleObjectAnimator != null && titleObjectAnimator.isRunning()) {
      titleObjectAnimator.cancel();
    }
  }
}