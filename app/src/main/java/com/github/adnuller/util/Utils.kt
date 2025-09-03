package com.github.adnuller.util


import android.app.AndroidAppHelper
import android.content.Context
import com.github.adnuller.HookEntry
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty


class Weak<T>(val initializer: () -> T?) {
    private var weakReference: WeakReference<T?>? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = weakReference?.get() ?: let {
        weakReference = WeakReference(initializer())
        weakReference
    }?.get()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = WeakReference(value)
    }
}

val systemContext: Context
    get() {
        val activityThread = "android.app.ActivityThread".findClassOrNull(null)
            ?.callStaticMethod("currentActivityThread")!!
        return activityThread.callMethodAs("getSystemContext")
    }

fun Context.getPackageVersion(packageName: String) = try {
    packageManager.getPackageInfo(packageName, 0).run {
        String.format("${packageName}@%s(%s)", versionName, getVersionCode(packageName))
    }
} catch (e: Throwable) {
    Log.e(e)
    "(unknown)"
}


fun getVersionCode(packageName: String) = try {
    @Suppress("DEPRECATION") systemContext.packageManager.getPackageInfo(packageName, 0).versionCode
} catch (e: Throwable) {
    Log.e(e)
    null
} ?: 6080000


val currentContext by lazy { AndroidAppHelper.currentApplication() as Context }

val packageName: String by lazy { currentContext.packageName }

val isBuiltIn
    get() = HookEntry.modulePath.endsWith("so")

val is64
    get() = currentContext.applicationInfo.nativeLibraryDir.contains("64")

inline fun <reified T> Any?.safeCast(): T? = this as? T


fun <T : Any> MutableCollection<T>.addNotNull(item: T?) = item?.let { add(it) }

fun Any?.onDestroy() = runCatching { this?.callMethod("onDestroy") }