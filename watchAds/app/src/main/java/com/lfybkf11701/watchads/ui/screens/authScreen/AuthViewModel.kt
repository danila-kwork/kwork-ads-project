package com.lfybkf11701.watchads.ui.screens.authScreen

import androidx.lifecycle.ViewModel
import com.lfybkf11701.watchads.data.firebase.auth.AuthDataStore

class AuthViewModel(
    private val authDataStore: AuthDataStore = AuthDataStore()
): ViewModel() {

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        try {
            authDataStore.signIn(email,password, onSuccess, onError)
        }catch (e: Exception){
            onError(e.message ?: "ERROR")
        }
    }

    fun registration(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        try {
            authDataStore.registration(email, password,onSuccess, onError)
        }catch (e: Exception){
            onError(e.message ?: "ERROR")
        }
    }
}