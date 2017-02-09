package com.move.ximageSelector.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by cxj on 2016/10/8.
 */
public interface XImageLoader {

    /**
     * 加载图片的工作留给使用者
     *
     * @param context   上下文
     * @param localPath 本地路径
     * @param iv        图片控件
     */
    void load(Context context, String localPath, ImageView iv);

}
