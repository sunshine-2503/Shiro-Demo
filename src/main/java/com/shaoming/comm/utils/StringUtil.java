package com.shaoming.comm.utils;

import java.util.Random;

/**
 * Created by ShaoMing on 2018/5/16
 */
public class StringUtil {

    /**
     * 生成指定长度的盐值（随机字符串）
     * @param length 盐值长度
     * @return 生成的盐字符串
     */
    public static String createRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuilder sb = new StringBuilder();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

}
