package comv.example.zyrmj.precious_time01.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import comv.example.zyrmj.precious_time01.RecentUseComparator;
import comv.example.zyrmj.precious_time01.WhiteApp;
import comv.example.zyrmj.precious_time01.activities.MonitorActivity;
import comv.example.zyrmj.precious_time01.Utils.L;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.ImageView;


public class MonitorService extends Service {

	boolean flag = true;
	private ActivityManager activityManager;
	private Timer timer;
	private List<String> whiteAppList;
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			if (activityManager == null) {
				activityManager = (ActivityManager) MonitorService.this
						.getSystemService(ACTIVITY_SERVICE);
			}
			String recentTaskName =getTopPackage ();
			if(!recentTaskName.equals ( "" ))	System.out.println ( "TopPackage : "+recentTaskName );

			boolean Iswhite = false;
			for(String name:whiteAppList){
//				System.out.println ( "white name: "+name );
				if(recentTaskName.equals ( name )||recentTaskName.equals ( "" )){
					Iswhite = true;
				}

			}
			System.out.println ( "white : "+Iswhite );
			if (!Iswhite) {
				//System.out.println ( recentTaskName );
				L.i("MonitorService", "Yes--recentTaskName=" + recentTaskName);
				Intent intentNewActivity = new Intent(MonitorService.this,
						MonitorActivity.class);
				intentNewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentNewActivity);

			}else{
				L.i("MonitorService", "No--recentTaskName="+recentTaskName);



			}
		}
	

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		whiteAppList = intent.getStringArrayListExtra ( "whitenames" );
		if (whiteAppList==null)
			whiteAppList=new ArrayList<>();
		whiteAppList.add ( "comv.example.zyrmj.precious_time01" );
		whiteAppList.add ( "" );
		if (flag == true) {
			timer = new Timer();
			timer.schedule(task, 0, 100);		
			flag = false;
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	//get top app package name
	public String getTopPackage(){
		long ts = System.currentTimeMillis();
		UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService( Context.USAGE_STATS_SERVICE);
		List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-1000, ts);
		if (usageStats == null || usageStats.size() == 0) {
			return "";
		}
		Collections.sort(usageStats, new RecentUseComparator ());//mRecentComp = new RecentUseComparator()
		return usageStats.get(0).getPackageName();
	}

}
