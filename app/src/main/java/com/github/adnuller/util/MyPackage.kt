package com.github.adnuller.util

import android.content.Context

class MyPackage(private val mClassLoader: ClassLoader, mContext: Context) {

    init {
        instance = this
    }

    // -------------------------------------------------------------------------------

    // 穿山甲广告sdk (Pangle)
    val ttAdSdk by Weak { "com.bytedance.sdk.openadsdk.TTAdSdk" from mClassLoader }

    // 腾讯广点通广告 SDK (GDT)
    val gdtAdSdk by Weak { "com.qq.e.comm.managers.GDTAdSdk" from mClassLoader }

    // 百度广告 SDK
    val baiduAdConfig by Weak { "com.baidu.mobads.sdk.api.BDAdConfig" from mClassLoader }

    // 快手广告 SDK (Kuaishou)
    val ksAdSdk by Weak { "com.kwad.sdk.api.KsAdSDK" from mClassLoader }

    // Google AdMob
    val mobileAds by Weak { "com.google.android.gms.ads.MobileAds" from mClassLoader }

    // Unity Ads
    val unityAds by Weak { "com.unity3d.ads.UnityAds" from mClassLoader }

    val levelPlay by Weak { "com.unity3d.mediation.LevelPlay" from mClassLoader }

    // AppLovin
    val appLovinSdk by Weak { "com.applovin.sdk.AppLovinSdk" from mClassLoader }

    // TaKu/TopOn
    val atSDK by Weak { "com.anythink.core.api.ATSDK" from mClassLoader }

    // 爱奇艺广告 SDK (Aqy)
    val qySdk by Weak { "com.mcto.sspsdk.QySdk" from mClassLoader }

    // Sigmob 广告 SDK
    val windAds by Weak { "com.sigmob.windad.WindAds" from mClassLoader }

    // Oppo 广告 SDK
    val mobAdManager by Weak { "com.heytap.msp.mobad.api.MobAdManager" from mClassLoader }

    // 阿里妈妈广告 SDK (Tanx)
    val tanxSdk by Weak { "com.alimm.tanx.ui.TanxSdk" from mClassLoader }

    // 游可赢 SDK (Klevin)
    val klevinManager by Weak { "com.tencent.klevin.KlevinManager" from mClassLoader }

    // 趣盟 广告 SDK (Qumeng)
    val aiClkAdManager by Weak { "com.qumeng.advlib.api.AiClkAdManager" from mClassLoader }

    // 米盟广告 SDK (Mimo)
    val mimoSdk by Weak { "com.miui.zeus.mimo.sdk.MimoSdk" from mClassLoader }
    val miMoNewSdk by Weak { "com.xiaomi.ad.mediation.MimoNewSdk" from mClassLoader }

    // 京东广告 SDK (JingDong)
    val jadYunSdk by Weak { "com.jd.ad.sdk.bl.initsdk.JADYunSdk" from mClassLoader }

    // Vivo 广告 SDK
    val vivoAdManager by Weak { "com.vivo.mobilead.manager.VivoAdManager" from mClassLoader }

    // 华为广告 SDK (Huawei)
    val huaweiAds by Weak { "com.huawei.hms.ads.HwAds" from mClassLoader }

    // TapTap 广告 SDK (TapTap)
    val tapAdSdk by Weak { "com.tapsdk.tapad.TapAdSdk" from mClassLoader }

    // 友盟 广告 SDK (Umeng Ads)
    val umAdSdk by Weak { "com.umeng.union.UMUnionSdk" from mClassLoader }

    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------

    // 友盟统计 SDK (Umeng)
    val umConfigure by Weak { "com.umeng.commonsdk.UMConfigure" from mClassLoader }

    val pushAgent by Weak { "com.umeng.message.PushAgent" from mClassLoader }

    // Firebase Analytics
    val firebaseAnalytics by Weak { "com.google.firebase.analytics.FirebaseAnalytics" from mClassLoader }

    // 神策分析 SDK (Sensors Analytics)
    val sensorsDataAPI by Weak { "com.sensorsdata.analytics.android.sdk.SensorsDataAPI" from mClassLoader }

    // -------------------------------------------------------------------------------

    companion object {
        @Volatile
        lateinit var instance: MyPackage
    }
}