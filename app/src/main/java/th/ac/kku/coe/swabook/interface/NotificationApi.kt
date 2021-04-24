package th.ac.kku.coe.swabook.`interface`

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import th.ac.kku.coe.swabook.Constants.Constants.Companion.CONTENT_TYPE
import th.ac.kku.coe.swabook.Constants.Constants.Companion.SERVER_KEY
import th.ac.kku.coe.swabook.dataclass.PushNotification

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY", "Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}