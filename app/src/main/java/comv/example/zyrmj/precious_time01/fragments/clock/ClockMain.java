package comv.example.zyrmj.precious_time01.fragments.clock;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.Utils.L;
import comv.example.zyrmj.precious_time01.Utils.SPUtils;
import comv.example.zyrmj.precious_time01.Utils.StringNotsUtils;
import comv.example.zyrmj.precious_time01.Utils.T;
import comv.example.zyrmj.precious_time01.Utils.TimeConvert;
import comv.example.zyrmj.precious_time01.circleprogress.DonutProgress;
import comv.example.zyrmj.precious_time01.constant.Constant;
import comv.example.zyrmj.precious_time01.ripplelibrary.RippleBackground;
import comv.example.zyrmj.precious_time01.service.MonitorService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClockMain extends Fragment {

    private TextView tv_start, tv_notes, tv_count;
    private Button give_up;
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
    private static final int count = 2000;
    private static boolean suspend = false;

    public ClockMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        return inflater.inflate ( R.layout.fragment_clock_main , container , false );
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );

        init();
        enableButtons();

    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        if (mShowAnimation != null) {
            mShowAnimation.cancel();
        }
        if (mHideAnimation != null) {
            mHideAnimation.cancel();
        }
    }

    public void init(){
        tv_start = getView ().findViewById(R.id.tv_start);
        tv_notes = getView ().findViewById(R.id.tv_notes);
        tv_count = getView ().findViewById(R.id.tv_count);
        give_up = getView ().findViewById(R.id.give_up );
        rippleBackground = getView ().findViewById(R.id.content);
        donutProgress = getView ().findViewById(R.id.donut_progress);

        tv_notes.setText( StringNotsUtils.setText(0));
        setMonitorDurationTime();
        animationTime = 1000;
        mc = new MyCount (Constant.TIME_DURATION * 1000 + 1000, 1000);
        donutProgress.setMax(Constant.TIME_DURATION);
        isStart = false;
        kind = getArguments ().getString ( "kind" );
        if (kind.equals("1")) {
            give_up.setVisibility(View.VISIBLE);
        } else {
            give_up.setVisibility(View.INVISIBLE);
        }
    }

    public void enableButtons(){
        tv_start.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (!isStart) {
                    isStart = true;
                    setRippleEffect();
                    donutProgress.setProgress(useTime);
                    L.d("start check!");
                    stratService();//start check (insert white list before it)
                }
            }
        } );

        donutProgress.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (kind.equals("1")) {
//                    T.showLong(getActivity (), "你还有  " + mDTime + "结束，加油~");
                    if(suspend){
                        suspend = false;
                        stratService();
                    }
                    else{
                        suspend = true;
                        tv_count.setText("");
                        useTime = donutProgress.getProgress();
                        prepareNewTask();
                        tv_start.setText("已暂停");
                    }


                } else if (kind.equals("2")) {
                    T.showLong(getActivity (), "你还有  " + mDTime + "才能解锁");
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
                        T.showLong(getActivity (), "已暂停");
                        tv_start.setText("已暂停");
                    }
                    if (Clickcount < count + 1 && Clickcount > 0) {
                        tv_count.setText("还需点击" + Clickcount + "次才能解锁");
                    }
                    Clickcount--;
                }
            }
        } );

        give_up.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //添加放弃按钮
                Bundle bundle = new Bundle (  );
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_clockMain_to_choseClock, bundle);
            }
        } );

    }

    public void setMonitorDurationTime() {
        String hour = getArguments ().getString ( "hour" );
        String minute = getArguments ().getString ( "minute" );
        System.out.println(hour + " time " + minute);
        Constant.TIME_DURATION = Integer.parseInt(hour) * 60 * 60 + Integer.parseInt(minute) * 60;
    }

//    public void saveUseTimes() {
//
//        int times = (Integer) SPUtils.get(getActivity (), "use_times", 0);
//        SPUtils.put(getActivity (), "use_times", times + 1);
//    }

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
        int times = (Integer) SPUtils.get(getActivity (), "use_times", 0);
        tv_notes.setText(StringNotsUtils.setText(times));
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
        Intent intent = new Intent(getContext (), MonitorService.class);
        intent.putStringArrayListExtra("whitenames", (ArrayList<String>) getArguments().getSerializable("whitenames"));
        getActivity ().startService(intent);

    }

    private void stopService() {
        Intent intent = new Intent(getContext (), MonitorService.class);
        getActivity ().stopService(intent);
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
            stopService ();
            System.out.println ( "finish!" );
//            saveUseTimes();
            Bundle bundle = new Bundle (  );
            bundle.putString ( "time", TimeConvert.secondsToMinute(Constant.TIME_DURATION) );
            if(getArguments ().getInt ( "single" )==1){
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_clockMain_to_choseClock, bundle);
            }
//            System.out.println ( "total time = "+TimeConvert.secondsToMinute(Constant.TIME_DURATION) );
            else{
                bundle.putString ( "todoName",getArguments ().getString("todoName") );
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_clockMain_to_clockFinish, bundle);
            }
            new Handler ().postDelayed( new Runnable() {
                @Override
                public void run() {
                    // AppManager.getInstance().AppExit(getApplicationContext());
                }
            }, 3000);

        }

    }
}
