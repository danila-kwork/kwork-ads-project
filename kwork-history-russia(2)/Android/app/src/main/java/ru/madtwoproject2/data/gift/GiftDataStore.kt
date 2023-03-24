package ru.madtwoproject2.data.gift

import android.content.Context
import org.joda.time.LocalDate


class GiftDataStore(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("Gift", Context.MODE_PRIVATE)

    fun isGiftVisibility(): Boolean {
        val date = LocalDate.now()
        val dayOfMonth = sharedPreferences.getInt("dayOfMonth", 0)

        if(dayOfMonth == 0)
            return true

        return date.dayOfMonth != dayOfMonth
    }

    fun saveDate() {
        sharedPreferences.edit()
            .putInt("dayOfMonth", LocalDate.now().dayOfMonth)
            .apply()
    }
}