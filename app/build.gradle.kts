import com.android.tools.build.apkzlib.sign.SigningExtension
import com.android.tools.build.apkzlib.sign.SigningOptions
import com.android.tools.build.apkzlib.zfile.ZFiles
import com.android.tools.build.apkzlib.zip.AlignmentRules
import com.android.tools.build.apkzlib.zip.CompressionMethod
import com.android.tools.build.apkzlib.zip.ZFile
import com.android.tools.build.apkzlib.zip.ZFileOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.lsplugin.jgit)
    alias(libs.plugins.lsplugin.resopt)
    alias(libs.plugins.lsplugin.lsparanoid)
}

val keystorePropertiesFile = rootProject.file("./keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

val repo = jgit.repo()
val commitCount = (repo?.commitCount("refs/remotes/origin/master") ?: 25)
println("commitCount: $commitCount")
val latestTag = repo?.latestTag?.removePrefix("v") ?: "1.x.x-SNAPSHOT"

val verCode by extra(commitCount)
val verName by extra(latestTag)
println("verCode: $verCode, verName: $verName")
val androidTargetSdkVersion by extra(36)
val androidMinSdkVersion by extra(26)

lsparanoid {
    seed = null
    classFilter = { true }
    includeDependencies = false
    variantFilter = { true }
}


android {
    namespace = "com.github.adnuller"
    compileSdk = androidTargetSdkVersion

    androidResources.additionalParameters += arrayOf(
        "--allow-reserved-package-id", "--package-id", "0x23"
    )

    signingConfigs {
        create("test") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    defaultConfig {
        minSdk = androidMinSdkVersion
        targetSdk = androidTargetSdkVersion
        versionCode = verCode
        versionName = verName

        signingConfig = signingConfigs.getByName("test")

        buildConfigField("long", "BUILD_TIMESTAMP", "${System.currentTimeMillis()}L")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }

    packagingOptions.apply {
        resources.excludes += mutableSetOf(
            "META-INF/**",
            "**/*.properties",
            "okhttp3/**",
            "schema/**",
            "**.bin",
            "kotlin-tooling-metadata.json"
        )
        dex.useLegacyPackaging = true
    }

    lint.abortOnError = false
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.dexkit)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.core.coroutines)
    compileOnly(libs.xposed)

//    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    /*
    // 穿山甲广告 SDK
    // https://www.csjplatform.com/supportcenter/5397
    compileOnly("com.pangle.cn:ads-sdk-pro:+") //7.0.1.0
    // 百度广告 SDK
    // https://union.baidu.com/docs/sdk/AndroidSDK/10025/
    compileOnly("com.baidu:mobads:+") //9.40.0
    // 广点通广告 SDK
    // https://developers.adnet.qq.com/doc/android/union/union_embed#%E5%B5%8C%E5%85%A5%E5%B9%BF%E5%91%8Asdk
    compileOnly("com.qq.e.union:union:+") //4.650.1520
    // Google AdMob (Mobile Ads)
    // 文档: https://developers.google.com/admob/android/quick-start?hl=zh-cn
    compileOnly("com.google.android.gms:play-services-ads:+") //24.5.0
    // Unity Ads / Unity Mediation
    // 文档: https://docs.unity.com/grow/zh-cn/ads/android-sdk/install-sdk
    compileOnly("com.unity3d.ads:unity-ads:+") //4.16.1
    // Mediation SDK
    // 文档: https://developers.is.com/ironsource-mobile/android/android-sdk/#step-1
    compileOnly("com.unity3d.ads-mediation:mediation-sdk:+") //8.11.0
    // AppLovin SDK
    // 文档: https://help.applovin.com/en/
    compileOnly("com.applovin:applovin-sdk:+") //13.3.1
    // Mintegral / MBridge SDK
    // 文档: https://developers.mintegral.com/
    compileOnly("com.mbridge.msdk.support:reward:+") //16.3.67
    // 快手 KsAd SDK
    compileOnly("com.anythink.sdk:sdk-ads-kuaishou:+") //4.6.30.1
    // TaKuAD SDK
    // https://help.toponad.net/cn/docs/ji-cheng-rnnt
    compileOnly("com.anythink.sdk:core-taku:+") //6.5.15
    compileOnly("com.anythink.sdk:core-china-taku:+") //6.5.15
    // 爱奇艺广告 SDK
    // https://cfe.iqiyi.com/
    compileOnly("com.anythink.sdk:sdk-ads-aqy:+") //1.15.006.51
    // Sigmob SDK
    // https://doc.sigmob.com/Sigmob%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97/SDK%E9%9B%86%E6%88%90%E8%AF%B4%E6%98%8E/Android/%E5%88%9D%E5%A7%8B%E5%8C%96%E8%AF%B4%E6%98%8E/
    compileOnly("com.anythink.sdk:sdk-ads-sigmob:+") //4.24.0
    // OPPO 广告 SDK
    // https://storepic.oppomobile.com/openplat/resource/201902/27/OPPO_SDK_DEV_DOC_V3.0.1.pdf
    compileOnly("com.anythink.sdk:sdk-ads-oppo:+") //8.1.0
    // 阿里妈妈 SDK (Tanx)
    // https://tanx.com/support/documents/1019
    compileOnly("com.tanx:TanxUISDK:+") //3.7.22
    // 游可赢 SDK (Klevin)
    // https://yky.qq.com/doc/android/android-install/
    compileOnly("com.anythink.sdk:sdk-ads-klevin:+") //2.11.0.3
    // 趣盟 SDK (Qumeng)
    compileOnly("com.anythink.sdk:sdk-ads-qumeng:+") //3.473.10.439
    // 米盟 SDK (Mimo)
    // https://dev.mi.com/xiaomihyperos/documentation/detail?pId=1659
    compileOnly("com.miui.zeus:mimo-ad-sdk:+") //5.3.6
    // 京东 SDK (Jingdong)
    // https://help-sdk-doc.jd.com/ansdkDoc/access_docs/Android/SDK%E9%9B%86%E6%88%90/AndroidSDK%E9%9B%86%E6%88%90%E8%AF%B4%E6%98%8E.html
    compileOnly("com.github.JAD-FE-TEAM.JADYunAndroid:jad_yun_sdk:+") // 2.6.20
    // Vivo 广告 SDK
    // https://dev.vivo.com.cn/documentCenter/doc/730#s-jn3t1hg6
    compileOnly("com.anythink.sdk:sdk-ads-vivo:+") //6.2.4.4
    // 华为广告 SDK (Huawei)
    // https://developer.huawei.com/consumer/cn/doc/HMSCore-Guides/publisher-service-integrating-sdk-0000001050066913
    compileOnly("com.huawei.hms:ads-lite:+") //13.4.80.301
    // TapTap
    compileOnly("com.anythink.sdk:sdk-ads-tap:+") //3.16.3.45
    // ---------- 常见统计 / Analytics SDK----------
    // Umeng 友盟 (UMeng Analytics)
    // 文档: https://developer.umeng.com/docs
    compileOnly("com.umeng.umsdk:common:+")
    compileOnly("com.umeng.umsdk:asms:+")
    compileOnly("com.umeng.umsdk:uyumao:+")
    compileOnly("com.umeng.umsdk:apm:+")
    compileOnly("com.umeng.umsdk:push:+")
    compileOnly("com.umeng.umsdk:link:+")
    compileOnly("com.umeng.umsdk:nns:+")
    compileOnly("com.umeng.umsdk:union:+")
    // Firebase Analytics
    // https://firebase.google.com/docs/android/setup?hl=zh-cn
    compileOnly("com.google.firebase:firebase-analytics:+")
    // 神策 Analytics
    compileOnly("com.sensorsdata.analytics.android:SensorsAnalyticsSDK:+")

     */
}

