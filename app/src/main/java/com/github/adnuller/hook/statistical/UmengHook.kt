package com.github.adnuller.hook.statistical

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AnalyticsSdkHook

@HookApp(appName = "友盟统计 (Umeng)", packageName = "", action = "禁止初始化")
object UmengHook : AnalyticsSdkHook() {
    override fun startHook() {
        instance.apply {
            umConfigure?.apply {
                internalHook(this)
            }

            pushAgent?.apply {
                internalHook(this)
            }
        }

    }

    override fun getSdkClass(): Class<*>? = null
}