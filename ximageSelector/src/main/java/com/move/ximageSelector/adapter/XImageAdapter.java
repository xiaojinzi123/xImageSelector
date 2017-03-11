package com.move.ximageSelector.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.move.ximageSelector.R;
import com.move.ximageSelector.XImage;
import com.move.ximageSelector.config.XImgSelConfig;
import com.move.ximageSelector.utils.XImageRecoder;

import java.util.List;

import com.move.ximageSelector.utils.other.recyclerView.CommonRecyclerViewAdapter;
import com.move.ximageSelector.utils.other.recyclerView.CommonRecyclerViewHolder;

/**
 * Created by cxj on 2016/9/29.
 * 显示图片的适配器
 */
public class XImageAdapter extends CommonRecyclerViewAdapter<String> {

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param data    显示的数据
     */
    public XImageAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, String entity, int position) {

        //图片
        ImageView iv = h.getView(R.id.iv);

        iv.setImageResource(R.mipmap.ic_default_image);

        //拿到覆盖层
        View view_cover = h.getView(R.id.view_cover);
        view_cover.setBackgroundColor(Color.parseColor("#666666"));

        ImageView iv_select_flag = h.getView(R.id.iv_select_flag);

        //配置文件
        XImgSelConfig imgSelConfig = XImage.getConfig();

        if (position == 0 && imgSelConfig.needCamera) {
            iv_select_flag.setVisibility(View.INVISIBLE);
            view_cover.setVisibility(View.INVISIBLE);
            iv.setImageBitmap(null);
            iv.setImageResource(imgSelConfig.cameraResId);
            return;
        }

        if (imgSelConfig.maxNum > 1) {
            iv_select_flag.setVisibility(View.VISIBLE);
            view_cover.setVisibility(View.VISIBLE);
        } else {
            iv_select_flag.setVisibility(View.INVISIBLE);
            view_cover.setVisibility(View.INVISIBLE);
        }

        //加载图片
        imgSelConfig.loader.load(context, entity, iv);

        if (XImageRecoder.getInstance().isSelect(entity)) { //如果是选中的
            iv_select_flag.setImageResource(R.mipmap.select);
            view_cover.setAlpha(0.5f);
        } else {
            iv_select_flag.setImageResource(R.mipmap.unselect);
            view_cover.setAlpha(0f);
        }

    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.image_item;
    }
}
