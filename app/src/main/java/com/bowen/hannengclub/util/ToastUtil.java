package com.bowen.hannengclub.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 作者：bowen
 * 邮箱：710081921@qq.com
 */

public class ToastUtil {

    public static Toast mToast;
    /**
     * id
     * @param context
     * @param id
     */
    public static void showToast(Context context, int id) {
        cancelToast();
        mToast = Toast.makeText(context, id, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * sring
     * @param context
     * @param str
     */
    public static void showToast(Context context, String str) {
        //先关闭显示，再去显示
        cancelToast();
        mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void cancelToast(){
        if(mToast != null){
            mToast.cancel();
            mToast = null;
        }
    }
}
