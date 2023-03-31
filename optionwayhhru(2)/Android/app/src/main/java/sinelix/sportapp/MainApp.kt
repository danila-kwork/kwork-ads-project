package sinelix.sportapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val options1 = FirebaseOptions.Builder()
            .setProjectId("option-way-hh-ru")
            .setApplicationId("1:69089145806:android:525cd101fc48a042e1b19f")
            .setApiKey("AIzaSyB5-7ofiUfO_zhF8xcmSp9B8pE8vEPv7DM")
            .build()

        val options2 = FirebaseOptions.Builder()
            .setProjectId("nemuka-79e12")
            .setApplicationId("1:1037326896175:android:47aa88f6664f7a39420ada")
            .setApiKey("AIzaSyBtSmuB1y3v-LfwPldmKtPzkhUN_kKa978")
            .build()

        FirebaseApp.initializeApp(this /* Context */, options1, "first");
        FirebaseApp.initializeApp(this /* Context */, options2, "secondary");
    }
}