package comv.example.zyrmj.precious_time01.fragments.plan;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TodoAdapter;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;
import comv.example.zyrmj.weekviewlibrary.DateTimeInterpreter;
import comv.example.zyrmj.weekviewlibrary.WeekView;
import comv.example.zyrmj.weekviewlibrary.WeekViewEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyPlan extends Fragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EmptyViewClickListener,
        WeekView.EmptyViewLongPressListener, WeekView.ScrollListener {
    private ArrayList<EditPlan.ToDoExtend> satisfiedTodos=new ArrayList<>();
    private ArrayList<EditPlan.ToDoExtend> unsatisfiedTodos=new ArrayList<>();
    private String userId;
    private Button confirm;
    private Switch showList;
    private RecyclerView bottomList;
    private WeekView mWeekView;
    List<WeekViewEvent> weekViewEvents;
    MutableLiveData<List<EditPlan.ToDoExtend>> listdatas;


    public ModifyPlan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.modify_plan, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null)
        {
            userId=getArguments().getString("userId");
            if(getArguments().getSerializable("toDoExtends")!=null)
            {ArrayList<EditPlan.ToDoExtend> toDoExtends= (ArrayList<EditPlan.ToDoExtend>) getArguments().getSerializable("toDoExtends");
            divideTodos(toDoExtends);}
            else
            {
                satisfiedTodos= (ArrayList<EditPlan.ToDoExtend>) getArguments().getSerializable("satisfiedTodos");
                unsatisfiedTodos= (ArrayList<EditPlan.ToDoExtend>) getArguments().getSerializable("unsatisfiedTodos");
            }
        }
        assignViews();
        enableButtons();

    }

    //将接受的todo分类，显示在周视图上的与显示在RecycleView中的
    public void divideTodos(ArrayList<EditPlan.ToDoExtend> toDoExtends)
    {
        for(EditPlan.ToDoExtend toDoExtend:toDoExtends)
        {
            if(toDoExtend.getTodo().getStartTime().contains(":"))
                satisfiedTodos.add(toDoExtend);
            else
                unsatisfiedTodos.add(toDoExtend);
        }
        System.out.println("divide result");
        System.out.println(satisfiedTodos);
        System.out.println(unsatisfiedTodos);

    }


    private void assignViews()
    {
        initList();
        showList=getView().findViewById(R.id.week_modify_switch);
        confirm=getView().findViewById(R.id.week_modify_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("",new PlanRepository(getContext()));


            }
        });
        if(unsatisfiedTodos.size()==0)
        {
           hideList();
        }
        else
        {
            showList();
        }
        mWeekView = getView().findViewById(R.id.weekview);
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

    private void initList()
    {
        bottomList=getView().findViewById(R.id.plan_modify);
        final TodoAdapter todoAdapter=new TodoAdapter(getActivity());
        todoAdapter.setUserId(userId);
        todoAdapter.setUnsatisfiedTodos(unsatisfiedTodos);
        todoAdapter.setSatisfiedTodos(satisfiedTodos);
        todoAdapter.notifyDataSetChanged();
        bottomList.setLayoutManager(new LinearLayoutManager(getContext()));
        bottomList.setAdapter(todoAdapter);

        /*
        MutableLiveData<String> nameEvent = mTestViewModel.getNameEvent();
nameEvent.observe(this, new Observer<String>() {
    @Override
    public void onChanged(@Nullable String s) {
        Log.i(TAG, "onChanged: s = " + s);
        mTvName.setText(s);
    }
});
        */

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final EditPlan.ToDoExtend toDoExtendToDelete = unsatisfiedTodos.get(viewHolder.getAdapterPosition());
               unsatisfiedTodos.remove(toDoExtendToDelete);
               initList();
                Snackbar.make(getView(), "删除了一个待办事项", Snackbar.LENGTH_SHORT).
                        setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       unsatisfiedTodos.add(toDoExtendToDelete);
                                       initList();
                                    }
                                }
                        ).show();

            }
            //在滑动的时候，画出浅灰色背景和垃圾桶图标，增强删除的视觉效果

            Drawable icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete_black_24dp);
            Drawable background = new ColorDrawable(Color.LTGRAY);

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

                int iconLeft, iconRight, iconTop, iconBottom;
                int backTop, backBottom, backLeft, backRight;
                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                iconBottom = iconTop + icon.getIntrinsicHeight();
                if (dX > 0) {
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else if (dX < 0) {
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else {
                    background.setBounds(0, 0, 0, 0);
                    icon.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                icon.draw(c);
            }
        }).attachToRecyclerView(bottomList);

    }

    private void enableButtons()
    {
        showList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b==true)
                {
                    showList();
                }
                else
                {
                    hideList();
                }
            }
        });

    }
