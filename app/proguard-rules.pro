#---------------------------------基本指令区---------------------------------
-optimizationpasses 5
-flattenpackagehierarchy
-allowaccessmodification
-keepattributes Signature,Exceptions,*Annotation*,
                InnerClasses,PermittedSubclasses,EnclosingMethod,
                Deprecated,SourceFile,LineNumberTable,Exceptions,
                AnnotationDefault,RuntimeVisibleAnnotations
-keepattributes 'SourceFile'
-obfuscationdictionary 'dictionary.txt'
-classobfuscationdictionary 'dictionary.txt'
-packageobfuscationdictionary 'dictionary.txt'
-dontusemixedcaseclassnames
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keepclasseswithmembers class * {
    @androidx.annotation.* <fields>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# Xposed API
-keep class de.robv.android.xposed.** { *; }
-keep public class * implements de.robv.android.xposed.IXposedHookLoadPackage
-keep class com.github.adnuller.HookEntry { *; }
-dontwarn de.robv.android.xposed.**
-dontwarn io.github.libxposed.api.**

# Keep Kotlin reflection metadata
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-keep class kotlin.jvm.internal.** { *; }

# Keep Kotlin companion objects
-keepclassmembers class * {
    static final *** Companion;
    static final *** INSTANCE;
}

# 保留资源绑定类
-keep class **.*R$* { *; }
-keep class **.R$* { *; }

# Remove Kotlin Intrinsics (no side effects)
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkExpressionValueIsNotNull(...);
    public static void checkFieldIsNotNull(...);
    public static void checkFieldIsNotNull(...);
    public static void checkNotNull(...);
    public static void checkNotNull(...);
    public static void checkNotNullExpressionValue(...);
    public static void checkNotNullParameter(...);
    public static void checkParameterIsNotNull(...);
    public static void checkReturnedValueIsNotNull(...);
    public static void checkReturnedValueIsNotNull(...);
    public static void throwUninitializedPropertyAccessException(...);
}

-dontwarn javax.annotation.**
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# ---------------------------------删除日志---------------------------------
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
#    public static *** e(...);
    public static *** wtf(...);
    public static *** println(...);
}

# 自定义 Log wrapper（移除其调用及日志常量）
-assumenosideeffects class com.github.adnuller.util.Log {
    public static *** d(...);
    public static *** i(...);
#    public static *** e(...);
    public static *** v(...);
    public static *** w(...);
    private static *** doLog(...);
}

# XposedBridge.log：通常用于模块内部与 Xposed 日志交互。
# 如果你希望完全剥离 Xposed 的日志输出（release 更安静，且更难被分析），启用下面规则（当前启用）。
-assumenosideeffects class de.robv.android.xposed.XposedBridge {
    public static void log(...);
}

# 如果需要保留 XposedBridge.log（例如供线上问题排查），请把上面替换为：
# -keep class de.robv.android.xposed.XposedBridge { public static void log(...); }

# ========================================================================
# 定制：类名可以被混淆，但 BaseHook.onHook 方法名不能被混淆
# 同时保证带 @HookApp 的类不会被裁剪（以便 DexKit 运行时扫描），但允许它们被重命名
# ========================================================================

# 保留注解类型自身
-keep @interface com.github.adnuller.annotations.HookApp

# 保留所有被 @HookApp 标记类的 "类名"（成员允许混淆 / 优化）
-keep,allowobfuscation @com.github.adnuller.annotations.HookApp class *

## 保留这些 Hook 类中用于反射构造的双参构造函数 (ClassLoader, DexKitBridge)
## （若不存在也不会报错）
#-keepclassmembers,allowobfuscation @com.github.adnuller.annotations.HookApp class * {
#    <init>(java.lang.ClassLoader, org.luckypray.dexkit.DexKitBridge);
#}
#
## 确保这些类不会被裁剪（否则仅 keepnames 可能在无引用时被移除）
#-keep @com.github.adnuller.annotations.HookApp class * {
#    <init>(...);
#}
#
## 仅强制保留特定构造函数 (ClassLoader, DexKitBridge)；允许其余成员重命名
#-keepclassmembers,allowobfuscation @com.github.adnuller.annotations.HookApp class * {
#    public <init>(java.lang.ClassLoader, org.luckypray.dexkit.DexKitBridge);
#}

# 为了防止 R8 内联过度导致字串常量再度暴露
-optimizations !code/simplification/arithmetic,!code/simplification/cast
