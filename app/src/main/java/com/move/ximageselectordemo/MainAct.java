package com.move.ximageselectordemo;

import android.content.Context;
import android.graphics.Color;
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
                .backTitle("")
                .isPreview(true)
                .maxNum(11)
                .isPreview(false)
                .cropSize(1, 1, 500, 500)
                .isCamera(true)
                .backResId(R.mipmap.chacha)
                .backResLeftMargin(40)
                .btnConfirmText("完成")
                .isCrop(true)
                .build(), 123);

    }

}
