package com.entertaining.maths.fragments.withdraw

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.widget.Toast
import com.entertaining.maths.R
import com.entertaining.maths.activity.MainViewModel.PrefsConfig.KEY_POINTS
import com.entertaining.maths.network.MyApi
import com.entertaining.maths.util.BaseViewModel
import com.entertaining.maths.util.UiActions
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(uiActions: UiActions): BaseViewModel(uiActions) {

    fun withdraw(wallet: String, sharedPrefs: SharedPreferences, callback: () -> Unit) {
        val amount = sharedPrefs.getFloat(KEY_POINTS, 0f)
        val nf = NumberFormat.getInstance().apply { maximumFractionDigits = 4 }

        val task = MyApi.retrofitService.requestWithdrawal(wallet, nf.format(amount))
        task.enqueue(object : Callback<Void> {

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(R.string.withdrawal_error, Toast.LENGTH_SHORT)
            }

            @SuppressLint("ApplySharedPref")
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                sharedPrefs.edit().putFloat(KEY_POINTS, 0f).commit()
                toast(R.string.withdraw_success, Toast.LENGTH_LONG)
                callback()
            }

        })
    }

}
