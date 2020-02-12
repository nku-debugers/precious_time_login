package comv.example.zyrmj.precious_time01.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import comv.example.zyrmj.precious_time01.PlanFinishLayout;
import comv.example.zyrmj.precious_time01.R;

public class PlanFinishActivity extends BaseActivity {

    private TextView name, time, number;
    private SeekBar progress;
    private Button confirm;
    private PlanFinishLayout layout;
    private int screenWidth;
    private ViewGroup.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView( R.layout.plan_finish);
        initView();
        initData();
        setLinstener();
        fillData();
    }

    @Override
    protected void initData() {
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
    }

    @Override
    protected void initView() {

        name = (TextView) this.findViewById ( R.id.plan_finish_name );
        time = (TextView) this.findViewById ( R.id.plan_finish_time );
        number = (TextView) this.findViewById ( R.id.plan_finish_number );
        progress = (SeekBar) this.findViewById ( R.id.plan_finish_progress );
        confirm = (Button) this.findViewById ( R.id.plan_finish_button );
        layoutParams = new ViewGroup.LayoutParams(screenWidth, 50);
        layout = (PlanFinishLayout) findViewById(R.id.textLayout);
        layout.addView(number, layoutParams);
    }

    @Override
    protected void setLinstener() {
        confirm.setOnClickListener ( this );
        progress.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener () {
            @Override
            public void onProgressChanged(SeekBar seekBar , int i , boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );
    }

    @Override
    protected void fillData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId ()){
            case R.id.plan_finish_button:
                //输出进度条的当前值
                System.out.println ( progress.getProgress () );
                break;
            default:
                break;
        }
    }
}
