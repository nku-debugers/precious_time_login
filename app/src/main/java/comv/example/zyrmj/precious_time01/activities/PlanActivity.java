package comv.example.zyrmj.precious_time01.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;

import android.os.Bundle;

public class PlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Plan plan1=new Plan();
        Plan plan2=new Plan();
        plan1.setStartDate("2020-01-13");
        plan1.setEndDate("2020-01-19");
        plan1.setPlanName("AA");
        plan2.setStartDate("2020-01-20");
        plan2.setEndDate("2020-01-26");
        plan2.setPlanName("AA");
        new PlanRepository(this).deletePlan(plan1,plan2);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        NavController controller = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, controller);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController controller = Navigation.findNavController(this, R.id.fragment);
        return controller.navigateUp();
    }
}
