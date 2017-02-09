package com.move.ximageSelector.utils.other;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 进度条对话框的工具类
 *
 * @author xiaojinzi
 */
public class ProgressDialogUtil {

    public static int MAX = 100;

    public static boolean defaultCancelable = false;

    public static String defaultMessage = "please wait";

    /**
     * 采用圆圈的转啊转的
     *
     * @param context
     * @return
     */
    public static ProgressDialog show(Context context) {
        return show(context, ProgressDialog.STYLE_SPINNER, defaultCancelable);
    }

    /**
     * 显示进度条对话框
     *
     * @param context
     * @param style
     * @param cancelAble
     * @return
     */
    public static ProgressDialog show(Context context, int style, boolean cancelAble) {
        ProgressDialog pd = create(context, style, cancelAble);
        pd.show();
        return pd;
    }

    /**
     * 创建一个对话框
     *
     * @param context    上下文
     * @param style      {@link ProgressDialog#STYLE_HORIZONTAL} or {@link ProgressDialog#STYLE_SPINNER}
     * @param cancelAble 是否可以取消
     * @return
     */
    public static ProgressDialog create(Context context, int style, boolean cancelAble) {
        ProgressDialog pd = new ProgressDialog(context);
        // 水平进度条的样式
        pd.setProgressStyle(style);
        pd.setMessage(defaultMessage);
        pd.setCancelable(cancelAble);
        pd.setMax(MAX);
        return pd;
    }

}
