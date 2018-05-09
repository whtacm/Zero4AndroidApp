package com.robin.atm.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.robin.atm.R;
import com.robin.atm.ui.base.BaseActivity;
import com.robin.atm.util.LogUtil;

/**
 * Created by robin on 5/8/18.
 */

public class PermissionRequestActivity extends BaseActivity {
    public final static String PERMISSION_SET = "permission-set";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        Intent intent = getIntent();
        LogUtil.v("permissionRequestActivity");

        String[] permissions = intent.getStringArrayExtra(PERMISSION_SET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            LogUtil.v(permissions, grantResults);

        }

        finish();
        back();
    }
}
