package com.android.javaprojectsetup.utility;

import android.content.Context;

import com.android.javaprojectsetup.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class PermissionUtil {

    private onRequestPermissionListener listener;
    private Context context;
    private String rationalMsg;
    private String deniedMsg;
    private String[] permissions;

    public PermissionUtil(onRequestPermissionListener listener, Context context, String rationalMsg, String deniedMsg, String[] permissions) {
        this.listener = listener;
        this.context = context;
        this.rationalMsg = rationalMsg;
        this.deniedMsg = deniedMsg;
        this.permissions = permissions;
    }

    public void doRequestForPermission() {
        if (context == null)
            return;

        TedPermission.with(context)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (listener != null) {
                            listener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        if (listener != null) {
                            listener.onPermissionDenied(deniedPermissions);
                        }
                    }
                })
                .setRationaleMessage(rationalMsg)
                .setDeniedMessage(deniedMsg)
                .setGotoSettingButtonText(context.getString(R.string.setting))
                .setPermissions(permissions)
                .check();
    }

    public interface onRequestPermissionListener {
        void onPermissionGranted();

        void onPermissionDenied(List<String> deniedPermissions);

    }
}