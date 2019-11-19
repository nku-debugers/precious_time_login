package comv.example.zyrmj.precious_time01.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonCenterFragment extends Fragment {
String userId="未登录";

    public PersonCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.personcenter, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Template t1=new Template("offline","study14");
        Template t2=new Template("offline","study13");
        Template t3=new Template("offline","study12");
        Template t4=new Template("offline","study11");





        TemplateRepository templateRepository=new TemplateRepository(getContext());
        templateRepository.deleteTemplates(t1,t2,t3,t4);



        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null) {
            userId = getArguments().getString("userId", "未登录");
        }
        Button tologin=getView().findViewById(R.id.tologin);
        Button logout=getView().findViewById(R.id.logout);
        TextView user=getView().findViewById(R.id.userId);
        View toTemplate=getView().findViewById(R.id.toTemplate);
        toTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller=Navigation.findNavController(view);
                controller.navigate(R.id.action_personCenterFragment_to_templateShowFragment);
            }
        });
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_personCenterFragment_to_loginFragment3);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_personCenterFragment_self);
            }
        });


        if(userId.equals("未登录"))
        {
            tologin.setVisibility(View.VISIBLE);
        }
        else
        {
            logout.setVisibility(View.VISIBLE);
        }
        user.setText(userId);
    }
}