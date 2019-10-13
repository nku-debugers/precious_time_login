package comv.example.zyrmj.precious_time01;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.BackendService.RegisterAndLoginBackendService;
import comv.example.zyrmj.precious_time01.Utils.MD5Util;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements Validator.ValidationListener{
    //定义界面上的控件,引入表单验证框架
    private Validator validator;//表单验证器
    Spinner option;
    private String idType;
    @Order(1)
    @NotEmpty(message = "此项不能为空")
    @Email
    EditText emailInput;
    @Order(2)
    @NotEmpty(message = "此项不能为空")
    @Pattern(regex = "^[0-9]*$",message = "不符合手机号码格式")
    @Length(min=11,max=11,message = "手机号码应为11位")
    EditText phoneInput;
    @Order(3)
    @NotEmpty(message = "此项不能为空")
    EditText usernameInput;
    @Order(4)
    @NotEmpty(message = "密码不能为空")
    @Password(min=6,message = "密码最少为6位")
    EditText password;

    Button toRegister;
    Button login;
    int  spinnerPosition=0;
    String userId="";
    String pwd="";
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null)
        { spinnerPosition=getArguments().getInt("spinnerPosition",0);
        Log.d("testinf",String.valueOf(spinnerPosition));
        userId=getArguments().getString("userId","");
        pwd=getArguments().getString("password","");}

        init();//初始化界面

    }
    public void init()
    {
        emailInput=getView().findViewById(R.id.email);
        phoneInput=getView().findViewById(R.id.phone);
        usernameInput=getView().findViewById(R.id.username);
        option=getView().findViewById(R.id.spinner);
        password=getView().findViewById(R.id.password);
        toRegister=getView().findViewById(R.id.next);
        login=getView().findViewById(R.id.login);
        validator = new Validator(this);
        validator.setValidationListener(this);
        //获取注册界面的数据
        option.setSelection(spinnerPosition);
        if(spinnerPosition==0)
        {
            emailInput.setText(userId);
        }
        else if(spinnerPosition==1)
        {
            phoneInput.setText(userId);
        }
        else
        {
            usernameInput.setText(userId);
        }
        password.setText(pwd);






        //下拉框监听器
        option.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                选择邮箱
                if(i==0)
                {
                    usernameInput.setVisibility(View.INVISIBLE);
                    emailInput.setVisibility(View.VISIBLE);
                    phoneInput.setVisibility(View.INVISIBLE);
                    idType="email";


                }
//                选择手机号
                else if(i==1)
                {
                    usernameInput.setVisibility(View.INVISIBLE);
                    emailInput.setVisibility(View.INVISIBLE);
                    phoneInput.setVisibility(View.VISIBLE);
                    idType="phone";

                }
//                选择用户昵称
                else
                {
                    usernameInput.setVisibility(View.VISIBLE);
                    emailInput.setVisibility(View.INVISIBLE);
                    phoneInput.setVisibility(View.INVISIBLE);
                    idType="username";


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //登录按钮监听器
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();

            }
        });

        //跳转注册按钮监听器
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_loginFragment3_to_registerFragment2);
            }
        });

    }
    //收集界面填写信息(返回一个String List）
    public List<String> collectData()
    {
        List<String> informations=new ArrayList<String>();

        if(idType=="email")
        {
            informations.add("1");//code
            userId=emailInput.getText().toString();

        }
        else if(idType=="phone")
        {
            informations.add("2");//code
            userId=phoneInput.getText().toString();
        }
        else
        {
            informations.add("3");//code
            userId=usernameInput.getText().toString();
        }
        //add MD5password
        informations.add(userId);
        String encrptedPassword= MD5Util.getMd5(password.getText().toString());
        informations.add(encrptedPassword);//password
        Log.d("collectedData",informations.toString());
        return informations;


    }
    //向后端上传数据
    public String postData(List<String> informations)
    {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", informations.get(0));
            jsonObject.put("id", informations.get(1));
            jsonObject.put("pw",informations.get(2));
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e);
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        System.out.println(jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/user/login/")
                .post(body)
                .build();
        RegisterAndLoginBackendService loginBackendService=new RegisterAndLoginBackendService(client,request);

        return loginBackendService.registerOrLogin();


    }

    @Override
    public void onValidationSucceeded() {
        //收集界面填写信息
        List<String> informations=collectData();
        Log.d("collectedData",informations.toString());
        //开始进行后台操作
        String reply=postData(informations);
        System.out.println("loginreply"+reply);
        if (reply.contains("1"))
        {
            Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_LONG).show();
            Bundle bundle=new Bundle();
            bundle.putString("userId",userId);
            NavController controller= Navigation.findNavController(getView());
            controller.navigate(R.id.action_loginFragment3_to_personCenterFragment,bundle);

        }
        else
        {
            Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            // 显示上面注解中添加的错误提示信息
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {

                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
