package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    private Button choseQuote, back;
    private HabitRepository habitRepository;
    private Habit newHabit;
    private NiceSpinner timePeriodSpinner, habitPrioritySpinner;
    private EditText habitPlace, advancedMinute;
    private Switch timeRemind;
    private ArrayList<Quote> selectedQuotes;
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
                quoteAdapter.setSelectedQuotes(selectedQuotes); //传递之前选择的quote
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
                                selectedQuotes = (ArrayList<Quote>) quoteAdapter.getSelectedQuotes();
                                if(selectedQuotes.size()!=0)
                                    choseQuote.setText("已选择");
                                else
                                    choseQuote.setText("未选择");
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                  dialog.dismiss();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //收集界面信息，统一放到habit中传回上一个界面
                setValues();
                NavController controller = Navigation.findNavController(view);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putSerializable("theHabit", newHabit);
                bundle.putSerializable("selectedQuotes", selectedQuotes);
                if (getArguments().getSerializable("labels") != null)
                    bundle.putSerializable("labels", getArguments().getSerializable("labels"));
                if (getArguments().getSerializable("selectedIndex") != null)
                    bundle.putSerializable("selectedIndex", getArguments().getSerializable("selectedIndex"));
                if (getArguments().getSerializable("oldHabit") != null)
                {
                    bundle.putSerializable("oldHabit",getArguments().getSerializable("oldHabit"));
                    bundle.putString("isUpdate","2");
                }
                controller.navigate(R.id.action_addHabit2_to_addHabit1, bundle);
            }
        });
    }

    public void init() {
        choseQuote = getView().findViewById(R.id.choseQuote);
        if(selectedQuotes.size()!=0)
            choseQuote.setText("已选择");
        back = getView().findViewById(R.id.advanced_back);
        timePeriodSpinner = getView().findViewById(R.id.time_period_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("无","上午", "下午", "晚上"));
        timePeriodSpinner.attachDataSource(dataset);
        timePeriodSpinner.setSelectedIndex(newHabit.getExpectedTime());
        habitPrioritySpinner = getView().findViewById(R.id.habbit_priority_spinner);
        List<String> priorities = new LinkedList<>(Arrays.asList("无","低", "中", "高"));
        habitPrioritySpinner.attachDataSource(priorities);
        habitPrioritySpinner.setSelectedIndex(newHabit.getPriority());
        habitPlace = getView().findViewById(R.id.habbit_place_input);
        if(newHabit.getLocation()!=null)
            habitPlace.setText(newHabit.getLocation());
        advancedMinute = getView().findViewById(R.id.advanceminute);
        timeRemind = getView().findViewById(R.id.time_remind_switch);
        if(newHabit.getReminder()!=0)
        {
            timeRemind.setChecked(true);
        advancedMinute.setText(String.valueOf(newHabit.getReminder())); //int一定要转换成string 型
        }
        mTvSelectedTime = getView().findViewById(R.id.tv_selected_end_time_inhabit);
        if(newHabit.getTime4once()!=null)
            mTvSelectedTime.setText(newHabit.getTime4once());
        else mTvSelectedTime.setText("0");
        getView().findViewById(R.id.habit_time).setOnClickListener(this);
        initTimerPicker();

    }

    public void setValues() {
        if (habitPlace.getText() != null) {
            newHabit.setLocation(habitPlace.getText().toString());
        }
        if (timeRemind.isChecked()) {
            if (advancedMinute.getText() != null) {
                newHabit.setReminder(Integer.valueOf(advancedMinute.getText().toString()));
            }
        } else newHabit.setReminder(0);
        newHabit.setExpectedTime(timePeriodSpinner.getSelectedIndex());//0-无 1-上午 2-下午 3-晚上
        newHabit.setPriority(habitPrioritySpinner.getSelectedIndex());//0-无 1-低 2-中 3-高
        //设置单次时长
        newHabit.setTime4once(mTvSelectedTime.getText().toString());


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "offline");
            newHabit = (Habit) getArguments().getSerializable("theHabit");
            if(getArguments().getSerializable("selectedQuotes")!=null)
            {
                selectedQuotes=(ArrayList<Quote>)getArguments().getSerializable("selectedQuotes");
            }
            else  //第一次跳转到高级设置页面
            {
                selectedQuotes=new ArrayList<>();
            }
        } else {
            newHabit = new Habit();
        }
        habitRepository = new HabitRepository(getContext());
        init();
        enableButtons();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.habit_time:
                // 日期格式为yyyy-MM-dd
                mTimePicker.show(mTvSelectedTime.getText().toString());
                break;
        }
    }
    private void initTimerPicker() {
        //String beginTime = "00:00";
        String beginTime = df.format(new Date());
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));


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
       String hour=time.substring(0,2);
       String minute=time.substring(3,5);
       return hour+":"+minute;
    }

}

