package comv.example.zyrmj.precious_time01.activities;

import comv.example.zyrmj.precious_time01.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MonitorActivity extends BaseActivity {
	TextView showed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor);
		init();

	}

	private void init()
	{
		showed=findViewById(R.id.quote_show);
		if(getIntent()!=null&&getIntent().getStringExtra("quote")!=null)
		{
			showed.setText(getIntent().getStringExtra("quote"));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setLinstener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void fillData() {
		// TODO Auto-generated method stub

	}
}
