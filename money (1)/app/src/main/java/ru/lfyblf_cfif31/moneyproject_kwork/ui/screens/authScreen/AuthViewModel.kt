package ru.lfyblf_cfif31.moneyproject_kwork.ui.screens.authScreen

import androidx.lifecycle.ViewModel
import ru.lfyblf_cfif31.moneyproject_kwork.data.firebase.auth.AuthDataStore

class AuthViewModel(
    private val authDataStore: AuthDataStore = AuthDataStore()
): ViewModel() {

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) = authDataStore.signIn(email,password, onSuccess, onError)

    fun registration(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) = authDataStore.registration(email, password,onSuccess, onError)
}