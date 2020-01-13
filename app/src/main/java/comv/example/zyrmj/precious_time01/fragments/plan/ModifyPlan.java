package comv.example.zyrmj.precious_time01.fragments.plan;


import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TodoAdapter;
import comv.example.zyrmj.precious_time01.entity.Todo;
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
        bottomList.setLayoutManager(new LinearLayoutManager(getContext()));
        bottomList.setAdapter(todoAdapter);
//        listdatas = new MutableLiveData<>();
//        listdatas.setValue(unsatisfiedTodos);
//        System.out.println("list size "+unsatisfiedTodos.size());
//        listdatas.observe(getActivity(), new Observer<List<EditPlan.ToDoExtend>>() {
//            @Override
//            public void onChanged(List<EditPlan.ToDoExtend> toDoExtends) {
//                todoAdapter.setSatisfiedTodos(satisfiedTodos);
//                todoAdapter.setUnsatisfiedTodos(unsatisfiedTodos);
//                System.out.println("list size "+unsatisfiedTodos.size());
//                todoAdapter.setUserId(userId);
//                todoAdapter.notifyDataSetChanged();
//            }
//        });

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
