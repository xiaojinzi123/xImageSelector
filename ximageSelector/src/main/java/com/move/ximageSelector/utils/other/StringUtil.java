package com.move.ximageSelector.utils.other;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;

/**
 * 有关字符串的工具类
 *
 * @author xiaojinzi
 */
@SuppressLint("DefaultLocale")
public class StringUtil {

    /**
     * 把一个流转化成字符串
     *
     * @param is           要转化的流对象
     * @param charEncoding 编码方式
     * @param pd           进度条对话框
     * @return
     * @throws IOException
     */
    public static String isToStr(InputStream is, String charEncoding) throws IOException {

        // 定义缓冲区
        byte[] bts = new byte[1024];

        // 定义读取的长度
        int len = -1;

        // 定义字节数组的输出的工具类
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 循环读取
        while ((len = is.read(bts)) != -1) {
            // 写出到out中
            out.write(bts, 0, len);
        }

        // 得到字符串
        String content = new String(out.toByteArray(), charEncoding);

        // 关闭资源
        is.close();
        out.close();

        // 返回数据
        return content;
    }

    /**
     * 格式化歌曲的时长 400 --> 06:40
     *
     * @param duration 需要格式化的整形时长
     * @return 返回一个整形格式化后的字符串, 如:<br>
     * 06:40
     */
    public static String formatMusciLength(int duration) {
        // 获取秒
        int seconds = duration % 60;
        // 获取分钟
        int minute = (duration - seconds) / 60;

        String tmp = "";

        if (("" + minute).length() == 1) {
            tmp += "0" + minute + ":";
        } else {
            tmp += minute + ":";
        }

        if (("" + seconds).length() == 1) {
            tmp += "0" + seconds;
        } else {
            tmp += seconds;
        }
        return tmp;
    }

    /**
     * 判断一个字符串是不是一个手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        String pattern = "^[1][34578][0-9]{9}$";
        return phone.matches(pattern);
    }

    /**
     * 首字母小写
     *
     * @param content
     * @return
     */
    public static String firstCharToLowerCase(String content) {
        return content.substring(0, 1).toLowerCase() + content.substring(1);
    }

    /**
     * 首字母大写
     *
     * @param content
     * @return
     */
    public static String firstCharToUpperCase(String content) {
        return content.substring(0, 1).toUpperCase() + content.substring(1);
    }

    /**
     * 截取一个字符串中的最后一点内容,这个字符串是通过一个标识一处或者多处隔开的,比如：<br>
     * D:\qq\ww\ee\rr\1.txt ,如果调用这个方法传入这个字符串和"\"标识,就会返回：<br>
     * 1.txt <br>
     * 如果没有这个标识符,那就返回原来的字符串
     *
     * @param content
     * @param flag
     * @return
     */
    public static String getLastContent(String content, String flag) {
        int index = content.lastIndexOf(flag);
        if (index != -1) {
            content = content.substring(index + 1);
        }
        return content;
    }

    /**
     * 过滤处理电话号码 <br>
     * 1.去掉电话中的空格 <br>
     * 2.去掉前面的+86 <br>
     * 3.经过前面两部处理,如果不满足十一位就直接pass掉
     *
     * @param phone
     * @return
     */
    public static String filterPhoneNumber(String phone) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < phone.length(); i++) {
            char c = phone.charAt(i);
            if (' ' != c) {
                sb.append(c);
            }
        }

        phone = sb.toString();

        if (phone.startsWith("+86")) {
            phone = phone.substring(3);
        }

        if (phone.length() == 11) {
            return phone;
        }

        return null;
    }

    /**
     * 对一个文件的大小进行格式化,默认保留小数点后一位
     *
     * @param byteSize
     * @return
     */
    public static String fileSizeFormat(long byteSize) {

        if (byteSize <= 0) {
            return "0B";
        }

        if (byteSize < 1024) {
            return byteSize + "B";
        }

        float tmp = byteSize / 1024f;

        if (tmp < 1024) {
            return saveAfterPoint(tmp + "", 1) + "KB";
        }

        tmp = tmp / 1024f;

        if (tmp < 1024) {
            return saveAfterPoint(tmp + "", 1) + "M";
        }

        tmp = tmp / 1024f;

        if (tmp < 1024) {
            return saveAfterPoint(tmp + "", 1) + "G";
        }

        return saveAfterPoint(tmp + "", 1) + "G";
    }

    /**
     * 保留小数点后面几位小叔 12.5697, 3 = 12.569
     *
     * @param content
     * @param n
     * @return
     */
    public static String saveAfterPoint(String content, int n) {
        int index = content.lastIndexOf(".");
        if (index == -1) {
            return content;
        } else {
            if (n == 0) {
                return content.substring(0, index);
            }
            index = index + 1 + n;
            if (index > content.length()) {
                return content;
            }
            return content.substring(0, index);
        }
    }

    /**
     * 格式化评论的数量
     * 1:如果超过一亿,就显示xx亿
     * 2:如果超过一万,就显示xx万
     *
     * @param commentNumber 需要格式化的评论数量
     * @return
     */
    public static String commentNumberFormat(int commentNumber) {
        if (commentNumber > 99999999) {
            return commentNumber / 100000000 + "亿";
        } else if (commentNumber > 9999) {
            return commentNumber / 10000 + "万";
        } else {
            return commentNumber + "";
        }
    }

}
