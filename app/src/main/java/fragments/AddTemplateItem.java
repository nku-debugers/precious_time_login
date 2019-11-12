package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;
import comv.example.zyrmj.precious_time01.datepicker.DateFormatUtils;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

public class AddTemplateItem extends Fragment implements View.OnClickListener {
    private TextView mTvSelectedTime1, mTvSelectedTime2;
    private CustomDatePicker mTimePicker1, mTimePicker2;
    private Button save, delete;
    private Date startText, endText;
    private EditText name;
    private String userId, templateName;

    public AddTemplateItem() {

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
            Log.d("mytag", templateName);
        }
        init();
    }

    private void init() {
        save = getView().findViewById(R.id.button2);
        name = getView().findViewById(R.id.editText);
        //取消删除按钮，重写返回键，提醒用户是否返回，“您的数据将不会被保存”

        getView().findViewById(R.id.start_time).setOnClickListener(this);

        getView().findViewById(R.id.end_time).setOnClickListener(this);
        mTvSelectedTime1 = getView().findViewById(R.id.tv_selected_start_time);
        mTvSelectedTime2 = getView().findViewById(R.id.tv_selected_end_time);
        initTimerPicker1();
        initTimerPicker2();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startText == null || endText == null) {
                    //提示查找
                    return;
                }
                if (name.getText().toString().equals("")) {
                    //提示没有写名字
                    return;
                }
                String startTIme = mTvSelectedTime1.getText().toString();
                String endTime = mTvSelectedTime2.getText().toString();

                TemplateItem item = new TemplateItem("offline", name.getText().toString(),
                        templateName, "study", endTime, startTIme);
                TemplateItemRepository t = new TemplateItemRepository(getActivity());
                t.insertTemplateItems(item);

            }
        });
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
        }
    }

    private void initTimerPicker1() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));

        mTvSelectedTime1.setText(endTimeShow);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker1 = new CustomDatePicker(this.getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime1.setText(DateFormatUtils.long2Str(timestamp, 3));
                try {
                    startText = new SimpleDateFormat("HH:mm", Locale.CHINA)
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
        String beginTime = "2018-10-17 00:00";
        final String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        mTvSelectedTime2.setText(endTimeShow);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker2 = new CustomDatePicker(this.getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime2.setText(DateFormatUtils.long2Str(timestamp, 3));
                try {
                    endText = new SimpleDateFormat("HH:mm", Locale.CHINA)
                            .parse(DateFormatUtils.long2Str(timestamp, 3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (startText != null && endText != null) {
                    if (endText.before(startText)) {
                        mTvSelectedTime2.setText(R.string.end_time_exceed_warning);
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
}
