package ru.money_ads.moneyproject_kwork

import android.app.Application
import com.yandex.mobile.ads.common.MobileAds

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this){  }
    }
}