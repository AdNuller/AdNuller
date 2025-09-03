package com.github.adnuller.hook.statistical

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AnalyticsSdkHook

@HookApp(appName = "Firebase Analytics", packageName = "", action = "禁止上报")
object FirebaseAnalyticsHook : AnalyticsSdkHook() {

    override fun getSdkClass(): Class<*>? = instance.firebaseAnalytics
}