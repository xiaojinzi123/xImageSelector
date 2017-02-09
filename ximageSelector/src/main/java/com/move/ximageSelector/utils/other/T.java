package com.move.ximageSelector.utils.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by xiaojinzi on 2015/8/5.
 * @author xiaojinzi
 */
public class T {

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 在控制台输出信息
     *
     * @param content
     */
    public static void syso(Object content) {
        System.out.println(content);
    }

    /**
     * 打电话
     * 用到的权限：
     * <uses-permission android:name="android.permission.CALL_PHONE"/>
     *
     * @param context
     * @param phone
     */
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + phone));

        context.startActivity(intent);
    }

    public static AlertDialog popupDialog(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);   //android.R.attr#alertDialogTheme

        builder.setTitle(title).setItems(items, listener);

        AlertDialog dialog = builder.create();

        dialog.show();

        return dialog;

    }


    public static AlertDialog popupSimpleDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);   //android.R.attr#alertDialogTheme

        builder.setTitle(title).setMessage(message);

        builder.setPositiveButton("确定", listener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

        return dialog;

    }


}
