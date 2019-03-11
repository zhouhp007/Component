package com.ehi.componentdemo;

import android.util.Log;

import com.ehi.component.anno.EHiGlobalInterceptorAnno;
import com.ehi.component.impl.EHiRouterInterceptor;

/**
 * 全局的一个监测的拦截器
 * time   : 2019/02/19
 *
 * @author : xiaojinzi 30212
 */
@EHiGlobalInterceptorAnno
public class MonitorInterceptor implements EHiRouterInterceptor{

    @Override
    public void intercept(Chain chain) throws Exception {
        String uriStr = chain.request().uri.toString();
        Log.d("全局监控拦截器", "uri = " + uriStr);
        chain.proceed(chain.request());
    }

}