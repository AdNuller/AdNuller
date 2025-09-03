package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "Google 移动广告 (Mobile Ads)", packageName = "", action = "禁止初始化"
)
object MobileAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.mobileAds
}