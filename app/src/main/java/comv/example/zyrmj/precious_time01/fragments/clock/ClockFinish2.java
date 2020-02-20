package comv.example.zyrmj.precious_time01.fragments.clock;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comv.example.zyrmj.precious_time01.PlanFinishLayout;
import comv.example.zyrmj.precious_time01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClockFinish2 extends Fragment {

    private TextView name, time;
    private Button confirm;

    public ClockFinish2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_clock_finish2 , container , false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );

        init();
        enableButtons();
    }

    public void init(){
        getActivity ().setContentView( R.layout.fragment_clock_finish2 );
        name = getView ().findViewById ( R.id.plan_finish_name );
        time = getView ().findViewById ( R.id.plan_finish_time );
        confirm = getView ().findViewById ( R.id.plan_finish_button );
        name.setText ( getArguments ().getString ( "todoName" ) );
        time.setText ( getArguments ().getString ( "time" ) );
    }

    public void enableButtons(){
        confirm.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //添加跳转逻辑

            }
        } );

    }

}
