package comv.example.zyrmj.precious_time01.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import comv.example.zyrmj.precious_time01.PlanFinishLayout;
import comv.example.zyrmj.precious_time01.R;

public class PlanFinishActivity extends Activity {

    private TextView name, time, number, startValue, endValue;
    private int start = 0, end = 100, total;
    private SeekBar progress;
    private Button confirm;
    private PlanFinishLayout layout;
    private int screenWidth;
    private float moveStep = 0;
    private ViewGroup.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView( R.layout.plan_finish);
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        time = (TextView) this.findViewById ( R.id.plan_finish_time );
        number = new TextView ( this );
        name = (TextView) this.findViewById ( R.id.plan_finish_name );
        time = (TextView) this.findViewById ( R.id.plan_finish_time );
        confirm = (Button) this.findViewById ( R.id.plan_finish_button );
        layoutParams = new ViewGroup.LayoutParams ( screenWidth, 50 );
        layout = findViewById ( R.id.textLayout );
        layout.addView ( number, layoutParams );
        number.layout ( 0,20, screenWidth, 80);
        progress = (SeekBar) findViewById ( R.id.plan_finish_progress );
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
        //startValue =  (TextView) findViewById ( R.id.startValue );
        //endValue = (TextView) findViewById ( R.id.endValue );
        init();
    }

    public void init(){
        //startValue.setText ( String.valueOf ( start ) );
        //endValue.setText ( String.valueOf ( end ) );
        Intent intent = getIntent();
        time.setText ( intent.getStringExtra("time") );
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

    private String check(int pro) {
        int curValue = total * pro/Math.abs(end);
        if (start <0 && end < 0) {
            curValue = start + curValue;
        } else if (start < 0 && end > -1) {
            curValue = curValue + start;
        }
        Log.e("check", total+"，"+curValue+"，"+ progress.getMax());
        return String.valueOf(curValue);
    }


}
