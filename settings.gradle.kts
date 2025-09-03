pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github.*")
            }
        }
        maven("https://api.xposed.info/") {
            mavenContent {
                includeGroup("de.robv.android.xposed")
            }
        }
        maven("https://artifact.bytedance.com/repository/pangle") {
            mavenContent {
                includeGroupByRegex("com\\.pangle.*")
            }
        }
        maven("https://repos.xiaomi.com/maven") {
            credentials {
                username = "mimo-developer"
                password =
                    "AKCp8ih1PFG9tV8qaLyws67dLGZi8udFM39SfsHgihN15cgsiRvHuxj8JzFmuZjaViVeNawaA"
            }
            mavenContent {
                includeGroupByRegex("com\\.miui.*")
                includeGroupByRegex("com\\.xiaomi.*")
            }
        }
        maven("https://jfrog.takuad.com/artifactory/china_sdk") {
            mavenContent {
                includeGroupByRegex("com\\.anythink.*")
            }
        }
        maven("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_support") {
            mavenContent {
                includeGroupByRegex("com\\.mbridge.*")
            }
        }
        maven("https://developer.huawei.com/repo/") {
            mavenContent {
                includeGroupByRegex("com\\.huawei.*")
            }
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "AdNuller"
include(":app")
