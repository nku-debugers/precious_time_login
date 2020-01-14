package comv.example.zyrmj.precious_time01.fragments.plan;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanTodosListView extends Fragment {
    private RecyclerView recyclerView;
    private TextView planName;
    private FloatingActionButton toPlanshow, addTodo;
    private Switch canUpdate, isWeekView;
    int modify=0; //0表示不可修改 1便是可修改
    private String userId;
    private Plan plan;


    public PlanTodosListView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_todos_list_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null)
        {
            plan= (Plan) getArguments().getSerializable("plan");
            userId=getArguments().getString("userId");
            modify=getArguments().getInt("modify");
            assignViews();
            enableButtons();
        }
    }

    private void assignViews()
    {
        planName=getView().findViewById(R.id.planName);
        planName.setText(plan.getPlanName());
        canUpdate = getView().findViewById(R.id.Is_modify);
        if(modify==1)
            canUpdate.setChecked(true);
        isWeekView = getView().findViewById(R.id.week_switch);
        toPlanshow = getView().findViewById(R.id.toPlanShow);
        addTodo=getView().findViewById(R.id.add);
        if(modify==0)
        addTodo.hide();
        //recyclerView=getView().findViewById(R.id.recycleView);
    }

    private void  enableButtons()
    {
        toPlanshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("plan", plan);
                bundle.putString("userId", userId);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_planTodosListView_to_planShow, bundle);

            }
        });

        //是否是更改模式
        canUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //开启更改模式
                if (b) {
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
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("plan", plan);
                    bundle.putString("userId", userId);
                    bundle.putInt("modify",modify);
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_planTodosListView_to_planWeekView, bundle);
                } else //列表视图
                {
                }


            }
        });
    }

    private void initList()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

}
