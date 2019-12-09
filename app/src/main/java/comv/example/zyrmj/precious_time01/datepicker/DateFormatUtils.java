package comv.example.zyrmj.precious_time01.datepicker;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 说明：日期格式化工具
 * 作者：liuwan1992
 * 添加时间：2018/12/17
 * 修改人：liuwan1992
 * 修改时间：2018/12/18
 */
public class DateFormatUtils {
    private static final String DATE_FORMAT_PATTERN_HM = "HH:mm";
    private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
    private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";

    /**
     * 时间戳转字符串
     *
     * @param timestamp     时间戳
     * @param isPreciseTime 是否包含时分
     * @return 格式化的日期字符串
     */
    public static String long2Str(long timestamp, int isPreciseTime) {
        return long2Str(timestamp, getFormatPattern(isPreciseTime));
    }

    private static String long2Str(long timestamp, String pattern) {
        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
        if (pattern.equals("week")) {
            Date date = new Date(timestamp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (week_index < 0) {
                week_index = 0;
            }
            return weeks[week_index];
        }
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr       日期字符串
     * @param isPreciseTime 是否包含时分
     * @return 时间戳
     */
    public static long str2Long(String dateStr, int isPreciseTime) {
        return str2Long(dateStr, getFormatPattern(isPreciseTime));
    }

    private static long str2Long(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        if(dateStr.length() == 5){
            return 1575820317;

        }
        return 0;
    }

    private static String getFormatPattern(int format) {
        if (format == 1) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else if (format == 2) {
            return DATE_FORMAT_PATTERN_YMD;
        } else if (format == 3) {
            return DATE_FORMAT_PATTERN_HM;
        } else {
            return "week";
        }
    }

}
