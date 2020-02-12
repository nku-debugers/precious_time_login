package comv.example.zyrmj.precious_time01.fragments.plan;


import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.activities.PersonCenterActivity;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;
import comv.example.zyrmj.precious_time01.repository.TodoRepository;
import comv.example.zyrmj.weekviewlibrary.DateTimeInterpreter;
import comv.example.zyrmj.weekviewlibrary.WeekView;
import comv.example.zyrmj.weekviewlibrary.WeekViewEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanWeekView extends Fragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EmptyViewClickListener,
        WeekView.EmptyViewLongPressListener, WeekView.ScrollListener {
    private WeekView mWeekView;
    private TextView plan_name;
    private ImageView clock, plan, personcenter;
    private FloatingActionButton toPlanshow, addTodo;
    private Switch canUpdate, isWeekView;
    int modify=0; //0表示不可修改 1便是可修改
    private String userId = "offline";
    private Plan showedPlan;
    List<Todo> todos;


    public PlanWeekView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final_plan_week_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.getStringExtra("userId") != null) {
            userId = intent.getStringExtra("userId");
        }
        showedPlan = choosePlan();
        assignViews();
        enableButtons();
        TextView Name = getView().findViewById(R.id.planName);
        Name.setText(showedPlan.getPlanName());


    }

    private void assignViews() {
        plan_name = getView().findViewById(R.id.planName);
        if (showedPlan != null) plan_name.setText(showedPlan.getPlanName());
        mWeekView = getView().findViewById(R.id.weekview);
        mWeekView.setNumberOfVisibleDays(7);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setScrollListener(this);
        setupDateTimeInterpreter();
        toPlanshow = getView().findViewById(R.id.toPlanShow);
        addTodo=getView().findViewById(R.id.add);
        if(modify==0)
        addTodo.hide();
        clock = getView().findViewById(R.id.clock);
        plan = getView().findViewById(R.id.plan);
        personcenter = getView().findViewById(R.id.personcenter);
        canUpdate = getView().findViewById(R.id.Is_modify);
        if(modify==1)
            canUpdate.setChecked(true);
        isWeekView = getView().findViewById(R.id.week_switch);

    }

    private void enableButtons() {
        toPlanshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("plan", showedPlan);
                bundle.putString("userId", userId);
                bundle.putString("weekView", "ture");
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_planWeekView_to_planShow, bundle);

            }
        });

        //添加todo,跳转到添加页面
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传递当前plan
                Bundle bundle=new Bundle();
                bundle.putSerializable("plan",showedPlan);
                bundle.putString("userId",userId);


            }
        });

        personcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("userId", userId);
                intent.setClass(getContext(), PersonCenterActivity.class);
                startActivity(intent);
            }
        });
        //是否是更改模式
        canUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //开启更改模式
                if (b == true) {
                    addTodo.show();
                    modify=1;
                }
                //关闭更改模式，隐藏+按钮
                else {
                    addTodo.hide();
                    modify=0;
                }
            }
        });

        //切换视图
        isWeekView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) //周视图
                {

                } else //列表视图
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("plan", showedPlan);
                    bundle.putString("userId", userId);
                    bundle.putInt("modify",modify);
                    System.out.println("modify:"+modify);
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_planWeekView_to_planTodosListView, bundle);
                }


            }
        });


    }

    private Plan choosePlan() {
        List<Plan> plans = new PlanRepository(getContext()).getAllPlans(userId);
        if (plans == null || plans.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_planWeekView_to_choseTemplate, bundle);
        } else {
            if (getArguments() != null && getArguments().getSerializable("plan") != null) {
                Plan plan = (Plan) getArguments().getSerializable("plan");
                modify=getArguments().getInt("modify");
                return plan;
            } else {
                System.out.println("plans result");
                Date currDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currDateString = sdf.format(currDate);
                Plan chosenPlan = null;
                int diff = Integer.MAX_VALUE;
                for (Plan plan : plans) {
                    System.out.println(plan.getPlanName());
                    System.out.println(plan.getStartDate());
                    System.out.println(plan.getEndDate());
                    int distance = TimeDiff.daysBetween(plan.getStartDate(), currDateString);
                    if (distance < diff) {
                        diff = distance;
                        chosenPlan = plan;
                    }

                }
                System.out.println("final plan");
                System.out.println(chosenPlan.getPlanName());
                System.out.println(chosenPlan.getStartDate());
                System.out.println(chosenPlan.getEndDate());
                return chosenPlan;
            }
        }
        return new Plan();
    }


    private void setupDateTimeInterpreter(/*final boolean shortDate*/) {
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
                int offset = date % 7;
                //计算endDate
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date start = null;
                try {
                    start = formatter.parse(showedPlan.getStartDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(start);
                //增加天数
                cal.add(Calendar.DAY_OF_MONTH, offset);
                Date end = cal.getTime();
                SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd");
                String currdate = formatter2.format(end);
                return currdate;

            }
        });
    }

    //点击event,如果是更改模式，则跳转到更新页面，否则弹出提示框，询问是否进入管控活动
    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        int index=event.getIndex();
        Todo todo=todos.get(index);

        //倒计时
        if(modify==0)
        {

        }
        //更新todo，向更新页面传递plan name ,userId,需更新的todo
        else
        {


        }

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        return todosToEvents(newYear,newMonth);

    }

    private ArrayList<WeekViewEvent> todosToEvents(int newYear,int newMonth)
    {
        ArrayList<WeekViewEvent> events=new ArrayList<>();
        todos=new TodoRepository(getContext()).getListTodoByPlanDate(userId,showedPlan.getStartDate());
        System.out.println("todos size: "+todos.size());
        int i=1;
        int index=0;
        for (Todo todo : todos) {
            String weekday = todo.getStartTime().split("-")[0];
            int diff = Integer.valueOf(weekday);//与周一的距离
            String starttime = todo.getStartTime().split("-")[1];
            String starthour = starttime.split(":")[0];
            String startminute = starttime.split(":")[1];
            String endtime = todo.getEndTime().split("-")[1];
            String endminute = endtime.split(":")[1];
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
            WeekViewEvent event = new WeekViewEvent(i, todo.getName(), startTime, endTime, index);
            if(todo.getType()==0 )
            {
                event.setColor(getResources().getColor(R.color.event_color_05));
            }

            else
            {
                if (i % 4 == 0)
                    event.setColor(getResources().getColor(R.color.event_color_01));
                else if (i % 4 == 1)
                    event.setColor(getResources().getColor(R.color.event_color_02));
                else if (i % 4 == 2)
                    event.setColor(getResources().getColor(R.color.event_color_03));
                else
                    event.setColor(getResources().getColor(R.color.event_color_04));
            }
            index += 1;
            events.add(event);
            i++;
        }
        return events;
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
