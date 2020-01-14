package comv.example.zyrmj.precious_time01.fragments.plan;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanWeekView extends Fragment {
    private String userId="offline";
    private Plan showedPlan;



    public PlanWeekView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_week_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent=getActivity().getIntent();
        if(intent!=null&&intent.getStringExtra("userId")!=null)
        { userId=intent.getStringExtra("userId");
        }
        showedPlan=choosePlan();
        TextView Name=getView().findViewById(R.id.name);
        Button add=getView().findViewById(R.id.button2);
        Name.setText(showedPlan.getStartDate());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("userId",userId);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_planWeekView_to_choseTemplate,bundle);
            }
        });



    }

    public Plan choosePlan()
    {
        List<Plan> plans = new PlanRepository(getContext()).getAllPlans(userId);
        if (plans == null || plans.size() == 0) {
            Bundle bundle=new Bundle();
            bundle.putString("userId",userId);
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_planWeekView_to_choseTemplate,bundle);
        } else {
            if(getArguments()!=null&&getArguments().getSerializable("plan")!=null)
            {
                Plan plan= (Plan) getArguments().getSerializable("plan");
                System.out.println("pass result");
                System.out.println(plan.getStartDate());
                return plan;
            }
            else {
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

}
