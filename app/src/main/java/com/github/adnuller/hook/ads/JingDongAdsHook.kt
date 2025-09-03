package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "京东广告 (JingDong)", packageName = "", action = "禁止初始化"
)
object JingDongAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.jadYunSdk
}