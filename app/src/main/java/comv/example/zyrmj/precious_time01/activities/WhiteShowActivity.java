package comv.example.zyrmj.precious_time01.activities;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.WhiteApp;

public class WhiteShowActivity extends BaseActivity {
    private Button button;
    private ImageView imageView1,imageView2,imageView3;
    private TextView textView1,textView2,textView3;
    private Switch aSwitch1,aSwitch2,aSwitch3;
    private String userId = "offline";
    private List<WhiteApp> whiteApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whiteshow_test);

        initView();
        initData();
        setLinstener();
        fillData();
    }

    @Override
    protected void initData() {
        if(needPermissionForBlocking(getApplicationContext ())) {
            Intent intent = new Intent ( Settings.ACTION_USAGE_ACCESS_SETTINGS );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity ( intent );
        }
        whiteApps = getAllAppNamesPackages ();
        imageView1.setImageDrawable (whiteApps.get ( 0 ).Appicon);
        imageView2.setImageDrawable (whiteApps.get ( 1 ).Appicon);
        imageView3.setImageDrawable (whiteApps.get ( 2 ).Appicon);
        textView1.setText ( whiteApps.get ( 0 ).AppName );
        textView2.setText ( whiteApps.get ( 1 ).AppName );
        textView3.setText ( whiteApps.get ( 2 ).AppName );
    }
    @Override
    protected void setLinstener() {
        button.setOnClickListener ( this );
        aSwitch1.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton , boolean b) {
                whiteApps.get ( 0 ).Iswhite = b;
            }
        } );
        aSwitch2.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton , boolean b) {
                whiteApps.get ( 1 ).Iswhite = b;
            }
        } );
        aSwitch3.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton , boolean b) {
                whiteApps.get ( 2 ).Iswhite = b;
            }
        } );
    }
    @Override
    protected void initView() {
        button = this.findViewById ( R.id.white_test_confirm );
        imageView1=this.findViewById ( R.id.imageView2 );
        imageView2=this.findViewById ( R.id.imageView3 );
        imageView3=this.findViewById ( R.id.imageView4 );
        textView1 = this.findViewById ( R.id.textView3 );
        textView2 = this.findViewById ( R.id.textView4 );
        textView3 = this.findViewById ( R.id.textView5 );
        aSwitch1 = this.findViewById ( R.id.switch1 );
        aSwitch2 = this.findViewById ( R.id.switch2 );
        aSwitch3 = this.findViewById ( R.id.switch3 );
    }



    @Override
    protected void fillData() {
    }

    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return  (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    private List<WhiteApp> getAllAppNamesPackages() {
        // TODO Auto-generated method stub
        PackageManager pm = getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<WhiteApp> whiteAppList = new ArrayList<> (  );
        for (PackageInfo packageInfo : list) {

            String appName = packageInfo.applicationInfo.loadLabel(
                    getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            Drawable appIcon = packageInfo.applicationInfo.loadIcon ( getPackageManager () );

            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0 && packageName!="comv.example.zyrmj.precious_time01"){
                Log.i("zyn", "app_icon : " + appIcon);
                Log.i("zyn", "app_name : " + appName);
                Log.i("zyn", "app_pkt_name : " + packageName);
                WhiteApp whiteApp = new WhiteApp ();
                whiteApp.Appicon = appIcon;
                whiteApp.AppName = appName;
                whiteApp.AppPkgName = packageName;
                whiteAppList.add ( whiteApp );
            }

        }
        return whiteAppList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId ()){
            case R.id.white_test_confirm:
                System.out.println ( "aswitch1: "+whiteApps.get ( 0 ).Iswhite );
                System.out.println ( "aswitch2: "+whiteApps.get ( 1 ).Iswhite );
                System.out.println ( "aswitch3: "+whiteApps.get ( 2 ).Iswhite );
                Intent intent=new Intent();
                Intent intent_t = getIntent ();
                ArrayList<String> whitenames = new ArrayList<> ();
                if(whiteApps.get ( 0 ).Iswhite){
                    whitenames.add ( whiteApps.get ( 0 ).AppPkgName );
                }
                if(whiteApps.get ( 1 ).Iswhite){
                    whitenames.add ( whiteApps.get ( 1 ).AppPkgName );
                }
                if(whiteApps.get ( 2 ).Iswhite){
                    whitenames.add ( whiteApps.get ( 2 ).AppPkgName );
                }
//                String hour = intent_t.getStringExtra ( "hour" );
//                String minute = intent_t.getStringExtra ( "minute" );
//                String kind = intent_t.getStringExtra ( "kind" );
//                System.out.println ( hour+" time "+minute );
                intent.putStringArrayListExtra (  "whitenames", whitenames );
                intent.putExtra ( "hour", intent_t.getStringExtra ( "hour" ) );
                intent.putExtra ( "minute", intent_t.getStringExtra ( "minute" ) );
                intent.putExtra ( "kind", intent_t.getStringExtra ( "kind" ) );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setClass(WhiteShowActivity.this, TimeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
