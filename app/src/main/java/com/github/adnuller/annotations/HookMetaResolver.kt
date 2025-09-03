package com.github.adnuller.annotations

/**
 * 用于解析 Hook 类上的注解，提取应用信息。
 */
object HookMetaResolver {

    data class HookMeta(
        val appName: String,
        val packageName: String,
        val action: String,
        val versions: Array<String>,
    ) {


        override fun hashCode(): Int {
            var result = appName.hashCode()
            result = 31 * result + packageName.hashCode()
            result = 31 * result + action.hashCode()
            result = 31 * result + versions.contentHashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as HookMeta

            if (appName != other.appName) return false
            if (packageName != other.packageName) return false
            if (action != other.action) return false
            if (!versions.contentEquals(other.versions)) return false

            return true
        }
    }

    fun resolve(clazz: Class<*>): HookMeta? {
        clazz.getAnnotation(HookApp::class.java)?.let {
            return HookMeta(
                appName = it.appName,
                packageName = it.packageName,
                action = it.action,
                versions = it.versions
            )
        }
        return null
    }
}