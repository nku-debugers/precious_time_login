package comv.example.zyrmj.precious_time01.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import comv.example.zyrmj.precious_time01.entity.Todo;

public class TimeDiff {

    public static String dateDiff(String startTime, String endTime,
                                String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        if (Integer.valueOf(startTime.split(":")[0])<10&&!startTime.split(":")[0].contains("0"))
            startTime="0"+startTime;
        if (Integer.valueOf(endTime.split(":")[0])<10&&!endTime.split(":")[0].contains("0"))
            endTime="0"+endTime;
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String hourStr=String.valueOf(hour);
        String minuteStr;
        if(min<10)
        minuteStr ="0"+String.valueOf(min);
        else
            minuteStr=String.valueOf(min);

       return hourStr+":"+minuteStr;
    }

    public static int compare(String time1,String time2)
    {
        String result=dateDiff(time1,time2,"HH:mm");
        if (result.contains("-"))
            return 1;
        else if(result.equals("0:00"))
            return 0;
        else return -1;

    }

    //将起始时间加上时间段，得到结束时间
    public static  String timeAdd(String startTime,String length)
    {
        if (Integer.valueOf(startTime.split(":")[0])<10&&!startTime.split(":")[0].contains("0"))
            startTime="0"+startTime;
        if (Integer.valueOf(length.split(":")[0])<10&&!length.split(":")[0].contains("0"))
            length="0"+length;
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        Date dt= null;
        try {
            dt = sdf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.HOUR,Integer.valueOf(length.split(":")[0]));//日期减1年
        rightNow.add(Calendar.MINUTE,Integer.valueOf(length.split(":")[1]));
        Date dt1=rightNow.getTime();
        String reStr = sdf.format(dt1);
        System.out.println("timeAdd");
        System.out.println(reStr);
        return reStr;
    }

    public static int daysBetween(String dateString1,String dateString2){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date1= null;
        Date date2=null;
        try {
            date1 = sdf.parse(dateString1);
            date2=sdf.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    public static boolean getCurrentWeekDay(String dateTime) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayOfWeek < 0) dayOfWeek = 6;
        return dateTime.substring(0, 1).equals(dayOfWeek + "");
    }

    public static boolean isOutDated(Todo todo) {
        Calendar cal = Calendar.getInstance();
        String splieTimes[] = todo.getPlanDate().split("-");
        Date start = new Date((Integer.valueOf(splieTimes[0]) - 1900),
                (Integer.valueOf(splieTimes[1]) - 1), (Integer.valueOf(splieTimes[2])));
        cal.setTime(start);
        //增加6天
        cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(todo.getStartTime().substring(0, 1)));
        //Calendar转为Date类型
        Date end = cal.getTime();
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        //将增加后的日期转为字符串
        String  todoToday = simpleDateFormat.format(end);
        String today = simpleDateFormat.format(now);
        if (todoToday.compareTo(today) < 0) {
            return true;
        }
        else if (todoToday.compareTo(today) > 0) {
            return false;
        }

        SimpleDateFormat sp = new SimpleDateFormat("HH:mm");
        return sp.format(now).compareTo(todo.getStartTime().substring(2)) > 0;

    }

    public static long getAlarmMillis(String dateTime, int reminder) {
        Log.d("mytag", "getAlarmMillis: The dateTime is :" + dateTime + "the reminder is " + reminder);

        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long MillisOfDateTime = Integer.parseInt(dateTime.substring(2, 4)) * nh + Integer.parseInt(dateTime.substring(5)) * nm;

        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        String nowTime = simpleDateFormat.format(d);
        long NowMillis = Integer.parseInt(nowTime.substring(0, 2)) * nh + Integer.parseInt(nowTime.substring(3, 5)) * nm + Integer.parseInt(nowTime.substring(6)) * ns;
        long Millis = MillisOfDateTime - NowMillis - reminder * nm;

        Log.d("mytag", "nowTimeMillis are " + NowMillis + " now is " + nowTime);
        Log.d("mytag", "the DateTimeMillis are " + MillisOfDateTime + dateTime);
        Log.d("mytag", "the Millis are" + Millis);

        return Millis;


    }

    public static void main(String[]args) {
        String x = "2017-09-07";
        String y = "2017-09-08";
        if (x.compareTo(y) < 0) {

        }

    }
}
