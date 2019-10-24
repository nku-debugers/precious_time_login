package comv.example.zyrmj.precious_time01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import comv.example.zyrmj.precious_time01.ViewModel.TemplateViewModel;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.entity.User;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;
import comv.example.zyrmj.precious_time01.repository.UserRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavController controller= Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this,controller);

//以下均为测试数据库的代码
        User user = new User();
        User user2 = new User();
        user2.setId("2");
        new UserRepository(this).insertUsers(user, user2);
        Template t = new Template("offline", "study");
        Template t2 = new Template("2", "study");
        new TemplateRepository(this).insertTemplates(t, t2);

        TemplateItem ti=new TemplateItem("offline","test","study"
        ,"test","1-2:00","1-1:00");
        TemplateItem ti2=new TemplateItem("offline","test2","study"
                ,"test","2-2:00","2-1:00");
        TemplateItem ti3=new TemplateItem("offline","test3","study"
                ,"test","3-2:00","3-1:00");
        new TemplateItemRepository(this).insertTemplateItems(ti, ti2, ti3);
        Log.d("here", "before search");
        List<TemplateItem>templateItems = new TemplateItemRepository(this).getAll();
        for(int i = 0; i< templateItems.size(); i++) {
            System.out.println(templateItems.get(i).getItemName());
            Log.d("here", templateItems.get(i).getItemName());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController controller= Navigation.findNavController(this,R.id.fragment);
        return controller.navigateUp();
    }
}
