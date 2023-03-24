package ru.madtwoproject1.common

import android.util.Log
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

fun vpn(): Boolean {
    var iface = ""
    try {
        for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
            if (networkInterface.isUp) iface = networkInterface.name
            Log.d("DEBUG", "IFACE NAME: $iface")
            if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                return true
            }
        }
    } catch (e1: SocketException) {
        e1.printStackTrace()
    }
    return false
}