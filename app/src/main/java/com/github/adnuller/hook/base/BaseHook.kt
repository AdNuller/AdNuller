package com.github.adnuller.hook.base

import android.content.Context
import com.github.adnuller.annotations.HookMetaResolver
import com.github.adnuller.util.Log
import com.github.adnuller.util.MyPackage
import com.github.adnuller.util.getPackageVersion
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.luckypray.dexkit.DexKitBridge

/**
 * @项目名 : QDReaderHook
 * @作者 : MissYang
 * @创建时间 : 2025/1/4 15:43
 * @介绍 :
 */
abstract class BaseHook : KoinComponent {


    protected val instance by inject<MyPackage>()
    protected val bridge by inject<DexKitBridge>()
    protected val classLoader by inject<ClassLoader>()
    protected val context by inject<Context>()

    protected lateinit var meta: HookMetaResolver.HookMeta
    protected lateinit var versions: Array<String>
    protected lateinit var appName: String
    protected lateinit var action: String

    protected lateinit var targetPackageName: String

    protected val versionName by lazy { context.getPackageVersion(targetPackageName) }

    /**
     * Hook状态枚举
     */
    enum class HookState {
        PENDING,        // 等待执行
        INITIALIZING,   // 初始化中
        RUNNING,        // 运行中
        COMPLETED,      // 完成
        FAILED,         // 失败
        DISABLED        // 禁用
    }

    @Volatile
    private var hookState: HookState = HookState.PENDING

    private var hookStartTime: Long = 0
    private var hookEndTime: Long = 0

    /**
     * 获取Hook执行耗时（毫秒）
     */
    val executionTime: Long
        get() = if (hookEndTime > hookStartTime) hookEndTime - hookStartTime else 0

    /**
     * 获取当前Hook状态
     */
    val currentState: HookState
        get() = hookState

    /**
     * 检查Hook是否应该执行
     */
    protected open fun shouldExecute(): Boolean {
        return isMetaValid() && isVersionSupported() && isPackageMatched()
    }

    /**
     * 检查元数据是否有效
     */
    private fun isMetaValid(): Boolean {
        val hookMeta = HookMetaResolver.resolve(this::class.java)
        if (hookMeta == null) {
            error("No valid Hook metadata found for ${this::class.java.name}")
            return false
        }
        meta = hookMeta
        return true
    }

    /**
     * 检查版本是否支持
     */
    private fun isVersionSupported(): Boolean {
        if (meta.versions.isNotEmpty() && !meta.versions.any { it in versionName }) {
            debug("Version not supported: $versionName, supported: ${meta.versions.contentToString()}")
            return false
        }
        return true
    }

    /**
     * 检查包名是否匹配
     */
    private fun isPackageMatched(): Boolean {
        if (meta.packageName.isNotEmpty() && meta.packageName != "all" && meta.packageName != targetPackageName) {
            debug("Package not matched: $targetPackageName, target: ${meta.packageName}")
            return false
        }
        return true
    }

    /**
     * 执行Hook主逻辑
     */
    fun onHook() {
        try {
            hookState = HookState.INITIALIZING
            hookStartTime = System.currentTimeMillis()

            if (!shouldExecute()) {
                hookState = HookState.DISABLED
                return
            }

            // 初始化目标包名等基础信息
            initializeMetadata()

            debug("Starting Hook: ${meta.appName} => ${meta.action}")

            hookState = HookState.RUNNING

            // 执行前置检查
            if (!preHookCheck()) {
                hookState = HookState.FAILED
                error("Pre-hook check failed")
                return
            }

            // 执行主要Hook逻辑
            startHook()

            hookState = HookState.COMPLETED
            hookEndTime = System.currentTimeMillis()

            debug("Hook completed: ${meta.appName}, time: ${executionTime}ms")

        } catch (e: Throwable) {
            hookState = HookState.FAILED
            hookEndTime = System.currentTimeMillis()
            onHookFailed(e)
            error("Hook failed: ${meta.appName}, error: ${e.message}")
        }
    }

    /**
     * 初始化元数据信息
     */
    private fun initializeMetadata() {
        versions = meta.versions
        appName = meta.appName
        action = meta.action
        targetPackageName = meta.packageName.ifEmpty { context.packageName }
    }

    /**
     * 前置检查，子类可重写进行自定义检查
     */
    protected open fun preHookCheck(): Boolean = true

    /**
     * Hook失败时的处理逻辑，子类可重写
     */
    protected open fun onHookFailed(e: Throwable) {
        // 默认实现：记录错误日志
    }

    protected abstract fun startHook()
    open fun lateInitHook() {}

    fun debug(msg: String) = Log.d(msg)
    fun error(msg: String) = Log.e("Hook: $appName => $msg")
}