package com.move.ximageSelector.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by cxj on 2016/10/9.
 * 记录图片的信息及其选中状态
 * 并且记录整个项目中的一些状态
 */
public class XImageRecoder {

    private XImageRecoder() {
    }

    private static XImageRecoder xImageRecoder;

    public static XImageRecoder getInstance() {
        if (xImageRecoder == null) {
            xImageRecoder = new XImageRecoder();
        }
        return xImageRecoder;
    }

    public static final int ACTION_FLAG = 0;
    public static final int ACTION_FLAG_ONE = 1;

    //控制在显示图片界面的onResume方式是否刷新数据
    public static final int ACTION_FLAG_TWO = 2;

    /**
     * 1表示预览界面点击了确定或者当一张图片的时候预览界面的选择按钮点击了,需要退出并返回图片路径
     */
    private int actionFlag;

    public int getActionFlag() {
        return actionFlag;
    }

    public void setActionFlag(int actionFlag) {
        this.actionFlag = actionFlag;
    }

    /**
     * 当前正在显示图片界面显示的图片的集合
     */
    private List<String> needPreviewImages = new ArrayList<String>();

    private HashMap<String, Boolean> isSelectFlagMap = new HashMap<String, Boolean>();

    //当前正在显示的图片的目录路径,如果为null,表示显示所有图片
    private String currentFolderPath;

    private String currentFolderName;

    public String getCurrentFolderName() {
        return currentFolderName;
    }

    public void setCurrentFolderName(String currentFolderName) {
        this.currentFolderName = currentFolderName;
    }

    public String getCurrentFolderPath() {
        return currentFolderPath;
    }

    public void setCurrentFolderPath(String currentFolderPath) {
        this.currentFolderPath = currentFolderPath;
    }

    public List<String> getNeedPreviewImages() {
        return needPreviewImages;
    }

    public void setNeedPreviewImages(List<String> needPreviewImages) {
        this.needPreviewImages = needPreviewImages;
    }

    public void setSelectStatus(String localImagePath, boolean isSelect) {
        isSelectFlagMap.put(localImagePath, isSelect);
    }

    public Boolean isSelect(String localImagePath) {
        return isSelectFlagMap.get(localImagePath);
    }

    /**
     * 初始化所有的图片状态
     *
     * @param flag
     */
    public void initAllImageStatus(boolean flag) {
        Set<String> keySet = isSelectFlagMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            isSelectFlagMap.put(key, flag);
        }
    }

    /**
     * 统计被选中的个数
     *
     * @return
     */
    public int getSelectImageNumber() {
        int count = 0;
        Set<String> keySet = isSelectFlagMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Boolean b = isSelectFlagMap.get(key);
            if (b != null && b == true) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取选中的图片
     *
     * @return
     */
    public ArrayList<String> getSelectImages() {
        ArrayList<String> images = new ArrayList<String>();
        Set<String> keySet = isSelectFlagMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Boolean b = isSelectFlagMap.get(key);
            if (b != null && b == true) {
                images.add(key);
            }
        }
        return images;
    }

    public List<String> getSelectList() {
        List<String> images = new ArrayList<String>();
        Set<String> keySet = isSelectFlagMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Boolean b = isSelectFlagMap.get(key);
            if (b != null && b == true) {
                images.add(key);
            }
        }
        return images;
    }

    /**
     * 其实就是在map集合中标识一下这个路径的文件是否选中了,如果根据key能查询出来的,就放弃标记,因为是重复的,不能重置为false
     *
     * @param images
     */
    public void initImage(List<String> images) {
        int size = images.size();
        for (int i = 0; i < size; i++) {
            String path = images.get(i);
            Boolean isSelect = isSelectFlagMap.get(path);
            if (isSelect == null) {
                isSelectFlagMap.put(path, false);
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        isSelectFlagMap.clear();
        needPreviewImages.clear();
        currentFolderPath = null;
        currentFolderName = null;
        actionFlag = ACTION_FLAG;
    }

}
