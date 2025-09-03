package com.github.adnuller.hook.ads

import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.AdSdkHook

@HookApp(
    appName = "Unity Ads", packageName = "", action = "禁止初始化"
)
object UnityAdsHook : AdSdkHook() {

    override fun startHook() {
        instance.apply {
            unityAds?.apply {
                internalHook(this)
            }
            levelPlay?.apply {
                internalHook(this)
            }
        }
    }

    override fun getSdkClass(): Class<*>? = null
}