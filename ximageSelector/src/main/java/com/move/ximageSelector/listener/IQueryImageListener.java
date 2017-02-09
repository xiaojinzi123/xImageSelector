package com.move.ximageSelector.listener;


import com.move.ximageSelector.imageView.popup.bean.FolderBean;

import java.util.List;

/**
 * Created by cxj on 2016/9/27.
 */
public interface IQueryImageListener {

    /**
     * 展示所有图片
     *
     * @param images
     */
    void disPlayAllImage(List<String> images);

    /**
     * 供给用户选择的所有文件夹
     *
     * @param folders
     */
    void disPlayAllFolder(List<String> folders);


    /**
     * 展示某一个文件夹下的图片
     *
     * @param images
     */
    void disPlayImageInFolder(List<String> images);

    void disFolderBeanList(List<FolderBean> folderBeanList);
}
