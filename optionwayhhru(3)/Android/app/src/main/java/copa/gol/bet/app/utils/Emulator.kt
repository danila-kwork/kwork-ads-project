package copa.gol.bet.app.utils

import android.os.Build
import com.airbnb.lottie.BuildConfig
import java.util.*

fun checkIsEmu(): Boolean {

    if (BuildConfig.DEBUG) return false

    val phoneModel = Build.MODEL
    val buildProduct = Build.PRODUCT
    val buildHardware = Build.HARDWARE
    val brand = Build.BRAND

    return (Build.FINGERPRINT.startsWith("generic")
            || phoneModel.contains("google_sdk")
            || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
            || phoneModel.contains("Emulator")
            || phoneModel.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || buildHardware.equals("goldfish")
            || brand.contains("google")
            || buildHardware.equals("vbox86")
            || buildProduct.equals("sdk")
            || buildProduct.equals("google_sdk")
            || buildProduct.equals("sdk_x86")
            || buildProduct.equals("vbox86p")
            || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
            || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
            || buildHardware.lowercase(Locale.getDefault()).contains("nox")
            || buildProduct.lowercase(Locale.getDefault()).contains("nox"))
            || (brand.startsWith("generic") && Build.DEVICE.startsWith("generic"))
}