package com.github.adnuller.hook.statistical

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AnalyticsSdkHook
import com.github.adnuller.util.replaceAllMethods

@HookApp(appName = "神策分析 (Sensors Analytics)", packageName = "", action = "禁止初始化")
object SensorsAnalyticsHook : AnalyticsSdkHook() {
    override fun startHook() {
        instance.sensorsDataAPI?.replaceAllMethods("startWithConfigOptions") {
            debug("Hook SensorsDataAPI.startWithConfigOptions")
            null
        }
    }

    override fun getSdkClass(): Class<*>? = instance.sensorsDataAPI
}