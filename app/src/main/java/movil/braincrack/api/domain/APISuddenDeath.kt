package movil.braincrack.api.domain

import android.util.Log
import movil.braincrack.api.repository.RetrofitHelper
import movil.braincrack.api.ui.model.Temas
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APISuddenDeath {

    fun obtenerDatosDesdeAPI(callback: (List<Temas>) -> Unit) {
        val apiService = RetrofitHelper.getRetrofitService()
        val call = apiService.getTemas()

        call.enqueue(object : Callback<List<Temas>> {
            override fun onResponse(call: Call<List<Temas>>, response: Response<List<Temas>>) {
                if (response.isSuccessful) {
                    val temas = response.body()  // Aquí tienes los datos
                    if (temas != null) {
                        Log.d("APISuddenDeath", "Temas recibidos: $temas")  // Imprime los datos obtenidos
                        callback(temas)  // Llamamos al callback con los datos
                    } else {
                        Log.e("APISuddenDeath", "No se recibieron temas en la respuesta.")
                    }
                } else {
                    Log.e("APISuddenDeath", "Error en la respuesta de la API: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Temas>>, t: Throwable) {
                Log.e("APISuddenDeath", "Fallo en la conexión: ${t.message}")
            }
        })
    }
}