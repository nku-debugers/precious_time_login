package comv.example.zyrmj.precious_time01.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TYPE = "type"; //这个type是为了Notification更新信息的，这个不明白的朋友可以去搜搜，很多

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("mytag", "onReceive: the click recevier worked");
        String action = intent.getAction();
        int type = intent.getIntExtra(TYPE, -1);

        if (type != -1) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(type);
        }

        if (action.equals("notification_clicked")) {
            //处理点击事件
            String message = intent.getStringExtra("MESSAGE");
            Toast.makeText(context, "clicked " + message, Toast.LENGTH_LONG).show();
            onClick(this);
        }

        if (action.equals("notification_cancelled")) {
            //处理滑动清除和点击删除事件
            Toast.makeText(context, "cancelled", Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(NotificationBroadcastReceiver view) {
        Log.d("mytag", "onClick: inside onclick");
    }
}
