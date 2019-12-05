package comv.example.zyrmj.precious_time01.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.User;
import comv.example.zyrmj.precious_time01.repository.*;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         if (getSupportActionBar() != null){
             getSupportActionBar().hide();
         }
 //首次启动app时执行下列代码
         User u = new User();
         UserRepository re = new UserRepository(this);
         re.insertUsers(u);
        Category category=new Category("offline","test");
         new CategoryRepository(this).insertCategory(category);
         Habit habit1=new Habit("offline","test1","length",20.0);
        Habit habit2=new Habit("offline","test2","length",30.0);
        new HabitRepository(this).insertHabit(habit1,habit2);

        NavController controller = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, controller);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController controller = Navigation.findNavController(this, R.id.fragment);
        return controller.navigateUp();
    }



}
