package com.move.ximageselectordemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.move.ximageSelector.config.XImgSelConfig;
import com.move.ximageSelector.imageView.XSelectAct;
import com.move.ximageSelector.utils.XImageLoader;

import java.util.ArrayList;

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
                .backTitle("返回")
                .isPreview(false)
                .maxNum(11)
                .isPreview(true)
                .cropSize(1, 1, 500, 500)
                .isCamera(true)
//                .btnConfirmAbleBgDrawable(R.drawable.able)
//                .btnConfirmDisableBgDrawable(R.drawable.able)
                .backResId(R.mipmap.chacha)
                .backResLeftMargin(40)
                .btnConfirmText("完成")
                .itemSelectedImg(R.mipmap.select)
                .itemUnSelectedImg(R.mipmap.unselect)
                .isCrop(true)
                .build(), 123);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra("data");

            String s = "";
            if (images != null && images.size() > 0) {
                for (int i = 0; i < images.size(); i++) {
                    s += images.get(i) + " \n";
                }
            }
            TextView tv_image = (TextView) findViewById(R.id.tv_image);
            tv_image.setText(s);
        }
    }

}
