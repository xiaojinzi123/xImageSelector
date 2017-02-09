package com.move.ximageSelector;


import com.move.ximageSelector.config.XImgSelConfig;

/**
 * Created by cxj on 2016/9/27.
 */
public class XImage {

    private static XImgSelConfig config;

    /**
     * 注入配置类
     *
     * @param config
     */
    public static void setConfig(XImgSelConfig config) {
        XImage.config = config;
    }

    /**
     * 获取配置类
     *
     * @return
     */
    public static XImgSelConfig getConfig() {
        return config;
    }

}
