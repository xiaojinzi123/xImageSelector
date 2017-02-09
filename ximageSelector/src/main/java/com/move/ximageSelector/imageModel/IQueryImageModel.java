package com.move.ximageSelector.imageModel;

import android.content.Context;


import com.move.ximageSelector.listener.IQueryImageListener;

import java.util.ArrayList;

/**
 * Created by cxj on 2016/9/27.
 * 查询本地图片的数据处理层面,要求回掉接口中返回要展示的图片的集合
 * 1:所有的图片集合List<String>形式
 * 2:所有的图片目录集合List<String>形式
 * 2:某一个目录下的图片集合List<String>形式
 */
public interface IQueryImageModel {

    /**
     * 查询本地的图片,整理成集合
     *
     * @param context
     * @param selectImages
     * @param listener
     */
    void getAllImages(Context context, ArrayList<String> selectImages, IQueryImageListener listener);

    /**
     * 获取所有的图片文件夹
     *
     * @param listener
     */
    void getAllFolders(IQueryImageListener listener);

    /**
     * 获取某一个目录下的图片集合
     *
     * @param folder
     * @param listener
     */
    void getImageInFolder(String folder, IQueryImageListener listener);

    /**
     * 获取文件目录选择所需要的数据
     *
     * @param listener
     */
    void getFolderBeanList(IQueryImageListener listener);

    void release();
}
