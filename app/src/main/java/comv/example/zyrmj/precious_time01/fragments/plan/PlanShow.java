package comv.example.zyrmj.precious_time01.fragments.plan;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.PlanAdapter;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanShow extends Fragment {
    private static String TAG = "mytag";
    private ImageView back;
    private FloatingActionButton addPlan;
    private RecyclerView recyclerView;
    private String userId = "offline";
    private Plan oldPlan;//之前用的plan ,若不选择新的plan,直接按back键，需返回此值
    private PlanRepository planRepository;


    public PlanShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: in plan show");
        super.onActivityCreated(savedInstanceState);
        if (!(getArguments() == null)) {
            userId = getArguments().getString("userId", "offline");
            oldPlan = (Plan) getArguments().getSerializable("plan");

        }
        assignViews();
        enableButtons();
        initList();
    }

    private void assignViews() {
        back = getView().findViewById(R.id.back);
        addPlan = getView().findViewById(R.id.addPlan);
        recyclerView = getView().findViewById(R.id.recycleView);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putSerializable("plan", oldPlan);
                    NavController controller = Navigation.findNavController(getView());
                    if (getArguments().getString("weekView") != null) {
                        controller.navigate(R.id.action_planShow_to_planWeekView, bundle);
                    } else {
                        controller.navigate(R.id.action_planShow_to_planTodosListView, bundle);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void enableButtons() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putSerializable("plan", oldPlan);
                NavController controller = Navigation.findNavController(view);
                if (getArguments().getString("weekView") != null) {
                    controller.navigate(R.id.action_planShow_to_planWeekView, bundle);
                } else {
                    controller.navigate(R.id.action_planShow_to_planTodosListView, bundle);
                }
            }
        });

        //跳转到选择模板界面
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putSerializable("plan",oldPlan);
                if (getArguments().getString("weekView") != null)
                    bundle.putString("weekView","true");
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_planShow_to_choseTemplate, bundle);

            }
        });


    }

    //填写列表数据
    private void initList() {
        planRepository = new PlanRepository(getContext());
        List<Plan> plans = planRepository.getAllPlans(userId);
        final PlanAdapter planAdapter = new PlanAdapter();
        planAdapter.setAllPlans(plans);
        if (getArguments().getString("weekView") != null) planAdapter.setBelongto(0);
        else planAdapter.setBelongto(1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(planAdapter);
    }
}
