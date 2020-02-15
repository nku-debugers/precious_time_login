package comv.example.zyrmj.precious_time01.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.fragments.plan.AddTodoAfterPlanned;

import static android.content.Context.NOTIFICATION_SERVICE;


public class AlarmReceiver extends BroadcastReceiver {
    private static String TAG = "mytag";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: finally received");
        if (intent.getAction().equals("notice")) {
            String id = "my_channel_01";
            String name = "我是渠道名字";
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                Toast.makeText(context, mChannel.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, mChannel.toString());
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(context)
                        .setChannelId(id)
                        .setContentTitle("5 new messages")
                        .setContentText("hahaha")
                        .setSmallIcon(R.drawable.icon_follow).build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setContentTitle("5 new messages")
                        .setContentText("hahaha")
                        .setSmallIcon(R.drawable.icon_follow)
                        .setOngoing(true);
//                    .setChannel(id);//无效
                notification = notificationBuilder.build();
            }
            notificationManager.notify(1000, notification);


//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                new Intent(context, AddTodoAfterPlanned.class), 0);
//        // 通过Notification.Builder来创建通知，注意API Level
//        // API16之后才支持
//        Notification notify = new Notification.Builder(context)
//                .setSmallIcon(R.drawable.icon_follow)
//                .setTicker("TickerText:" + "您有新短消息，请注意查收！")
//                .setContentTitle("Notification Title")
//                .setContentText("This is the notification message")
//                .setContentIntent(pendingIntent).setNumber(1000).build(); // 需要注意build()是在API
//        // level16及之后增加的，API11可以使用getNotificatin()来替代
//        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
//        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
//        NotificationManager manager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(1000, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
//        Log.d(TAG, "onReceive: after notify");
//            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification notification = new Notification(R.drawable.icon_follow, "用电脑时间过长了！白痴！"
//                    , System.currentTimeMillis());
//            notification.
////            notification.setLatestEventInfo(context, "快去休息！！！",
////                    "一定保护眼睛,不然遗传给孩子，老婆跟别人跑啊。", null);
//            notification.defaults = Notification.DEFAULT_ALL;
//            manager.notify(1, notification);


            //再次开启LongRunningService这个服务，从而可以
            Intent i = new Intent(context, LongRunningService.class);
            context.startService(i);
        }

    }

}
