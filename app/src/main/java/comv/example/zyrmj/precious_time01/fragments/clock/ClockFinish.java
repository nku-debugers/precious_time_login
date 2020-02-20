package comv.example.zyrmj.precious_time01.fragments.clock;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;

import comv.example.zyrmj.precious_time01.PlanFinishLayout;
import comv.example.zyrmj.precious_time01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClockFinish extends Fragment {

    private TextView name, time;
    private LinearLayout fail_linear;
    private EditText fail;
    private Switch isFinish;
    private Button confirm;

    public ClockFinish() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_clock_finish , container , false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );

        init();
        enableButtons();
    }

    public void init(){
        name = getView ().findViewById ( R.id.plan_finish_name );
        time = getView ().findViewById ( R.id.plan_finish_time );
        fail_linear = getView ().findViewById ( R.id.fail_linear );
        fail = getView ().findViewById ( R.id.fail_input );
        isFinish = getView ().findViewById ( R.id.isFinish );
        confirm = getView ().findViewById ( R.id.plan_finish_button );
        name.setText ( getArguments ().getString ( "todoName" ) );
        if(getArguments ().getString ( "hour" ).equals ( "0" )){
            time.setText ( getArguments ().getString ( "minute" ) +"分钟");
        }
        else{
            time.setText ( getArguments ().getString ( "hour" )+"小时"+getArguments ().getString ( "minute" ) +"分钟");
        }
        isFinish.setText ( "未完成" );
    }

    public void enableButtons(){
        confirm.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //添加跳转逻辑

            }
        } );
        isFinish.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton , boolean b) {
                if(b==false){
                    isFinish.setText ( "已完成" );
                    fail_linear.setVisibility ( View.INVISIBLE );
                }
                else{
                    isFinish.setText ( "未完成" );
                    fail_linear.setVisibility ( View.VISIBLE );
                }
            }
        } );
    }

}
