package com.bowen.hannengclub.util;

import android.app.Activity;
import android.widget.ImageView;

import com.bowen.hannengclub.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;

/**
 * ================================================
 * 作    者：bowen
 * 版    本：1.0
 * 创建日期：2016/11/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        Glide.with(activity)                           //配置上下文
             //.load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
             .load(path)
             .error(R.mipmap.ic_launcher)           //设置错误图片
             .placeholder(R.mipmap.ic_launcher)     //设置占位图片
             .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
             .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
