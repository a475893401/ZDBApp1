package com.zhuandanbao.app.utils;

import java.util.Random;

/**
 * Created by BFTECH on 2016/4/28.
 */
public class SuiJiShu {

    public static int Num() {
        int a = 10086;
        Random random = new Random();
        int[] arr = new int[4];
        arr[0] = random.nextInt(9);
        int i = 1;
        //外循环定义四个数
        while (i <= 3) {
            int x = random.nextInt(9);
            /*内循环：新生成随机数和已生成的比较，
             *相同则跳出内循环，再生成一个随机数进行比较
             *和前几个生成的都不同则这个就是新的随机数
             */
            for (int j = 0; j <= i - 1; j++) {
                //相同则跳出内循环，再生成一个随机数进行比较
                if (arr[j] == x) {
                    break;
                }
                //执行完循环和前几个生成的都不同则这个就是新的随机数
                if (j + 1 == i) {
                    arr[i] = x;
                    i++;
                }
            }
        }
        //打印出来生成的随机数
        for (int aaa : arr) {
            a += aaa;
        }
        return a;
    }
}
