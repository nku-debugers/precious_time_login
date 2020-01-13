package comv.example.zyrmj.precious_time01.fragments.plan;

import android.graphics.Color;
import android.media.Image;
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
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;
import comv.example.zyrmj.precious_time01.repository.TodoRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

public class UpdateTodo extends Fragment implements View.OnClickListener {
    static String TAG = "mytag";
    private Todo myTodo;
    private ImageView back;
    private Button choseQuote, confirm,delete;
    private Switch timeType, timeReminder;
    private boolean timeTypeFlag;
    private EditText todoName, reminder;
    private LabelsView labelsView;
    private String userId = "offline";
    private ArrayList<String> labels, selectedLabels;
    private CategoryRepository categoryRepository;
    private TodoRepository todoRepository;
    private ArrayList<Integer> selectedIndex;
    private TextView mTvSelectedTime1, mTvSelectedTime2, mTvSelectedTimeWeek, mTvSelectedLength;
    private CustomDatePicker mTimePicker1, mTimePicker2, mTimePickerWeek, mLengthPicker;
    private CardView start, end, week;
    private Date startDate, endDate, lengthDate;
    private boolean startDateModified, endDateModified;
    private boolean timeReverse;
    private ArrayList<Quote> selectedQuotes;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//设置日期格式
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.CANADA);
    private String[] weekString = {"日", "一", "二", "三", "四", "五", "六"};
    private String[] weekString2 = {"一", "二", "三", "四", "五", "六", "日"};
    private LinearLayout timeLength;
    private List<Todo> todos;
    private ArrayList<EditPlan.ToDoExtend> satisfiedTodos;
    private ArrayList<EditPlan.ToDoExtend> unsatisfiedTodos;
    private EditPlan.ToDoExtend myTodoExtend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_todo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getString("index")!=null)
            {
                userId = getArguments().getString("userId", "offline");
                satisfiedTodos= (ArrayList<EditPlan.ToDoExtend>) getArguments().getSerializable("satisfiedTodos");
                System.out.println("satisfied:"+String.valueOf(satisfiedTodos.size()));
                unsatisfiedTodos= (ArrayList<EditPlan.ToDoExtend>) getArguments().getSerializable("unsatisfiedTodos");
                System.out.println("unsatisfied:"+String.valueOf(unsatisfiedTodos.size()));
                myTodoExtend=unsatisfiedTodos.get(Integer.valueOf(getArguments().getString("index")));
                myTodo=myTodoExtend.getTodo();
                selectedQuotes=myTodoExtend.getQuotes();
                selectedLabels=myTodoExtend.getLabels();

            }
            else {
                userId = getArguments().getString("userId", "offline");
                todos = (List<Todo>) getArguments().getSerializable("toDos");
                myTodo = (Todo) getArguments().getSerializable("mytodo");
                selectedLabels = (ArrayList<String>) getArguments().getSerializable("selectedlabels");
                selectedQuotes = (ArrayList<Quote>) getArguments().getSerializable("selectedquotes");
            }

        }
        assignViews();
        init();
        enableButtons();
    }

    private boolean checkAndInsert(String week, String startFinal, String endFinal) {
        String alreadyExistStart;
        String alreadyExistEnd;
        Log.d(TAG, "checkAndInsert: the week is" + week);
        for(int i=0 ;i < todos.size(); i++) {
            Log.d(TAG, "checkAndInsert: the start time is" + todos.get(i).getStartTime());
            Log.d(TAG, "checkAndInsert: the end time is " + todos.get(i).getEndTime());
            if(todos.get(i).getStartTime().length() != 0 && todos.get(i).getStartTime().substring(0,1).equals(week)){

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

    private void saveLabels() {
        selectedLabels = new ArrayList<>();
        for (int index : selectedIndex) {
            if(!selectedLabels.contains(labels.get(index)))
                selectedLabels.add(labels.get(index));
        }
    }

    private boolean saveTime() {
        if (timeTypeFlag) {
            if (endDateModified || startDateModified) {
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
                    PromptDialog promptDialog = new PromptDialog(getActivity());
                    promptDialog.showWarnAlert("存在冲突时间，请重新选择！", new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {

                        }
                    }));
                    return false;
                }
            } else {
                return true;
            }
        } else if (mTvSelectedLength.getText().length() != 0) {
            myTodo.setStartTime(getWeek(mTvSelectedTimeWeek.getText().toString()) + "-");
            myTodo.setLength(mTvSelectedLength.getText().toString());
            return true;
        } else {
            //提示没有写日期
            return false;
        }
    }

    private void enableButtons() {
        getView().findViewById(R.id.start_time_in_todo).setOnClickListener(this);
        getView().findViewById(R.id.end_time_in_todo).setOnClickListener(this);
        getView().findViewById(R.id.week_time_in_todo).setOnClickListener(this);
        getView().findViewById(R.id.todo_length).setOnClickListener(this);
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
                        if (getArguments().getString("card") != null)
                        {
                            bundle.putString("userId",userId);
                            bundle.putSerializable("satisfiedTodos", satisfiedTodos);
                            bundle.putSerializable("unsatisfiedTodos",unsatisfiedTodos);
                            controller.navigate(R.id.action_updateTodo_to_modifyPlan, bundle);
                        }
                        else {
                            bundle.putSerializable("mytodo", myTodo);
                            bundle.putSerializable("labels", selectedLabels);
                            bundle.putSerializable("quotes", selectedQuotes);
                            bundle.putString("userId", userId);
                            bundle.putString("templateName", getArguments().getString("templateName"));
                            bundle.putSerializable("habits", getArguments().getSerializable("habits"));
                            bundle.putSerializable("idleTimes", getArguments().getSerializable("idleTimes"));
                            bundle.putSerializable("toDoExtends", getArguments().getSerializable("toDoExtends"));
                            bundle.putSerializable("toDos", getArguments().getSerializable("toDos"));
                            controller.navigate(R.id.action_updateTodo_to_editPlan, bundle);
                        }
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
                if(getArguments().getString("card")!=null)
                {
                    //更改labels
                   saveLabels();
                    myTodoExtend.setLabels(selectedLabels);
                    //更改Quotes
                    myTodoExtend.setQuotes(selectedQuotes);
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
                    String startTime="1 times",endTime="";
                    if (timeTypeFlag) {
                        startTime = mTvSelectedTime1.getText().toString();
                        endTime = mTvSelectedTime2.getText().toString();
                    }
                    String week = getWeek(mTvSelectedTimeWeek.getText().toString());
                    String startFinal = week + "-" + startTime;
                    String endFinal = week + "-" + endTime;
                    myTodo.setStartTime(startFinal);
                    myTodo.setEndTime(endFinal);
                    myTodoExtend.setTodo(myTodo);
                    unsatisfiedTodos.set(Integer.valueOf(getArguments().getString("index")),myTodoExtend);
                    Bundle bundle=new Bundle();
                    bundle.putString("userId",userId);
                    bundle.putSerializable("satisfiedTodos", satisfiedTodos);
                    bundle.putSerializable("unsatisfiedTodos",unsatisfiedTodos);
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_updateTodo_to_modifyPlan, bundle);

                }
                else {
                    saveLabels();
                    myTodo.setLength(mTvSelectedTimeWeek.getText().toString());
                    if (saveTime()) {
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
                        NavController controller = Navigation.findNavController(getView());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mytodo", myTodo);
                        bundle.putSerializable("labels", selectedLabels);
                        bundle.putSerializable("quotes", selectedQuotes);
                        bundle.putString("userId", userId);
                        bundle.putString("templateName", getArguments().getString("templateName"));
                        bundle.putSerializable("habits", getArguments().getSerializable("habits"));
                        bundle.putSerializable("idleTimes", getArguments().getSerializable("idleTimes"));
                        bundle.putSerializable("toDoExtends", getArguments().getSerializable("toDoExtends"));
                        bundle.putSerializable("toDos", getArguments().getSerializable("toDos"));
                        controller.navigate(R.id.action_updateTodo_to_editPlan, bundle);
                    }
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                if(!(getArguments().getString("card")==null))
                {
                    PromptDialog promptDialog = new PromptDialog(getActivity());
                    PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            unsatisfiedTodos.remove(myTodoExtend);
                            bundle.putString("userId",userId);
                            bundle.putSerializable("satisfiedTodos", satisfiedTodos);
                            bundle.putSerializable("unsatisfiedTodos",unsatisfiedTodos);
                            controller.navigate(R.id.action_updateTodo_to_modifyPlan, bundle);

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
                    promptDialog.showWarnAlert("确认删除？", cancel, confirm);

                }

                else
                {
                    bundle.putSerializable("delete", "true");
                    bundle.putString("userId", userId);
                    bundle.putString("templateName", getArguments().getString("templateName"));
                    bundle.putSerializable("habits", getArguments().getSerializable("habits"));
                    bundle.putSerializable("idleTimes", getArguments().getSerializable("idleTimes"));
                    bundle.putSerializable("toDoExtends", getArguments().getSerializable("toDoExtends"));
                    bundle.putSerializable("toDos", getArguments().getSerializable("toDos"));
                    System.out.println("删除!!");
                    controller.navigate(R.id.action_updateTodo_to_editPlan, bundle);
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

        timeType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    timeTypeFlag = true;
                    timeLength.setVisibility(View.GONE);
                    start.setVisibility(View.VISIBLE);
                    end.setVisibility(View.VISIBLE);
                } else {
                    timeTypeFlag = false;
                    startDate = null;
                    endDate = null;
                    timeLength.setVisibility(View.VISIBLE);
                    start.setVisibility(View.GONE);
                    end.setVisibility(View.GONE);
                    mTvSelectedLength.setText(" ");
                }
            }
        });
    }

    private void assignViews() {
        week = getView().findViewById(R.id.week_card);
        back=getView().findViewById(R.id.back);
        confirm = getView().findViewById(R.id.todo_confirm);
        delete=getView().findViewById(R.id.clear);
        if(getArguments().getString("option").equals("update"))
            delete.setText("删除");
        choseQuote = getView().findViewById(R.id.choseQuoteTodo);
        timeType = getView().findViewById(R.id.todo_time);
        timeReminder = getView().findViewById(R.id.time_remind_switch2);
        reminder = getView().findViewById(R.id.advanceminute2);
        todoName = getView().findViewById(R.id.todo_name_input);
        labelsView = getView().findViewById(R.id.todo_category_select);
        categoryRepository = new CategoryRepository(getContext());
        mTvSelectedTime1 = getView().findViewById(R.id.tv_selected_start_time_in_todo);
        mTvSelectedTime2 = getView().findViewById(R.id.tv_selected_end_time_in_todo);
        mTvSelectedTimeWeek = getView().findViewById(R.id.tv_selected_week);
        mTvSelectedLength = getView().findViewById(R.id.tv_selected_length);
        start = getView().findViewById(R.id.card_start);
        end = getView().findViewById(R.id.card_end);
        timeLength = getView().findViewById(R.id.todo_length);
    }

    private void init() {
        startDateModified = false;
        endDateModified = false;
        timeReverse = false;

        //初始化reminder 值
        reminder.setText(String.valueOf(myTodo.getReminder()));
        if(myTodo.getReminder() > 0) {
            timeReminder.setChecked(true);
        } else {
            timeReminder.setChecked(false);
        }
        todoName.setText(myTodo.getName());
        //初始化标签
        labels = new ArrayList<>();
        selectedIndex = new ArrayList<>();
        List<Category> categories = categoryRepository.getAllCateories(userId);
        int i = 0;
        for (Category category : categories) {
            labels.add(category.getName());
            if (Arrays.asList(selectedLabels).contains(category.getName())){
                selectedIndex.add(i++);
            }
        }
//        for(int m=0; i < selectedLabels.size(); m++)
//        {
//            Log.d(TAG, "onActivityCreated: the labels selected are " + selectedLabels.get(m));
//        }
        for (int k = 0; k < selectedLabels.size(); k++) {
            Log.d(TAG, "init: This is the i " + i);
            Log.d(TAG, "init: This is k " + k);
            labels.add(selectedLabels.get(k));
            selectedIndex.add(i + k);
        }
        labels.add("+");
        labelsView.setLabels(labels); //给labelView设置字符串数组。
        labelsView.setSelects(selectedIndex);
        initTimerPicker1();
        initTimerPicker2();
        initTimerPicker3();
        initTimerPicker4();
        //初始化quote
        if(selectedQuotes.size() != 0) {
            choseQuote.setText("已选择");
        }
        //初始化时间选择
        Log.d(TAG, "init: the weekstring is " + weekString2[Integer.valueOf(myTodo.getStartTime().substring(0, 1))]);
        mTvSelectedTimeWeek.setText(weekString2[Integer.valueOf(myTodo.getStartTime().substring(0, 1))]);
        if (myTodo.getStartTime().length() >= 3) {
            try {
                startDate = new SimpleDateFormat("HH:mm", Locale.CHINA).parse(myTodo.getStartTime());
                endDate = new SimpleDateFormat("HH:mm", Locale.CHINA).parse(myTodo.getEndTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeType.setChecked(true);
            Log.d(TAG, "init: This is the startTime: " + myTodo.getStartTime());
            Log.d(TAG, "init: This is the endTime: " + myTodo.getEndTime());
            mTvSelectedTime1.setText(myTodo.getStartTime().substring(2));
            mTvSelectedTime2.setText(myTodo.getEndTime().substring(2));
            timeTypeFlag = true;
            timeLength.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            end.setVisibility(View.VISIBLE);
        } else {
            timeType.setChecked(false);
            Log.d(TAG, "init: this is saved length " + myTodo.getLength());
            mTvSelectedLength.setText(myTodo.getLength());
            timeTypeFlag = false;
            timeLength.setVisibility(View.VISIBLE);
            start.setVisibility(View.GONE);
            end.setVisibility(View.GONE);
        }
        endDate = new Date();
        //初始化quote选择界面
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time_in_todo:
                // 日期格式为yyyy-MM-dd
                mTimePicker1.show(mTvSelectedTime1.getText().toString());
                break;

            case R.id.end_time_in_todo:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimePicker2.show(mTvSelectedTime2.getText().toString());
                break;

            case R.id.week_time_in_todo:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimePickerWeek.show(mTvSelectedTimeWeek.getText().toString());
                break;

            case R.id.todo_length:
                // 日期格式为yyyy-MM-dd HH:mm
                mLengthPicker.show(mTvSelectedLength.getText().toString());
        }

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
                startDateModified = true;
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

    private void initTimerPicker4() { //时长选择器
        String beginTime = df.format(new Date());
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), 1);
        String endTimeShow = new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mLengthPicker = new CustomDatePicker(this.getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String time = getTime(DateFormatUtils.long2Str(timestamp, 3));
                mTvSelectedLength.setText(time);
                try {
                    lengthDate = new SimpleDateFormat("HH:mm", Locale.CHINA)
                            .parse(DateFormatUtils.long2Str(timestamp, 3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "2019-08-12 00:00", endTime, 2);
        // 允许点击屏幕或物理返回键关闭
        mLengthPicker.setCancelable(true);
        // 显示时和分
        mLengthPicker.setCanShowYearAndDay(false);
        mLengthPicker.setCanShowPreciseTime(true);

        // 允许循环滚动
        mLengthPicker.setScrollLoop(true);
        // 允许滚动动画
        mLengthPicker.setCanShowAnim(true);

        //boolean k = mTimePicker.setSelectedTime(1575820800, true);
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
