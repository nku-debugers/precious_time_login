package comv.example.zyrmj.precious_time01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.User;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;
import comv.example.zyrmj.precious_time01.repository.UserRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavController controller= Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this,controller);


        User user = new User();
        User user2 = new User();
        user2.setId("2");
        new UserRepository(this).insertUsers(user, user2);
        Template t = new Template("offline", "study");
        Template t2 = new Template("2", "study");
        new TemplateRepository(this).insertTemplates(t, t2);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController controller= Navigation.findNavController(this,R.id.fragment);
        return controller.navigateUp();
    }
}
