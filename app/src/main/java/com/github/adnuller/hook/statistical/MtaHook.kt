package com.github.adnuller.hook.statistical

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AnalyticsSdkHook

@HookApp(appName = "腾讯移动分析 (MTA)", packageName = "", action = "禁止初始化")
object MtaHook : AnalyticsSdkHook() {
    override fun getSdkClass(): Class<*>? = null

}