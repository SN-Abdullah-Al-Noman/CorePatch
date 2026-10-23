package org.lsposed.corepatch

import org.lsposed.corepatch.XposedHelper.prefs

object Config {
    const val BYPASS_DOWNGRADE = "downgrade"
    const val BYPASS_MIN_SDK = "bypass_min_sdk"
    const val BYPASS_VERIFICATION = "bypass_verification"
    const val BYPASS_RESOURCE_ARSC_RESTRICTIONS = "bypass_resource_arsc_restrictions"
    const val BYPASS_DIGEST = "bypass_digest"
    const val BYPASS_EXACT_SIGNATURE_MATCH = "bypass_exact_sig_match"
    const val USE_PREVIOUS_SIGNATURES = "use_previous_signatures"
    const val ALLOW_HIDDEN_APIS_FOR_SYSTEM_APPS = "allow_hidden_apis_for_system_apps"
    const val BYPASS_SHARED_USER = "bypass_shared_user"
    const val DISABLE_VERIFICATION_AGENT = "disable_verification_agent"
    const val BYPASS_BLOCK = "bypass_block"

    fun printAllConfig() {
        XposedHelper.log("$BYPASS_VERIFICATION: ${prefs.getBoolean(BYPASS_VERIFICATION, false)}")
    }

    fun isBypassVerificationEnabled(): Boolean = prefs.getBoolean(BYPASS_VERIFICATION, false)

    // Removed features are hard-disabled so stale preferences cannot reactivate them.
    fun isBypassDowngradeEnabled() = false
    fun isBypassMinSdkEnabled() = false
    fun isBypassResourceArscRestrictionsEnabled() = false
    fun isBypassDigestEnabled() = false
    fun isBypassExactSignatureMatch() = false
    fun isUsePreviousSignaturesEnabled() = false
    fun isAllowHiddenApisForSystemAppsEnabled() = false
    fun isBypassSharedUserEnabled() = false
    fun isDisableVerificationAgentEnabled() = false
    fun isBypassBlockEnabled() = false

    fun getConfig(key: String): Boolean = if (key == BYPASS_VERIFICATION) {
        prefs.getBoolean(BYPASS_VERIFICATION, false)
    } else false

    fun setConfig(key: String, value: Boolean) {
        if (key == BYPASS_VERIFICATION) {
            App.rwPrefs?.edit()?.putBoolean(BYPASS_VERIFICATION, value)?.apply()
        }
    }
}
