package th.ac.kku.coe.swabook

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import th.ac.kku.coe.swabook.Constants.Constants
import th.ac.kku.coe.swabook.`interface`.NotificationApi

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }
}