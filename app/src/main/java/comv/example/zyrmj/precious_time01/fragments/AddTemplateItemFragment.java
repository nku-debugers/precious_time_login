package comv.example.zyrmj.precious_time01.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;
import comv.example.zyrmj.precious_time01.datepicker.DateFormatUtils;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

public class AddTemplateItemFragment extends Fragment implements View.OnClickListener {
    private TextView mTvSelectedTime1, mTvSelectedTime2, mTvSelectedTimeWeek;
    private CustomDatePicker mTimePicker1, mTimePicker2, mTimePickerWeek;
    private Button save, delete;
    private Date startDate, endDate;
    private boolean startDateModified, endDateModified;
    private EditText name;
    private String userId, templateName,viewOption;
    private Date today;
    private boolean timeReverse;
    private String[] weekString = {"日","一","二","三","四","五","六"};
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//设置日期格式
    public AddTemplateItemFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_template_item, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "");
            templateName = getArguments().getString("templateName", "");
            viewOption=getArguments().getString("viewOption","0");
        }
        startDateModified = false;
        endDateModified = false;
        timeReverse = false;
        today = new Date();
        startDate = today;
        endDate = today;
        init();
        enableBackButton();
    }

    private void enableBackButton() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP )
                {
                    PromptDialog promptDialog = new PromptDialog (getActivity ());
                    PromptButton confirm = new PromptButton("确定", new PromptButtonListener () {
                        @Override
                        public void onClick(PromptButton button) {
                            Log.d("mytag", "onKey: Back button successfully enabled!");
                            NavController controller = Navigation.findNavController(getView());
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", userId);
                            bundle.putString("templateName", templateName);
                            if(viewOption.equals("0"))
                            controller.navigate(R.id.action_addTemplateItem_to_testWeekView, bundle);
                            else
                                controller.navigate(R.id.action_addTemplateItem_to_tmpItemListFragment,bundle);
                        }
                    });
                    PromptButton cancel = new PromptButton("取消", new PromptButtonListener () {
                        @Override
                        public void onClick(PromptButton button) {
                            //Nothing
                        }
                    });
                    confirm.setTextColor( Color.parseColor("#DAA520"));
                    confirm.setFocusBacColor(Color.parseColor("#FAFAD2"));
                    promptDialog.showWarnAlert("您的数据将不会被保存，是否退出？", cancel, confirm);
                    return true;
                }
                return false;
            }
        } );
    }

    private void init() {
        save = getView().findViewById(R.id.button2);
        name = getView().findViewById(R.id.editText);
        //取消删除按钮，重写返回键，提醒用户是否返回，“您的数据将不会被保存”

        getView().findViewById(R.id.start_time).setOnClickListener(this);

        getView().findViewById(R.id.end_time).setOnClickListener(this);
        getView().findViewById(R.id.week_time).setOnClickListener(this);
        mTvSelectedTime1 = getView().findViewById(R.id.tv_selected_start_time);
        mTvSelectedTime2 = getView().findViewById(R.id.tv_selected_end_time);
        mTvSelectedTimeWeek = getView().findViewById(R.id.tv_selected_week);

        initTimerPicker1();
        initTimerPicker2();
        initTimerPicker3();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate == null || endDate == null) {
                    //用户输入起止时间
                    PromptDialog promptDialog = new PromptDialog (getActivity ());
                    promptDialog.showWarn ( "未填写开始或终止时间！" );
                    return;
                }
                if (name.getText().toString().equals("")) {
                    //提示用户指定用户名字
                    PromptDialog promptDialog = new PromptDialog (getActivity ());
                    promptDialog.showWarn ( "未填写名称！" );
                    return;
                }

                if (timeReverse) {
                    PromptDialog promptDialog = new PromptDialog (getActivity ());
                    promptDialog.showWarn ( "结束时间应晚于开始时间！" );
                    return;
                }

                String startTime = mTvSelectedTime1.getText().toString();
                String endTime = mTvSelectedTime2.getText().toString();

                String week = getWeek(mTvSelectedTimeWeek.getText().toString());
                String startFinal = week + "-" + startTime;
                String endFinal = week + "-" + endTime;

                boolean canInsert = checkAndInsert(week, startFinal, endFinal);
                if (canInsert) {
                    NavController controller = Navigation.findNavController(getView());
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putString("templateName", templateName);
                    if(viewOption.equals("0"))
                    controller.navigate(R.id.action_addTemplateItem_to_testWeekView, bundle);
                    else
                        controller.navigate(R.id.action_addTemplateItem_to_tmpItemListFragment, bundle);
                } else {
                   //提示有重复项
                }
            }
        });
    }
    private boolean checkAndInsert(String week, String start, String end) {
        TemplateItem item = new TemplateItem("offline", name.getText().toString(),
                templateName, "study", end, start);
        TemplateItemRepository t = new TemplateItemRepository(getActivity());
        int s = t.ifTimeConfilict(week, start);
        int e = t.ifTimeConfilict(week, end);
        if (s == 0 || e == 0) {
            return false;
        }
        else{
            t.insertTemplateItems(item);
            return true;
        }
    }
    private String getWeek(String selected) {
        String week = "";
        switch (selected) {
            case "日":
                week = "6";
                break;
            case "一":
                week = "0";
                break;
            case "二":
                week = "1";
                break;
            case "三":
                week = "2";
                break;
            case "四":
                week = "3";
                break;
            case "五":
                week = "4";
                break;
            case "六":
                week = "5";
                break;
            default:
                return "0";
        }
        return week;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time:
                // 日期格式为yyyy-MM-dd
                mTimePicker1.show(mTvSelectedTime1.getText().toString());
                break;

            case R.id.end_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimePicker2.show(mTvSelectedTime2.getText().toString());
                break;
            case R.id.week_time:
                mTimePickerWeek.show(mTvSelectedTimeWeek.getText().toString());
        }
    }

    private void initTimerPicker1() {
        String beginTime = df.format(new Date());
        //String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis()-1, 1);
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        mTvSelectedTime1.setText(endTimeShow);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker1 = new CustomDatePicker(this.getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime1.setText(DateFormatUtils.long2Str(timestamp, 3));
                startDateModified = true;
                try {
                    startDate = new SimpleDateFormat("HH:mm", Locale.CHINA)
                            .parse(DateFormatUtils.long2Str(timestamp, 3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimePicker1.setCancelable(true);
        // 显示时和分
        mTimePicker1.setCanShowYearAndDay(false);
        mTimePicker1.setCanShowPreciseTime(true);

        // 允许循环滚动
        mTimePicker1.setScrollLoop(true);
        // 允许滚动动画
        mTimePicker1.setCanShowAnim(true);
    }

    private void initTimerPicker2() {
        String beginTime = df.format(new Date());
        final String endTime = DateFormatUtils.long2Str(System.currentTimeMillis() + 1, 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        mTvSelectedTime2.setText(endTimeShow);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker2 = new CustomDatePicker(this.getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime2.setText(DateFormatUtils.long2Str(timestamp, 3));
                endDateModified = true;
                try {
                    endDate = new SimpleDateFormat("HH:mm", Locale.CHINA)
                            .parse(DateFormatUtils.long2Str(timestamp, 3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null && startDateModified) {
                    if (endDate.before(startDate)) {
                        mTvSelectedTime2.setText(R.string.end_time_exceed_warning);
                        timeReverse = true;
                    }
                }
            }

        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimePicker2.setCancelable(true);
        // 显示时和分
        mTimePicker2.setCanShowYearAndDay(false);
        mTimePicker2.setCanShowPreciseTime(true);

        // 允许循环滚动
        mTimePicker2.setScrollLoop(true);
        // 允许滚动动画
        mTimePicker2.setCanShowAnim(true);
    }
    private void initTimerPicker3() {
        String beginTime = df.format(today);
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        Calendar calendar = Calendar.getInstance();
        int k = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        mTvSelectedTimeWeek.setText(weekString[k]);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePickerWeek = new CustomDatePicker(this.getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTimeWeek.setText(DateFormatUtils.long2Str(timestamp, 4));
                try {
                    startDate = new SimpleDateFormat("HH:mm", Locale.CHINA)
                            .parse(DateFormatUtils.long2Str(timestamp, 3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭

        mTimePickerWeek.setCancelable(true);
        // 显示时和分
        mTimePickerWeek.setCanShowYearAndDay(false);
        mTimePickerWeek.setCanShowPreciseTime(false);

        // 允许循环滚动
        mTimePickerWeek.setScrollLoop(true);
        // 允许滚动动画
        mTimePickerWeek.setCanShowAnim(true);
    }
}
