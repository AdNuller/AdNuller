package com.github.adnuller.annotations

/**
 * 统一的 Hook 注解。可以取代 Run / RunJiaGu。
 * mode 用于区分加固或其他特殊模式，以便在 HookEntry 中决定执行时机。
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class HookApp(
    val appName: String,
    val packageName: String,
    val action: String = "",
    val versions: Array<String> = []
)