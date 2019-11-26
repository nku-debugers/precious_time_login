package comv.example.zyrmj.precious_time01.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.BackendService.RegisterAndLoginBackendService;
import comv.example.zyrmj.precious_time01.R;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
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
public class RegisterFragment extends Fragment implements Validator.ValidationListener {
    private Validator validator;//表单验证器
    //定义界面上的控件,引入表单验证框架
    Spinner option;
    private String idType;
    public int spinnerPosition = 0;//设定跳转至登录界面时下拉框的默认位置
    public String userId;//传递给登陆界面的id
    @Order(1)
    @NotEmpty(message = "此项不能为空")
    @Email
    EditText emailInput;
    @Order(2)
    @NotEmpty(message = "此项不能为空")
    @Pattern(regex = "^[0-9]*$", message = "不符合手机号码格式")
    @Length(min = 11, max = 11, message = "手机号码应为11位")
    EditText phoneInput;
    @Order(3)
    @NotEmpty(message = "此项不能为空")
    EditText usernameInput;
    @Order(4)
    @NotEmpty(message = "密码不能为空")
    @Password(min = 6, message = "密码最少为6位")
    EditText password;
    @Order(5)
    @NotEmpty(message = "确认密码不能为空")
    @ConfirmPassword(message = "两次密码输入不一致")
    EditText password2;

    Button toLogin;
    Button register;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();//初始化界面

    }

    public void init()//初始化各个控件，并定义按钮,下拉框监听器
    {
        emailInput = getView().findViewById(R.id.email);
        phoneInput = getView().findViewById(R.id.phone);
        usernameInput = getView().findViewById(R.id.username);
        option = getView().findViewById(R.id.spinner);
        password = getView().findViewById(R.id.password);
        password2 = getView().findViewById(R.id.password2);
        toLogin = getView().findViewById(R.id.next);
        register = getView().findViewById(R.id.register);
        validator = new Validator(this);
        validator.setValidationListener(this);

        //下拉框监听器
        option.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                选择邮箱
                if (i == 0) {
                    usernameInput.setVisibility(View.INVISIBLE);
                    emailInput.setVisibility(View.VISIBLE);
                    phoneInput.setVisibility(View.INVISIBLE);
                    idType = "email";
                    spinnerPosition = 0;

                }
//                选择手机号
                else if (i == 1) {
                    usernameInput.setVisibility(View.INVISIBLE);
                    emailInput.setVisibility(View.INVISIBLE);
                    phoneInput.setVisibility(View.VISIBLE);
                    idType = "phone";
                    spinnerPosition = 1;
                }
//                选择用户昵称
                else {
                    usernameInput.setVisibility(View.VISIBLE);
                    emailInput.setVisibility(View.INVISIBLE);
                    phoneInput.setVisibility(View.INVISIBLE);
                    idType = "username";
                    spinnerPosition = 2;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //注册按钮监听器
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();

            }
        });

        //跳转登录按钮监听器
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_registerFragment2_to_loginFragment33);
            }
        });
        //测试周试图


    }

    //收集界面填写信息(返回一个String List）
    public List<String> collectData() {
        List<String> informations = new ArrayList<String>();

        if (idType == "email") {
            informations.add("1");//code
            userId = emailInput.getText().toString();

        } else if (idType == "phone") {
            informations.add("2");//code
            userId = phoneInput.getText().toString();
        } else {
            informations.add("3");//code
            userId = usernameInput.getText().toString();
        }
        //add MD5password
        informations.add(userId);
        String encrptedPassword = MD5Util.getMd5(password.getText().toString());
        informations.add(encrptedPassword);//password
        Log.d("collectedData", informations.toString());
        return informations;


    }

    //向后端上传数据
    public String postData(List<String> informations) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", informations.get(0));
            jsonObject.put("id", informations.get(1));
            jsonObject.put("pw", informations.get(2));
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e);
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        System.out.println(jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/user/register/")
                .post(body)
                .build();
        RegisterAndLoginBackendService registerBackendService = new RegisterAndLoginBackendService(client, request);

        return registerBackendService.registerOrLogin();


    }

    @Override
    public void onValidationSucceeded() {
        //收集界面填写信息
        List<String> informations = collectData();
        Log.d("collectedData", informations.toString());
        //开始进行后台操作
        String reply = postData(informations);
        if (reply.contains("1")) {
            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putInt("spinnerPosition", spinnerPosition);
            bundle.putString("userId", userId);
            bundle.putString("password", password.getText().toString());
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_registerFragment2_to_loginFragment33, bundle);
            //跳转到登陆界面，并将注册的信息传递过去
        } else {
            Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
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
                // 显示edittext外的提示，如：必须接受服务协议条款的CheckBox
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
