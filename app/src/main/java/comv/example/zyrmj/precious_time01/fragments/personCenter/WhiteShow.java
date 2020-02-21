package comv.example.zyrmj.precious_time01.fragments.personCenter;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.WhiteAppAdapter;
import comv.example.zyrmj.precious_time01.WhiteApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhiteShow extends Fragment {


    private Button button;
    RecyclerView recyclerView;
    private String userId = "offline";
    private List<WhiteApp> whiteApps;
    final WhiteAppAdapter whiteAppAdapter = new WhiteAppAdapter();

    public WhiteShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_white_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        enableButtons();
    }

    public void init() {

        recyclerView = getView().findViewById(R.id.white_recyclerView);
        button = getView().findViewById(R.id.white_confirm);
        initList();


    }

    private void initList() {
        whiteApps = getAllAppNamesPackages();
        whiteAppAdapter.setAllWhiteApps(whiteApps);
        List<WhiteApp> selectedWhiteApps = new ArrayList<>();
        //从SharePreference中读取已选中的白名单app
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("whiteApps", Context.MODE_PRIVATE);
        for (WhiteApp whiteApp : whiteApps) {
            String isWhite = sharedPreferences.getString(whiteApp.AppName, "");
            if (isWhite.equals("true")) {
                selectedWhiteApps.add(whiteApp);
            }
        }
        whiteAppAdapter.setSelectedWhiteApps(selectedWhiteApps);

        //后续获取个人中心的已选择的白名单 todo
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(whiteAppAdapter);
        recyclerView.setItemViewCacheSize(whiteApps.size() - 4);
        //解决RecyclerView点击一个item，后面每间隔9个item就会触发一次同样的事件的问题

    }

    public void enableButtons() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSharedPreference();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_whiteShow2_to_personCenterFragment, bundle);
            }
        });

    }

    private void updateSharedPreference() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("whiteApps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<WhiteApp> selectedWhiteApps = whiteAppAdapter.getSelectedWhiteApps();
        for (WhiteApp whiteApp : whiteApps) {

            int flag = 0;
            String isWhite = sharedPreferences.getString(whiteApp.AppName, "");
            System.out.println("judgewhite"+isWhite);
            for (WhiteApp selected : selectedWhiteApps) {
                if (whiteApp.AppName.equals(selected.AppName)) {
                    if (isWhite.equals("")) {
                        System.out.println("addwhite");
                        editor.putString(whiteApp.AppName, "true");
                    }
                    flag = 1;
                    break;
                }
            }
            if (isWhite.equals("true") && flag == 0) {
                editor.remove(whiteApp.AppName);
                System.out.println("removewhite");

            }

        }
        editor.apply();

    }


    private List<WhiteApp> getAllAppNamesPackages() {
        // TODO Auto-generated method stub
        PackageManager pm = getActivity().getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<WhiteApp> whiteAppList = new ArrayList<>();
        for (PackageInfo packageInfo : list) {

            String appName = packageInfo.applicationInfo.loadLabel(
                    getActivity().getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            Drawable appIcon = packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager());

            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0 && !packageName.equals("comv.example.zyrmj.precious_time01")) {
                Log.i("zyn", "app_icon : " + appIcon);
                Log.i("zyn", "app_name : " + appName);
                Log.i("zyn", "app_pkt_name : " + packageName);
                WhiteApp whiteApp = new WhiteApp();
                whiteApp.Appicon = appIcon;
                whiteApp.AppName = appName;
                whiteApp.AppPkgName = packageName;
                whiteAppList.add(whiteApp);
            }

        }
        return whiteAppList;
    }
}