val adbExecutable: String = androidComponents.sdkComponents.adb.get().asFile.absolutePath

val restartQQReader = task("restartQQReader").apply {
    doLast {
        exec {
            commandLine(adbExecutable, "shell", "am", "force-stop", "com.coolapk.market")
        }
        exec {
            commandLine(
                adbExecutable,
                "shell",
                "am",
                "start",
                "$(pm resolve-activity --components com.coolapk.market)"
            )
        }
    }
}

afterEvaluate {
    tasks.getByPath("installDebug").finalizedBy(restartQQReader)
    tasks.getByPath("installRelease").finalizedBy(restartQQReader)
}

val synthesizeDistReleaseApksCI by tasks.registering {
    group = "build"
    dependsOn(":app:packageRelease")
    inputs.files(tasks.named("packageRelease").get().outputs.files)
    val srcApkDir =
        File(project.buildDir, "outputs" + File.separator + "apk" + File.separator + "release")
    if (srcApkDir !in tasks.named("packageRelease").get().outputs.files) {
        val msg =
            "srcApkDir should be in packageRelease outputs, srcApkDir: $srcApkDir, " + "packageRelease outputs: ${
                tasks.named("packageRelease").get().outputs.files.files
            }"
        logger.error(msg)
    }
    val outputAbiVariants = mapOf(
        "arm32" to arrayOf("armeabi-v7a"),
        "arm64" to arrayOf("arm64-v8a"),
        "armAll" to arrayOf("armeabi-v7a", "arm64-v8a"),
        "universal" to arrayOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
    )
    val versionName = android.defaultConfig.versionName
    val versionCode = android.defaultConfig.versionCode
    val outputDir = File(project.buildDir, "outputs" + File.separator + "ci")
    outputAbiVariants.forEach { (variant, _) ->
        val outputName = "AdNuller-v${versionName}-${versionCode}-${variant}.apk"
        outputs.file(File(outputDir, outputName))
    }
    val signConfig = android.signingConfigs.findByName("test")
    val minSdk = android.defaultConfig.minSdk!!
    doLast {
        if (signConfig == null) {
            logger.error("Task :app:synthesizeDistReleaseApksCI: No release signing config found, skip signing")
        }
        val requiredAbiList = listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        outputDir.mkdir()
        val options = ZFileOptions().apply {
            alignmentRule = AlignmentRules.constantForSuffix(".so", 4096)
            noTimestamps = true
            autoSortFiles = true
        }
        require(srcApkDir.exists()) { "srcApkDir not found: $srcApkDir" }
        // srcApkDir should have one apk file
        val srcApkFiles =
            srcApkDir.listFiles()?.filter { it.isFile && it.name.endsWith(".apk") } ?: emptyList()
        require(srcApkFiles.size == 1) { "input apk should have one apk file, but found ${srcApkFiles.size}" }
        val inputApk = srcApkFiles.single()
        val startTime = System.currentTimeMillis()
        ZFile.openReadOnly(inputApk).use { srcApk ->
            // check whether all required abis are in the apk
            requiredAbiList.forEach { abi ->
                val path = "lib/$abi/libdexkit.so"
                require(srcApk.get(path) != null) { "input apk should contain $path, but not found" }
            }
            outputAbiVariants.forEach { (variant, abis) ->
                val outputApk =
                    File(outputDir, "AdNuller-v${versionName}-${versionCode}-${variant}.apk")
                if (outputApk.exists()) {
                    outputApk.delete()
                }
                ZFiles.apk(outputApk, options).use { dstApk ->
                    if (signConfig != null) {
                        val keyStore =
                            KeyStore.getInstance(signConfig.storeType ?: KeyStore.getDefaultType())
                        FileInputStream(signConfig.storeFile!!).use {
                            keyStore.load(it, signConfig.storePassword!!.toCharArray())
                        }
                        val protParam =
                            KeyStore.PasswordProtection(signConfig.keyPassword!!.toCharArray())
                        val keyEntry = keyStore.getEntry(signConfig.keyAlias!!, protParam)
                        val privateKey = keyEntry as KeyStore.PrivateKeyEntry
                        val signingOptions = SigningOptions.builder().setMinSdkVersion(minSdk)
                            .setV1SigningEnabled(minSdk < 24).setV2SigningEnabled(true)
                            .setKey(privateKey.privateKey)
                            .setCertificates(privateKey.certificate as X509Certificate)
                            .setValidation(SigningOptions.Validation.ASSUME_INVALID).build()
                        SigningExtension(signingOptions).register(dstApk)
                    }
                    // add input apk to the output apk
                    srcApk.entries().forEach { entry ->
                        val cdh = entry.centralDirectoryHeader
                        val name = cdh.name
                        val isCompressed =
                            cdh.compressionInfoWithWait.method != CompressionMethod.STORE
                        if (name.startsWith("lib/")) {
                            val abi = name.substring(4).split('/').first()
                            if (abis.contains(abi)) {
                                dstApk.add(name, entry.open(), isCompressed)
                            }
                        } else if (name.startsWith("META-INF/com/android/")) {
                            // drop gradle version
                        } else {
                            // add all other entries to the output apk
                            dstApk.add(name, entry.open(), isCompressed)
                        }
                    }
                    dstApk.update()
                }
            }
        }
        val endTime = System.currentTimeMillis()
        logger.info("Task :app:synthesizeDistReleaseApksCI: completed in ${endTime - startTime}ms")
    }
}