package com.xiaojinzi.componentdemo.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaojinzi.base.ModuleConfig;
import com.xiaojinzi.component.anno.RouterAnno;
import com.xiaojinzi.component.bean.ActivityResult;
import com.xiaojinzi.component.impl.BiCallback;
import com.xiaojinzi.component.impl.Router;
import com.xiaojinzi.component.impl.RouterErrorResult;
import com.xiaojinzi.component.impl.RouterRequest;
import com.xiaojinzi.component.impl.RouterResult;
import com.xiaojinzi.componentdemo.R;

@RouterAnno(
        path = ModuleConfig.App.TEST_REPEAT_REQUEST_CODE
)
public class TestRepeatRequestCodeAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_repeat_request_code_act);
        findViewById(R.id.bt_test).setOnClickListener(v -> {
            Router.with(this)
                    .host(ModuleConfig.App.NAME)
                    .path(ModuleConfig.App.TEST_REPEAT_REQUEST_CODE)
                    .requestCode(123)
                    .forwardForResult(new BiCallback.BiCallbackAdapter<ActivityResult>() {
                    });
        });
    }

}
