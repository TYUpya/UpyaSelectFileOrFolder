package vip.upya.lib.sfof;

import android.os.Handler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** 工具类 */
@SuppressWarnings("all")
public class Utils {

    public static final Handler mHandler = new Handler();

    /** 时间戳 TO 字符串日期 */
    public static String getTimestampToDate(String format, long timestamp) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(timestamp);
    }

    /**
     * 格式化文件大小
     *
     * @param length 文件大小(以Byte为单位)
     * @return String 格式化的常见文件大小(保留两位小数)
     */
    public static String formatFileSize(long length) {
        String result;
        int subString;

        if (length >= 1073741824) { // 如果文件长度大于1GB
            subString = String.valueOf((float) length / 1073741824).indexOf(".");
            result = ((float) length / 1073741824 + "000").substring(0, subString + 3) + "GB";
        } else if (length >= 1048576) { // 如果文件长度大于1MB且小于1GB
            subString = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0, subString + 3) + "MB";
        } else if (length >= 1024) { // 如果文件长度大于1KB且小于1MB
            subString = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0, subString + 3) + "KB";
        } else
            result = length + "B";
        return result;
    }

    /** 排序文件列表 */
    public static void sortFileList(List<File> fileList) {
        Collections.sort(fileList, (o1, o2) -> {
            if (o1.isDirectory() && o2.isFile())
                return -1;
            if (o1.isFile() && o2.isDirectory())
                return 1;
            return o1.getName().compareTo(o2.getName());
        });
    }

}