//隐藏下方列表
    private void hideList()
    {
        getView().findViewById(R.id.modify_number).setVisibility(View.GONE);
        getView().findViewById(R.id.modify_name).setVisibility(View.GONE);
        getView().findViewById(R.id.modify_week).setVisibility(View.GONE);
        getView().findViewById(R.id.modify_start_time).setVisibility(View.GONE);
        getView().findViewById(R.id.modify_end_time).setVisibility(View.GONE);
        getView().findViewById(R.id.modify_count).setVisibility(View.GONE);
        getView().findViewById(R.id.view5).setVisibility(View.GONE);
        getView().findViewById(R.id.view11).setVisibility(View.GONE);
        getView().findViewById(R.id.listTitle).setVisibility(View.GONE);
        bottomList.setVisibility(View.GONE);
        showList.setChecked(false);
        confirm.setVisibility(View.VISIBLE);
    }
    private void showList()
    {
        getView().findViewById(R.id.listTitle).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.modify_number).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.modify_name).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.modify_week).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.modify_start_time).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.modify_end_time).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.modify_count).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.view5).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.view11).setVisibility(View.VISIBLE);
        bottomList.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        showList.setChecked(true);

    }

    private List<WeekViewEvent> Todo2WeekViewEvent(int newYear,int newMonth)
    {
      weekViewEvents=new ArrayList<>();
        int i = 1;
        int index = 0;
        for (EditPlan.ToDoExtend ti : satisfiedTodos) {
            Todo todo=ti.getTodo();
            String weekday = todo.getStartTime().split("-")[0];
            int diff = Integer.valueOf(weekday);//与周一的距离
            String starttime = todo.getStartTime().split("-")[1];
            String starthour = starttime.split(":")[0];
            String startminute = starttime.split(":")[1];
            String endtime = todo.getEndTime().split("-")[1];
            String endhour = endtime.split(":")[0];
            String endminute = endtime.split(":")[1];
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
            if(todo.getType()==0)
            event.setColor(getResources().getColor(R.color.event_color_05)); //template中事项统一颜色
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
            weekViewEvents.add(event);
            i++;
        }
        return weekViewEvents;
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

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        int index = event.getIndex();
        EditPlan.ToDoExtend toDoExtend=satisfiedTodos.get(index);
        if(!(toDoExtend.getTodo().getType()==0)) {
            satisfiedTodos.remove(toDoExtend);
            unsatisfiedTodos.add(toDoExtend);
            System.out.println("sort results");
            System.out.println(satisfiedTodos);
            System.out.println(unsatisfiedTodos);
            Bundle bundle = new Bundle();
            bundle.putSerializable("satisfiedTodos", satisfiedTodos);
            bundle.putSerializable("unsatisfiedTodos", unsatisfiedTodos);
            NavController controller = Navigation.findNavController(getView());
          controller.navigate(R.id.action_modifyPlan_self, bundle);
        }

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
                                //判断名字是否为空
                                if (input.toString().equals("")) {
                                    dialog.setContent("计划名字不能为空，请重新输入！");
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
                                    plan.setStartDate(input.toString());
                                    plan.setUserId(userId);
                                    plan.setPlanName(info);
                                    //计算endDate
                                    Calendar cal = Calendar.getInstance();
                                    String splieTimes[] = plan.getStartDate().split("/");
                                    Date start = new Date((Integer.valueOf(splieTimes[0]) - 1900),
                                            (Integer.valueOf(splieTimes[1]) - 1), (Integer.valueOf(splieTimes[2])));
                                    cal.setTime(start);
                                    //增加6天
                                    cal.add(Calendar.DAY_OF_MONTH, 6);
                                    //Calendar转为Date类型
                                    Date end = cal.getTime();
                                    //将增加后的日期转为字符串
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    plan.setStartDate( formatter.format(start));
                                    plan.setEndDate(formatter.format(end));
                                    int flag = 0;
                                    List<Plan> plans = planRepository.getAllPlans(userId);
                                    for (Plan p : plans) {
                                        if (p.getStartDate().equals(plan.getStartDate())) {
                                            flag = 1;
                                            break;
                                        }

                                    }
                                    if (flag == 0) {
                                        planRepository.insertPlan(plan);
                                        dialog.dismiss();
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable("plan",plan);
                                        bundle.putString("userId",userId);
                                        NavController controller = Navigation.findNavController(getView());
                                        controller.navigate(R.id.action_modifyPlan_to_planWeekView, bundle);

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
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

     return Todo2WeekViewEvent(newYear,newMonth);
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
