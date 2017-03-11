package com.move.ximageSelector.imageModel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.move.ximageSelector.imageView.popup.bean.FolderBean;
import com.move.ximageSelector.listener.IQueryImageListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cxj on 2016/9/27.
 */
public class QueryImageModel implements IQueryImageModel {

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;
            if (what == 0) { //表示调用
                if (mListener != null) {
                    mListener.disPlayAllImage(mImages);
                }
            }

        }
    };

    //扫描一遍之后就不会再扫描了,缓存了集合
    private List<String> mImages = new ArrayList<String>();

    private HashMap<String, List<String>> folders = new HashMap<String, List<String>>();

    private List<FolderBean> folderBeanList = new ArrayList<FolderBean>();

    private IQueryImageListener mListener;

    @Override
    public void getAllImages(final Context context, final ArrayList<String> selectImages, IQueryImageListener listener) {

        if (mImages.size() > 0) {
            listener.disPlayAllImage(mImages);
            return;
        }

        mListener = listener;

        new Thread() {
            @Override
            public void run() {

                //要查询的图片类型
                String[] mimeType = new String[]{"image/png", "image/jpeg"};

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver =
                        context.getContentResolver();

                //查询的条件
                StringBuffer selection = new StringBuffer();
                //利用循环生成条件
                for (int i = 0; i < mimeType.length; i++) {
                    if (i == mimeType.length - 1) { //如果是最后一个
                        selection.append(MediaStore.Images.Media.MIME_TYPE + " = ?");
                    } else {
                        selection.append(MediaStore.Images.Media.MIME_TYPE + " = ? or ");
                    }
                }

                //执行查询
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        selection.toString(),
                        mimeType,
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");


                //循环结果集
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String imagePath = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    mImages.add(imagePath);

                    File file = new File(imagePath);
                    //拿到目录
                    String folderPath = file.getParent();

                    List<String> images = folders.get(folderPath);
                    if (images == null) {
                        images = new ArrayList<String>();
                        folders.put(folderPath, images);
                    }
                    images.add(imagePath);
                }

                //如果有事先选中的图片集合
                if (selectImages != null) {
                    int size = selectImages.size();
                    for (int i = 0; i < size; i++) {
                        //拿到选中的图片路径
                        String path = selectImages.get(i);
                        if (!mImages.contains(path)) {
                            mImages.add(0,path);
                            File file = new File(path);
                            //拿到目录
                            String folderPath = file.getParent();

                            List<String> images = folders.get(folderPath);
                            if (images == null) {
                                images = new ArrayList<String>();
                                folders.put(folderPath, images);
                            }
                            images.add(0, path);
                        }
                    }
                }

                FolderBean folderBean = new FolderBean(mImages.get(0), "全部图片", mImages.size(), true);
                folderBeanList.add(folderBean);

                Iterator<String> it = folders.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    List<String> tmpImages = folders.get(key);
                    File file = new File(key);
                    folderBean = new FolderBean(tmpImages.get(0), file.getName(), tmpImages.size(), false);
                    folderBean.setFolderPath(file.getPath());
                    folderBeanList.add(folderBean);
                }

                h.sendEmptyMessage(0);

            }
        }.start();

    }

    @Override
    public void getAllFolders(IQueryImageListener listener) {
        Iterator<String> it = folders.keySet().iterator();
        ArrayList<String> tmpFolders = new ArrayList<String>();
        while (it.hasNext()) {
            String folderPath = it.next();
            tmpFolders.add(folderPath);
        }
        listener.disPlayAllFolder(tmpFolders);
    }

    @Override
    public void getImageInFolder(String folder, IQueryImageListener listener) {
        listener.disPlayImageInFolder(folders.get(folder));
    }

    @Override
    public void getFolderBeanList(IQueryImageListener listener) {
        listener.disFolderBeanList(folderBeanList);
    }

    @Override
    public void release() {
        mImages.clear();
        folders.clear();
        folderBeanList.clear();
    }
}
