package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.QuoteAdapter;
import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;
import comv.example.zyrmj.precious_time01.datepicker.DateFormatUtils;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabit2 extends Fragment implements View.OnClickListener {
    private String userId = "offline";
    private Button choseQuote;
    private HabitRepository habitRepository;
    private Habit newHabit;
    private NiceSpinner timePeriodSpinner;
    //时间选择器
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimePicker;
    private Date time4once;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//设置日期格式

    public AddHabit2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_habit_item2, container, false);
    }

    private void enableButtons() {
        choseQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Quote> quotes = new QuoteRepository(getContext()).getAllQuotes2(userId);
                final QuoteAdapter quoteAdapter = new QuoteAdapter(quotes);
                new MaterialDialog.Builder(getContext())
                        .title("选择箴言")// 标题
                        // adapter 方法中第一个参数表示自定义适配器，该适配器必须继承 RecyclerView.Adapter
                        // 第二个参数表示布局管理器，如果不需要设置就为 null，可选择的值只有线性布局管理器（LinearLayoutManager）
                        // 和网格布局管理器（GridLayoutManager）两种
                        .adapter(quoteAdapter, new LinearLayoutManager(getContext()))
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                List<Quote> selectedQuotes = quoteAdapter.getSelctedQuotes();

                                for (Quote q : selectedQuotes) {
                                    Habit h=new Habit("offline","test","test",0.0);
                                    HabitQuote habitQuote=new HabitQuote(q,h) ;
                                    habitRepository.insertHabitQuote(habitQuote);
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();// 显示对话框
            }
        });
        timePeriodSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.habit_time:
                // 日期格式为yyyy-MM-dd
                mTimePicker.show(mTvSelectedTime.getText().toString());
                break;
        }
        Log.d("myTag", "onClick: clicked!!!!!!!!");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null) {
            newHabit = (Habit)getArguments().getSerializable("theHabit");
        }
        habitRepository=new HabitRepository(getContext());
        choseQuote = getView().findViewById(R.id.choseQuote);
        timePeriodSpinner = (NiceSpinner) getView().findViewById(R.id.time_period_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("上午", "下午", "晚上"));
        timePeriodSpinner.attachDataSource(dataset);
        enableButtons();
        init();
    }

    private void init() {
        mTvSelectedTime = getView().findViewById(R.id.tv_selected_end_time_inhabit);
        getView().findViewById(R.id.habit_time).setOnClickListener(this);
        initTimerPicker();
    }

    private void initTimerPicker() {
        //String beginTime = "00:00";
        String beginTime = df.format(new Date());
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        mTvSelectedTime.setText("0");

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker = new CustomDatePicker(this.getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String time = getTime(DateFormatUtils.long2Str(timestamp, 3));
                mTvSelectedTime.setText(time);
                try {
                    time4once = new SimpleDateFormat("HH:mm", Locale.CHINA)
                            .parse(DateFormatUtils.long2Str(timestamp, 3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "2019-08-12 00:00", endTime, 2);
        // 允许点击屏幕或物理返回键关闭
        mTimePicker.setCancelable(true);
        // 显示时和分
        mTimePicker.setCanShowYearAndDay(false);
        mTimePicker.setCanShowPreciseTime(true);

        // 允许循环滚动
        mTimePicker.setScrollLoop(true);
        // 允许滚动动画
        mTimePicker.setCanShowAnim(true);

        //boolean k = mTimePicker.setSelectedTime(1575820800, true);
    }

    String getTime(String time) {
        String x = "";
        if (time.startsWith("0")) {
            x = x.concat(time.substring(1, 2));
        } else {
            x = x.concat(time.substring(0, 2));
        }
        x = x.concat("小时");
        if (time.substring(3, 4).equals("0")){
            x = x.concat(time.substring(4));
        } else {
            x = x.concat(time.substring(3));
        }
        x = x.concat("分钟");
        return x;
    }
}
