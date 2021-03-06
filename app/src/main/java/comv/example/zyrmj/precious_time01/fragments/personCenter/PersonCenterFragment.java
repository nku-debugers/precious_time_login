package comv.example.zyrmj.precious_time01.fragments.personCenter;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.activities.ClockActivity;
import comv.example.zyrmj.precious_time01.activities.PersonCenterActivity;
import comv.example.zyrmj.precious_time01.activities.PlanActivity;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.entity.User;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;
import comv.example.zyrmj.precious_time01.repository.UserRepository;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonCenterFragment extends Fragment {
    String userId = "offline";
    private long mExitTime = 0;
    private ImageView clock,plan,personcenter;
    public PersonCenterFragment() {
        // Required empty public constructor
    }

    private void enableBackButton() {
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        //大于2000ms则认为是误操作，使用Toast进行提示
                        Toast.makeText(getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        //并记录下本次点击“返回键”的时刻，以便下次进行判断
                        mExitTime = System.currentTimeMillis();
                    } else {
                        //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.personcenter, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent=getActivity().getIntent();
        if(intent!=null&&intent.getStringExtra("userId")!=null)
        { userId=intent.getStringExtra("userId");
        Log.d("pass",userId);
        }

        enableBackButton();
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "offline");
        }
        Button tologin = getView().findViewById(R.id.tologin);
        Button logout = getView().findViewById(R.id.logout);
        TextView user = getView().findViewById(R.id.userId);
        View toTemplate = getView().findViewById(R.id.toTemplate);
        View toQuote = getView().findViewById(R.id.toQuote);
        View toHabit=getView().findViewById(R.id.toHabit);
        View toWhite = getView().findViewById ( R.id.toWhite );
        clock=getView().findViewById(R.id.clock);
        plan=getView().findViewById(R.id.plan);
        personcenter=getView().findViewById(R.id.personcenter);
        toTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                Bundle bundle=new Bundle();
                bundle.putString("userId",userId);
                controller.navigate(R.id.action_personCenterFragment_to_templateShowFragment,bundle);
            }
        });
        toQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                Bundle bundle=new Bundle();
                bundle.putString("userId",userId);
                controller.navigate(R.id.action_personCenterFragment_to_quoteFragment,bundle);
            }
        });
        toHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                Bundle bundle=new Bundle();
                bundle.putString("userId",userId);
                controller.navigate(R.id.action_personCenterFragment_to_habitShow,bundle);
            }
        });
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_personCenterFragment_to_loginFragment3);
            }
        });
        toWhite.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                Bundle bundle=new Bundle();
                bundle.putString("userId",userId);
                controller.navigate(R.id.action_personCenterFragment_to_whiteShow2);
            }
        } );
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_personCenterFragment_self);
            }
        });

        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("userId",userId);
                intent.setClass(getContext(), PlanActivity.class);
                startActivity(intent);
            }
        });

        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("userId",userId);
                intent.setClass(getContext(), ClockActivity.class);
                startActivity(intent);
            }
        });

        if (userId.equals("offline")) {
            tologin.setVisibility(View.VISIBLE);
        } else {
            logout.setVisibility(View.VISIBLE);
        }
        user.setText(userId);
    }
}
