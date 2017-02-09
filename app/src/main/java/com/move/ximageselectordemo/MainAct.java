package com.move.ximageselectordemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.move.ximageSelector.config.XImgSelConfig;
import com.move.ximageSelector.imageView.XSelectAct;
import com.move.ximageSelector.utils.XImageLoader;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
    }

    public void clickView(View view) {

        XImageLoader imageLoader = new XImageLoader() {
            @Override
            public void load(Context context, String localPath, ImageView iv) {
                Glide.with(context).load(localPath).into(iv);
            }
        };

        XSelectAct.open(this, new XImgSelConfig.Builder(imageLoader)
                .btnConfirmText("完成")
                .title("图片")
                .isPreview(true)
                .maxNum(1)
                .isPreview(false)
                .cropSize(1, 1, 800, 800)
                .isCamera(true)
                .isCrop(true)
                .build(), 123);

    }

}