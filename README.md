# xImageSelector
图片选择器

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

### version 1.0
---
	dependencies {
	        compile 'com.github.xiaojinzi123:xImageSelector:1.2.1'
	}
### how to use
---
  
```
        XImageLoader imageLoader = new XImageLoader() {
            @Override
            public void load(Context context, String localPath, ImageView iv) {
                Glide.with(context).load(localPath).into(iv);
            }
        };

        //创建启动图片选择器的配置文件
        XImage.setConfig(new XImgSelConfig.Builder(imageLoader)
                .btnConfirmText("完成")
                .backTitle("")
                .backResLeftMargin(30)
                .backResId(R.mipmap.ic_close)
                .maxNum(1)
                .isPreview(true)
                .cropSize(1, 1, 600, 300)
                .isCamera(true)
                .isCrop(false)
                .build());

        //启动界面
        Intent i = new Intent(mContext, XSelectAct.class);
        startActivityForResult(i, requestImageCode);
```
123是请求码,需要在activity的OnActivityresult方法中使用
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestImageCode && resultCode == RESULT_OK && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra("data");

            if (images != null && images.size() > 0) {
                localImagePath = images.get(0);
                disPlayImageToIcon();
            } else {
                T.showShort(context, "更换头像失败");
            }

        }
    }
```
只有这个方法能打开选择界面,单选就是maxNum(1),多选的时候maxNum(1+)即可
框架中有对多选和单选时候的不同处理

![image1](https://github.com/xiaojinzi123/ximageLib/blob/master/desc/image1.png) 
![image2](https://github.com/xiaojinzi123/ximageLib/blob/master/desc/image2.png) 
![image3](https://github.com/xiaojinzi123/ximageLib/blob/master/desc/image3.png) 
