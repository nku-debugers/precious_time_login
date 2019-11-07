package comv.example.zyrmj.precious_time01.viewModels;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import comv.example.zyrmj.precious_time01.R;

import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;
import comv.example.zyrmj.precious_time01.datepicker.DateFormatUtils;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;

public class TestDatePicker extends Activity implements View.OnClickListener {

    private TextView mTvSelectedTime1, mTvSelectedTime2;
    private CustomDatePicker mTimePicker1, mTimePicker2;
    private Button save, delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_template_item);

        save = findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                判断数据是否存在
//                判断数据是否正确
//                将数据插入数据库
            }
        });

        //取消删除按钮，重写返回键，提醒用户是否返回，“您的数据将不会被保存”

        findViewById(R.id.start_time).setOnClickListener(this);

        findViewById(R.id.end_time).setOnClickListener(this);
        mTvSelectedTime1 = findViewById(R.id.tv_selected_start_time);
        mTvSelectedTime2 = findViewById(R.id.tv_selected_end_time);
        initTimerPicker1();
        initTimerPicker2();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimePicker1.onDestroy();
    }


    private void initTimerPicker1() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);

        mTvSelectedTime1.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker1 = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime1.setText(DateFormatUtils.long2Str(timestamp, 3));
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
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);

        mTvSelectedTime2.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker2 = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime2.setText(DateFormatUtils.long2Str(timestamp, 3));
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