package ru.movie.quiz.data.fcm

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val SERVER_KEY = "AAAA36G0Q8g:APA91bGsIhbOiO5qTX574zlfzvlApBipOe_xXEOTnXCUin3n9oRBiMX9EcXz6wKslHNrio7p5h44fBGYBLLoahRdBokc37rzoGoqmaKyLt6iliOMH1r0-O66S10PdWgcMg0ckU2FbXBD"

interface FcmApi {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:application/json")
    @POST("/fcm/send")
    suspend fun pushFirebase(
        @Body body: PushNotificationBody
    ): Response<Unit?>
}

val fcmApi = Retrofit.Builder()
    .baseUrl("https://fcm.googleapis.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create<FcmApi>()

data class PushNotificationBody(
    val data: NotificationDataBody,
    val to:String = "topics/users"
)

data class NotificationDataBody(
    val title:String,
    val message:String
)