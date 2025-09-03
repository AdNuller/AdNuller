package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(appName = "游可赢广告 (Klevin)", packageName = "", action = "禁止初始化")
object KlevinAdsHook : AdSdkHook() {
    override fun getSdkClass(): Class<*>? = instance.klevinManager
}