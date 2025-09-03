package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "TaKu/TopOn广告", packageName = "", action = "禁止初始化"
)
object TaKuADHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.atSDK
}