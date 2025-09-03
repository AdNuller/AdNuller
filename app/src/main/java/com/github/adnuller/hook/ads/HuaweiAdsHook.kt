package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "华为广告 (HuaweiSDK)", packageName = "", action = "禁止初始化"
)
object HuaweiAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.huaweiAds
}