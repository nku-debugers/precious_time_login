package comv.example.zyrmj.precious_time01.fragments.clock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.activities.PersonCenterActivity;
import comv.example.zyrmj.precious_time01.activities.PlanActivity;

public class ChoseClock extends Fragment {
    private Button accordButton, forceButton, boringButton;
    private String userId="offline";
    private ImageView clock,plan,personcenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chose_clock , container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );
        Intent intent=getActivity().getIntent();
        if(intent!=null&&intent.getStringExtra("userId")!=null)
        { userId=intent.getStringExtra("userId");
            Log.d("pass",userId);
        }
        init();
        enableButtons();
    }

    public void init(){
        accordButton = getView ().findViewById ( R.id.button_accord );
        forceButton = getView ().findViewById ( R.id.button_force );
        boringButton = getView ().findViewById ( R.id.button_boring );
        clock=getView().findViewById(R.id.clock);
        plan=getView().findViewById(R.id.plan);
        personcenter=getView().findViewById(R.id.personcenter);
    }

    public void enableButtons(){
        //暂定使用getArguments（）判断是否为null，改进后或许为getArguments().getString("hour")==null
        if(getArguments()==null){
            accordButton.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle ();
                    bundle.putString ( "kind","1" );
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_choseClock_to_addTime,bundle);
                }
            } );
            forceButton.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle ();
                    bundle.putString ( "kind","2" );
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_choseClock_to_addTime, bundle);
                }
            } );
            boringButton.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle ();
                    bundle.putString ( "kind","3" );
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_choseClock_to_addTime, bundle);
                }
            } );
        }
        else{
            accordButton.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle ();
                    bundle.putString ( "kind","1" );
                    bundle.putString ( "hour",getArguments().getString("hour") );
                    bundle.putString ( "minute",getArguments().getString("minute") );
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_choseClock_to_clockMain,bundle);
                }
            } );
            forceButton.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle ();
                    bundle.putString ( "kind","2" );
                    bundle.putString ( "hour",getArguments().getString("hour") );
                    bundle.putString ( "minute",getArguments().getString("minute") );
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_choseClock_to_clockMain,bundle);
                }
            } );
            boringButton.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle ();
                    bundle.putString ( "kind","3" );
                    bundle.putString ( "hour",getArguments().getString("hour") );
                    bundle.putString ( "minute",getArguments().getString("minute") );
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_choseClock_to_clockMain,bundle);
                }
            } );
        }

        personcenter.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("userId",userId);
                intent.setClass(getContext(), PersonCenterActivity.class);
                startActivity(intent);
            }
        } );
        plan.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("userId",userId);
                intent.setClass(getContext(), PlanActivity.class);
                startActivity(intent);
            }
        } );
    }
}
