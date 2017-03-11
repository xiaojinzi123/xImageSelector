package com.move.ximageSelector.config;

import android.graphics.Color;


import com.move.ximageSelector.R;
import com.move.ximageSelector.utils.XImageLoader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author xiaojinzi.
 */
public class XImgSelConfig {

    /**
     * 表示没有
     */
    public static final int NULL = -1;

    /**
     * 预选中的图片
     */
    public ArrayList<String> selectImage;//

    /**
     * sd卡的临时文件夹名称
     */
    public String sdCardFolderName = "XImageCache";//

    /**
     * 是否清理图片
     */
    public boolean isClearImageCache;//

    /**
     * 是否需要裁剪
     */
    public boolean needCrop;//

    /**
     * 是否需要预览
     */
    public boolean needPreview;//

    /**
     * 最多选择图片数
     */
    public int maxNum = 9;//

    /**
     * 第一个item是否显示相机
     */
    public boolean needCamera;//

    /**
     * 状态栏的背景色
     */
    public int statusBarColor = Color.parseColor("#343D44");//

    /**
     * 标题栏背景色
     */
    public int titlebarBgColor = Color.parseColor("#343D44");//

    /**
     * 返回图标的左边距
     */
    public int backResLeftMargin = 0;

    /**
     * 返回图标资源
     */
    public int backResId = R.mipmap.back_icon; //

    /**
     * 列表中item0位置的照相机的图片资源
     */
    public int cameraResId = R.mipmap.capture; //

    /**
     * 左下角弹出目录选择的小图标
     */
    public int popupTipResId = -1;

    /**
     * 标题
     */
    public String backTitle = "返回"; //

    /**
     * 标题颜色
     */
    public int titleColor = Color.WHITE; //

    /**
     * 确定按钮文字颜色
     */
    public int btnConfirmTextColor = Color.WHITE;//

    /**
     * 确定按钮的文本
     */
    public String btnConfirmText = "确定";//

    /**
     * 确定按钮的背景
     */
    public int btnConfirmBgDrawable = R.drawable.confirm_bg;//

    /**
     * 文本不可用颜色值
     */
    public int textDisabledColor = -1;//

    /**
     * 文字可用的颜色值
     */
    public int textAbledColor = -1;//

    /**
     * 自定义图片加载器
     */
    public XImageLoader loader;//

    /**
     * 裁剪输出大小
     */
    public int aspectX = 1;
    public int aspectY = 1;
    public int outputX = 400;
    public int outputY = 400;

    public XImgSelConfig(Builder builder) {
        this.selectImage = builder.selectImage;
        if (builder.sdCardFolderName != null && !builder.sdCardFolderName.equals("")) {
            this.sdCardFolderName = builder.sdCardFolderName;
        }
        this.isClearImageCache = builder.isClearImageCache;
        this.needCrop = builder.isCrop;
        this.needPreview = builder.isPreview;
        this.maxNum = builder.maxNum;
        this.needCamera = builder.isCamera;
        if (builder.statusBarColor != NULL) {
            this.statusBarColor = builder.statusBarColor;
        }
        if (builder.backResLeftMargin != NULL) {
            this.backResLeftMargin = builder.backResLeftMargin;
        }
        if (builder.backResId != NULL) {
            this.backResId = builder.backResId;
        }
        if (builder.cameraResId != NULL) {
            this.cameraResId = builder.cameraResId;
        }
        if (builder.backTitle != null) {
            this.backTitle = builder.backTitle;
        }
        if (builder.titlebarBgColor != NULL) {
            this.titlebarBgColor = builder.titlebarBgColor;
        }
        if (builder.btnConfirmBgDrawable != NULL) {
            this.btnConfirmBgDrawable = builder.btnConfirmBgDrawable;
        }
        if (builder.titleColor != NULL) {
            this.titleColor = builder.titleColor;
        }
        if (builder.btnConfirmText != null) {
            this.btnConfirmText = builder.btnConfirmText;
        }
        if (builder.btnConfirmTextColor != NULL) {
            this.btnConfirmTextColor = builder.btnConfirmTextColor;
        }

        this.loader = builder.loader;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
    }

    public static class Builder implements Serializable {

        private ArrayList<String> selectImage;
        private String sdCardFolderName = null;
        private boolean isClearImageCache = false;
        private boolean isCrop = false;
        private boolean isPreview = false;
        private int maxNum = 9;
        private boolean isCamera = true;
        public int statusBarColor = NULL;
        private int backResLeftMargin = NULL;
        private int backResId = NULL;
        private int cameraResId = NULL;
        private String backTitle = null;
        private int titleColor = NULL;
        private int titlebarBgColor = NULL;
        private int btnConfirmBgDrawable = NULL;
        private int btnConfirmTextColor = NULL;
        private String btnConfirmText = null;

        private XImageLoader loader;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        public Builder(XImageLoader loader) {
            this.loader = loader;
        }

        public Builder selectImage(ArrayList<String> selectImage) {
            this.selectImage = selectImage;
            return this;
        }

        public Builder sdCardFolderName(String sdCardFolderName) {
            this.sdCardFolderName = sdCardFolderName;
            return this;
        }

        public Builder isCrop(boolean isCrop) {
            this.isCrop = isCrop;
            return this;
        }

        public Builder isClearImageCache(boolean isClearImageCache) {
            this.isClearImageCache = isClearImageCache;
            return this;
        }

        public Builder isPreview(boolean isPreview) {
            this.isPreview = isPreview;
            return this;
        }

        public Builder btnConfirmText(String btnConfirmText) {
            this.btnConfirmText = btnConfirmText;
            return this;
        }

        public Builder cameraResId(int cameraResId) {
            this.cameraResId = cameraResId;
            return this;
        }

        public Builder maxNum(int maxNum) {
            this.maxNum = maxNum;
            return this;
        }

        public Builder isCamera(boolean isCamera) {
            this.isCamera = isCamera;
            return this;
        }

        public Builder statusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public Builder backResId(int backResId) {
            this.backResId = backResId;
            return this;
        }

        public Builder backResLeftMargin(int backResLeftMargin) {
            this.backResLeftMargin = backResLeftMargin;
            return this;
        }

        public Builder backTitle(String backTitle) {
            this.backTitle = backTitle;
            return this;
        }

        public Builder titleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder titlebarBgColor(int titleBgColor) {
            this.titlebarBgColor = titleBgColor;
            return this;
        }

        public Builder btnConfirmTextColor(int btnConfirmTextColor) {
            this.btnConfirmTextColor = btnConfirmTextColor;
            return this;
        }

        public Builder btnConfirmBgDrawable(int btnConfirmBgDrawable) {
            this.btnConfirmBgDrawable = btnConfirmBgDrawable;
            return this;
        }


        public Builder cropSize(int aspectX, int aspectY, int outputX, int outputY) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        public XImgSelConfig build() {
            return new XImgSelConfig(this);
        }
    }

}
