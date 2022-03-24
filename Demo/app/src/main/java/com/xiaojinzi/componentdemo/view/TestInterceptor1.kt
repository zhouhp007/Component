package com.xiaojinzi.componentdemo.view

import com.xiaojinzi.component.impl.RouterInterceptor

class TestInterceptor1: RouterInterceptor {
    override fun intercept(chain: RouterInterceptor.Chain) {
        chain.proceed(request = chain.request())
    }
}