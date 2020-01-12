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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class EditPlan extends Fragment  implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EmptyViewClickListener,
        WeekView.EmptyViewLongPressListener, WeekView.ScrollListener{
    private Button confirm;
    private FloatingActionMenu fl_menu;
    private com.ddz.floatingactionbutton.FloatingActionButton addHabit,addToDo,habitList,toDoList;
    private WeekView mWeekView;
    private ImageView returnImge;
    private List<WeekViewEvent> events;
    private List<TemplateItem> datas;
    private String userId="offline";
    private String templateName;
    private List<Habit> selectedHabits=new ArrayList<>();
    private List<Todo> addedToDos=new ArrayList<>();
    private ArrayList<ArrayList<IdleTime>> idleTimes=new ArrayList<>();

    private class IdleTime
            //3个string:startTime,endTime,length
    {
        private String startTime,endTime,length;

        public IdleTime(String startTime, String endTime) {
            if (Integer.valueOf(startTime.split(":")[0])<10&&!startTime.split(":")[0].contains("0"))
                startTime="0"+startTime;
            if (Integer.valueOf(endTime.split(":")[0])<10&&!endTime.split(":")[0].contains("0"))
                endTime="0"+endTime;
            this.startTime = startTime;
            this.endTime = endTime;
            this.length = TimeDiff.dateDiff(startTime,endTime,"HH:mm");
        }

        //对两个IdleTime进行比较看是否冲突
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
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "offline");
            templateName = getArguments().getString("templateName", "");
        }
        assignViews();
        initEdleTime();
        enableBackButton();

    }
    private void initEdleTime()
    {
        int i=0;
        for ( i=0;i<=6;i++)
        { idleTimes.add(new ArrayList<>());
            idleTimes.get(i).add(new IdleTime("8:00","23:00"));}

    }


    private void assignViews() {
        confirm=getView().findViewById(R.id.create_plan);
        fl_menu = getView().findViewById(R.id.menu);
        addHabit=getView().findViewById(R.id.addHabit);
        addToDo=getView().findViewById(R.id.addToDo);
        habitList=getView().findViewById(R.id.habitList);
        toDoList=getView().findViewById(R.id.toDoList);
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
                List<Habit> habits=new HabitRepository(getContext()).getAllHabits2(userId);
                Log.d("userId",userId);
                Log.d("habits",String.valueOf(habits==null));
                if (habits==null) habits=new ArrayList<>();
                final HabitAdapter habitAdapter=new HabitAdapter(habits);
                new MaterialDialog.Builder(getContext())
                        .autoDismiss(false)
                        .title("选择习惯")
                        .adapter(habitAdapter,new LinearLayoutManager(getContext()))
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                List<Habit> currentSelectedHabits=habitAdapter.getSelectedHabits();
                                if(currentSelectedHabits==null||currentSelectedHabits.size()==0)
                                {
                                    Toast.makeText(getContext(),"未选择习惯",Toast.LENGTH_LONG);
                                    dialog.dismiss();
                                }
                                else
                                {
                                    dialog.dismiss();
                                    for(Habit habit:currentSelectedHabits)
                                    {
                                       for (Habit h :selectedHabits)
                                       {
                                           if (h.getName()==habit.getName()) break;
                                       }
                                       selectedHabits.add(habit);
                                    }

                                }
                                for (Habit h :selectedHabits)
                                {
                                  Log.d("habit",h.getName());
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
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_editPlan_to_addToDo2);
            }
        });

        habitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size=selectedHabits.size();
                String[] items = new String[size];
                int index=0;
                for (Habit habit :selectedHabits)
                {
                    items[index]=habit.getName();
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
                //int size=addedToDos.size();
                String[] items = new String[3];
                // 根据todo的名称，开始时间，结束时间拼接字符串
                items[0]="test1"+"  "+"8:00-9:00";
                items[1]="test2"+"  "+"9:00-10:00";
                items[2]="test3"+"  "+"10:00-11:00";
                //int index=0;
                new MaterialDialog.Builder(getContext())
                        .title("已添加事项")// 标题
                        .items(items)// 列表数据
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            //修改用户自己添加的todo
                                NavController controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_editPlan_to_addToDo2);
                            }
                        })
                        .show();// 显示对话框
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //生成最终计划
                //添加计划
                showDialog("",new PlanRepository(getContext()));


            }
        });
    }

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
                          showDialog2(planName,planRepository);

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
                                   Plan plan=new Plan();
                                   plan.startDate=input.toString();

                                   plan.planName=info;
                                   //计算endDate
                                    Calendar cal = Calendar.getInstance();
                                    String splieTimes[]=plan.startDate.split("/");
                                    Date start=new Date((Integer.valueOf(splieTimes[0])-1900),
                                            (Integer.valueOf(splieTimes[1])-1),(Integer.valueOf(splieTimes[2])));
                                    cal.setTime(start);
                                        //增加6天
                                    cal.add(Calendar.DAY_OF_MONTH, 6);
                                    //Calendar转为Date类型
                                    Date end=cal.getTime();
                                //将增加后的日期转为字符串
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                   plan.startDate = formatter.format(start);
                                   plan.endDate=formatter.format(end);
                                    int flag=0;
                                    List<Plan> plans=planRepository.getAllPlans(userId);
                                    for (Plan p :plans)
                                    {
                                        if(p.startDate.equals(plan.startDate))
                                        {
                                            flag=1;
                                            break;
                                        }

                                    }
                                    if(flag==0)
                                    {planRepository.insertPlan(plan);
                                   dialog.dismiss();}

                                    else
                                    {

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
        events = new ArrayList<WeekViewEvent>();
        datas = new TemplateItemRepository(getContext()).getSpecificList(templateName, userId);
        templateItems2ToDo(datas);
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
    public void templateItems2ToDo(List<TemplateItem> templateItems)
    {
        for (TemplateItem ti:templateItems)
        {
            Todo todo=new Todo();
            todo.setName(ti.getItemName());
            todo.setStartTime(ti.getStartTime());
            todo.setEndTime(ti.getEndTime());
            updateiIdleTimes(ti.getStartTime(),ti.getEndTime());
            todo.setLength(TimeDiff.dateDiff(ti.getStartTime().split("-")[1],ti.getEndTime().split("-")[1],"HH:mm"));
            todo.setType(0);//0表明是templateItem转化而来
            addedToDos.add(todo);
            System.out.println("idleTimes");
            int index=0;
            for(ArrayList<IdleTime> list:idleTimes)
            {
                System.out.println(index+":");
                for (IdleTime idleTime:list)
                {
                    System.out.println(idleTime.startTime+" "+idleTime.endTime+" "+idleTime.length);
                }
                index++;
            }
        }


    }

    public void updateiIdleTimes(String startTime, String endTime)
    {
        //每添加一个新的todo便更新空闲时间列表
        int index=Integer.valueOf(startTime.split("-")[0]);//确定weekday 0-Mon ...
        startTime=startTime.split("-")[1];
        endTime=endTime.split("-")[1];
        ArrayList<IdleTime> idleTimesList=idleTimes.get(index);
        for (IdleTime idleTime:idleTimesList)
        {
            //startTime>=空闲时间start  endTime<= 空闲时间end
            if((TimeDiff.compare(startTime,idleTime.startTime)>=0||TimeDiff.compare("8:00",startTime)>0)
                    &&(TimeDiff.compare(endTime,idleTime.endTime)<=0||TimeDiff.compare("23:00",endTime)<0))
            {
                idleTimesList.remove(idleTime);
               //拆分空闲时间
                if(TimeDiff.compare(startTime,idleTime.startTime)>0)
                {
                    idleTimesList.add(new IdleTime(idleTime.startTime,startTime));
                }
                else if(TimeDiff.compare("8:00",startTime)>0)
                {
                    if(TimeDiff.compare(endTime,idleTime.endTime)<0)
                        idleTimesList.add(new IdleTime(endTime,idleTime.endTime));
                }
                else if(TimeDiff.compare(endTime,idleTime.endTime)<0)
                {
                    idleTimesList.add(new IdleTime(endTime,idleTime.endTime));
                }
                else if(TimeDiff.compare("23:00",endTime)<0)
                {
                    if(TimeDiff.compare(startTime,idleTime.startTime)>0)
                    {
                        idleTimesList.add(new IdleTime(idleTime.startTime,startTime));
                    }
                }


            }

        }

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
