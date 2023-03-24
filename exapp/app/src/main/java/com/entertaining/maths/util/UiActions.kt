package com.entertaining.maths.util

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.entertaining.maths.annotations.ToastDuration

/**
 * Use this class in order to get access to actions of user interface
 */
interface UiActions {

    val context: Context

    /** Shows string from resources as toast */
    @MainThread
    fun toast(@StringRes resId: Int, @ToastDuration length: Int)

    /** Show [message] as toast */
    @MainThread
    fun toast(message: String, @ToastDuration length: Int)

    /** Gets string from resources */
    fun getString(@StringRes resId: Int, vararg args: Any): String
}

class UiActionsImpl(override val context: Context) : UiActions {

    override fun toast(resId: Int, length: Int) =
        toast(getString(resId), length)

    override fun toast(message: String, length: Int) =
        Toast.makeText(context, message, length).show()

    override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)
}

