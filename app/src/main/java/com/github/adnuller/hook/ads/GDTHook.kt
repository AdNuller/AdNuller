package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "腾讯广点通广告 (GDT)", packageName = "", action = "禁止初始化"
)
object GDTHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.gdtAdSdk
}