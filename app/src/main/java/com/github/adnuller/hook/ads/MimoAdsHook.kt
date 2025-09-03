package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "米盟广告 (Mimo)", packageName = "", action = "禁止初始化")
object MimoAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.mimoSdk
}