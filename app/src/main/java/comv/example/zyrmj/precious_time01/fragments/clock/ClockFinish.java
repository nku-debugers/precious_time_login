package comv.example.zyrmj.precious_time01.fragments.clock;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import comv.example.zyrmj.precious_time01.PlanFinishLayout;
import comv.example.zyrmj.precious_time01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClockFinish extends Fragment {

    private TextView name, time, number;
    private int start = 0, end = 100, total;
    private SeekBar progress;
    private Button confirm;
    private PlanFinishLayout layout;
    private int screenWidth;
    private float moveStep = 0;
    private ViewGroup.LayoutParams layoutParams;

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
        getActivity ().setContentView( R.layout.fragment_clock_finish2 );
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getActivity ().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        number = new TextView ( getActivity () );
        name = getView ().findViewById ( R.id.plan_finish_name );
        time = getView ().findViewById ( R.id.plan_finish_time );
        confirm = getView ().findViewById ( R.id.plan_finish_button );
        layoutParams = new ViewGroup.LayoutParams ( screenWidth, 50 );
        layout = getView ().findViewById ( R.id.textLayout );
        layout.addView ( number, layoutParams );
        number.layout ( 0,20, screenWidth, 80);
        progress = getView ().findViewById ( R.id.plan_finish_progress );
        String total_time = getArguments ().getString ( "time" );
        time.setText ( total_time );
        number.setText ( String.valueOf ( start ) );
        progress.setEnabled ( true );
        progress.setMax ( Math.abs ( end ) );
        progress.setProgress ( start );

        if (start < 0 && end < 0) {
            total = Math.abs(start) - Math.abs(end);
        } else if (start < 0 && end > -1) {
            total = end + Math.abs(start);
        } else {
            total = end - start;
        }
        moveStep = (float) (((float) screenWidth / total) * 0.8);
    }

    public void enableButtons(){

        progress.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener () {
            @Override
            public void onProgressChanged(SeekBar seekBar , int i , boolean b) {
                number.layout((int) (i * moveStep), 20, screenWidth, 80);
                number.setText(check(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );

    }

    private String check(int pro) {
        int curValue = total * pro/Math.abs(end);
        if (start <0 && end < 0) {
            curValue = start + curValue;
        } else if (start < 0 && end > -1) {
            curValue = curValue + start;
        }
        Log.e("check", total+"，"+curValue+"，"+ progress.getMax());
        System.out.println ( "curValue = "+curValue );
        return String.valueOf(curValue);
    }

}
