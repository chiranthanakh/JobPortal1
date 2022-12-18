package com.chiranths.jobportal1;

import android.content.Context;
import android.content.pm.PackageManager;

public class Utilitys {

    private boolean checkWriteExternalPermission(Context context)
    {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}
