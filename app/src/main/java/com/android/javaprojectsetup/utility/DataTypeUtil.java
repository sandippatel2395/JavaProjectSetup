package com.android.javaprojectsetup.utility;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class DataTypeUtil {
    private static DataTypeUtil mInstance = null;
    public String TAG = getClass().getName();
    private static Toast toast;

    public static DataTypeUtil getInstance() {
        if (mInstance == null) {
            mInstance = new DataTypeUtil();
        }
        return mInstance;
    }

    public void showToast(Context context, String message) {// change it like awk
        if (message == null || message.isEmpty() || context == null)
            return;

        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
