package com.tomorrowhi.thdemo.util.imageLoderUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by zhaotaotao on 2016/11/9.
 * Picasso的使用工具类
 * Picasso常用参数定义：
 * .fit()                                  说明：控件不能设置成wrap_content,也就是必须有大小才行,fit()才让图片的宽高等于控件的宽高，设置fit()，不能再调用resize()
 * .placeholder(R.drawable.topic_tom)      说明：当图片没有加载上的时候，显示的图片
 * .error(R.drawable.topic_sky)            说明：当图片加载错误的时候，显示图片
 * .into(img_one)                          说明：将图片加载到哪个控件中
 * .resize(200, 150)                       说明：为图片重新定义大小
 * .centerCrop()                           说明：图片要填充整个控件，去两边留中间
 * .memoryPolicy(NO_CACHE, NO_STORE)       说明：NO_CACHE是指图片加载时放弃在内存缓存中查找，NO_STORE是指图片加载完不缓存在内存中。
 * 关于Picasso的优化可参考：http://blog.csdn.net/ashqal/article/details/48005833
 */

public class PicassoUtil {

    private static int defaultDrawable = 0;
    private static int errDrawable = 0;
    private static Picasso picasso;

    /**
     * 初始化picasso对象
     *
     * @param context
     */
    public static void setPicasso(Context context) {
        picasso = Picasso.with(context);
    }

    /**
     * 设置加载时显示的默认图片
     *
     * @param drawable
     */
    public static void setDefaultDrawable(int drawable) {
        defaultDrawable = drawable;
    }

    public static void clearFileCache(String filePath) {
        picasso.invalidate(filePath);
    }

    /**
     * 设置加载失败后显示的图片
     *
     * @param drawable
     */
    public static void setErrDrawable(int drawable) {
        errDrawable = drawable;
    }

    /**
     * 设置普通的不透明图片，采用Bitmap.Config.RGB_565编码格式，每个像素占用2byte内存
     * ALPHA_8：每个像素占用1byte内存
     * ARGB_4444:每个像素占用2byte内存
     * ARGB_8888:每个像素占用4byte内存（Android默认）
     */
    public static void setImageViewNoTransparent(String imageViewPath, ImageView iv) {
        if ((picasso != null) && (defaultDrawable != 0) && (errDrawable != 0)) {
            picasso.load(imageViewPath)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(defaultDrawable)
                    .error(errDrawable)
                    .into(iv);
        }
    }

    /**
     * 设置透明图片，采用Bitmap.Config.ARGB_8888编码格式，每个像素占用4byte内存
     * ALPHA_8：每个像素占用1byte内存
     * ARGB_4444:每个像素占用2byte内存
     * ARGB_8888:每个像素占用4byte内存（Android默认）
     */
    public static void setImageViewTransparent(String imageViewPath, ImageView iv) {
        if ((picasso != null) && (defaultDrawable != 0) && (errDrawable != 0)) {
            picasso.load(imageViewPath)
                    .config(Bitmap.Config.ARGB_8888)
                    .placeholder(defaultDrawable)
                    .error(errDrawable)
                    .into(iv);
        }
    }
}
