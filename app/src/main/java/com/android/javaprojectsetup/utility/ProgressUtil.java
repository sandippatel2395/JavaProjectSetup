package com.android.javaprojectsetup.utility;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressUtil {

    private static ProgressUtil mInstance = null;

    public static ProgressUtil getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressUtil();
        }
        return mInstance;
    }

    public KProgressHUD initProgressBar(Context context) {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setDimAmount(0.5f);
    }

    public void showDialog(KProgressHUD dialog, ProgressBar pb, boolean isLoaderRequired) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        } else if (pb != null && isLoaderRequired) {
            pb.setVisibility(View.VISIBLE);
        }
    }


    public void showDialogWithMsg(KProgressHUD dialog, ProgressBar pb, boolean isLoaderRequired, String msg) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.setLabel(msg);
            dialog.show();
        } else if (pb != null && isLoaderRequired) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    public void showDialogWithMsgChat(KProgressHUD dialog, ProgressBar pb, boolean isLoaderRequired, String msg) {
        if (dialog != null && !dialog.isShowing()) {
            if (msg != null && !msg.isEmpty()) {
                dialog.setLabel(msg);
                dialog.show();
            } else {
                dialog.dismiss();
            }
        } else if (pb != null && isLoaderRequired) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    public void setMessage(KProgressHUD dialog, String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.setLabel(msg);
        }
    }

    public void setMessageChat(KProgressHUD dialog, String msg) {
        if (dialog != null && dialog.isShowing()) {
            if (msg != null && !msg.isEmpty()) {
                dialog.setLabel(msg);
            } else {
                dialog.dismiss();
            }
        }
    }

    public void hideDialog(KProgressHUD dialog, ProgressBar pb) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else if (pb != null) {
            pb.setVisibility(View.GONE);
        }
    }
}
