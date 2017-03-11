package com.move.ximageSelector.imageView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.move.ximageSelector.R;
import com.move.ximageSelector.XImage;
import com.move.ximageSelector.config.XImgSelConfig;
import com.move.ximageSelector.imageView.fragment.XPreviewFrag;
import com.move.ximageSelector.utils.XImageRecoder;

import java.util.ArrayList;
import java.util.List;

import com.move.ximageSelector.autolayout.AutoLayoutActivity;
import com.move.ximageSelector.utils.other.ScreenUtils;
import com.move.ximageSelector.utils.other.T;

/**
 * 实现图片预览的界面
 * 首先这个界面支持的用法做一个说明:
 * 会从单例类中获取要显示的图片的集合,所以在进入此界面前,
 * 请在单例类中设置好要显示的集合,并且这个单例类中还有图
 * 片是否选中的记录,以便预览的时候可以显示图片是否选中
 */
public class XPreviewAct extends AutoLayoutActivity implements View.OnClickListener {

    public static final String IMAGELIST_FLAG = "imagelist_flag";
    public static final String IMAGEPOSITION_FLAG = "image_position_flag";

    private Context mContext;

    private RelativeLayout rl_root;


    //标题栏
    private RelativeLayout rl_titlebar_container;
    private RelativeLayout rl_titlebar;

    private RelativeLayout rl_foot_menu;

    //返回图标和文本
    private RelativeLayout rl_back;
    private ImageView iv_back;

    //确定按钮
    private Button tv_confirm;

    private TextView tv_index_of_all_image;

    private TextView tv_select;
    private ImageView iv_select_icon;

    private ViewPager vp = null;

    private PagerAdapter adapter;

