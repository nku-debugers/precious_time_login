package comv.example.zyrmj.precious_time01.activities;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.circleprogress.DonutProgress;
import comv.example.zyrmj.precious_time01.constant.Constant;
import comv.example.zyrmj.precious_time01.ripplelibrary.RippleBackground;
import comv.example.zyrmj.precious_time01.service.MonitorService;
import comv.example.zyrmj.precious_time01.Utils.L;
import comv.example.zyrmj.precious_time01.Utils.SPUtils;
import comv.example.zyrmj.precious_time01.Utils.StringNotsUtils;
import comv.example.zyrmj.precious_time01.Utils.StringRandomUtils;
import comv.example.zyrmj.precious_time01.Utils.T;
import comv.example.zyrmj.precious_time01.Utils.TimeConvert;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.util.List;

public class TimeActivity extends BaseActivity {
	private static String TAG = TimeActivity.class.getName();
	private TextView tv_start, tv_notes;
	private RippleBackground rippleBackground;
	private DonutProgress donutProgress;
	private boolean isStart;
	private MyCount mc;
	private AlphaAnimation mHideAnimation = null;
	private AlphaAnimation mShowAnimation = null;
	private int animationTime;
	private String mDTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		initView();
		initData();
		setLinstener();
		fillData();
	}

	@Override
	protected void initData() {
		// int times = (Integer) SPUtils.get(TimeActivity.this, "use_times", 0);
		tv_notes.setText(StringNotsUtils.setText(0));
		setMonitorDurationTime();
		L.i(TAG, "this time random time is =" + Constant.TIME_DURATION);
		animationTime = 1000;
		mc = new MyCount(Constant.TIME_DURATION * 1000 + 1000, 1000); // ����1��
		donutProgress.setMax(Constant.TIME_DURATION);
		isStart = false;

	}

	@Override
	protected void initView() {
		tv_start = (TextView) this.findViewById(R.id.tv_start);
		tv_notes = (TextView) this.findViewById(R.id.tv_notes);
		rippleBackground = (RippleBackground) findViewById(R.id.content);
		donutProgress = (DonutProgress) this.findViewById(R.id.donut_progress);
	}

	@Override
	protected void setLinstener() {
		tv_start.setOnClickListener(this);
		donutProgress.setOnClickListener(this);

	}

	@Override
	protected void fillData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		System.out.println ( needPermissionForBlocking(getApplicationContext ()) );
        getAllAppNamesPackages ();
		if(needPermissionForBlocking(getApplicationContext ())) {
			Intent intent = new Intent ( Settings.ACTION_USAGE_ACCESS_SETTINGS );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity ( intent );
		}
		else{

            switch (v.getId()) {
                case R.id.tv_start:
                    if (!isStart) {
                        isStart = true;
						setRippleEffect();
                        donutProgress.setProgress(0);
                        L.d ( "start check!" );
                        stratService();//start check (insert white list before it)
                    } else {
                        L.i(TAG, "�Ѿ���ʼ");
                    }

                    break;

                case R.id.donut_progress:
                    T.showLong( TimeActivity.this, "�㽫��  " + mDTime + " ��ָ��ֻ�ʹ��Ȩ");
                    break;
                default:
                    break;
            }
        }
	}

	/**
	 * TODO<�����ʼ��ʱ���������Ч����3s��ֹͣ����Ч��������������ʱ>
	 * 
	 * @throw
	 * @return void
	 */
	private void setRippleEffect() {

		rippleBackground.startRippleAnimation();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				rippleBackground.stopRippleAnimation();
				tv_start.setVisibility(View.INVISIBLE);
				donutProgress.setVisibility(View.VISIBLE);
				donutProgress.setClickable(true);
				setHideAnimation(tv_start, animationTime);
				setShowAnimation(donutProgress, animationTime);
				Countdown();

			}
		}, 3000);

	}

	/**
	 * TODO<��������ʱ>
	 * 
	 * @throw
	 * @return void
	 */
	private void Countdown() {
		tv_notes.setText("please wait or restart phone");
		mc.start();// ��������ʱ
	}

	/**
	 * TODO<����һ�����ƺ�׼����һ�ε���������׼��>
	 * 
	 * @throw
	 * @return void
	 */
	public void prepareNewTask() {
		L.i(TAG, "this time is over");
		isStart = false;
		tv_start.setVisibility(View.VISIBLE);
		donutProgress.setProgress(0);
		donutProgress.setVisibility(View.INVISIBLE);
		donutProgress.setClickable(false);
		setHideAnimation(donutProgress, animationTime);
		setShowAnimation(tv_start, animationTime);
		stopService();
		int times = (Integer) SPUtils.get( TimeActivity.this, "use_times", 0);
		tv_notes.setText(StringNotsUtils.setText(times));
	}

	/**
	 * TODO<�������ƴ���>
	 * 
	 * @throw
	 * @return void
	 */
	public void saveUseTimes() {

		int times = (Integer) SPUtils.get( TimeActivity.this, "use_times", 0);
		SPUtils.put( TimeActivity.this, "use_times", times + 1);
	}

	/**
	 * TODO<���ÿ�ʼ�����뵹��ʱ�����л�ʱ�Ķ���Ч��>
	 * 
	 * @param view
	 * @param duration
	 * @throw
	 * @return void
	 */
	private void setHideAnimation(View view, int duration) {

		if (null == view || duration < 0) {

			return;

		}

		if (null != mHideAnimation) {

			mHideAnimation.cancel();

		}

		mHideAnimation = new AlphaAnimation(1.0f, 0.0f);

		mHideAnimation.setDuration(duration);

		mHideAnimation.setFillAfter(true);

		view.startAnimation(mHideAnimation);

	}

	private void setShowAnimation(View view, int duration) {

		if (null == view || duration < 0) {

			return;

		}

		if (null != mShowAnimation) {

			mShowAnimation.cancel();

		}

		mShowAnimation = new AlphaAnimation(0.0f, 1.0f);

		mShowAnimation.setDuration(duration);

		mShowAnimation.setFillAfter(true);

		view.startAnimation(mShowAnimation);

	}

	/**
	 * TODO<��ʼ��������>
	 * 
	 * @throw
	 * @return void
	 */
	private void stratService() {

		Intent intent = new Intent( TimeActivity.this, MonitorService.class);
		startService(intent);

	}

	/**
	 * TODO<ֹͣ����>
	 * 
	 * @throw
	 * @return void
	 */
	private void stopService() {
		Intent intent = new Intent( TimeActivity.this, MonitorService.class);
		stopService(intent);
	}

	/* ����һ������ʱ���ڲ��� */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		// ���һ�β����ٵ���
		@Override
		public void onTick(long millisUntilFinished) {
			L.i(TAG, "���ڵ���ʱ");
			if (donutProgress.getProgress() != Constant.TIME_DURATION) {
				donutProgress.setProgress(donutProgress.getProgress() + 1);
				mDTime = TimeConvert.secondsToMinute1(Constant.TIME_DURATION
						- donutProgress.getProgress());

			}

		}

		/**
		 * ���ط������������Ҫ����������ر�APP
		 */
		@Override
		public void onFinish() {
			saveUseTimes();
			prepareNewTask();

			L.i(TAG, "����ʱ����");
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// AppManager.getInstance().AppExit(getApplicationContext());
				}
			}, 3000);

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// mc.cancel(); ��������
		if (mShowAnimation != null) {
			mShowAnimation.cancel();
		}
		if (mHideAnimation != null) {
			mHideAnimation.cancel();
		}

	}

	// ���ѡ��30���ӣ�33��33�룬35��������������ʱ��
	public void setMonitorDurationTime() {
		Intent intent = getIntent ();
		String hour = intent.getStringExtra ( "hour" );
		String minute = intent.getStringExtra ( "minute" );
		System.out.println ( "hour : "+hour+"minute : "+minute );
		Constant.TIME_DURATION = Integer.parseInt( hour )*60*60+Integer.parseInt( minute )*60;
//		Constant.TIME_DURATION = Integer.parseInt( minute );
//		switch (StringRandomUtils.getRandomNumber(1, 3)) {
//		case 1:
//			Constant.TIME_DURATION = 20; // 5s
//			// Constant.TIME_DURATION = 60;
//
//			break;
//		case 2:
//			Constant.TIME_DURATION = 25; // 6s
//			// Constant.TIME_DURATION = 60;
//
//			break;
//		case 3:
//			Constant.TIME_DURATION = 15; // 7s
//			// Constant.TIME_DURATION = 60;
//
//			break;
//
//		default:
//			break;
//		}
	}

	public static boolean needPermissionForBlocking(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
			AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
			int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
			return  (mode != AppOpsManager.MODE_ALLOWED);
		} catch (PackageManager.NameNotFoundException e) {
			return true;
		}
	}

    private void getAllAppNamesPackages() {
        // TODO Auto-generated method stub
        PackageManager pm = getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : list) {
            // ��ȡ���豸���Ѿ���װ��Ӧ�õ�����,����AndriodMainfest�е�app_name��
            String appName = packageInfo.applicationInfo.loadLabel(
                    getPackageManager()).toString();
            // ��ȡ��Ӧ�����ڰ�������,����AndriodMainfest�е�package��ֵ��
            String packageName = packageInfo.packageName;
            Log.i("zyn", "app_name:" + appName);
            Log.i("zyn", "app_pkt_name:" + packageName);
//            i++;
        }
//        Log.i("zyn", "Ӧ�õ��ܸ���:" + i);

    }
}
