package com.robin.atm.util;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.robin.atm.BuildConfig;

/**
 * Created by robin on 12/25/17.
 */
public class LogUtil {

    public static StringBuffer sbLog = null;

    public final static String TAG = "robin";

    /**
     * @param caller
     * @return
     */
    private static String genTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d, Tid:%d, Pid:%d)";
        String clsName = caller.getClassName();
        clsName = clsName.substring(clsName.lastIndexOf(".") + 1);
        tag = String.format(tag, clsName, caller.getMethodName(), caller.getLineNumber(), Thread.currentThread().getId(), Process.myPid());
        tag = TextUtils.isEmpty(TAG) ? tag : TAG + " " + tag;
        return tag;
    }

    /**
     * @param cls
     * @param methodname
     * @param info
     */
    @Deprecated
    public static void v(Class cls, String methodname, String info) {
        StackTraceElement stackTraceElement = getStackTrace();
        String tag = genTag(stackTraceElement);
        if (BuildConfig.ENV_DEBUG) {
            Log.v(tag, "! " + info + "\n");
        }
    }

    /**
     *
     * @param objects
     */
    public static void v(Object... objects) {
        StackTraceElement stackTraceElement = getStackTrace();
        String tag = genTag(stackTraceElement);
        if (BuildConfig.ENV_DEBUG) {
            StringBuffer sb = new StringBuffer();
            for (Object object : objects) {
                sb.append(object.toString() + "\n");
            }
            Log.v(tag, "! " + sb.toString());
            if (sbLog != null) {
                sbLog.append(sb.toString() + "\n");
            }
        }
    }

    /**
     * @return
     */
    public static StackTraceElement getStackTrace() {
        return Thread.currentThread().getStackTrace()[4];
    }
}