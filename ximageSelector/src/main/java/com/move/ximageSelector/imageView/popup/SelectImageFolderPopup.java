package com.move.ximageSelector.imageView.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.move.ximageSelector.R;
import com.move.ximageSelector.XImage;
import com.move.ximageSelector.imageView.popup.bean.FolderBean;
import com.move.ximageSelector.utils.XImageRecoder;


import java.util.ArrayList;
import java.util.List;

import com.move.ximageSelector.utils.other.recyclerView.CommonRecyclerViewAdapter;
import com.move.ximageSelector.utils.other.recyclerView.CommonRecyclerViewHolder;


/**
 * Created by cxj on 2016/10/9.
 */
public class SelectImageFolderPopup extends BottomSheetDialog {

    private Context mContext;

    private RecyclerView rv;

    private CommonRecyclerViewAdapter<FolderBean> adapter;

    private List<FolderBean> folders = new ArrayList<FolderBean>();

    public SelectImageFolderPopup(@NonNull Context context, final List<FolderBean> folders) {
        super(context);
        this.folders = folders;
        mContext = context;
        View v = View.inflate(mContext, R.layout.act_xselect_folder, null);

        rv = (RecyclerView) v.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setLayoutManager(layoutManager);

        adapter = new CommonRecyclerViewAdapter<FolderBean>(mContext, this.folders) {

            @Override
            public void convert(CommonRecyclerViewHolder h, FolderBean entity, int position) {

                XImage.getConfig().loader.load(mContext, entity.getFirstImagePath(), (ImageView) h.getView(R.id.iv));

                h.setText(R.id.tv_folder_name, entity.getFolderName());
                h.setText(R.id.tv_images_number, entity.getNumberOfImagesInFolder() + "");

                ImageView iv_choose_flag = h.getView(R.id.iv_choose_flag);

                if (entity.isChoosed()) {
                    iv_choose_flag.setVisibility(View.VISIBLE);
                } else {
                    iv_choose_flag.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.act_xselect_folder_item;
            }
        };

        rv.setAdapter(adapter);

        setContentView(v);

        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                for (int i = 0; i < folders.size(); i++) {
                    folders.get(i).setChoosed(false);
                }
                folders.get(position).setChoosed(true);
                XImageRecoder imageRecoder = XImageRecoder.getInstance();
                if (position == 0) {
                    imageRecoder.setCurrentFolderPath(null);
                    imageRecoder.setCurrentFolderName("全部图片");
                } else {
                    imageRecoder.setCurrentFolderPath(folders.get(position).getFolderPath());
                    imageRecoder.setCurrentFolderName(folders.get(position).getFolderName());
                }
                dismiss();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


}