    //需要展示的View试图的集合
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private List<String> images;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_xpreview);

        mContext = this;

        //检查运行必要的配置参数
        if (XImage.getConfig() == null) {
            Toast.makeText(mContext, "参数错误", Toast.LENGTH_SHORT).show();
            return;
        }

        //初始化控件
        initView();

        //初始化数据
        initData();

        //设置点击事件
        setOnListener();

    }

    /**
     * 设置监听
     */
    private void setOnListener() {

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeFootSelectStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        iv_select_icon.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

    }

    private void changeFootSelectStatus(int position) {
        tv_index_of_all_image.setText((position + 1) + "/" + images.size());
        Boolean b = XImageRecoder.getInstance().isSelect(images.get(position));
        if (b != null && b) {
            iv_select_icon.setImageResource(XImage.getConfig().itemSelectedImg);
        } else {
            iv_select_icon.setImageResource(XImage.getConfig().itemUnSelectedImg);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //拿到显示第几张图片的位置
        position = getIntent().getIntExtra(IMAGEPOSITION_FLAG, 0);
        //拿到要预览的图片集合
        images = XImageRecoder.getInstance().getNeedPreviewImages();
        if (images.size() == 0) {
            T.showShort(mContext, "没有需要预览的图片");
            finish();
            return;
        }
        //显示出来
        disPlay();
    }

    /**
     * 显示数据
     */
    private void disPlay() {

        if (images == null) {
            return;
        }

        XImgSelConfig config = XImage.getConfig();

        int size = images.size();
        for (int i = 0; i < size; i++) {
            String localPath = images.get(i);
            XPreviewFrag frag = new XPreviewFrag();
            frag.setLocalPath(localPath);
            fragments.add(frag);
        }

        adapter.notifyDataSetChanged();
        vp.setCurrentItem(position);

        tv_index_of_all_image.setText((position + 1) + "/" + images.size());
        //拿到选中的图片个数
        int selectImageNumber = XImageRecoder.getInstance().getSelectImageNumber();
        if (selectImageNumber == 0) {
            tv_confirm.setText(config.btnConfirmText);
            tv_confirm.setTextColor(config.textDisabledColor);
            tv_confirm.setBackgroundResource(config.btnConfirmDisableBgDrawable);
        } else {
            tv_confirm.setText(config.btnConfirmText + "（" + selectImageNumber + "/" + config.maxNum + ")");
            tv_confirm.setTextColor(config.textAbledColor);
            tv_confirm.setBackgroundResource(config.btnConfirmAbleBgDrawable);
        }


        changeFootSelectStatus(position);

    }

    /**
     * 初始化控件
     */
    private void initView() {

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //找到控件
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        rl_titlebar_container = (RelativeLayout) findViewById(R.id.rl_titlebar_container);
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        rl_foot_menu = (RelativeLayout) findViewById(R.id.rl_foot_menu);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_index_of_all_image = (TextView) findViewById(R.id.tv_index_of_all_image);
        tv_confirm = (Button) findViewById(R.id.tv_confirm);
        tv_select = (TextView) findViewById(R.id.tv_select);
        iv_select_icon = (ImageView) findViewById(R.id.iv_select_icon);
        vp = (ViewPager) findViewById(R.id.vp);

        int statusHeight = ScreenUtils.getStatusHeight(mContext);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) rl_titlebar.getLayoutParams();
        lp.topMargin = statusHeight;

        XImgSelConfig imgSelConfig = XImage.getConfig();

        //状态栏北京
        rl_titlebar_container.setBackgroundColor(imgSelConfig.statusBarColor);

        //标题栏背景
        rl_titlebar.setBackgroundColor(imgSelConfig.titlebarBgColor);
        rl_foot_menu.setBackgroundColor(imgSelConfig.titlebarBgColor);

        //返回图标
        //iv_back.setImageResource(imgSelConfig.backResId);
        //如果返回图标有左边距
        lp = (ViewGroup.MarginLayoutParams) iv_back.getLayoutParams();
        lp.leftMargin = imgSelConfig.backResLeftMargin;
        iv_back.setLayoutParams(lp);


        //确定按钮的背景颜色
        tv_confirm.setBackgroundResource(imgSelConfig.btnConfirmAbleBgDrawable);

        //确定按钮的文字颜色
        tv_confirm.setTextColor(imgSelConfig.btnConfirmTextColor);


        //确定按钮的文本
        tv_confirm.setText(imgSelConfig.btnConfirmText);

        //创建适配器
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        //设置适配器
        vp.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {

        XImgSelConfig config = XImage.getConfig();
        XImageRecoder xImageRecoder = XImageRecoder.getInstance();

        int id = view.getId();

        if (id == R.id.tv_select || id == R.id.iv_select_icon) {
            XImageRecoder imageRecoder = xImageRecoder;
            //拿到当前显示的这个是否是选中的
            Boolean b = imageRecoder.isSelect(images.get(vp.getCurrentItem()));
            if (b) { //如果是选中的
                imageRecoder.setSelectStatus(images.get(vp.getCurrentItem()), false);
                iv_select_icon.setImageResource(config.itemUnSelectedImg);
                //让前面的界面刷新数据
                imageRecoder.setActionFlag(XImageRecoder.ACTION_FLAG_TWO);
            } else {
                int selectImageNumber = imageRecoder.getSelectImageNumber();
                if (selectImageNumber >= config.maxNum) {
                    T.showShort(mContext, "最多选择" + config.maxNum + "张图片");
                    return;
                }
                imageRecoder.setSelectStatus(images.get(vp.getCurrentItem()), true);
                iv_select_icon.setImageResource(config.itemSelectedImg);
                if (config.maxNum <= 1) {
                    xImageRecoder.setActionFlag(XImageRecoder.ACTION_FLAG_ONE);
                    finish();
                } else {
                    //让前面的界面刷新数据
                    imageRecoder.setActionFlag(XImageRecoder.ACTION_FLAG_TWO);
                }
            }

            //拿到选中的图片的个数
            int selectImageNumber = xImageRecoder.getSelectImageNumber();
            if (selectImageNumber == 0) {
                tv_confirm.setText(config.btnConfirmText);
                tv_confirm.setTextColor(config.textDisabledColor);
                tv_confirm.setBackgroundResource(config.btnConfirmDisableBgDrawable);
            } else {
                tv_confirm.setText(config.btnConfirmText + "（" + selectImageNumber + "/" + config.maxNum + ")");
                tv_confirm.setTextColor(config.textAbledColor);
                tv_confirm.setBackgroundResource(config.btnConfirmAbleBgDrawable);
            }

        }

        if (id == R.id.tv_confirm && xImageRecoder.getSelectImageNumber() > 0) { //如果点击了确定并且选中的个数大于0个
            xImageRecoder.setActionFlag(XImageRecoder.ACTION_FLAG_ONE);
            finish();
        }
    }

    //标题栏是否显示了
    private boolean isShow = true;

    /**
     * 隐藏标题栏和下面
     */
    public void toggle() {
        if (isShow) {
            rl_titlebar_container.setVisibility(View.INVISIBLE);
            rl_titlebar.setVisibility(View.INVISIBLE);
            rl_foot_menu.setVisibility(View.INVISIBLE);
            rl_titlebar.setFitsSystemWindows(false);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            rl_titlebar_container.setVisibility(View.VISIBLE);
            rl_titlebar.setVisibility(View.VISIBLE);
            rl_foot_menu.setVisibility(View.VISIBLE);
            rl_titlebar.setFitsSystemWindows(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        isShow = !isShow;
    }

}
