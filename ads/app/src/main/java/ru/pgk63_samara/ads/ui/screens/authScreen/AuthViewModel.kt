package ru.pgk63_samara.ads.ui.screens.authScreen

import androidx.lifecycle.ViewModel
import ru.pgk63_samara.ads.data.firebase.auth.AuthDataStore

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