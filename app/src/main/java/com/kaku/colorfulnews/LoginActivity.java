package com.kaku.colorfulnews;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.login)
    Button login;
    String url = "http://v.juhe.cn/sms/send?mobile=17342068539&tpl_id=73189&tpl_value=%23code%23%3D431515&dtype=&key=a4617950e2cc8edf6a538ae55d2d8f10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText())) {
                    Toast.makeText(LoginActivity.this, "手机号为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkPhone(phone.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "手机号码格式有误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(code.getText())) {
                    Toast.makeText(LoginActivity.this, "验证码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void getCode() {
        OkHttpClient client = new OkHttpClient.Builder()
                //设置读取数据的时间
                .readTimeout(5, TimeUnit.SECONDS)
                //对象的创建
                .build();
        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url(url).build();

        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e(TAG, Thread.currentThread().getName() + "结果  " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功
                //子线程
                //main thread1
                Log.e(TAG, Thread.currentThread().getName() + "结果  " + response.body().string());
            }
        });
    }

    protected boolean checkPhone(String str) {
        String pattern = "0?(13|14|15|17|18)[0-9]{9}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            send.setText("重新发送");
            send.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            send.setClickable(false);
            send.setText(millisUntilFinished / 1000 + "秒后重发");
        }
    }
}
