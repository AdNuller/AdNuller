package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "阿里妈妈广告 (Tanx)", packageName = "", action = "禁止初始化")
object TanxAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.tanxSdk
}