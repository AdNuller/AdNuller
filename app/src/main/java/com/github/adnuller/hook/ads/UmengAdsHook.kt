package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "友盟广告 (Umeng Ads)", packageName = "", action = "禁止初始化")
object UmengAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.umAdSdk
}