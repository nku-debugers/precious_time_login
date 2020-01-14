package comv.example.zyrmj.precious_time01.fragments.plan;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.donkingliang.labels.LabelsView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.QuoteAdapter;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;
import comv.example.zyrmj.precious_time01.datepicker.DateFormatUtils;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.entity.relations.TodoCategory;
import comv.example.zyrmj.precious_time01.entity.relations.TodoQuote;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;
import comv.example.zyrmj.precious_time01.repository.TodoRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

public class AddTodoAfterPlanned extends Fragment implements View.OnClickListener{
    static String TAG = "mytag";
    private Todo myTodo;
    private ImageView back;
    private Button choseQuote, confirm,clear;
    private Switch timeReminder;
    private EditText todoName, reminder;
    private LabelsView labelsView;
    private String userId = "offline",templateName;
    private ArrayList<String> labels, selectedLabels;
    private CategoryRepository categoryRepository;
    private TodoRepository todoRepository;
    private ArrayList<Integer> selectedIndex;
    private TextView mTvSelectedTime1, mTvSelectedTime2, mTvSelectedTimeWeek;
    private CustomDatePicker mTimePicker1, mTimePicker2, mTimePickerWeek;
    private CardView start, end, week;
    private Date startDate, endDate;
    private boolean startDateModified, endDateModified;
    private boolean timeReverse;
    private ArrayList<Quote> selectedQuotes;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//设置日期格式
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.CANADA);
    private String[] weekString = {"日", "一", "二", "三", "四", "五", "六"};
    private List<Todo> todos;
    private String planDate;
    public AddTodoAfterPlanned() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_todo_after_planned, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "offline");
            todos = (List<Todo>) getArguments().getSerializable("toDos");
            templateName=getArguments().getString("templateName","");
            planDate = getArguments().getString("planDate", "");
        }
        assignViews();
        init();
        enableButtons();
    }

    private void saveLabels() {
        selectedLabels = new ArrayList<>();
        for (int index : selectedIndex) {
            selectedLabels.add(labels.get(index));
        }
    }

    private boolean saveTime() {
        if(startDate.after(endDate)) {
            return false;
        }

        String startTime = mTvSelectedTime1.getText().toString();
        String endTime = mTvSelectedTime2.getText().toString();

        String week = getWeek(mTvSelectedTimeWeek.getText().toString());
        boolean canInsert = checkAndInsert(week, startTime, endTime);
        String startFinal = week + "-" + startTime;
        String endFinal = week + "-" + endTime;
        Log.d(TAG, "saveTime: the caninserte is " + canInsert);
        if (canInsert) {
            myTodo.setStartTime(startFinal);
            myTodo.setEndTime(endFinal);
            return true;
        } else {
            //提示有重复项
            PromptDialog promptDialog = new PromptDialog (getActivity ());
            promptDialog.showWarnAlert ( "存在冲突时间，请重新选择！",new PromptButton( "确定" , new PromptButtonListener() {
                @Override
                public void onClick(PromptButton button) {

                }
            } ) );
            return false;
        }

    }

    private boolean checkAndInsert(String week, String startFinal, String endFinal) {
        String alreadyExistStart;
        String alreadyExistEnd;
        Log.d(TAG, "checkAndInsert: the week is" + week);
        for(int i=0 ;i < todos.size(); i++) {
            Log.d(TAG, "checkAndInsert: the start time is" + todos.get(i).getStartTime());
            Log.d(TAG, "checkAndInsert: the end time is " + todos.get(i).getEndTime());
            if(todos.get(i).getStartTime().length() >= 3 && todos.get(i).getStartTime().substring(0,1).equals(week)){

                Log.d(TAG, "checkAndInsert: inside if the week is");
                alreadyExistStart = todos.get(i).getStartTime().substring(2);
                alreadyExistEnd = todos.get(i).getEndTime().substring(2);

                if ( !checkExceedStart(alreadyExistStart, alreadyExistEnd, startFinal)) {
                    return false;
                }
                if ( !checkExceedEnd(alreadyExistStart, alreadyExistEnd, endFinal)) {
                    return false;
                }
                if ( !checkIfContained(alreadyExistStart, alreadyExistEnd, startFinal, endFinal)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkExceedStart(String start, String end, String ready) {
        if (TimeDiff.compare(start, ready) == 0) {
            return false;
        }
        if ((TimeDiff.compare(start, ready) == -1) && TimeDiff.compare(end, ready) == 1  ) {
            return false;
        }
        return true;
    }

    private boolean checkExceedEnd(String start, String end, String ready) {
        if (TimeDiff.compare(end, ready) == 0) {
            return false;
        }
        if ((TimeDiff.compare(start, ready) == -1) && TimeDiff.compare(end ,ready) == 1) {
            return false;
        }
        return true;
    }

    private boolean checkIfContained(String start, String end, String realStart, String realEnd) {
        return TimeDiff.compare(start, realStart) < 0 || TimeDiff.compare(end, realEnd) > 0;
    }

    private void enableButtons() {
        getView().findViewById(R.id.start_time_in_todo).setOnClickListener(this);
        getView().findViewById(R.id.end_time_in_todo).setOnClickListener(this);
        getView().findViewById(R.id.week_time_in_todo).setOnClickListener(this);
        week.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptDialog promptDialog = new PromptDialog(getActivity());
                PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        NavController controller = Navigation.findNavController(getView());
                        Bundle bundle = new Bundle();
                        bundle.putString("delete", "true");
                        bundle.putString("userId", userId);
                        bundle.putString("templateName", templateName);
                        bundle.putSerializable("habits",getArguments().getSerializable("habits"));
                        bundle.putSerializable("idleTimes",getArguments().getSerializable("idleTimes"));
                        bundle.putSerializable("toDoExtends",getArguments().getSerializable("toDoExtends"));
                        bundle.putSerializable("toDos",getArguments().getSerializable("toDos"));
                        controller.navigate(R.id.action_addToDo2_to_editPlan, bundle);
                        // TODO: 2020/1/14  需要变成新的
                    }
                });
                PromptButton cancel = new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        //Nothing
                    }
                });
                confirm.setTextColor(Color.parseColor("#DAA520"));
                confirm.setFocusBacColor(Color.parseColor("#FAFAD2"));
                promptDialog.showWarnAlert("您的数据将不会被保存，是否退出？", cancel, confirm);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveLabels();
                if (saveTime()) { // TODO: 2020/1/14 Save Time 的逻辑需要改，控制不能 结束时间 小于开始时间
                    myTodo.setType(2);
                    if (timeReminder.isChecked()) {
                        myTodo.setReminder(Integer.valueOf(reminder.getText().toString()));
                    } else {
                        myTodo.setReminder(0);
                    }
                    if (todoName.getText().length() != 0) {
                        myTodo.setName(todoName.getText().toString());
                    } else {
                        PromptDialog promptDialog = new PromptDialog(getActivity());
                        promptDialog.showWarnAlert("请填写名字！", new PromptButton("确定", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {

                            }
                        }));
                    }
                    selectedLabels = new ArrayList<String>(new HashSet<String>(selectedLabels));
                    List<Category>allCategories = categoryRepository.getAllCateories(userId);
                    List<Category>needToAdd = new ArrayList<>();
                    for(String s:selectedLabels) {
                        int flag = 0;
                        for(Category c: allCategories) {
                            if (c.getName().equals(s))
                                flag = 1;
                        }
                        if (flag == 0) {
                            categoryRepository.insertCategory(new Category(userId, s));
                        }
                    }

                    myTodo.setPlanDate(planDate);
                    todoRepository.insertTodo(myTodo);
                    for(Quote quote:selectedQuotes) {
                        todoRepository.insertTodoQuote(new TodoQuote(quote, myTodo));
                    }
                    for(String s : selectedLabels) {
                        todoRepository.insertTodoCategory(new TodoCategory(userId, s, myTodo.getStartTime(), myTodo.getPlanDate()));
                    }

                    NavController controller = Navigation.findNavController(getView());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mytodo", myTodo);
                    bundle.putSerializable("labels", selectedLabels);
                    bundle.putSerializable("quotes", selectedQuotes);
                    bundle.putString("userId", userId);
                    bundle.putString("templateName", templateName);
                    bundle.putSerializable("habits", getArguments().getSerializable("habits"));
                    bundle.putSerializable("idleTimes", getArguments().getSerializable("idleTimes"));
                    bundle.putSerializable("toDoExtends", getArguments().getSerializable("toDoExtends"));
                    bundle.putSerializable("toDos", getArguments().getSerializable("toDos"));
                    controller.navigate(R.id.action_addToDo2_to_editPlan, bundle);
                    // TODO: 2020/1/14 这里也需要改成新的
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeReminder.setChecked(false);
                todoName.setText("");
                reminder.setText("");
                selectedIndex=new ArrayList<>();
                selectedLabels=new ArrayList<>();
                labelsView.setSelects(selectedIndex);
                mTvSelectedTimeWeek.setText("一");
                mTvSelectedTime1.setText("00:00");
                mTvSelectedTime2.setText("00:00");
                selectedQuotes=new ArrayList<>();
                choseQuote.setText("未选择");
            }
        });

        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                data = (String) data;
                if (data.equals("+")) {
                    showDialog("");

                }
            }
        });

        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                data = (String) data;
                if (!data.equals("+")) {
                    if (isSelect) {
                        selectedIndex.add(position);
                    } else {
                        selectedIndex.remove(Integer.valueOf(position));
                    }
                }
            }
        });

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
    }

    private void assignViews() {
        week = getView().findViewById(R.id.week_card_after);
        back=getView().findViewById(R.id.back_after);
        confirm = getView().findViewById(R.id.todo_confirm);
        clear=getView().findViewById(R.id.clear_after);
        choseQuote = getView().findViewById(R.id.choseQuoteTodo_after);
        timeReminder = getView().findViewById(R.id.time_remind_switch2_after);
        reminder = getView().findViewById(R.id.advanceminute2_after);
        todoName = getView().findViewById(R.id.todo_name_input_after);
        labelsView = getView().findViewById(R.id.todo_category_select_after);
        categoryRepository = new CategoryRepository(getContext());
        mTvSelectedTime1 = getView().findViewById(R.id.tv_selected_start_time_in_todo_after);
        mTvSelectedTime2 = getView().findViewById(R.id.tv_selected_end_time_in_todo_after);
        mTvSelectedTimeWeek = getView().findViewById(R.id.tv_selected_week_after);
        start = getView().findViewById(R.id.card_start_after);
        end = getView().findViewById(R.id.card_end_after);
    }

    private void init() {
        myTodo = new Todo();
        startDateModified = false;
        endDateModified = false;
        timeReverse = false;
        startDate = new Date();
        endDate = new Date();
        selectedQuotes=new ArrayList<>();
        initTimerPicker1();
        initTimerPicker2();
        initTimerPicker3();
        labels = new ArrayList<>();
        selectedIndex = new ArrayList<>();
        List<Category> categories = categoryRepository.getAllCateories(userId);
        for (Category category : categories) {
            labels.add(category.getName());
        }
        labels.add("+");
        labelsView.setLabels(labels);
    }

    private void showDialog(String info) {
        new MaterialDialog.Builder(getContext())
                .title("添加新类别")
                .content(info)
                .inputType(InputType.TYPE_CLASS_TEXT)
                //前2个一个是hint一个是预输入的文字
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //判断模板名是否为空
                        if (input.toString().equals("")) {
                            dialog.setContent("类名不能为空，请重新输入！");
                        } else {
                            String categoryName = input.toString();
                            //查找是否有同名Category存在
                            if (categoryRepository.getSpecificCategory(userId, categoryName) != null) {
                                Log.i("dialog", "存在同名类别");
                                dialog.setContent("存在同名类别，请重新输入！");
                            } else {
                                labels.remove("+");
                                labels.add(categoryName);
                                labels.add("+");
                                ArrayList<Integer> lastSelected = new ArrayList<>();
                                for (Integer index : selectedIndex) {
                                    lastSelected.add(index);
                                }
                                labelsView.setLabels(labels);
                                labelsView.setSelects(lastSelected);
                                dialog.dismiss();
                                //

                            }
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .autoDismiss(false).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time_in_todo_after:
                // 日期格式为yyyy-MM-dd
                mTimePicker1.show(mTvSelectedTime1.getText().toString());
                break;

            case R.id.end_time_in_todo_after:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimePicker2.show(mTvSelectedTime2.getText().toString());
                break;

            case R.id.week_time_in_todo_after:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimePickerWeek.show(mTvSelectedTimeWeek.getText().toString());
                break;
        }
    }

    private void initTimerPicker1() {
        String beginTime = df.format(new Date());
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
                if (startDate != null && endDate != null && startDateModified) {
                    if (endDate.before(startDate)) {
                        mTvSelectedTime1.setText(R.string.end_time_exceed_warning);
                        timeReverse = true;
                    } else {
                        mTvSelectedTime2.setText(hourFormat.format(endDate));
                    }
                }
            }
        }, beginTime, endTime, 1);
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
                    } else {
                        mTvSelectedTime1.setText(hourFormat.format(startDate));
                    }
                }
            }

        }, beginTime, endTime, 1);
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
        String beginTime = df.format(new Date());
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
        }, beginTime, endTime, 1);
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

    private String getTime(String time) {
        String hour=time.substring(0,2);
        String minute=time.substring(3,5);
        System.out.println("timeresult "+hour+" "+minute);
        return hour+":"+minute;
    }

    private String getWeek(String selected) {
        String week;
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
}
