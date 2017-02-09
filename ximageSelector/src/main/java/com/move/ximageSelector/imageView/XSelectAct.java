package com.move.ximageSelector.imageView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.move.ximageSelector.R;
import com.move.ximageSelector.XImage;
import com.move.ximageSelector.adapter.XImageAdapter;
import com.move.ximageSelector.config.XImgSelConfig;
import com.move.ximageSelector.imagePresenter.IQueryImagePresenter;
import com.move.ximageSelector.imageView.popup.SelectImageFolderPopup;
import com.move.ximageSelector.imageView.popup.bean.FolderBean;
import com.move.ximageSelector.utils.XImageRecoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.move.ximageSelector.autolayout.AutoLayoutActivity;
import com.move.ximageSelector.utils.other.ActivityUtil;
import com.move.ximageSelector.utils.other.recyclerView.CommonRecyclerViewAdapter;
import com.move.ximageSelector.utils.other.ProgressDialogUtil;
import com.move.ximageSelector.utils.other.SDCardUtils;
import com.move.ximageSelector.utils.other.T;


/**
 * 选择图片的界面
 */
public class XSelectAct extends AutoLayoutActivity implements IQueryImageView, View.OnClickListener {

    //请求照相的请求码
    public static final int REQUEST_CAMERA_CODE = 123;
    //请求裁剪的请求码
    public static final int REQUEST_IMAGE_CROP_CODE = 312;
    //请求裁剪一张图片的请求码
    public static final int REQUEST_IMAGE_CROP_ONE_CODE = 333;
    //请求存储权限的请求码
    public static final int REQUEST_STORAGE_CODE = 321;

