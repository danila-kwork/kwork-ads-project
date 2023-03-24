package com.entertaining.maths.fragments.home

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.entertaining.maths.R
import com.entertaining.maths.activity.MainViewModel.PrefsConfig.KEY_COUNTER
import com.entertaining.maths.activity.MainViewModel.PrefsConfig.KEY_POINTS
import com.entertaining.maths.util.BaseViewModel
import com.entertaining.maths.util.ProblemGenerator
import com.entertaining.maths.util.UiActions
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    val points = MutableLiveData<String>()

    val problem = MutableLiveData<ProblemGenerator.Problem>()

    private lateinit var adCallback: () -> Unit

    private var hintUsed = false

    private var counter = 0

    private lateinit var sharedPrefs: SharedPreferences

    private val totalCounter get() = sharedPrefs.getInt(KEY_COUNTER, 0)

    fun initSharedPrefs(sharedPreferences: SharedPreferences) {
        sharedPrefs = sharedPreferences
        loadPoints()
        loadProblem()
    }

    fun initAds(callback: () -> Unit) {
        adCallback = callback
    }

    fun loadProblem() {
        val lvl = when {
            totalCounter < 50 -> 1
            totalCounter < 100 -> 2
            else -> 3
        }

        counter++
        problem.value = ProblemGenerator.generateProblem(lvl)
    }

    private fun loadPoints() {
        val currentPoints = sharedPrefs.getFloat(KEY_POINTS, 0f)
        val nf = NumberFormat.getInstance().apply { maximumFractionDigits = 4 }
        points.value = getString(R.string.points, nf.format(currentPoints))
    }

    fun checkInput(input: Int): Boolean {
        return problem.value!!.result == input
    }

    fun getHint(): String {
        hintUsed = true
        return problem.value!!.result.toString()
    }

    fun onResult() {
        sharedPrefs.edit().putInt(KEY_COUNTER, totalCounter + 1).apply()
        counter++

        if (!hintUsed) {
            hintUsed = false
            sharedPrefs.edit().putFloat(KEY_POINTS, sharedPrefs.getFloat(KEY_POINTS, 0f) + 0.0001f).apply()
            loadPoints()
        }

        if (counter % 3 == 0) {
            adCallback()
        }

        loadProblem()
    }

    fun isRewardAvailable(): Boolean {
        return sharedPrefs.getFloat(KEY_POINTS, 0f) >= 10f
    }

    fun onSuccessfulAd() {
        sharedPrefs.edit().putFloat(KEY_POINTS, sharedPrefs.getFloat(KEY_POINTS, 0f) + 0.01f).apply()
        loadPoints()
    }
}
