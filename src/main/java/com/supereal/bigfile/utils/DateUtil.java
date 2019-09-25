package com.supereal.bigfile.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间处理工具类<BR/>
 * 请不要在此类中进行代码修改,谢谢。
 *
 * @author handong
 */
final public class DateUtil {

    private DateUtil() {
    }

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将date格式化为String类型
     *
     * @param date 要格式化的时间
     * @return 格式化后的时间字符串
     */
    public static String formatDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
        return simpleDateFormat.format(date);
    }

    /**
     * 根据输入的格式将date格式化为String类型
     *
     * @param date    要格式化的时间
     * @param formStr 格式化格式  例如"yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的时间字符串
     */
    public static String formatDateToString(Date date, String formStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formStr);
        return simpleDateFormat.format(date);
    }

    /**
     * 根据输入的格式将时间字符串转化为date类型
     *
     * @param dateStr 要格式化的日期型字符串
     * @param formStr 格式化格式  例如"yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的date对象
     * @throws ParseException 格式化失败
     */
    public static Date formatStringToDate(String dateStr, String formStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formStr);
        return simpleDateFormat.parse(dateStr);
    }

    /**
     * 将date数据根据给定的格式进行格式化
     *
     * @param date    要格式化的date对象
     * @param formStr 格式化格式 例如 "yyyy-MM-dd HH:mm:ss"
     * @return
     * @throws ParseException
     */
    public static Date formatDateToDate(Date date, String formStr) throws ParseException {
        return formatStringToDate(formatDateToString(date, formStr), formStr);
    }

    /**
     * 获取两个date之间的月数差
     *
     * @param firstDate 减数日期,一般为较小日期
     * @param lastDate  被减数日期,一般为较大日期
     * @return 被减数日期与减数日期之间的月数差
     */
    public static int getBetweenMonth(Date firstDate, Date lastDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        int firstMonth = calendar.get(Calendar.MONTH);
        calendar.setTime(lastDate);
        int lastMonth = calendar.get(Calendar.MONTH);
        return lastMonth - firstMonth;
    }

    /**
     * 获取两个date之间的天数差
     *
     * @param firstDate 减数日期,一般为较小日期
     * @param lastDate  被减数日期,一般为较大日期
     * @return 被减数日期与减数日期之间天数差
     */
    public static int getBetweenDay(Date firstDate, Date lastDate) {
        long firstTime = firstDate.getTime();
        long endTime = lastDate.getTime();
        long dayNum = (endTime - firstTime) / (60 * 60 * 24 * 1000);
        return Integer.parseInt(String.valueOf(dayNum));
    }

    /**
     * 获取两个date之间的小时差
     *
     * @param firstDate 减数日期,一般为较小日期
     * @param lastDate  被减数日期,一般为较大日期
     * @return 被减数日期与减数日期之间小时差
     */
    public static int getBetweenHour(Date firstDate, Date lastDate) {
        long firstTime = firstDate.getTime();
        long endTime = lastDate.getTime();
        long dayNum = (endTime - firstTime) / (60 * 60 * 1000);
        return Integer.parseInt(String.valueOf(dayNum));
    }

    /**
     * 获取两个date之间的分钟差
     *
     * @param firstDate 减数日期,一般为较小日期
     * @param lastDate  被减数日期,一般为较大日期
     * @return 被减数日期与减数日期之间分钟差
     */
    public static int getBetweenMinute(Date firstDate, Date lastDate) {
        long firstTime = firstDate.getTime();
        long endTime = lastDate.getTime();
        long dayNum = (endTime - firstTime) / (60 * 1000);
        return Integer.parseInt(String.valueOf(dayNum));
    }

    /**
     * 获取两个date之间的秒数差
     *
     * @param firstDate 减数日期,一般为较小日期
     * @param lastDate  被减数日期,一般为较大日期
     * @return 被减数日期与减数日期之间秒数差
     */
    public static int getBetweenSecond(Date firstDate, Date lastDate) {
        long firstTime = firstDate.getTime();
        long endTime = lastDate.getTime();
        long dayNum = (endTime - firstTime) / (1000);
        return Integer.parseInt(String.valueOf(dayNum));
    }

    /**
     * 获取两个date之间相差的日期集合
     *
     * @param firstDate 较小日期
     * @param lastDate  较大日期
     * @return 两个日期差的日期集合, 每天为一个元素, 不包含lastDate当天, 例如2015-08-15~2015-08-18,那么将得到15日、16日、17日三个日期
     * @throws ParseException
     */
    public static List<Date> getBetweenDaysList(Date firstDate, Date lastDate) throws ParseException {
        List<Date> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        int firstDayNum = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(lastDate);
        int lastDayNum = calendar.get(Calendar.DAY_OF_YEAR);
        int difference = lastDayNum - firstDayNum;
        if (difference > 0) {
            list.add(firstDate);
        }
        for (int i = 0; i < difference - 1; i++) {
            calendar.setTime(firstDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1 + i);
            list.add(calendar.getTime());
        }
        return list;
    }

    /**
     * 获取两个date之间相差的日期集合
     *
     * @param firstDate 较小日期
     * @param lastDate  较大日期
     * @return 两个日期差的日期集合, 每天为一个元素, 不包含lastDate当天, 例如2015-08-15~2015-08-18,那么将得到15日、16日、17日三个日期
     * @throws ParseException
     */
    public static List<String> getBetweenDaysList(Date firstDate, Date lastDate, String formStr) {
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        int firstDayNum = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(lastDate);
        int lastDayNum = calendar.get(Calendar.DAY_OF_YEAR);
        int difference = lastDayNum - firstDayNum;
        if (difference > 0) {
            list.add(DateUtil.formatDateToString(firstDate, formStr));
        }
        for (int i = 0; i < difference - 1; i++) {
            calendar.setTime(firstDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1 + i);
            list.add(DateUtil.formatDateToString(calendar.getTime(), formStr));
        }
        return list;
    }


    /**
     * 获取指定时间、相对偏移量月份的第一天
     *
     * @param date   指定时间
     * @param offset 相对指定时间月份的偏移量,0表示指定时间的月,-1表示指定时间月上个月,1表示指定时间月下个月,依此类推
     * @return 相对月份的第一天的"当前时间"
     */
    public static Date getMonthFirstDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, offset);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定时间、相对偏移量月份的最后一天
     *
     * @param date   指定时间
     * @param offset 相对指定时间月份的偏移量,0表示指定时间的月,-1表示指定时间月上个月,1表示指定时间月下个月,依此类推
     * @return 相对月份的最后一天的"当前时间"
     */
    public static Date getMonthLastDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, offset + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取指定时间、相对偏移量周的第一天(此方法默认周一为第一天)
     *
     * @param date   指定时间
     * @param offset 相对指定时间周的偏移量,0表示指定时间的周,-1表示指定时间的上一周,1表示指定时间的下一周,依此类推
     * @return 相对星期的第一天的"当前时间"
     */
    public static Date getWeekFirstDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_MONTH, offset);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        return calendar.getTime();
    }

    /**
     * 获取指定时间、相对偏移量周的最后一天(此方法默认周日为最后一天)
     *
     * @param date   指定时间
     * @param offset 相对指定时间周的偏移量,0表示指定时间的周,-1表示指定时间的上一周,1表示指定时间的下一周,依此类推
     * @return 相对星期的最后一天的"当前时间"
     */
    public static Date getWeekLastDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_MONTH, offset + 1);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    /**
     * 对date对象进行加减
     *
     * @param date   date基数
     * @param type   加减类型,例如日期则输入：Calendar.DAY_OF_MONTH,依此类推
     * @param offset 数量,-1代表减一天、一月等,1代表加一天、一月等,依此类推
     * @return 加减后的date对象
     */
    public static Date addDate(Date date, int type, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, offset);
        return calendar.getTime();
    }
}
