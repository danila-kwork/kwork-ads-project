package copa.gol.bet.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val options1 = FirebaseOptions.Builder()
            .setProjectId("option-way-hh-ru")
            .setApplicationId("1:69089145806:android:bea38e87e3f07b9ae1b19f")
            .setApiKey("AIzaSyB5-7ofiUfO_zhF8xcmSp9B8pE8vEPv7DM")
            .build()

        val options2 = FirebaseOptions.Builder()
            .setProjectId("cpgb-1f602")
            .setApplicationId("1:767005592694:android:050a97f1c5632b2f432357")
            .setApiKey("AIzaSyCE_C9cUAGmnsJzIrrlt1BkKU5O5VJcaCo")
            .build()

        FirebaseApp.initializeApp(this /* Context */, options1, "first");
        FirebaseApp.initializeApp(this /* Context */, options2, "secondary");
    }
}