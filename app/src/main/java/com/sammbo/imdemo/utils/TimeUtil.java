package com.sammbo.imdemo.utils;


import android.content.Context;
import android.text.format.DateUtils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 */

public class TimeUtil {

    /**
     * 消息列表最大时间间隔 5*60*1000
     */
    public static final long MAX_TIME_GAP = 300000;
    private static String FORMAT_TODAY = "HH:mm";
    private static String FORMAT_DAY = "yyyy/MM/dd";
    private static String FORMAT_DAY_SECOND = "yyyy.MM.dd";
    private static String FORMAT_ALL = "yyyy.MM.dd HH:mm:ss";
    private static String FORMAT_DAY_MINIT = "yyyy/MM/dd HH:mm";
    public static String FORMAT_DAY2 = "yyyy年MM月dd日";

    public static String[] DAYS_WEEK = new String[]{
            "星期日",
            "星期一",
            "星期二",
            "星期三",
            "星期四",
            "星期五",
            "星期六"
    };

    /**
     * 将整数秒转换成时分秒
     *
     * @param time 整数秒
     * @return 时分秒
     */
    public static String secToTime(int time) {
        String timeStr;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }


    /**
     * 将整数秒转换成时分秒
     *
     * @param time 整数秒
     * @return 时分秒
     */
    public static String secToTime(long time) {
        return secToTime((int) time);
    }

    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }

        return retStr;
    }

    public static String convertTime(long sec) {
        if (sec < 0) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        sec = sec / 1000;
        if (sec >= 86400) {
            int day = (int) (sec / 86400);
            int hour = (int) ((sec % 86400) / 3600);
            int min = (int) ((sec % 86400 % 3600) / 60);
            int second = (int) (sec % 86400 % 3600 % 60);
            buf.append(day).append("天").append(hour).append("小时").append(min).append("分").append(second).append("秒");
        } else if (sec > 3600) {
            int hour = (int) (sec / 3600);
            int min = (int) ((sec % 3600) / 60);
            int second = (int) (sec % 3600 % 60);
            buf.append(hour).append("小时").append(min).append("分").append(second).append("秒");
        } else if (sec > 60) {
            int min = (int) (sec / 60);
            int second = (int) (sec % 60);
            buf.append(min).append("分").append(second).append("秒");
        } else {
            buf.append(sec).append("秒");
        }

        return buf.toString();
    }

    public static String formatConversationTime(Context context, long millis) {
        if (DateUtils.isToday(millis)) {
            return displayToday(millis);
        } else if (isYesterday(millis)) {
            return "昨天";
        } else if (within7Days(millis)) {
            return displayWeek(context, millis);
        } else {
            return displayDay(millis);
        }
    }


    public static String formatChattingTime(Context context, long millis) {
        if (DateUtils.isToday(millis)) {
            return displayToday(millis);
        } else if (isYesterday(millis)) {
            return "昨天" + displayToday(millis);
        } else if (within7Days(millis)) {
            return displayWeek(context, millis) + " " + displayToday(millis);
        } else {
            return displayFullTime(millis) + " " + displayToday(millis);
        }
    }

    private static boolean isYesterday(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        if (now.get(Calendar.YEAR) == (calendar.get(Calendar.YEAR))) {
            int diffDay = now.get(Calendar.DAY_OF_YEAR)
                    - calendar.get(Calendar.DAY_OF_YEAR);
            return diffDay == 1;
        } else {
            return false;
        }
    }

    public static boolean within7Days(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        if (now.get(Calendar.YEAR) == (calendar.get(Calendar.YEAR))) {
            int diffDay = now.get(Calendar.DAY_OF_YEAR)
                    - calendar.get(Calendar.DAY_OF_YEAR);
            return diffDay < 7;
        } else {
            return false;
        }
    }

    private static String displayToday(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_TODAY);
        return dateFormat.format(new Date(millis));
    }

    private static String displayWeek(Context context, long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return DAYS_WEEK[dayInWeek - 1];
    }

    private static String displayDay(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DAY);
        return dateFormat.format(new Date(millis));
    }

    private static String displayFullTime(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DAY2);
        return dateFormat.format(new Date(millis));
    }

    public static String displayDaySecond(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DAY_SECOND);
        return dateFormat.format(new Date(millis));
    }

    public static String displayAll(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_ALL);
        return dateFormat.format(new Date(millis));
    }

    public static String formatMeetingTime(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DAY_MINIT);
        return dateFormat.format(new Date(millis));
    }

    public static String formatTimeNormal(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date(millis));
    }

    public static String format(long millis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(millis));
    }

    public static String getTime(long duration) {
        SimpleDateFormat formatter;
        if (duration > 60 * 60 * 1000) {
            formatter = new SimpleDateFormat("HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat("mm:ss");
        }
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        // 此方法计算得出的值会多1秒
        //return formatter.format(duration > 1000 ? duration - 1000 : duration);
        return formatter.format(duration);
    }


}
