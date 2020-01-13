package comv.example.zyrmj.precious_time01.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
}
