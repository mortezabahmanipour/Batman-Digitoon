package tv.batman.digitoon.android.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import tv.batman.digitoon.android.Listeners.FragmentNavigation;
import tv.batman.digitoon.android.Utils.AndroidUtilities;
import tv.batman.digitoon.android.Utils.RequestHelper;
import tv.batman.digitoon.android.Utils.Theme;

// created by morti on 2018/4/2

public class BaseDialogFragment extends DialogFragment {
    protected Activity activity;
    protected FragmentNavigation fragmentNavigation;
    protected int tag = AndroidUtilities.random.nextInt();
    protected int[] requestsId = new int[100];
    protected Dialog dialog;
    private boolean destroy;

    public boolean isDestroy() {
        return destroy;
    }

    protected boolean onBackPressed() {
        return true;
    }

    private void setStatusBarTheme() {
        Window window = dialog.getWindow();
        if(window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int color = Theme.getColor(Theme.key_action_bar_default);
                int flags = window.getDecorView().getSystemUiVisibility();
                if (color == 0xffffffff) {
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                window.getDecorView().setSystemUiVisibility(flags);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Theme.getColor(Theme.key_app_primary));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                return !onBackPressed();
            }
            return false;
        });
        setStatusBarTheme();
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy = true;
        RequestHelper.getInstance().cancel(tag);
    }
}