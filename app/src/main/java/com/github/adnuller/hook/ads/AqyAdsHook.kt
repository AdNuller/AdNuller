package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "爱奇艺广告 (Aqy)", packageName = "", action = "禁止初始化"
)
object AqyAdsHook : AdSdkHook() {

    override fun getSdkClass(): Class<*>? = instance.qySdk

}