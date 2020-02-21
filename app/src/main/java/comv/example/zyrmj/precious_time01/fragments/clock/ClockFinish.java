package comv.example.zyrmj.precious_time01.fragments.clock;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;

import comv.example.zyrmj.precious_time01.PlanFinishLayout;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.activities.ClockActivity;
import comv.example.zyrmj.precious_time01.activities.PlanActivity;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;
import comv.example.zyrmj.precious_time01.repository.TodoRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClockFinish extends Fragment {

    private TextView name, time;
    private LinearLayout fail_linear;
    private EditText fail;
    private Switch isFinish;
    private Button confirm;
    private ImageView back;


    public ClockFinish() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_clock_finish , container , false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );

        init();
        enableButtons();
    }

    public void init(){
        name = getView ().findViewById ( R.id.plan_finish_name );
        time = getView ().findViewById ( R.id.plan_finish_time );
        fail_linear = getView ().findViewById ( R.id.fail_linear );
        fail = getView ().findViewById ( R.id.fail_input );
        isFinish = getView ().findViewById ( R.id.isFinish );
        confirm = getView ().findViewById ( R.id.plan_finish_button );
        back=getView().findViewById(R.id.back);
        name.setText ( getArguments ().getString ( "todoName" ) );
        if(getArguments ().getString ( "hour" ).equals ( "0" )){
            time.setText ( getArguments ().getString ( "minute" ) +"分钟");
        }
        else{
            time.setText ( getArguments ().getString ( "hour" )+"小时"+getArguments ().getString ( "minute" ) +"分钟");
        }
        isFinish.setChecked(true);
        isFinish.setText ( "未完成" );
    }

    public void enableButtons(){
        confirm.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //添加跳转逻辑
                if(getActivity().getIntent()!=null) {
                    Todo todo = (Todo) getActivity().getIntent().getSerializableExtra("todo");

                    if(!isFinish.isChecked())
                    {
                        todo.setCompletion(true);
                        if(todo.getType()==1) //更改对应习惯完成度
                        {
                            Habit habit = new HabitRepository(getContext()).getSpecificHabit(todo.getUserId(), todo.getName());
                            //计算完成度
                            double perCompletion= TimeDiff.calCompletion(todo.getLength(), habit.getLength());
                            double newCompletion=perCompletion+habit.getCompletion();
                            habit.setCompletion(newCompletion);
                            new HabitRepository(getContext()).updateHabit(habit);
                        }
                    }
                    else
                    {
                        todo.setFailureTrigger(fail.getText().toString());
                        todo.setCompletion(false);
                    }

                    new TodoRepository(getContext()).updateTodo(todo);
                    Plan plan=new PlanRepository(getContext()).getSpecificPlan(todo.getUserId(),todo.getPlanDate());
                    Intent intent = new Intent();
                    intent.putExtra("userId",getActivity().getIntent().getStringExtra("userId") );
                    intent.putExtra("plan",plan);
                    intent.setClass(getContext(), PlanActivity.class);
                    startActivity(intent);

                }


            }
        } );
        isFinish.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton , boolean b) {
                if(b==false){
                    isFinish.setText ( "已完成" );
                    fail_linear.setVisibility ( View.INVISIBLE );
                }
                else{
                    isFinish.setText ( "未完成" );
                    fail_linear.setVisibility ( View.VISIBLE );
                }
            }
        } );
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
    }


}
