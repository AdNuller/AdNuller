package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "穿山甲广告 (Pangle)", packageName = "", action = "禁止初始化"
)
object PangleHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.ttAdSdk
}