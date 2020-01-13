package comv.example.zyrmj.precious_time01.fragments.plan;


import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.HabitAdapter;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.weekviewlibrary.DateTimeInterpreter;
import comv.example.zyrmj.weekviewlibrary.WeekView;
import comv.example.zyrmj.weekviewlibrary.WeekViewEvent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ddz.floatingactionbutton.FloatingActionMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPlan extends Fragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EmptyViewClickListener,
        WeekView.EmptyViewLongPressListener, WeekView.ScrollListener {
    private Button confirm;
    private FloatingActionMenu fl_menu;
    private com.ddz.floatingactionbutton.FloatingActionButton addHabit, addToDo, habitList, toDoList;
    private WeekView mWeekView;
    private ImageView returnImge;
    private List<WeekViewEvent> events;
    private List<TemplateItem> datas;
    private String userId = "offline";
    private String templateName;
    private ArrayList<Habit> selectedHabits = new ArrayList<>();
    private ArrayList<Todo> addedToDos = new ArrayList<>();
    private ArrayList<ArrayList<IdleTime>> idleTimes;
    private ArrayList<ToDoExtend> toDoExtends = new ArrayList<>();
    private static final String START = "8:00";
    private static final String END = "23:00";

    private class IdleTime implements Serializable
            //3个string:startTime,endTime,length
    {
        private String startTime, endTime, length;

        public IdleTime(String startTime, String endTime) {
            if (Integer.valueOf(startTime.split(":")[0]) < 10 && !startTime.split(":")[0].contains("0"))
                startTime = "0" + startTime;
            if (Integer.valueOf(endTime.split(":")[0]) < 10 && !endTime.split(":")[0].contains("0"))
                endTime = "0" + endTime;
            this.startTime = startTime;
            this.endTime = endTime;
            this.length = TimeDiff.dateDiff(startTime, endTime, "HH:mm");
        }

    }

    private class ToDoExtend implements Serializable {
        private Todo todo;
        private ArrayList<String> labels ; //类别
        private ArrayList<Quote> quotes ; //箴言
        private Integer flag=0;//是否安排完时间？

        public ToDoExtend(Todo todo, ArrayList<String> labels, ArrayList<Quote> quotes) {
            this.todo = todo;
            this.labels = labels;
            this.quotes = quotes;
            if(todo.getStartTime().contains(":"))
                flag=1;
        }

        @NonNull
        @Override
        public String toString() {
            String result;
            result=todo.getName()+" "+todo.getStartTime();
            if(quotes!=null&&labels!=null)
                result+="lable num: "+labels.size()+"quotes num: "+quotes.size();
            return result;
        }

        public ToDoExtend() {
        }

        public Todo getTodo() {
            return todo;
        }

        public void setTodo(Todo todo) {
            this.todo = todo;
        }

        public ArrayList<String> getLabels() {
            return labels;
        }

        public void setLabels(ArrayList<String> labels) {
            this.labels = labels;
        }

        public ArrayList<Quote> getQuotes() {
            return quotes;
        }

        public void setQuotes(ArrayList<Quote> quotes) {
            this.quotes = quotes;
        }
    }


    public EditPlan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_plan, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dealReceivedData();
        assignViews();
        enableBackButton();
    }

    private void dealReceivedData() {
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "offline");
            templateName = getArguments().getString("templateName", "");
            datas = new TemplateItemRepository(getContext()).getSpecificList(templateName, userId);
            if (datas == null) datas = new ArrayList<>();
            Todo todo = (Todo) getArguments().getSerializable("mytodo");
            String delete=getArguments().getString("delete","");
            if (todo != null||!delete.equals("")) {
                selectedHabits = (ArrayList<Habit>) getArguments().getSerializable("habits");
                idleTimes = (ArrayList<ArrayList<IdleTime>>) getArguments().getSerializable("idleTimes");
                System.out.println("idleTimes size"+idleTimes.size());
                toDoExtends = (ArrayList<ToDoExtend>) getArguments().getSerializable("toDoExtends");
                addedToDos = (ArrayList<Todo>) getArguments().getSerializable("toDos");
               if(todo!=null) {
                   ArrayList<Quote> quotes = (ArrayList<Quote>) getArguments().getSerializable("quotes");
                   ArrayList<String> labels = (ArrayList<String>) getArguments().getSerializable("labels");
                   addedToDos.add(todo);
                   if (todo.getStartTime().contains(":")) //todo有具体时间需update空闲时间
                   {
                       updateIdleTimes1(todo.getStartTime(), todo.getEndTime());
                   }

                   ToDoExtend toDoExtend = new ToDoExtend();
                   toDoExtend.setTodo(todo);
                   if (quotes != null)
                       toDoExtend.setQuotes(quotes);
                   if (labels != null)
                       toDoExtend.setLabels(labels);
                   toDoExtends.add(toDoExtend);
               }
            } else {
                System.out.println("init data");
                initDatas();
            }
        }
    }

    private void initDatas() {
        int i = 0;
        idleTimes=new ArrayList<>();
        for (i = 0; i <= 6; i++) {
            idleTimes.add(new ArrayList<>());
            idleTimes.get(i).add(new IdleTime(START, END));
        }
        datas = new TemplateItemRepository(getContext()).getSpecificList(templateName, userId);
        if (datas == null) datas = new ArrayList<>();
        templateItems2ToDo(datas);

    }


    private void assignViews() {
        confirm = getView().findViewById(R.id.create_plan);
        fl_menu = getView().findViewById(R.id.menu);
        addHabit = getView().findViewById(R.id.addHabit);
        addToDo = getView().findViewById(R.id.addToDo);
        habitList = getView().findViewById(R.id.habitList);
        toDoList = getView().findViewById(R.id.toDoList);
        mWeekView = (WeekView) getView().findViewById(R.id.weekview);
        returnImge = getView().findViewById(R.id.toChoseTemplate);
        returnImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_editPlan_to_choseTemplate);
            }
        });

        mWeekView.setNumberOfVisibleDays(7);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setScrollListener(this);
        WeekView.EmptyViewClickListener emptyViewClickListener = new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Toast.makeText(getContext(), time.getTime().toString(), Toast.LENGTH_LONG).show();
            }
        };
        mWeekView.setEmptyViewClickListener(emptyViewClickListener);
        setupDateTimeInterpreter();

    }

    private void setupDateTimeInterpreter(/*final boolean shortDate*/) {
        final String[] weekLabels = {"一", "二", "三", "四", "五", "六", "日"};
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                return null;

            }

            @Override
            public String interpretTime(int hour) {
                return String.format("%02d:00", hour);

            }

            @Override
            public String interpretWeek(int date) {
                return weekLabels[date % 7];

            }
        });
    }

    private void enableBackButton() {
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_editPlan_to_choseTemplate);
                    return true;
                }
                return false;
            }
        });

        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Habit> habits = new HabitRepository(getContext()).getAllHabits2(userId);
                Log.d("userId", userId);
                Log.d("habits", String.valueOf(habits == null));
                if (habits == null) habits = new ArrayList<>();
                final HabitAdapter habitAdapter = new HabitAdapter(habits);
                new MaterialDialog.Builder(getContext())
                        .autoDismiss(false)
                        .title("选择习惯")
                        .adapter(habitAdapter, new LinearLayoutManager(getContext()))
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                List<Habit> currentSelectedHabits = habitAdapter.getSelectedHabits();
                                if (currentSelectedHabits == null || currentSelectedHabits.size() == 0) {
                                    Toast.makeText(getContext(), "未选择习惯", Toast.LENGTH_LONG);
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    for (Habit habit : currentSelectedHabits) {
                                        for (Habit h : selectedHabits) {
                                            if (h.getName() == habit.getName()) break;
                                        }
                                        selectedHabits.add(habit);
                                    }

                                }
                                for (Habit h : selectedHabits) {
                                    Log.d("habit", h.getName());
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();

                            }
                        })
                        .show();

            }
        });

        addToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("templateName", templateName);
                bundle.putString("userId", userId);
                bundle.putSerializable("toDos", addedToDos);
                bundle.putSerializable("habits", selectedHabits);
                bundle.putSerializable("idleTimes", idleTimes);
                bundle.putString("option","add");
                System.out.println("idleTimes size"+idleTimes.size());
                bundle.putSerializable("toDoExtends", toDoExtends);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_editPlan_to_addToDo2, bundle);
            }
        });

        habitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = selectedHabits.size();
                String[] items = new String[size];
                int index = 0;
                for (Habit habit : selectedHabits) {
                    items[index] = habit.getName();
                    index++;
                }
                new MaterialDialog.Builder(getContext())
                        .title("已选择习惯")// 标题
                        .items(items)// 列表数据
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                            }
                        })
                        .show();// 显示对话框
            }
        });

        toDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = 0;
                for (ToDoExtend toDoExtend : toDoExtends) {
                    if (toDoExtend.todo.getType() == 2) size++;
                }
                String[] items = new String[size];
                int[] positions=new int[size];
                int index = 0;
                String[] weekdays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                for (int i=0;i<toDoExtends.size();i++) {
                    ToDoExtend toDoExtend=toDoExtends.get(i);
                    if (toDoExtend.todo.getType() == 2) {
                        items[index] = toDoExtend.todo.getName() + "    " +
                                weekdays[Integer.valueOf(toDoExtend.todo.getStartTime().split("-")[0])]
                                + "    ";

                        if (!(toDoExtend.todo.getStartTime().split("-").length == 1))
                            items[index] += ("具体时间：" + toDoExtend.todo.getStartTime().split("-")[1]
                                    + "-" + toDoExtend.todo.getEndTime().split("-")[1]
                            );
                        else
                            items[index] += ("   时长： " + toDoExtend.todo.getLength());
                        positions[index]=i;
                        index++;
                        System.out.println("todoextend");
                        System.out.println(toDoExtend.getQuotes().size());
                        System.out.println(toDoExtend.getLabels().size());
                    }
                }

                new MaterialDialog.Builder(getContext())
                        .title("已添加事项")// 标题
                        .items(items)// 列表数据
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                int index=positions[position];
                                ToDoExtend toDoExtend=toDoExtends.get(index);
                                toDoExtends.remove(toDoExtend);
                                addedToDos.remove(toDoExtend.todo);
                                if(toDoExtend.todo.getStartTime().contains(":"))
                                {updateIdleTimes2(toDoExtend.todo.getStartTime(),toDoExtend.todo.getEndTime());}
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("mytodo", toDoExtend.getTodo());
                                bundle.putSerializable("selectedlabels", toDoExtend.getLabels());
                                bundle.putSerializable("selectedquotes", toDoExtend.getQuotes());
                                bundle.putString("userId", userId);
                                bundle.putString("templateName", templateName);
                                bundle.putSerializable("habits",selectedHabits);
                                bundle.putSerializable("idleTimes",idleTimes);
                                System.out.println("idleTimes size"+idleTimes.size());
                                bundle.putSerializable("toDoExtends",toDoExtends);
                                bundle.putSerializable("toDos",addedToDos);
                                bundle.putString("option","update");
                                System.out.println("update todo");
                                System.out.println(toDoExtend);
                                System.out.println(toDoExtends.size());
                                System.out.println(addedToDos.size());
                                NavController controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_editPlan_to_updateTodo,bundle);
                            }
                        })
                        .show();// 显示对话框
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //生成最终计划
                //添加计划(是否在此添加？)
