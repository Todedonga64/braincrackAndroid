package movil.braincrack.api.repository

import movil.braincrack.api.ui.model.Temas
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("temas")
    fun getTemas(): Call<List<Temas>>
}