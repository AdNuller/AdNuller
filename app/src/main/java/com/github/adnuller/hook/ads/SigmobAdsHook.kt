package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "Sigmob", packageName = "", action = "禁止初始化")
object SigmobAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.windAds
}