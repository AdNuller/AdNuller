package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "TapTap Ads", packageName = "", action = "禁止初始化")
object TapTapAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.tapAdSdk
}