package com.bowen.zdsjclub.util;

import android.content.Context;
import android.widget.ImageView;

import com.bowen.zdsjclub.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class ToolImage {
	public static void loading(Context context, ImageView imageView){
		Glide.with(context).load(R.mipmap.loading_data).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
	}
}