//                showDialog("", new PlanRepository(getContext()));
                System.out.println(toDoExtends);
                //计划最终包含的todo,每个todo的信息均应是完整的
                ArrayList<ToDoExtend> finalToDos=new ArrayList<>();
                for(ToDoExtend toDoExtend:toDoExtends)
                {
                    Todo todo=toDoExtend.todo;
                    if(todo.getType()==0)  //templateItem可直接添加
                    {
                        finalToDos.add(toDoExtend);
                    }
                    else if(todo.getType()==2)//用户自己的toDo,须区分两种类型
                    {
                        if(todo.getStartTime().contains(":")) //可直接添加
                        {
                            finalToDos.add(toDoExtend);
                        }
                        else //须进行时间安排
                        {
                            List<IdleTime> allIdleTimes=findAllIdleTimes(todo.getStartTime().split("-")[0],todo.getLength());
                            todo=arrangeTimeForUserTodo(todo,allIdleTimes);
                            toDoExtend.todo=todo;
                            finalToDos.add(toDoExtend);

                        }
                    }

                    //安排习惯
                    for(Habit habit:selectedHabits)
                    {
                        List<ToDoExtend> toDoExtends=arrangeTimeForHabit(habit);
                    }

                }
                int index=0;
                System.out.println("final result");
                for (ArrayList<IdleTime> list : idleTimes) {
                    System.out.println(index + ":");
                    for (IdleTime idleTime : list) {
                        System.out.println(idleTime.startTime + " " + idleTime.endTime + " " + idleTime.length);
                    }
                    index++;
                }


            }
        });
    }
