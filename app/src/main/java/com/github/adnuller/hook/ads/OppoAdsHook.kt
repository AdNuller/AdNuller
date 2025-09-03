package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "Oppo 广告", packageName = "", action = "禁止初始化")
object OppoAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.mobAdManager
}