    //等待小米收集裁剪文件的完成
    private int waitCameraCropCompleteCode = 999;
    private int waitSelectOneImageCropCompleteCode = 998;

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == waitCameraCropCompleteCode) {
                if (pictureFile != null && pictureFile.exists() && pictureFile.isFile() && pictureFile.length() > 0) {
                    dialog.dismiss();
                    pictureImages.add(pictureFile.getPath());
                    mImages.add(1, pictureFile.getPath());
                    XImageRecoder.getInstance().setSelectStatus(pictureFile.getPath(), false);
                    adapter.notifyItemInserted(1);
                } else {
                    h.sendEmptyMessageDelayed(waitCameraCropCompleteCode, 1000);
                }
            } else if (what == waitSelectOneImageCropCompleteCode) {
                if (tmpCropFile != null && tmpCropFile.exists() && tmpCropFile.isFile() && tmpCropFile.length() > 0) {
                    //保持一张裁剪后的图片被选中,然后直接返回
                    XImageRecoder.getInstance().initAllImageStatus(false);
                    XImageRecoder.getInstance().setSelectStatus(tmpCropFile.getPath(), true);
                    returnImages();
                } else {
                    h.sendEmptyMessageDelayed(waitSelectOneImageCropCompleteCode, 1000);
                }
            }
        }
    };

    private ProgressDialog dialog;

    //状态栏背景色
    private RelativeLayout rl_root;

    //标题栏
    private RelativeLayout rl_titlebar;

    //返回图标和文本
    private RelativeLayout rl_back;

    private ImageView iv_back;
    private TextView tv_back;

    //确定按钮
    private Button tv_confirm;

    //左下角的提示文本
    private TextView tv_tip;

    private TextView tv_preview;

    //显示图片的列表控件
    private RecyclerView rv;

    //主持人
    private IQueryImagePresenter presenter = new IQueryImagePresenter(this);

    //上下文
    private Context mContext;

    //要显示的数据
    private List<String> mImages = new ArrayList<String>();

    //显示的数据的图片属于的目录路径,为null表示所有图片
    private String mCurrentFolderPath;

    //显示的数据的适配器
    private XImageAdapter adapter;

    //显示图片的布局管理器
    private GridLayoutManager layoutManager;

    //照相得到的图片
    private List<String> pictureImages = new ArrayList<String>();

    //最近拍照的图片的文件对象
    private File pictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_xselect);

        mContext = this;

        initView();

        initData();

        setOnListener();

    }

    //设置监听事件
    private void setOnListener() {

        rl_back.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        //预览的点击事件
        tv_preview.setOnClickListener(this);

        tv_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取弹出的目录选择的数据
                presenter.getFolderBeanList();
            }
        });

        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                XImgSelConfig imgSelConfig = XImage.getConfig();

                if (position == 0 && imgSelConfig.needCamera) {
                    startCamera();
                    return;
                }

                if (imgSelConfig.needPreview) { //如果需要预览
                    ArrayList<String> tmpImages = new ArrayList<String>();
                    tmpImages.addAll(mImages);
                    if (imgSelConfig.needCamera) {
                        tmpImages.remove(0);
                    }
                    XImageRecoder.getInstance().setNeedPreviewImages(tmpImages);
                    Intent i = new Intent(mContext, XPreviewAct.class);
                    i.putExtra(XPreviewAct.IMAGEPOSITION_FLAG, position - (imgSelConfig.needCamera ? 1 : 0));
                    mContext.startActivity(i);
                } else {
                    if (imgSelConfig.maxNum > 1) { //如果要选择多张
                        toggleItemSelectStatus((ImageView) v.findViewById(R.id.iv_select_flag), position);
                    } else {
                        //设置为选中状态
                        XImageRecoder.getInstance().setSelectStatus(mImages.get(position), true);
                        if (imgSelConfig.needCrop) {
                            //开始裁剪
                            startCrop(new File(mImages.get(position)));
                        } else {
                            //返回路径
                            returnImages();
                        }
                    }
                }
            }
        });

        adapter.setOnViewInItemClickListener(new CommonRecyclerViewAdapter.OnViewInItemClickListener() {
            @Override
            public void onViewInItemClick(View v, int position) {
                toggleItemSelectStatus((ImageView) v, position);
            }
        }, R.id.iv_select_flag);

    }

    /**
     * 切换Item的选中状态
     *
     * @param v        item右上角的选中标记ImageView
     * @param position item的位置
     */
    private void toggleItemSelectStatus(ImageView v, int position) {
        //拿到选中的个数
        int selectImageNumber = getSelectImageNumber();

        XImageRecoder imageRecoder = XImageRecoder.getInstance();
        Boolean b = !imageRecoder.isSelect(mImages.get(position));
        if (b && selectImageNumber >= XImage.getConfig().maxNum) {
            T.showShort(mContext, "最多选择" + XImage.getConfig().maxNum + "张图片");
            return;
        }
        imageRecoder.setSelectStatus(mImages.get(position), b);
        ImageView iv = v;
        View view_cover = ((ViewGroup) iv.getParent()).findViewById(R.id.view_cover);
        if (b) {
            selectImageNumber++;
            iv.setImageResource(R.mipmap.select);
            view_cover.setAlpha(0.5f);
        } else {
            selectImageNumber--;
            iv.setImageResource(R.mipmap.unselect);
            view_cover.setAlpha(0f);
        }

        if (selectImageNumber > 0) {
            tv_confirm.setText(XImage.getConfig().btnConfirmText + "(" + selectImageNumber + "/" + XImage.getConfig().maxNum + ")");
            tv_preview.setText("预览(" + selectImageNumber + ")");
            tv_preview.setTextColor(XImage.getConfig().textAbledColor);
        } else {
            tv_confirm.setText(XImage.getConfig().btnConfirmText);
            tv_preview.setText("预览");
            tv_preview.setTextColor(XImage.getConfig().textDisabledColor);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //申请存储的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_CODE);
        } else {
            presenter.getAllImages();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {

        dialog = ProgressDialogUtil.create(this, ProgressDialog.STYLE_SPINNER, false);

        XImgSelConfig imgSelConfig = XImage.getConfig();

        if (imgSelConfig.isClearImageCache) {
            clearImageCache();
        }

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //找到控件
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_confirm = (Button) findViewById(R.id.tv_confirm);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_preview = (TextView) findViewById(R.id.tv_preview);
        rv = (RecyclerView) findViewById(R.id.rv);

        //状态栏背景
        if (imgSelConfig.statusBarColor != -1) {
            rl_root.setBackgroundColor(imgSelConfig.statusBarColor);
        }

        //标题栏背景
        if (imgSelConfig.titlebarBgColor != -1) {
            rl_titlebar.setBackgroundColor(imgSelConfig.titlebarBgColor);
        }

        //返回图标
        if (imgSelConfig.backResId != -1) {
            iv_back.setImageResource(imgSelConfig.backResId);
        }

        //标题文本
        if (!TextUtils.isEmpty(imgSelConfig.title)) {
            tv_back.setText(imgSelConfig.title);
        }

        //标题文本的颜色
        if (imgSelConfig.titleColor != -1) {
//            tv_back.setTextColor(imgSelConfig.titleColor);
        }

        //确定按钮背景
        if (imgSelConfig.btnConfirmBgDrawable != -1) {
            tv_confirm.setBackgroundResource(imgSelConfig.btnConfirmBgDrawable);
        }

        //确定按钮的文字颜色
        if (imgSelConfig.btnConfirmTextColor != -1) {
            tv_confirm.setTextColor(imgSelConfig.btnConfirmTextColor);
        }

        //是否显示右下角的预览文本
        if (!imgSelConfig.needPreview) {
            tv_preview.setVisibility(View.INVISIBLE);
        }

        if (imgSelConfig.textDisabledColor != -1) {
            tv_preview.setTextColor(imgSelConfig.textDisabledColor);
        }

        if (!TextUtils.isEmpty(imgSelConfig.btnConfirmText)) {
            tv_confirm.setText(imgSelConfig.btnConfirmText);
        }


        //创建一个对话框
        dialog = ProgressDialogUtil.create(mContext, ProgressDialog.STYLE_SPINNER, false);

        //创建适配器
        adapter = new XImageAdapter(mContext, mImages);

        layoutManager = new GridLayoutManager(mContext, 3);
        rv.setLayoutManager(layoutManager);

        //设置适配器
        rv.setAdapter(adapter);

    }


    //获取被选中的图片的个数
    private int getSelectImageNumber() {
        return XImageRecoder.getInstance().getSelectImageNumber();
    }

    /**
     * 清理图片
     */
    private void clearImageCache() {
        File folder = new File(SDCardUtils.getSDCardPath(), XImage.getConfig().sdCardFolderName);
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * 开启照相机
     */
    private void startCamera() {

        //申请存储的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_CODE);
            return;
        }

        boolean sdCardEnable = SDCardUtils.isSDCardEnable();
        if (!sdCardEnable) {
            T.showShort(mContext, "SD卡不可用,拍照功能不可用");
            return;
        }
        //获取到sd卡的路径
        boolean isCreate = SDCardUtils.createFolder(XImage.getConfig().sdCardFolderName);
        if (!isCreate) {
            T.showShort(mContext, "启动相机失败");
            return;
        }

        File folder = new File(SDCardUtils.getSDCardPath(), XImage.getConfig().sdCardFolderName);

        String fileName = System.currentTimeMillis() + ".png";

        boolean isCreateFile = SDCardUtils.createFile(folder, fileName);
        if (!isCreateFile) {
            T.showShort(mContext, "启动相机失败");
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        //photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        pictureFile = new File(folder, fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
        startActivityForResult(intent, REQUEST_CAMERA_CODE);

    }

    private File tmpCropFile;

    /**
     * 给一张图片选择的时候如果需要裁剪用的
     *
     * @param inFile
     */
    private void startCrop(File inFile) {

        boolean isCanCrop = true;

        if (isCanCrop) {
            isCanCrop = SDCardUtils.isSDCardEnable();
        }

        if (isCanCrop) {
            //获取到sd卡的路径
            isCanCrop = SDCardUtils.createFolder(XImage.getConfig().sdCardFolderName);
        }

        File folder = new File(SDCardUtils.getSDCardPath(), XImage.getConfig().sdCardFolderName);

        String fileName = System.currentTimeMillis() + ".png";

        if (isCanCrop) {
            isCanCrop = SDCardUtils.createFile(folder, fileName);
        }


        //如果可裁剪
        if (isCanCrop) {
            //创建输出文件
            tmpCropFile = new File(folder, fileName);
            //裁剪,在onActivityResult方法中返回
            crop(inFile, tmpCropFile, REQUEST_IMAGE_CROP_ONE_CODE);
        } else {
            //返回原图的路径
            XImageRecoder.getInstance().initAllImageStatus(false);
            XImageRecoder.getInstance().setSelectStatus(inFile.getPath(), true);
            returnImages();
        }

    }

    @Override
    public void showDialog(String content) {
    }

    @Override
    public void closeDialog() {
    }

    @Override
    public void disPlayAllImage(List<String> images) {


        int mSize = mImages.size();
        mImages.clear();

        adapter.notifyItemRangeRemoved(0, mSize);

        //如果需要相机
        if (XImage.getConfig().needCamera) {
            mImages.add("");
        }
        //如果有拍照过的
        mImages.addAll(pictureImages);
        //添加所有的图片
        mImages.addAll(images);

        //记录对象
        XImageRecoder imageRecoder = XImageRecoder.getInstance();

        //设置预选中的图片,如果在扫描出来的图片中没有,那么久添加进去
        ArrayList<String> selectImage = XImage.getConfig().selectImage;
        if (selectImage != null) {
            int size = selectImage.size();
            for (int i = 0; i < size; i++) {
                //拿到要选中的图片的路径
                String path = selectImage.get(i);
                //设置为选中状态
                imageRecoder.setSelectStatus(path, true);
            }
        }

        //如果key存在是不会做操作的
        imageRecoder.initImage(mImages);

        if (selectImage != null) {
            int size = selectImage.size();
            if (size > 0) {
                tv_confirm.setText(XImage.getConfig().btnConfirmText + "(" + size + "/" + XImage.getConfig().maxNum + ")");
            } else {
                tv_confirm.setText(XImage.getConfig().btnConfirmText);
            }
        }

        adapter.notifyItemRangeInserted(0, mImages.size());

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void disPlayFolderBeanList(List<FolderBean> folderBeanList) {
        SelectImageFolderPopup p = new SelectImageFolderPopup(mContext, folderBeanList);
        p.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                XImageRecoder imageRecoder = XImageRecoder.getInstance();
                //拿到当前需要显示的目录
                String currentFolderPath = imageRecoder.getCurrentFolderPath();
                //拿到当前的目录名字
                String currentFolderName = imageRecoder.getCurrentFolderName();

                if (currentFolderPath == null) {
                    if (mCurrentFolderPath != null) {
                        presenter.getAllImages();
                        mCurrentFolderPath = null;
                        tv_tip.setText(currentFolderName);
                    }
                } else {
                    if (mCurrentFolderPath != null && mCurrentFolderPath.equals(currentFolderPath)) {
                    } else {
                        presenter.getAllImagesInFolder(currentFolderPath);
                        mCurrentFolderPath = currentFolderPath;
                        tv_tip.setText(currentFolderName);
                    }
                }
            }
        });
        p.show();
    }

    @Override
    public void disPlayAllImageInFolder(List<String> images) {
        int size = mImages.size();
        mImages.clear();
        adapter.notifyItemRangeRemoved(0, size);
        if (XImage.getConfig().needCamera) {
            mImages.add("");
        }
        mImages.addAll(pictureImages);
        mImages.addAll(images);
        adapter.notifyItemRangeInserted(0, mImages.size());
    }

    @Override
    protected void onResume() {
        super.onResume();

        XImageRecoder imageRecoder = XImageRecoder.getInstance();

        int actionFlag = imageRecoder.getActionFlag();

        //如果要结束并且返回
        if (actionFlag == XImageRecoder.ACTION_FLAG_ONE) {
            if (XImage.getConfig().maxNum <= 1 && XImage.getConfig().needCrop) {
                ArrayList<String> images = imageRecoder.getSelectImages();
                if (images.size() > 0) {
                    startCrop(new File(images.get(0)));
                    imageRecoder.setActionFlag(XImageRecoder.ACTION_FLAG);
                    return;
                }
            }
            //返回多个图片
            returnImages();
            imageRecoder.setActionFlag(XImageRecoder.ACTION_FLAG);
            return;
        }

        //如果要刷新item
        if (actionFlag == XImageRecoder.ACTION_FLAG_TWO) {
            reFreshVisibleItem();
            imageRecoder.setActionFlag(XImageRecoder.ACTION_FLAG);
            return;
        }

    }

    /**
     * 刷新可见的item
     */
    private void reFreshVisibleItem() {
        //更新看得见的这些数据
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
            adapter.notifyItemRangeChanged(firstVisibleItemPosition, lastVisibleItemPosition - firstVisibleItemPosition + 1);
        }

        //拿到选中的个数
        int selectImageNumber = getSelectImageNumber();

        if (selectImageNumber > 0) {
            tv_confirm.setText(XImage.getConfig().btnConfirmText + "(" + selectImageNumber + "/" + XImage.getConfig().maxNum + ")");
            tv_preview.setText("预览(" + selectImageNumber + ")");
            tv_preview.setTextColor(XImage.getConfig().textAbledColor);
        } else {
            tv_confirm.setText(XImage.getConfig().btnConfirmText);
            tv_preview.setText("预览");
            tv_preview.setTextColor(XImage.getConfig().textDisabledColor);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK) { //如果拍照成功
            if (pictureFile != null && pictureFile.exists() && pictureFile.isFile()) {
                if (XImage.getConfig().needCrop) {
                    crop(pictureFile, pictureFile, REQUEST_IMAGE_CROP_CODE);
                } else {
                    pictureImages.add(pictureFile.getPath());
                    mImages.add(1, pictureFile.getPath());
                    XImageRecoder.getInstance().setSelectStatus(pictureFile.getPath(), false);
                    adapter.notifyItemInserted(1);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CROP_CODE && resultCode == RESULT_OK) { //如果裁剪成功
            dialog.setMessage("正在生成裁剪图片");
            dialog.show();
            h.sendEmptyMessageDelayed(waitCameraCropCompleteCode, 1000);
        } else if (requestCode == REQUEST_IMAGE_CROP_ONE_CODE) {
            if (resultCode == RESULT_OK) {//如果裁剪成功
                dialog.setMessage("正在生成裁剪图片");
                dialog.show();
                h.sendEmptyMessageDelayed(waitSelectOneImageCropCompleteCode, 1000);
            } else if (resultCode == RESULT_CANCELED) { //如果取消裁剪
                XImageRecoder.getInstance().initAllImageStatus(false);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果申请存储的权限成功,那么我们就去获取图片信息来展示
        if (requestCode == REQUEST_STORAGE_CODE) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.getAllImages();
            }else{
                T.showShort(mContext,"请在权限管理中允许外部存储权限");
            }
        } else if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //开启相机
                startCamera();
            }else{
                T.showShort(mContext,"请在权限管理中允许相机权限");
            }

        }
    }

    /**
     * 进行裁剪
     *
     * @param inPictureFile  源文件
     * @param outPictureFile 输出文件
     * @param requestCode    裁剪的请求码
     */
    private void crop(File inPictureFile, File outPictureFile, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(inPictureFile), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", XImage.getConfig().aspectX);
        intent.putExtra("aspectY", XImage.getConfig().aspectY);
        intent.putExtra("outputX", XImage.getConfig().outputX);
        intent.putExtra("outputY", XImage.getConfig().outputY);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPictureFile));
        startActivityForResult(intent, requestCode);
    }

    /**
     * 打开图片选择器
     *
     * @param context
     * @param config
     */
    public static void open(Activity context, XImgSelConfig config, int requestCode) {
        if (config == null) {
            throw new NullPointerException("the config can not be null");
        }
        XImage.setConfig(config);
        Intent i = new Intent(context, XSelectAct.class);
        context.startActivityForResult(i, requestCode);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_back) {
            finish();
        }
        if (id == R.id.tv_confirm) {
            returnImages();
        }
        if (id == R.id.tv_preview) { //如果是预览的按钮
            XImageRecoder imageRecoder = XImageRecoder.getInstance();
            imageRecoder.setNeedPreviewImages(imageRecoder.getSelectList());
            ActivityUtil.startActivity(mContext, XPreviewAct.class);
        }
    }

    /**
     * 返回结果
     */
    private void returnImages() {
        Intent i = new Intent();
        i.putStringArrayListExtra("data", XImageRecoder.getInstance().getSelectImages());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void finish() {
        dialog.dismiss();
        super.finish();
        int size = mImages.size();
        mImages.clear();
        adapter.notifyItemRangeRemoved(0, size);
        XImageRecoder.getInstance().release();
        presenter.release();
    }
}
