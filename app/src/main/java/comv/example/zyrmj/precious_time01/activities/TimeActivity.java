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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;


public class TimeActivity extends BaseActivity {
    private static String TAG = TimeActivity.class.getName();
    private TextView tv_start, tv_notes, tv_count;
    private Button button;
    private RippleBackground rippleBackground;
    private DonutProgress donutProgress;
    private boolean isStart;
    private MyCount mc;
    private AlphaAnimation mHideAnimation = null;
    private AlphaAnimation mShowAnimation = null;
    private int animationTime;
    private String mDTime;
    private int Clickcount = 5;
    private int useTime = 0;
    private String kind;
    private static final int count = 5;

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
        mc = new MyCount(Constant.TIME_DURATION * 1000 + 1000, 1000);
        donutProgress.setMax(Constant.TIME_DURATION);
        isStart = false;
        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        if (kind.equals("1")) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initView() {
        tv_start = (TextView) this.findViewById(R.id.tv_start);
        tv_notes = (TextView) this.findViewById(R.id.tv_notes);
        tv_count = (TextView) this.findViewById(R.id.tv_count);
        button = (Button) this.findViewById(R.id.button);
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
        //System.out.println ( needPermissionForBlocking(getApplicationContext ()) );
        if (needPermissionForBlocking(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            switch (v.getId()) {
                case R.id.tv_start:
                    if (!isStart) {
                        isStart = true;
                        setRippleEffect();
                        donutProgress.setProgress(useTime);
                        L.d("start check!");
                        stratService();//start check (insert white list before it)
                    }
                    break;

                case R.id.donut_progress:
                    System.out.println("kind = " + kind);
                    if (kind.equals("1")) {
                        T.showLong(TimeActivity.this, "你还有  " + mDTime + "结束，加油~");
                    } else if (kind.equals("2")) {
                        T.showLong(TimeActivity.this, "你还有  " + mDTime + "才能解锁");
                    } else if (kind.equals("3")) {
                        if (Clickcount == count + 1) {
                            stratService();
                            Clickcount = count;
                        } else if (Clickcount == 0) {
                            tv_count.setText("");
                            useTime = donutProgress.getProgress();
                            prepareNewTask();
                            System.out.println("useTime = " + useTime);
                            Clickcount = count + 2;
                            T.showLong(TimeActivity.this, "已暂停");
                            tv_start.setText("已暂停");
                        }
                        if (Clickcount < count + 1 && Clickcount > 0) {
                            tv_count.setText("还需点击" + Clickcount + "次才能解锁");
//							T.showLong( TimeActivity.this, );
                        }
                        Clickcount--;
                    }
                    break;

                case R.id.button:
                    //放弃闹钟回到主界面（仅自觉模式可用）
                    break;
                default:
                    break;
            }
        }
    }

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

    private void Countdown() {
        tv_notes.setText("please wait or restart phone");
        mc.start();
    }


    public void prepareNewTask() {
        System.out.println("this time is over");
        L.i(TAG, "this time is over");
        isStart = false;
        tv_start.setVisibility(View.VISIBLE);
        tv_start.setText("开始");
        donutProgress.setProgress(0);
        donutProgress.setVisibility(View.INVISIBLE);
        donutProgress.setClickable(false);
        setHideAnimation(donutProgress, animationTime);
        setShowAnimation(tv_start, animationTime);
        mc.cancel();
        stopService();
        int times = (Integer) SPUtils.get(TimeActivity.this, "use_times", 0);
        tv_notes.setText(StringNotsUtils.setText(times));
    }

    public void saveUseTimes() {

        int times = (Integer) SPUtils.get(TimeActivity.this, "use_times", 0);
        SPUtils.put(TimeActivity.this, "use_times", times + 1);
    }

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

    private void stratService() {
        Intent intent_t = getIntent();
        Intent intent = new Intent(TimeActivity.this, MonitorService.class);
        intent.putStringArrayListExtra("whitenames", intent_t.getStringArrayListExtra("whitenames"));
        startService(intent);

    }

    private void stopService() {
        Intent intent = new Intent(TimeActivity.this, MonitorService.class);
        stopService(intent);
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (donutProgress.getProgress() != Constant.TIME_DURATION) {
                donutProgress.setProgress(donutProgress.getProgress() + 1);
                mDTime = TimeConvert.secondsToMinute1(Constant.TIME_DURATION
                        - donutProgress.getProgress());

            }

        }

        @Override
        public void onFinish() {
            saveUseTimes();
//			prepareNewTask();
            Intent intent = new Intent();
            intent.putExtra("time", TimeConvert.secondsToMinute(Constant.TIME_DURATION));
            intent.setClass(TimeActivity.this, PlanFinishActivity.class);
            startActivity(intent);
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

        if (mShowAnimation != null) {
            mShowAnimation.cancel();
        }
        if (mHideAnimation != null) {
            mHideAnimation.cancel();
        }

    }

    public void setMonitorDurationTime() {
        Intent intent = getIntent();
        String hour = intent.getStringExtra("hour");
        String minute = intent.getStringExtra("minute");
        System.out.println(hour + " time " + minute);
        Constant.TIME_DURATION = Integer.parseInt(hour) * 60 * 60 + Integer.parseInt(minute) * 60;
    }

    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
