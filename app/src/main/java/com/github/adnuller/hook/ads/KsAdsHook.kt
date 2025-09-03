package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "快手广告 (KsAdSDK)", packageName = "", action = "禁止初始化"
)
object KsAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.ksAdSdk
}