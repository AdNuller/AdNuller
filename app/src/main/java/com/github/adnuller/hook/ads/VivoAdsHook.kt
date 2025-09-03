package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "Vivo 广告 (VivoAds)", packageName = "", action = "禁止初始化")
object VivoAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.vivoAdManager
}