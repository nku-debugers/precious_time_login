package comv.example.zyrmj.precious_time01.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.donkingliang.labels.LabelsView;

import java.util.ArrayList;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

public class AddHabbitItemFragment extends AppCompatActivity implements View.OnClickListener {
    LabelsView labelsView;
    Button button;
    TextView advanced_option;
    private void init(){
        labelsView =findViewById ( R.id.category );
        button = findViewById ( R.id.habbit_complete );
        advanced_option = findViewById ( R.id.advanced_option);
        final ArrayList<String> labels = new ArrayList<> ();
        //添加从数据库中获取的标签名称

        labels.add("+");
        labelsView.setLabels(labels); //直接设置一个字符串数组就可以了。
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
