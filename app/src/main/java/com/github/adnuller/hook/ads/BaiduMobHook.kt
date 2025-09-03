package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "百度移动广告 (BaiduMob)", packageName = "", action = "禁止初始化"
)
object BaiduMobHook : AdSdkHook() {

    override fun getSdkClass(): Class<*>? = instance.baiduAdConfig
}