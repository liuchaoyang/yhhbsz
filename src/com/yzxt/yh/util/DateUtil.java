package com.yzxt.yh.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{

    // Json日期格式
    private static SimpleDateFormat jsonDf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final int invalidAge = -1;//非法的年龄，用于处理异常。
    // 日期格式
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static SimpleDateFormat htmlTimeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat htmlDateDf = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat synTimeDf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");

    private static SimpleDateFormat TimeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 客户端日期格式
    private static SimpleDateFormat tranSdf = new SimpleDateFormat("yyyy/MM/dd");

    public static Timestamp getTimeByStr(String str)
    {
        if (StringUtil.isNotEmpty(str))
        {
            try
            {
                return new Timestamp(TimeDf.parse(str).getTime());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 将日期转化为JSON字符串
    public static String toJsonStr(Date date)
    {
        if (date != null)
        {
            return jsonDf.format(date);
        }
        else
        {
            return "";
        }
    }

    // 将日期转化为JSON字符串
    public static Date fromJsonStr(String str)
    {
        if (StringUtil.isNotEmpty(str))
        {
            try
            {
                return jsonDf.parse(str);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    // 按天截取日期
    public static Date truncDay(Date date)
    {
        if (date == null)
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        try
        {
            return sdf.parse(dateStr);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    // 将页面日期字符串转为日期
    public static Date toDate(String dateStr)
    {
        dateStr = dateStr != null ? dateStr.trim() : null;
        if (dateStr == null || dateStr.length() == 0)
        {
            return null;
        }
        try
        {
            return df.parse(dateStr);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    // 增加天数
    public static Date addDay(Date date, int days)
    {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    //增加月数
    public static Date addMonth(Date date,int months){
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }
    // 格式化字符串
    public static String toHtmlTime(Date date)
    {
        if (date == null)
        {
            return "";
        }
        return htmlTimeDf.format(date);
    }

    // 格式化字符串
    public static Date getTimeFromHtml(String timeStr) throws ParseException
    {
        if (timeStr == null || timeStr.length() == 0)
        {
            return null;
        }
        if (timeStr != null && timeStr.length() > 0)
            try
            {
                return htmlTimeDf.parse(timeStr);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        return null;
    }

    // 格式化字符串
    public static String toHtmlDate(Date date)
    {
        if (date == null)
        {
            return "";
        }
        return htmlDateDf.format(date);
    }

    // 格式化字符串
    public static Date getDateFromHtml(String dateStr) throws ParseException
    {
        if (dateStr == null || dateStr.length() == 0)
        {
            return null;
        }
        dateStr = dateStr.replace('/', '-');
        if (dateStr != null && dateStr.length() > 0)
            try
            {
                return htmlDateDf.parse(dateStr);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * 获取同步时间，不处理空值
     * @param str
     * @return
     */
    public static Timestamp getSynTimeByStr(String str)
    {
        if (StringUtil.isNotEmpty(str))
        {
            try
            {
                return new Timestamp(synTimeDf.parse(str).getTime());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取同步时间字体串，不处理空值
     * @param date
     * @return
     */
    public static String toSynTimeStr(Timestamp time)
    {
        if (time != null)
        {
            return synTimeDf.format(time);
        }
        return null;
    }

    /**
     * 根据生日计算年龄
     * @param birthday
     * @param now
     * @return
     */
    public static int getAge(Date birthday, Date now)
    {
        if (birthday == null)
        {
            return 0;
        }
        Calendar b = Calendar.getInstance();
        b.setTime(birthday);
        Calendar n = Calendar.getInstance();
        if (now != null)
        {
            n.setTime(now);
        }
        int age = n.get(Calendar.YEAR) - b.get(Calendar.YEAR);
        if (n.get(Calendar.MONTH) < b.get(Calendar.MONTH) || n.get(Calendar.MONTH) == b.get(Calendar.MONTH)
                && n.get(Calendar.DAY_OF_MONTH) < b.get(Calendar.DAY_OF_MONTH))
        {
            age--;
        }
        return age;
    }

    /**
     * 获取今天刚好等于指定岁数的最晚出生日期
     * @param now
     * @param age
     * @return
     */
    public static Date getLastestBirthday(int age, Date now)
    {
        if (now == null || age < 0)
        {
            return null;
        }
        Calendar n = Calendar.getInstance();
        if (now != null)
        {
            n.setTime(now);
        }
        Calendar b = Calendar.getInstance();
        b.setTime(n.getTime());
        b.add(Calendar.YEAR, -age);
        return truncDay(b.getTime());
    }

    /**
     * 客户端日期
     * @param date
     * @return
     */
    public static String getTranDate(Date date)
    {
        if (date != null)
        {
            return tranSdf.format(date);
        }
        else
        {
            return "";
        }
    }

    /**
     * 客户端日期
     * @param date
     * @return
     */
    public static Date getFromTranDate(String date)
    {
        try
        {
            if (StringUtil.isNotEmpty(date))
            {
                return tranSdf.parse(date);
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }

    /**
     * 客户端时间
     * @param date
     * @return
     */
    public static String getTranTime(Date date)
    {
        if (date != null)
        {
            return df.format(date);
        }
        else
        {
            return "";
        }
    }

    /**
     * 客户端时间
     * @param date
     * @return
     */
    public static Date getFromTranTime(String date)
    {
        try
        {
            if (StringUtil.isNotEmpty(date))
            {
                return df.parse(date);
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }

    /**
     * 客户端时间戳
     * @param date
     * @return
     */
    public static String getTranTimestamp(Date date)
    {
        if (date != null)
        {
            return synTimeDf.format(date);
        }
        else
        {
            return "";
        }
    }

    /**
     * 客户端时间戳
     * @param date
     * @return
     */
    public static Date getFromTranTimestamp(String date)
    {
        try
        {
            if (StringUtil.isNotEmpty(date))
            {
                return synTimeDf.parse(date);
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }
    /**
     * 
     * 根据身份证号码计算年龄
     */
    public static int getAgeByIDNumber(String idNumber) {
        String dateStr;
        if (idNumber.length() == 15) {
            dateStr = "19" + idNumber.substring(6, 12);
        } else if (idNumber.length() == 18) {
            dateStr = idNumber.substring(6, 14);
        } else {//默认是合法身份证号，但不排除有意外发生
            return invalidAge;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date birthday = simpleDateFormat.parse(dateStr);
            return getAgeByDate(birthday);
        } catch (ParseException e) {
            return invalidAge;
        }

    }
    /**
     *根据生日计算年龄
     * @param dateStr 这样格式的生日 1990-01-01
     */

    public static int getAgeByDateString(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = simpleDateFormat.parse(dateStr);
            return getAgeByDate(birthday);
        } catch (ParseException e) {
            return -1;
        }
    }


    public static int getAgeByDate(Date birthday) {
        Calendar calendar = Calendar.getInstance();

        //calendar.before()有的点bug
        if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
            return invalidAge;
        }

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthday);


        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirthday;


        if (monthNow <= monthBirthday && monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday || monthNow < monthBirthday) {
            age--;
        }
        return age;
    }
    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringtoDate(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }
    
    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringtoDate(String dateStr, String format,
            ParsePosition pos) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr, pos);
        } catch (Exception e) {
            d = null;
        }
        return d;
    }
    /**
     * 把日期转换为字符串
     */
    public static String dateToString(Date date, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }
    public static String getDatePoor(Date endDate, Date nowDate) {

    	long nd = 1000 * 24 * 60 * 60;
    	long nh = 1000 * 60 * 60;

    	long nm = 1000 * 60;

    	// long ns = 1000;

    	// 获得两个时间的毫秒时间差异

    	long diff = endDate.getTime() - nowDate.getTime();

    	// 计算差多少天

    	long day = diff / nd;

    	// 计算差多少小时

    	long hour = diff % nd / nh;

    	// 计算差多少分钟

    	long min = diff % nd % nh / nm;

    	// 计算差多少秒//输出结果

    	// long sec = diff % nd % nh % nm / ns;

    	return day + "天" + hour + "小时" + min + "分钟";

    	}

}
