package movil.braincrack.api.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    //Configurar retrofit , creamos el objeto retrofit
    private val URL_BASE = "http://10.0.2.2:3000/"

    fun getRetrofitService(): ApiInterface {
        val retrofit =  Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}