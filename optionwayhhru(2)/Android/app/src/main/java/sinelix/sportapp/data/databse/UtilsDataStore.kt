package sinelix.sportapp.data.databse

import android.content.Context

class UtilsDataStore(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("utils", Context.MODE_PRIVATE)

    fun getUrl(): String? {
        return sharedPreferences.getString("url", null)
    }

    fun saveUrl(url: String?) {
        sharedPreferences.edit()
            .putString("url", url)
            .apply()
    }
}