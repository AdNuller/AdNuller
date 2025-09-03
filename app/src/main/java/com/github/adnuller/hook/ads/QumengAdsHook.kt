package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "趣盟广告 (Qumeng)", packageName = "", action = "禁止初始化")
object QumengAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.aiClkAdManager
}