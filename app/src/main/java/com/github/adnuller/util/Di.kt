package com.github.adnuller.util

import android.app.Application
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.lazyModule
import org.luckypray.dexkit.DexKitBridge

val appModule = lazyModule {
    singleOf(::provideMyPackage)
    singleOf(::provideDexKitBridge)
    factoryOf(::provideClassLoader)
}

private fun provideMyPackage(application: Application): MyPackage =
    MyPackage(application.classLoader, application.applicationContext)

private fun provideDexKitBridge(application: Application): DexKitBridge =
    DexKitBridge.create(application.applicationInfo.sourceDir)

private fun provideClassLoader(application: Application): ClassLoader = application.classLoader
