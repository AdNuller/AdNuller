package com.github.adnuller.hook.base

import com.github.adnuller.util.MethodHookParam
import com.github.adnuller.util.replaceAllMethods

/**
 * 统计分析SDK Hook基类
 * 为统计分析SDK Hook提供通用方法和模式
 */
abstract class AnalyticsSdkHook : BaseHook() {

    /**
     * 常见的统计初始化方法名
     */
    protected open val initMethods =
        listOf("init", "preInit", "initialize", "start", "setup", "startWithAppId", "register")

    /**
     * 常见的事件追踪方法名
     */
    protected open val trackMethods = listOf("track", "trackEvent", "log", "logEvent", "report")

    /**
     * 常见的用户属性设置方法名
     */
    protected open val userPropertyMethods =
        listOf("setUserId", "setUserProperty", "identify", "setup")

    /**
     * 常见的配置方法名
     */
    protected open val configMethods =
        listOf("config", "configure", "setConfig", "enable", "disable", "startWithConfigOptions")


    override fun startHook() {
        val sdkClass = getSdkClass()
        if (sdkClass == null) {
            debug("Analytics SDK class not found, skipping hook")
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

        // 阻止事件追踪方法
        blockTrackMethods(sdkClass)

        // 阻止用户属性设置方法
        blockUserPropertyMethods(sdkClass)

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
     * 阻止初始化方法
     */
    protected open fun blockInitMethods(sdkClass: Class<*>) {
        initMethods.forEach { method ->
            sdkClass.replaceAllMethods(method) { param ->
                debug("Blocked analytics ${sdkClass.simpleName}.$method")
                provideDefaultReturnValue(param, getInitMethodReturnValue())
            }
        }
    }

    /**
     * 阻止事件追踪方法
     */
    protected open fun blockTrackMethods(sdkClass: Class<*>) {
        trackMethods.forEach { method ->
            sdkClass.replaceAllMethods(method) { param ->
                debug("Blocked analytics ${sdkClass.simpleName}.$method")
                provideDefaultReturnValue(param, getTrackMethodReturnValue())
            }
        }
    }

    /**
     * 阻止用户属性设置方法
     */
    protected open fun blockUserPropertyMethods(sdkClass: Class<*>) {
        userPropertyMethods.forEach { method ->
            sdkClass.replaceAllMethods(method) { param ->
                debug("Blocked analytics ${sdkClass.simpleName}.$method")
                provideDefaultReturnValue(param, getUserPropertyMethodReturnValue())
            }
        }
    }

    /**
     * 阻止配置方法
     */
    protected open fun blockConfigMethods(sdkClass: Class<*>) {
        configMethods.forEach { method ->
            sdkClass.replaceAllMethods(method) { param ->
                debug("Blocked analytics ${sdkClass.simpleName}.$method")
                provideDefaultReturnValue(param, getConfigMethodReturnValue())
            }
        }
    }

    /**
     * 获取初始化方法的返回值，子类可重写
     */
    protected open fun getInitMethodReturnValue(): Any? = null

    /**
     * 获取追踪方法的返回值，子类可重写
     */
    protected open fun getTrackMethodReturnValue(): Any? = null

    /**
     * 获取用户属性方法的返回值，子类可重写
     */
    protected open fun getUserPropertyMethodReturnValue(): Any? = null

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