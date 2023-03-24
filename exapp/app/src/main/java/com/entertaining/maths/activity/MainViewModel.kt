package com.entertaining.maths.activity

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.entertaining.maths.util.BaseViewModel
import com.entertaining.maths.util.UiActions
import com.entertaining.maths.util.UiActionsImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(uiActions: UiActions): BaseViewModel(uiActions) {

    private val masterKey: MasterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val encryptedSharedPrefs = EncryptedSharedPreferences.create(
        context,
        SHARED_PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    init {
        if (!encryptedSharedPrefs.contains(KEY_POINTS)) {
            encryptedSharedPrefs.edit().putFloat(KEY_POINTS,0f).commit()
        }
    }

    companion object PrefsConfig {
        private const val SHARED_PREFS_NAME = "encryptedSharedPrefs"

        const val KEY_POINTS = "points"
        const val KEY_COUNTER = "counter"
    }
}
