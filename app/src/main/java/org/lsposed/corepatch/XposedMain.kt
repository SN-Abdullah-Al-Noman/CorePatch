package org.lsposed.corepatch

import android.os.Build
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import org.lsposed.corepatch.hook.ApkSignatureVerifierHook
import org.lsposed.corepatch.hook.ApkSigningBlockUtilsHook
import org.lsposed.corepatch.hook.MessageDigestHook
import org.lsposed.corepatch.hook.PackageManagerServiceUtilsHook
import org.lsposed.corepatch.hook.ScanPackageUtilsHook
import org.lsposed.corepatch.hook.StrictJarVerifierHook

class XposedMain : XposedModule() {
    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        super.onModuleLoaded(param)
        XposedHelper.setXposedModule(this)
    }

    override fun onSystemServerStarting(param: XposedModuleInterface.SystemServerStartingParam) {
        super.onSystemServerStarting(param)
        XposedHelper.log("onSystemServerStarting: Current sdk version is ${Build.VERSION.SDK_INT}")
        XposedHelper.setHostClassLoader(param.classLoader)
        Config.printAllConfig()

        listOf(
            ApkSignatureVerifierHook,
            ApkSigningBlockUtilsHook,
            MessageDigestHook,
            PackageManagerServiceUtilsHook,
            ScanPackageUtilsHook,
            StrictJarVerifierHook,
        ).forEach { it.init() }
    }
}