//以下两函数供生成真正的plan时使用
    public void showDialog(String info, final PlanRepository planRepository) {
        new MaterialDialog.Builder(getContext())
                .title("添加新计划")
                .content(info)
                .inputType(InputType.TYPE_CLASS_TEXT)
                //前2个一个是hint一个是预输入的文字
                .input("计划名称", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                //判断开始日期是否为空
                                if (input.toString().equals("")) {
                                    dialog.setContent("开始日期不能为空，请重新输入！");
                                } else {
                                    String planName = input.toString();
                                    dialog.dismiss();
                                    showDialog2(planName, planRepository);

                                }
                            }
                        }

                )

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .autoDismiss(false).show();
    }

    public void showDialog2(String info, final PlanRepository planRepository) {
        new MaterialDialog.Builder(getContext())
                .title("添加新计划")
                .content(info)
                .inputType(InputType.TYPE_CLASS_TEXT)
                //前2个一个是hint一个是预输入的文字
                .input("开始日期", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                //判断计划名是否为空
                                if (input.toString().equals("")) {
                                    dialog.setContent("开始日期不能为空，请重新输入！");
                                } else {
                                    Plan plan = new Plan();
                                    plan.startDate = input.toString();

                                    plan.planName = info;
                                    //计算endDate
                                    Calendar cal = Calendar.getInstance();
                                    String splieTimes[] = plan.startDate.split("/");
                                    Date start = new Date((Integer.valueOf(splieTimes[0]) - 1900),
                                            (Integer.valueOf(splieTimes[1]) - 1), (Integer.valueOf(splieTimes[2])));
                                    cal.setTime(start);
                                    //增加6天
                                    cal.add(Calendar.DAY_OF_MONTH, 6);
                                    //Calendar转为Date类型
                                    Date end = cal.getTime();
                                    //将增加后的日期转为字符串
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    plan.startDate = formatter.format(start);
                                    plan.endDate = formatter.format(end);
                                    int flag = 0;
                                    List<Plan> plans = planRepository.getAllPlans(userId);
                                    for (Plan p : plans) {
                                        if (p.startDate.equals(plan.startDate)) {
                                            flag = 1;
                                            break;
                                        }

                                    }
                                    if (flag == 0) {
                                        planRepository.insertPlan(plan);
                                        dialog.dismiss();
                                    } else {

                                        Log.i("dialog", "存在相同开始时间的计划");
                                        dialog.setContent("存在相同开始时间的计划，请重新输入！");
                                    }
                                }
                            }
                        }

                )

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .autoDismiss(false).show();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        int index = event.getIndex();
        TemplateItem templateItem = datas.get(index);
        Bundle bundle = new Bundle();
        bundle.putSerializable("templateItem", templateItem);
        bundle.putString("viewOption", "0");
