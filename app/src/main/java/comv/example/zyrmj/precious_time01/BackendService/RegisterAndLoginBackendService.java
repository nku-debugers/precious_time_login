package comv.example.zyrmj.precious_time01.BackendService;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterAndLoginBackendService {

    OkHttpClient client;
    Request request;

    public RegisterAndLoginBackendService(OkHttpClient client, Request request) {
        this.client = client;
        this.request = request;
    }


    public String registerOrLogin() {
        ExecutorService exec = Executors.newCachedThreadPool();
        List<Future<String>> results = new ArrayList<Future<String>>();

//        FutureTask<String> futureTask=new FutureTask<String>(new LoginThread(client,request)) ;
//
//        Future future=exec.submit(futureTask);
        results.add(exec.submit(new RegisterOrLoginThread(client, request)));
        String answer = null;
//        try {
//            answer = future.get().toString();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            answer = results.get(0).get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(answer);
        return answer;
    }
}

class RegisterOrLoginThread implements Callable<String> {
    OkHttpClient client;
    Request request;

    public RegisterOrLoginThread(OkHttpClient client, Request request) {
        this.client = client;
        this.request = request;
    }

    @Override
    public String call() {
        try {
            Log.d("Thread", "begin");
            Response response = client.newCall(request).execute();
            String sss;
            sss = response.body().string();
            System.out.println("Sucess");
            System.out.println(sss);
            return sss;
        } catch (Exception e) {
            System.out.println(e);
            return "error";
        }
    }
}
