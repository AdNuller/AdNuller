package com.github.adnuller.util

import android.util.Log
import com.github.adnuller.BuildConfig
import com.github.adnuller.HookEntry
import de.robv.android.xposed.XposedBridge

object Log {

    @JvmStatic
    private fun doLog(f: (String, String) -> Int, obj: Any?, toXposed: Boolean = false) {
        val str = if (obj is Throwable) Log.getStackTraceString(obj) else obj.toString()

        if (str.length > maxLength) {
            val chunkCount: Int = str.length / maxLength
            for (i in 0..chunkCount) {
                val max: Int = maxLength * (i + 1)
                if (max >= str.length) {
                    doLog(f, str.substring(maxLength * i))
                } else {
                    doLog(f, str.substring(maxLength * i, max))
                }
            }
        } else {
            f(HookEntry.Companion.TAG, str)
            if (toXposed) XposedBridge.log("${HookEntry.Companion.TAG} : $str")
        }
    }

    @JvmStatic
    fun d(obj: Any?) {
        if (!BuildConfig.DEBUG) return
        doLog(Log::d, obj)
    }

    @JvmStatic
    fun i(obj: Any?) {
        doLog(Log::i, obj)
    }

    @JvmStatic
    fun e(obj: Any?) {
        doLog(Log::e, obj, true)
    }

    @JvmStatic
    fun v(obj: Any?) {
        doLog(Log::v, obj)
    }

    @JvmStatic
    fun w(obj: Any?) {
        doLog(Log::w, obj)
    }

    private const val maxLength = 3000
}