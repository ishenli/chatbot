package com.workdance.core.util;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

/**
 * @创建者 CSDN_LQR
 * @描述 时间工具（需要joda-time）
 */
public class TimeUtils {

    public static String getMsgFormatTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        long timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        return getMsgFormatTime(timestamp);
    }
    /**
     * 得到仿微信日期格式输出
     *
     * @param msgTimeMillis
     * @return
     */
    public static String getMsgFormatTime(long msgTimeMillis) {
        if (msgTimeMillis == 0) {
            return "";
        }

        DateTime msgTime = new DateTime(msgTimeMillis);
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.get(Calendar.DAY_OF_YEAR);
        Calendar msgCalendar = Calendar.getInstance();
        msgCalendar.setTimeInMillis(msgTimeMillis);

        if (nowCalendar.get(Calendar.YEAR) == msgCalendar.get(Calendar.YEAR)) {
            if (nowCalendar.get(Calendar.DAY_OF_YEAR) == msgCalendar.get(Calendar.DAY_OF_YEAR)) {
                //早上、下午、晚上 1:40
                return getTime(msgTime);
            } else if (msgCalendar.get(Calendar.DAY_OF_YEAR) + 1 == nowCalendar.get(Calendar.DAY_OF_YEAR)) {
                //昨天
                return "昨天 " + getTime(msgTime);
            } else if (nowCalendar.get(Calendar.WEEK_OF_YEAR) == msgCalendar.get(Calendar.WEEK_OF_YEAR)) {
                //星期
                switch (msgTime.getDayOfWeek()) {
                    case DateTimeConstants.SUNDAY:
                        return "周日 " + getTime(msgTime);
                    case DateTimeConstants.MONDAY:
                        return "周一 " + getTime(msgTime);
                    case DateTimeConstants.TUESDAY:
                        return "周二 " + getTime(msgTime);
                    case DateTimeConstants.WEDNESDAY:
                        return "周三 " + getTime(msgTime);
                    case DateTimeConstants.THURSDAY:
                        return "周四 " + getTime(msgTime);
                    case DateTimeConstants.FRIDAY:
                        return "周五 " + getTime(msgTime);
                    case DateTimeConstants.SATURDAY:
                        return "周六 " + getTime(msgTime);
                    default:
                        break;
                }
                return "";
            } else {
                //12月22日
                return msgTime.toString("MM月dd日 HH:mm");
            }
        } else {
            return msgTime.toString("yyyy年MM月dd日 HH:mm");
        }
    }

    @NonNull
    private static String getTime(DateTime msgTime) {
        return msgTime.toString("HH:mm");
    }
    private static Formatter sFormatter;
    private static StringBuilder sFormatBuilder;

    /**
     * 格式化时间 timeMS -> HH:MM:SS
     *
     * @param timeMs 根据短视频的应用场景，使用int
     */
    public static String time2String(long timeMs) {
        if (timeMs < 0) {
            return ""; //or throw an exception
        }

        long totalSeconds = timeMs / 1000;

        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        if (sFormatter == null) {
            sFormatBuilder = new StringBuilder();
            sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
        }

        sFormatBuilder.setLength(0);
        if (hours > 0) {
            return sFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return sFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
