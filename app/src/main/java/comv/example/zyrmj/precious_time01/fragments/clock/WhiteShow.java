package comv.example.zyrmj.precious_time01.fragments.clock;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.WhiteApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhiteShow extends Fragment {

    private Button button;
    private ImageView imageView1,imageView2,imageView3;
    private TextView textView1,textView2,textView3;
    private Switch aSwitch1,aSwitch2,aSwitch3;
    private String userId = "offline";
    private List<WhiteApp> whiteApps;

    public WhiteShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_white_show , container , false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );
        init();
        enableButtons();
    }

    public void init(){
        button = getView ().findViewById ( R.id.white_test_confirm2 );
        imageView1 = getView ().findViewById ( R.id.imageView6 );
        imageView2 = getView ().findViewById ( R.id.imageView5 );
        imageView3 = getView ().findViewById ( R.id.imageView );
        textView1 = getView ().findViewById ( R.id.textView8 );
        textView2 = getView ().findViewById ( R.id.textView7 );
        textView3 = getView ().findViewById ( R.id.textView6 );
        aSwitch1 = getView ().findViewById ( R.id.switch6 );
        aSwitch2 = getView ().findViewById ( R.id.switch5 );
        aSwitch3 = getView ().findViewById ( R.id.switch4 );
        if(needPermissionForBlocking(getActivity ().getApplicationContext ())) {
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

    public void enableButtons(){
        button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
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
                Bundle bundle = new Bundle (  );
                bundle.putString ( "hour", getArguments ().getString ( "hour" ));
                bundle.putString ( "minute", getArguments ().getString ( "minute" ));
                bundle.putString ( "kind", getArguments ().getString ( "kind" ));
                bundle.putStringArrayList ( "whitenames",whitenames);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_whiteShow_to_clockMain,bundle);
            }
        } );
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
        PackageManager pm = getActivity ().getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<WhiteApp> whiteAppList = new ArrayList<> (  );
        for (PackageInfo packageInfo : list) {

            String appName = packageInfo.applicationInfo.loadLabel(
                    getActivity ().getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            Drawable appIcon = packageInfo.applicationInfo.loadIcon ( getActivity ().getPackageManager () );

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
}
