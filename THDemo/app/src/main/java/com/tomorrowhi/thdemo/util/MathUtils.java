package com.tomorrowhi.thdemo.util;

import java.text.DecimalFormat;

/**
 * Created by zhaotaotao on 2017/5/9.
 */

public class MathUtils {

    public static DecimalFormat dfInt = new DecimalFormat("#");   //一个数字
    public static DecimalFormat dfFloat = new DecimalFormat("##.#");   //一个数字
    public static DecimalFormat dfDouble = new DecimalFormat("##.##");   //一个数字

    public static String getdfInt(double dec){
        return dfInt.format(dec);
    }
}
