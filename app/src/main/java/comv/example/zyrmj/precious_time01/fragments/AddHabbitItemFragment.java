package comv.example.zyrmj.precious_time01.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.donkingliang.labels.LabelsView;

import java.util.ArrayList;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;
import me.leefeng.promptlibrary.PromptDialog;

public class AddHabbitItemFragment extends AppCompatActivity implements View.OnClickListener {
    LabelsView labelsView;
    Button button;
    TextView advanced_option;
    private void init(){
        labelsView =(LabelsView)findViewById ( R.id.category );
        button = findViewById ( R.id.habbit_complete );
        advanced_option = findViewById ( R.id.toAddHabit2 );
        final ArrayList<String> label = new ArrayList<> ();
        //添加从数据库中获取的标签名称
        //label.add()
        label.add("Android");
        label.add("IOS");
        label.add("前端");
        label.add("后台");
        label.add("微信开发");
        label.add("游戏开发");
        label.add("+");
        labelsView.setLabels(label); //直接设置一个字符串数组就可以了。
        labelsView.setOnLabelClickListener ( new LabelsView.OnLabelClickListener () {
            @Override
            public void onLabelClick(TextView label , Object data , int position) {
                if(data.toString().equals("+")){
                    //添加模板名称
                    //showDialog (  );
                }
            }
        } );
        button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //获取选中标签的数据，返回一个list
                labelsView.getSelectLabelDatas ();
                //获取选中标签的位置，返回一个Integer数组
                labelsView.getSelectLabels ();
                System.out.println (  );
            }
        } );
//        advanced_option.setOnClickListener ( new View.OnClickListener () {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater factory = LayoutInflater.from(AddHabbitItemFragment.this);
//                View myView = factory.inflate(R.layout.login, null);
//
//                Dialog dialog = new AlertDialog.Builder(AddHabbitItemFragment.this)
//                        .setTitle("用户登录")
//                        .setView(myView)
//                        .setPositiveButton("登录",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                })
//                        .setNegativeButton("取消",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                })
//
//                        .create();
//
//                dialog.show();
//            }
//        } );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.add_habit_item );
        init ();
    }

    @Override
    public void onClick(View view) {

    }
    public void showDialog(String info, final TemplateRepository templateRepository)
    {
        //添加标签dialog，同添加模板名称
    }
}
