package comv.example.zyrmj.precious_time01.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;

import comv.example.zyrmj.precious_time01.R;
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

        User u = new User();
        UserRepository r = new UserRepository(this);
        r.insertUsers(u);

        NavController controller= Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this,controller);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController controller= Navigation.findNavController(this,R.id.fragment);
        return controller.navigateUp();
    }
}