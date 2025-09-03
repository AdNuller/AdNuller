package com.github.adnuller.hook.base

import com.github.adnuller.util.MethodHookParam
import com.github.adnuller.util.replaceAllMethods
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberFunctions

abstract class AdSdkHook : BaseHook() {

    /**
     * 常见的SDK初始化方法名
     */
    protected open val initMethods = listOf(
        "init",
        "preInit",
        "initWithoutStart",
        "asyncInit",
        "syncInit",
        "initialize",
        "start",
        "setup",
        "unInit"
    )

    /**
     * 常见的广告加载方法名
     */
    protected open val loadMethods = listOf("load", "loadAd", "show", "showAd", "request")

    /**
     * 常见的配置方法名
     */
    protected open val configMethods = listOf("config", "configure", "setConfig", "updateAdConfig")

    override fun startHook() {
        val sdkClass = getSdkClass()
        if (sdkClass == null) {
            debug("SDK class not found, skipping hook")
            return
        }
        internalHook(sdkClass)
    }

    /**
     * 内部hook逻辑
     */
    internal fun internalHook(sdkClass: Class<*>) {
        // 阻止初始化方法
        blockInitMethods(sdkClass)

        // 阻止广告加载方法
        blockLoadMethods(sdkClass)

        // 阻止配置方法
        blockConfigMethods(sdkClass)

        // 执行自定义Hook逻辑
        performCustomHook(sdkClass)
    }

    /**
     * 获取SDK主类，子类必须实现
     */
    protected abstract fun getSdkClass(): Class<*>?

    /**
     * 执行自定义Hook逻辑，子类可重写
     */
    protected open fun performCustomHook(sdkClass: Class<*>) {
        // 默认不执行额外操作
    }

    /**
     * 通过 Kotlin + Java 反射筛选实际存在的方法名，避免无效 hook
     * - 使用 kotlin.reflect 的 memberFunctions / declaredFunctions
     * - 合并 Java 的 methods / declaredMethods
     * - 忽略大小写可选，这里保持精确匹配
     */
    protected open fun Class<*>.existingMethodNames(candidates: List<String>): List<String> {
        return try {
            val kClass = this.kotlin
            val existed = buildSet<String> {
                // Kotlin 反射方法名
                kClass.memberFunctions.forEach { add(it.name) }
                kClass.declaredFunctions.forEach { add(it.name) }
                // Java 反射方法名
                methods.forEach { add(it.name) }
                declaredMethods.forEach { add(it.name) }
            }
            val result = candidates.filter { it in existed }
            if (result.isEmpty()) debug("No candidate methods found in ${simpleName}: ${candidates.joinToString()}")
            result
        } catch (t: Throwable) {
            debug("existingMethodNames fallback due to ${t.javaClass.simpleName}: ${t.message}")
            // 发生异常时退回原始列表，保持兼容
            candidates
        }
    }

    /**
     * 阻止初始化方法
     */
    protected open fun blockInitMethods(sdkClass: Class<*>) {
        val targets = sdkClass.existingMethodNames(initMethods)
        if (targets.isNotEmpty()) debug("Init methods to block(${sdkClass.simpleName}): ${targets.joinToString()}")
        targets.forEach { method ->
            sdkClass.replaceAllMethods(method) { param ->
                debug("Blocked ${sdkClass.simpleName}.$method")
                provideDefaultReturnValue(param, getInitMethodReturnValue())
            }
        }
    }

    /**
     * 阻止广告加载方法
     */
    protected open fun blockLoadMethods(sdkClass: Class<*>) {
        val targets = sdkClass.existingMethodNames(loadMethods)
        if (targets.isNotEmpty()) debug("Load methods to block(${sdkClass.simpleName}): ${targets.joinToString()}")
        targets.forEach { method ->
            sdkClass.replaceAllMethods(method) { param ->
                debug("Blocked ${sdkClass.simpleName}.$method")
                provideDefaultReturnValue(param, getLoadMethodReturnValue())
            }
        }
    }

    /**
     * 阻止配置方法
     */
    protected open fun blockConfigMethods(sdkClass: Class<*>) {
        val targets = sdkClass.existingMethodNames(configMethods)
        if (targets.isNotEmpty()) debug("Config methods to block(${sdkClass.simpleName}): ${targets.joinToString()}")
        targets.forEach { method ->
            sdkClass.replaceAllMethods(method) { param ->
                debug("Blocked ${sdkClass.simpleName}.$method")
                provideDefaultReturnValue(param, getConfigMethodReturnValue())
            }
        }
    }

    /**
     * 获取初始化方法的返回值，子类可重写
     */
    protected open fun getInitMethodReturnValue(): Any? = false

    /**
     * 获取加载方法的返回值，子类可重写
     */
    protected open fun getLoadMethodReturnValue(): Any? = null

    /**
     * 获取配置方法的返回值，子类可重写
     */
    protected open fun getConfigMethodReturnValue(): Any? = null

    /**
     * 根据方法返回类型提供默认返回值：
     * - boolean/Boolean -> false
     * - 数值类型 -> 0 / 0L / 0f / 0.0 / 0.toShort() / 0.toByte()
     * - char/Character -> '\u0000'
     * - String -> ""
     * - List/Set/Map 接口 -> emptyX
     * - 其它 -> null
     * 若子类提供的自定义值 (base) 不为 null，则优先返回该值。
     */
    protected open fun provideDefaultReturnValue(param: MethodHookParam, base: Any?): Any? {
        val member = param.method
        val returnType = if (member is java.lang.reflect.Method) member.returnType else Void.TYPE
        // 若已有自定义值且类型兼容，直接返回
        if (base != null) {
            if (returnType.isPrimitive) return base
            if (returnType.isAssignableFrom(base.javaClass)) return base
        }
        return when (returnType) {
            Void.TYPE, Void::class.java -> null
            java.lang.Boolean.TYPE, java.lang.Boolean::class.java -> false
            Integer.TYPE, Integer::class.java -> 0
            java.lang.Long.TYPE, java.lang.Long::class.java -> 0L
            java.lang.Short.TYPE, java.lang.Short::class.java -> 0.toShort()
            java.lang.Byte.TYPE, java.lang.Byte::class.java -> 0.toByte()
            java.lang.Float.TYPE, java.lang.Float::class.java -> 0f
            java.lang.Double.TYPE, java.lang.Double::class.java -> 0.0
            Character.TYPE, Character::class.java -> '\u0000'
            String::class.java -> ""
            else -> {
                when {
                    returnType.isInterface -> when (returnType.name) {
                        List::class.java.name, MutableList::class.java.name, java.util.List::class.java.name -> emptyList<Any>()
                        Set::class.java.name, MutableSet::class.java.name, java.util.Set::class.java.name -> emptySet<Any>()
                        Map::class.java.name, MutableMap::class.java.name, java.util.Map::class.java.name -> emptyMap<Any, Any>()
                        else -> null
                    }

                    else -> null
                }
            }
        }
    }
}