package copa.gol.bet.app.data.remoteConfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.delay
import copa.gol.bet.app.utils.FirebaseUtils

class RemoteConfigDataStore {

    private val remoteConfig = FirebaseRemoteConfig.getInstance(FirebaseUtils.secondary) // secondary

    private val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
        .setMinimumFetchIntervalInSeconds(1) // 43200 - 12 hour
        .build()

    init {
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
    }

    suspend fun getUrl(
        count: Int = 0
    ): String? {
        return try {
            remoteConfig.fetchAndActivate()

            val url = remoteConfig.getValue("url").asString()

            if(url.isEmpty() && count < 5){
                delay(2000L)
                getUrl(count = count+1)
            }else {
                url
            }
        }catch (e:Exception){
            null
        }
    }
}