//        NavController controller = Navigation.findNavController(getView());
//        controller.navigate(R.id.action_testWeekView_to_updateTemplateItemFragment, bundle);


    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        datas = new TemplateItemRepository(getContext()).getSpecificList(templateName, userId);
        if (datas == null) datas = new ArrayList<>();
        events = new ArrayList<WeekViewEvent>();
        int i = 1;
        int index = 0;
        for (TemplateItem ti : datas) {
            Log.d("列表2", ti.getItemName());
            String weekday = ti.getStartTime().split("-")[0];
            int diff = Integer.valueOf(weekday);//与周一的距离
            String starttime = ti.getStartTime().split("-")[1];
            Log.d("列表2", starttime);
            String starthour = starttime.split(":")[0];
            String startminute = starttime.split(":")[1];
            String endtime = ti.getEndTime().split("-")[1];
            String endminute = endtime.split(":")[1];
            Log.d("列表2", starthour);
            String endhour = endtime.split(":")[0];
            Calendar startTime = Calendar.getInstance();
            int currdiff = startTime.get(Calendar.DAY_OF_WEEK) - 2;
            if (currdiff < 0) currdiff = 6;
            int distance = diff - currdiff;
            startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) + distance);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starthour));
            startTime.set(Calendar.MINUTE, Integer.valueOf(startminute));
            Calendar endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endhour));
            endTime.set(Calendar.MINUTE, Integer.valueOf(endminute));
            WeekViewEvent event = new WeekViewEvent(i, ti.getItemName(), startTime, endTime, index);
            event.setColor(getResources().getColor(R.color.event_color_05)); //template中事项统一颜色
            index += 1;
            events.add(event);
            i++;
        }
        return events;
    }

    //将templateItem转换成toDo
    public void templateItems2ToDo(List<TemplateItem> templateItems) {
        for (TemplateItem ti : templateItems) {
            Todo todo = new Todo();
            todo.setName(ti.getItemName());
            todo.setStartTime(ti.getStartTime());
            todo.setEndTime(ti.getEndTime());
            updateIdleTimes1(ti.getStartTime(), ti.getEndTime());
            todo.setLength(TimeDiff.dateDiff(ti.getStartTime().split("-")[1], ti.getEndTime().split("-")[1], "HH:mm"));
            todo.setType(0);//0表明是templateItem转化而来
            todo.setReminder(0);
            addedToDos.add(todo);
            ToDoExtend toDoExtend=new ToDoExtend();
            toDoExtend.setTodo(todo);
            toDoExtend.setQuotes(null);
            toDoExtend.setLabels(null);
            toDoExtends.add(toDoExtend);

        }
        System.out.println("todoslength");
        System.out.println(addedToDos.size());
        System.out.println("idleTimes");
        int index = 0;
        for (ArrayList<IdleTime> list : idleTimes) {
            System.out.println(index + ":");
            for (IdleTime idleTime : list) {
                System.out.println(idleTime.startTime + " " + idleTime.endTime + " " + idleTime.length);
            }
            index++;
        }


    }

    //所有其他todo安排完后调用
    // 将习惯列表中各个元素转化为toDo:此时的toDo没有具体时间，只有相应时长
    public void habit2ToDo() {
        for (Habit habit : selectedHabits) {
            Todo todo = new Todo();
            todo.setType(1);//1表示习惯
            todo.setUserId(habit.userId);
            todo.setName(habit.getName());
            todo.setReminder(habit.getReminder());
            if (habit.getTime4once() != null)
                todo.setLength(habit.getTime4once());
                //没有设定每次时长，怎么安排
            else {

            }
            //根据剩下的空余时间，habit的priority,expectedTime等安排具体时间


        }


    }

    //每添加一个新的todo便更新空闲时间列表
    public void updateIdleTimes1(String startTime, String endTime) {
        int index = Integer.valueOf(startTime.split("-")[0]);//确定weekday 0-Mon ...
        startTime = startTime.split("-")[1];
        endTime = endTime.split("-")[1];
        ArrayList<IdleTime> idleTimesList = idleTimes.get(index);
        for (IdleTime idleTime : idleTimesList) {
            //startTime>=空闲时间start  endTime<= 空闲时间end
            if (((TimeDiff.compare(startTime, idleTime.startTime) >= 0) || TimeDiff.compare(START, startTime) > 0)
                    && ((TimeDiff.compare(endTime, idleTime.endTime) <= 0) || TimeDiff.compare(END, endTime) < 0)) {
                idleTimesList.remove(idleTime);
                //拆分空闲时间
                if (TimeDiff.compare(startTime, idleTime.startTime) > 0) {
                    idleTimesList.add(new IdleTime(idleTime.startTime, startTime));
                }
                if (TimeDiff.compare(endTime, idleTime.endTime) < 0) {
                    idleTimesList.add(new IdleTime(endTime, idleTime.endTime));
                }

            }

        }
//        Iterator<IdleTime> iterator = idleTimesList.iterator();
//        //删除所有时长为10分钟的空闲段
//        while (iterator.hasNext()) {
//            if (TimeDiff.compare(iterator.next().length, "0:10") == 0)
//                iterator.remove();
//        }

    }

    //每删除一个todo便更新空闲时间列表
    public void updateIdleTimes2(String startTime, String endTime) {
        String weekDay=startTime.split("-")[0];
        int index = Integer.valueOf(weekDay);//确定weekday 0-Mon ...
        startTime = startTime.split("-")[1];
        endTime = endTime.split("-")[1];
        ArrayList<IdleTime> idleTimesList = idleTimes.get(index);
        IdleTime theIdleTime=null;
        int flag=0;
        for (IdleTime idleTime : idleTimesList)
        {
            //startTime==空闲时间end  endTime== 空闲时间start,需要进行合并
            if (TimeDiff.compare(startTime, idleTime.endTime) == 0) {
                idleTimesList.remove(idleTime);
                theIdleTime=new IdleTime(idleTime.startTime,endTime);
                idleTimesList.add(theIdleTime);
                startTime=idleTime.startTime;
                flag=1;
                break;
            }
            else if(TimeDiff.compare(endTime,idleTime.startTime)==0)
            {
                idleTimesList.remove(idleTime);
                theIdleTime=new IdleTime(startTime,idleTime.endTime);
                idleTimesList.add(theIdleTime);
                endTime=idleTime.endTime;
                flag=1;
                break;
            }

        }
        //不需要合并，直接添加
        if(flag==0)
        {
            idleTimesList.add(new IdleTime(startTime,endTime));
        }
        //进一步整理
        else
        {
            updateIdleTimes3(weekDay,theIdleTime);
        }

    }
    //
    public void updateIdleTimes3(String weekDay,IdleTime theIdleTime) {
        int index = Integer.valueOf(weekDay);//确定weekday 0-Mon ...
        String startTime = theIdleTime.startTime;
        String endTime = theIdleTime.endTime;
        ArrayList<IdleTime> idleTimesList = idleTimes.get(index);
        IdleTime newIdleTime=null;
        for (IdleTime idleTime : idleTimesList)
        {
            //startTime==空闲时间end  endTime== 空闲时间start,需要进行合并
            if (TimeDiff.compare(startTime, idleTime.endTime) == 0) {
                idleTimesList.remove(theIdleTime);
                idleTimesList.remove(idleTime);
                newIdleTime=new IdleTime(idleTime.startTime,endTime);
                idleTimesList.add(newIdleTime);
                break;
            }
            else if(TimeDiff.compare(endTime,idleTime.startTime)==0)
            {
                idleTimesList.remove(theIdleTime);
                idleTimesList.remove(idleTime);
                newIdleTime=new IdleTime(startTime,idleTime.endTime);
                idleTimesList.add(newIdleTime);
                break;
            }

        }
        if(newIdleTime!=null) updateIdleTimes3(weekDay,newIdleTime);

    }



    //根据时长找到适合的所有空闲时间
    public List<IdleTime> findAllIdleTimes(String weekday, String length) {
        List<IdleTime> idleTimeList=idleTimes.get(Integer.valueOf(weekday));
        List<IdleTime> theIdleTimes=new ArrayList<>();
        for(IdleTime idleTime:idleTimeList)
        {
            if (TimeDiff.compare(idleTime.length,length)>0)
            {
                theIdleTimes.add(idleTime);
            }
        }
        return theIdleTimes;
    }

    //根据不同偏好选择最佳的空闲时间
    public Todo arrangeTimeForUserTodo(Todo todo,List<IdleTime> idleTimes) {
        IdleTime chosenIdleTime=null;
        //worst fit 选择时长最大的空闲时间段
        for (IdleTime idleTime:idleTimes)


        updateToDo(todo,chosenIdleTime);
        return todo;
    }

    public List<ToDoExtend> arrangeTimeForHabit(Habit habit)
    {
        return null;
    }

    //找到空闲时间后，给todo设置具体时间，并更新空闲时间
    public Todo updateToDo(Todo todo, IdleTime idleTime) {
        //
        return todo;
    }


    @Override
    public void onEmptyViewClicked(Calendar time) {

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {

    }
}
