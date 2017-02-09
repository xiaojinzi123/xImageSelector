package com.move.ximageSelector.utils.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * activity的工具类
 *
 * @author xiaojinzi
 */
public class ActivityUtil {

    /**
     * 启动一个activity
     *
     * @param context
     * @param clazz
     */
    public static void startActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    /**
     * 启动一个带有请求码的Intent
     *
     * @param act
     * @param clazz
     * @param requestCode
     */
    public static void startActivityForResult(Activity act, Class<? extends Activity> clazz, int requestCode) {
        Intent intent = new Intent(act, clazz);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动其他应用
     *
     * @param c 上下文
     * @param p 包的信息对象
     * @throws NameNotFoundException 可能抛出的异常
     */
    public static void startOtherActivity(Context c, PackageInfo p) throws NameNotFoundException {
        PackageInfo packageInfo = c.getPackageManager().getPackageInfo(p.packageName, PackageManager.GET_ACTIVITIES);
        // 拿到具有启动功能的activity
        ActivityInfo info = packageInfo.activities[0];
        // 创建意图启动其他应用程序
        Intent intent = new Intent();
        intent.setClassName(p.packageName, info.name);
        c.startActivity(intent);
    }

}
