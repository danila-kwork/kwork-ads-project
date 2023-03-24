package ru.movie.quiz

import android.app.Application
import com.yandex.mobile.ads.common.MobileAds

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this){  }

//        val firebaseMessaging = FirebaseMessaging.getInstance()
//        firebaseMessaging.subscribeToTopic("topics/users")
    }
}