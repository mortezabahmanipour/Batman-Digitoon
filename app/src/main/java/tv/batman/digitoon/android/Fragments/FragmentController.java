package tv.batman.digitoon.android.Fragments;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import tv.batman.digitoon.android.Utils.AppLog;

public class FragmentController {
  private FragmentManager fragmentManager;

  public static FragmentController newInstance(FragmentManager fragmentManager) {
    return new FragmentController(fragmentManager);
  }

  private FragmentController(FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  public void showFragment(DialogFragment dialogFragment) {
    if (fragmentManager == null) {
      return;
    }
    try {
      dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
    } catch (Exception e) {
      AppLog.e(FragmentController.class, e);
    }
  }
}
