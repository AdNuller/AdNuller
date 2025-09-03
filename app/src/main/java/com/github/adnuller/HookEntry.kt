package com.github.adnuller

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.content.res.Resources
import android.content.res.XModuleResources
import android.os.Build
import android.os.Bundle
import com.github.adnuller.annotations.HookApp
import com.github.adnuller.hook.base.BaseHook
import com.github.adnuller.util.Log
import com.github.adnuller.util.MyPackage
import com.github.adnuller.util.appModule
import com.github.adnuller.util.getPackageVersion
import com.github.adnuller.util.hookBeforeMethod
import com.github.adnuller.util.is64
import com.github.adnuller.util.isBuiltIn
import com.github.adnuller.util.new
import com.github.adnuller.util.safeCast
import com.github.adnuller.util.systemContext
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.lazyModules
import org.luckypray.dexkit.DexKitBridge


class HookEntry : IXposedHookLoadPackage, IXposedHookZygoteInit {
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
        moduleRes = getModuleRes(modulePath)
    }


    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val moduleClassLoader = this::class.java.classLoader
        Instrumentation::class.java.hookBeforeMethod(
            "callApplicationOnCreate", Application::class.java
        ) { param ->
            val application = param.args[0].safeCast<Application>() ?: return@hookBeforeMethod
            try {
                startKoin {
                    androidContext(application)
                    lazyModules(appModule)
                }
            } catch (_: Exception) {

            }
            application.registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {

                override fun onActivityResumed(activity: Activity) {
                    Log.d("onActivityResumed: $activity")
                }

                override fun onActivityDestroyed(activity: Activity) {

                }

                override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                    // 打印所有传参
                    Log.d("onActivityCreated: $activity, $bundle")
                    val intent = activity.intent
                    val extras = intent.extras
                    if (extras != null) {
                        Log.d("---------------------${activity.javaClass.name}---------------------")
                        for (key in extras.keySet()) {
                            Log.d("Extra: $key -> ${extras.get(key)}")
                        }
                        Log.d("---------------------${activity.javaClass.name}----------")
                    }
                }

                override fun onActivityStarted(activity: Activity) {

                }


                override fun onActivityPaused(activity: Activity) {
                }


                override fun onActivityStopped(activity: Activity) {

                }

                override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

                }

            })

            Log.d("${BuildConfig.APPLICATION_ID} process launched ...")
            Log.d("${BuildConfig.APPLICATION_ID} version: ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE}) from $modulePath${if (isBuiltIn) "(BuiltIn)" else ""}")
            Log.d("${BuildConfig.APPLICATION_ID} version: ${systemContext.getPackageVersion(lpparam.packageName)} (${if (is64) "64" else "32"}bit)")
            Log.d("SDK: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT}); Phone: ${Build.BRAND} ${Build.MODEL}")

            MyPackage(application.classLoader, application.applicationContext)

            val moduleDexKitBridge = DexKitBridge.create(modulePath)

            moduleDexKitBridge.use { bridge ->
                bridge.findClass {
                    matcher {
                        annotations {
                            add {
                                type = HookApp::class.java.name
                            }
                        }
                    }
                }.forEach { data ->
                    val hooker = moduleClassLoader?.let { classLoader ->
                        // 创建kotlin 的object类
                        data.getInstance(classLoader).new()
                    }
                    hooker?.let {
                        startHook(it as BaseHook)
                    }
                }
            }

        }
        lateInitHook = Activity::class.java.hookBeforeMethod("onResume") {
            startLateHook()
            lateInitHook?.unhook()
        }
    }


    private fun startHook(hooker: BaseHook) {
        try {
            hookers.add(hooker)
            hooker.onHook()
        } catch (e: Throwable) {
            Log.e(e)
        }
    }

    private fun startLateHook() {
        hookers.forEach {
            try {
                it.lateInitHook()
            } catch (e: Throwable) {
                Log.e(e)
            }
        }
    }


    companion object {

        const val TAG = "AdNuller"

        lateinit var modulePath: String
        lateinit var moduleRes: Resources

        private val hookers = ArrayList<BaseHook>()
        private var lateInitHook: XC_MethodHook.Unhook? = null

        @JvmStatic
        fun getModuleRes(path: String): Resources {
            return XModuleResources.createInstance(path, null)
        }

        init {
            try {
                System.loadLibrary("dexkit")
            } catch (_: Exception) {
            }
        }
    }
}
