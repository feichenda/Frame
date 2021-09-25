package com.lenovo.frame;

import android.widget.TextView;

import com.lenovo.frame.base.BaseActivity;
import com.lenovo.frame.base.BaseModel;
import com.lenovo.frame.base.BaseObserver;
import com.lenovo.frame.entity.User;
import com.lenovo.frame.net.ExceptionHandle;
import com.lenovo.frame.net.RetrofitClient;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * @author feizai
 * @date 2021/5/3 0003 上午 9:16:31
 */
public class TextActivity extends BaseActivity {

    TextView textView;
    private RetrofitClient client;
    private RetrofitClient retrofitClient;

    public TextActivity() {
        super(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        textView = findViewById(R.id.login);
        client = RetrofitClient.getInstance(this);
        retrofitClient = RetrofitClient.getInstance(this, "http://172.21.58.82:8080/smm/");
        get();
    }

    private void get() {
        retrofitClient.selectAllUser(new BaseObserver<BaseModel<User>>(this) {
            @Override
            protected void showDialog() {

            }

            @Override
            protected void hideDialog() {

            }

            @Override
            protected void successful(BaseModel<User> userBaseModel) {
                List<User> datas = userBaseModel.getDatas();
                for (User data : datas) {
                    textView.append(data.toString() + "\n");
                }
            }

            @Override
            protected void defeated(BaseModel<User> userBaseModel) {
                Logger.e(userBaseModel.getMessage());
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                Logger.e(e.getMessage());
            }
        });
    }
}
