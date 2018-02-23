package com.daasuu.exoplayerfilter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by gaowei on 21/06/2017.
 */

public class PermissionUtils {

    public static boolean checkPermission(Context context, String permission) {
        if(PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(context, permission)) {
            return true;
        }
        return false;
    }
}
