package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "AppLovin广告 (AppLovinSdk)", packageName = "", action = "禁止初始化"
)
object AppLovinHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.appLovinSdk
}