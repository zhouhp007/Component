package com.ehi.componentdemo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ehi.base.ModuleConfig;
import com.ehi.component.anno.EHiRouterAnno;
import com.ehi.component.impl.EHiRouter;
import com.ehi.component.impl.EHiRouterResult;
import com.ehi.component.impl.EHiRxRouter;
import com.ehi.component.support.EHiCallbackAdapter;
import com.ehi.component.support.EHiRouterInterceptor;
import com.ehi.componentdemo.R;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@EHiRouterAnno(
        host = ModuleConfig.App.NAME,
        value = ModuleConfig.App.TEST_ROUTER,
        desc = "测试跳转的界面"
)
public class TestRouterAct extends AppCompatActivity {

    private TextView tv_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_router_act);
        getSupportActionBar().setTitle("高级路由测试");
        tv_detail = findViewById(R.id.tv_detail);
    }

    private void addInfo(@Nullable EHiRouterResult routerResult, @Nullable Exception error, @NonNull String url, @Nullable Integer requestCode) {
        if (requestCode == null) {
            if (routerResult != null) {
                tv_detail.setText(tv_detail.getText() + "\n\n普通跳转成功,目标:" + url);
            } else {
                tv_detail.setText(tv_detail.getText() + "\n\n普通跳转失败,目标:" + url + ",error = " + error.getClass().getSimpleName() + " ,errorMsg = " + error.getMessage());
            }
        } else {
            if (routerResult != null) {
                tv_detail.setText(tv_detail.getText() + "\n\nRequestCode=" + requestCode + "普通跳转成功,目标:" + url);
            } else {
                tv_detail.setText(tv_detail.getText() + "\n\nRequestCode=" + requestCode + "普通跳转失败,目标:" + url + ",error = " + error.getClass().getSimpleName() + " ,errorMsg = " + error.getMessage());
            }
        }
    }

    public void openUriTest(View view) {
        EHiRouter.open(this, "EHi://component1/test?data=openUriTest");
    }

    public void normalJump(View view) {
        EHiRouter
                .with(TestRouterAct.this)
                .host("component1")
                .path("test")
                .query("data", "normalJump")
                .putString("name", "cxj1")
                .putInt("age", 25)
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, "component1/test?data=normalJump", null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, "component1/test?data=normalJump", null);
                    }
                });
    }

    public void normalJumpTwice(View view) {

        EHiRxRouter
                .with(this)
                .host("component1")
                .path("test")
                .query("data", "normalJumpTwice1")
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, "component1/test?data=normalJumpTwice1", null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, "component1/test?data=normalJumpTwice1", null);
                    }
                });

        EHiRxRouter
                .with(this)
                .host("component1")
                .path("test")
                .query("data", "normalJumpTwice2")
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, "component1/test?data=normalJumpTwice2", null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, "component1/test?data=normalJumpTwice2", null);
                    }
                });

    }

    public void jumpGetData(View view) {

        EHiRxRouter
                .with(this)
                .host("component1")
                .path("test")
                .query("data", "jumpGetData")
                .requestCode(123)
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, "component1/test?data=jumpGetData", 123);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, "component1/test?data=jumpGetData", 123);
                    }
                });

    }

    public void rxJumpGetData(View view) {

        EHiRxRouter
                .with(this)
                .host("component1")
                .path("test")
                .query("data", "rxJumpGetData")
                .requestCode(456)
                .intentCall()
                .subscribe(new Consumer<Intent>() {
                    @Override
                    public void accept(Intent intent) throws Exception {
                        tv_detail.setText(tv_detail.getText() + "\n\nrequestCode=456,目标:component1/test?data=rxJumpGetData,获取目标页面数据成功啦：Data = " + intent.getStringExtra("data"));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tv_detail.setText(tv_detail.getText() + "\n\nrequestCode=456,目标:component1/test?data=rxJumpGetData,获取目标页面数据失败,error = " + throwable.getClass().getSimpleName() + " ,errorMsg = " + throwable.getMessage());
                    }
                });

    }

    public void rxJumpGetDataStartWithTask(final View view) {

        SingleTransformer<String, Intent> transformer = EHiRxRouter
                .with(this)
                .host("component1")
                .path("test")
                .query("data", "rxJumpGetDataFromQuery")
                .putString("data", "rxJumpGetDataFromBundle")
                .requestCode(789)
                .intentSingleTransformer();

        Single
                .just("value")
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        view.setEnabled(false);
                        tv_detail.setText(tv_detail.getText() + "\n\n耗时2秒,模拟耗时任务后执行跳转界面拿数据");
                    }
                })
                .observeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent(new BiConsumer<String, Throwable>() {
                    @Override
                    public void accept(String s, Throwable throwable) throws Exception {
                        view.setEnabled(true);
                    }
                })
                .compose(transformer)
                .subscribe(new Consumer<Intent>() {
                    @Override
                    public void accept(Intent intent) throws Exception {
                        tv_detail.setText(tv_detail.getText() + "\n\nrequestCode=789,目标:component1/test?data=rxJumpGetData,执行耗时任务后获取目标页面数据成功啦：Data = " + intent.getStringExtra("data"));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tv_detail.setText(tv_detail.getText() + "\n\nrequestCode=789,目标:component1/test?data=rxJumpGetData,执行耗时任务后获取目标页面数据失败,error = " + throwable.getClass().getSimpleName() + " ,errorMsg = " + throwable.getMessage());
                    }
                });

    }

    public void jumpWithInterceptor(View view) {

        EHiRxRouter
                .with(this)
                .host(ModuleConfig.Component1.NAME)
                .path(ModuleConfig.Component1.TEST)
                .query("data", "jumpWithInterceptor")
                .requestCode(123)
                .interceptors(new EHiRouterInterceptor() {
                    @Override
                    public void intercept(final Chain chain) throws Exception {
                        final ProgressDialog dialog = ProgressDialog.show(chain.request().getRawContext(), "温馨提示", "耗时操作进行中,2秒后结束", true, false);
                        dialog.show();
                        Single
                                .fromCallable(new Callable<String>() {
                                    @Override
                                    public String call() throws Exception {
                                        return "test";
                                    }
                                })
                                .delay(2, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        dialog.dismiss();
                                        chain.proceed(chain.request());
                                    }
                                });
                    }
                },new EHiRouterInterceptor() {
                    @Override
                    public void intercept(final Chain chain) throws Exception {
                        final ProgressDialog dialog = ProgressDialog.show(chain.request().getRawContext(), "温馨提示", "再一次耗时操作,2秒后结束", true, false);
                        dialog.show();
                        Single
                                .fromCallable(new Callable<String>() {
                                    @Override
                                    public String call() throws Exception {
                                        return "test";
                                    }
                                })
                                .delay(2, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        dialog.dismiss();
                                        chain.proceed(chain.request());
                                    }
                                });
                    }
                })
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, "component1/test?data=jumpWithInterceptor", 123);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, "component1/test?data=jumpWithInterceptor", 123);
                    }
                });
    }

    public void clearInfo(View view) {
        tv_detail.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            tv_detail.setText(tv_detail.getText() + "\n\nrequestCode=" + requestCode + "的跳转返回数据啦：Data = " + data.getStringExtra("data"));
        } /*else {
            tv_detail.setText(tv_detail.getText() + "\n requestCode=" + requestCode + "的跳转返回数据啦,但是未被处理");
        }*/
    }

    public void testQueryPass(View view) {
        EHiRouter
                .with(this)
                .host(ModuleConfig.Component1.NAME)
                .path(ModuleConfig.Component1.TEST_QUERY)
                .query("name", "我是小金子")
                .query("pass", "我是小金子的密码")
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, ModuleConfig.Component1.NAME + "/" + ModuleConfig.Component1.TEST_QUERY + "?name=我是小金子&pass=我是小金子的密码", null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, ModuleConfig.Component1.NAME + "/" + ModuleConfig.Component1.TEST_QUERY, null);
                    }
                });
    }

    public void testLogin(View view) {
        EHiRouter
                .with(this)
                .host(ModuleConfig.Component1.NAME)
                .path(ModuleConfig.Component1.TEST_LOGIN)
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, ModuleConfig.Component1.NAME + "/" + ModuleConfig.Component1.TEST_LOGIN, null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, ModuleConfig.Component1.NAME + "/" + ModuleConfig.Component1.TEST_LOGIN, null);
                    }
                });
    }

    public void testDialog(View view) {
        EHiRouter
                .with(this)
                .host(ModuleConfig.Component1.NAME)
                .path(ModuleConfig.Component1.TEST_DIALOG)
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, ModuleConfig.Component1.NAME + "/" + ModuleConfig.Component1.TEST_DIALOG, null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, ModuleConfig.Component1.NAME + "/" + ModuleConfig.Component1.TEST_DIALOG, null);
                    }
                });
    }

    public void testGotoKotlin(View view) {
        EHiRouter
                .with(this)
                .host(ModuleConfig.Component2.NAME)
                .path(ModuleConfig.Component2.MAIN)
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, ModuleConfig.Component2.NAME + "/" + ModuleConfig.Component2.MAIN, null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, ModuleConfig.Component2.NAME + "/" + ModuleConfig.Component2.MAIN, null);
                    }
                });
    }

    public void testCustomIntent(View view) {

        EHiRouter
                .with(this)
                .host(ModuleConfig.Component2.NAME)
                .path(ModuleConfig.Component2.MAIN)
                .intentConsumer(new com.ehi.component.support.Consumer<Intent>() {
                    @Override
                    public void accept(@NonNull Intent intent) throws Exception {
                        Toast.makeText(TestRouterAct.this, intent.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .navigate(new EHiCallbackAdapter() {
                    @Override
                    public void onSuccess(@NonNull EHiRouterResult result) {
                        addInfo(result, null, ModuleConfig.Component2.NAME + "/" + ModuleConfig.Component2.MAIN, null);
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        addInfo(null, error, ModuleConfig.Component2.NAME + "/" + ModuleConfig.Component2.MAIN, null);
                    }
                });

    }

}