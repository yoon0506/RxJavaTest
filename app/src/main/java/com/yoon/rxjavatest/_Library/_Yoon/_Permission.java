package com.yoon.rxjavatest._Library._Yoon;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class _Permission {

    private _Permission This = this;

    private Activity mActivity;
    public String[] mPermissions;

    // #1 설정되어있는 퍼미션을 갖고있나 확인함과 동시에 퍼미션 선언.
    public boolean hasPermissions(Activity activity, String[] permissions) {
        mActivity = activity;

        int result;
        mPermissions = permissions;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions) {
            result = ContextCompat.checkSelfPermission(mActivity, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                //허가 안된 퍼미션 발견
                return false;
            }
        }
        //모든 퍼미션이 허가되었음
        return true;
    }

    // #2 퍼미션 요청하기
    public void requestPermissions(final int requestCode) {
        if (mPermissions != null) {
            // #3 퍼미션 request
            mActivity.requestPermissions(mPermissions, requestCode);
        }
    }

}
