package movil.braincrack.api.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import movil.braincrack.api.domain.APISuddenDeath
import movil.braincrack.api.ui.model.Temas

class SuddenDeathViewModel : ViewModel() {

    // Definimos las variables para el estado de los temas
    var idPreguntas by mutableStateOf(listOf<String>())
    var opcionesPreguntas by mutableStateOf(listOf<Int>())
    var respuestasPreguntas by mutableStateOf(listOf<Int>())
    var opciones2Preguntas by mutableStateOf(listOf<List<String>>())
    var temas by mutableStateOf<List<Temas>>(emptyList())

    private val apiSuddenDeath = APISuddenDeath()

    // Función para obtener los datos de la API
    fun obtenerDatos() {
        viewModelScope.launch {
            apiSuddenDeath.obtenerDatosDesdeAPI { temas ->
                // Aquí llamamos a la función de "extraerDatos" para guardar las variables
                extraerDatos(temas)
            }
        }
    }

    // Función que extrae los datos de los temas obtenidos
    private fun extraerDatos(temas: List<Temas>) {
        val idList = mutableListOf<String>()
        val opcionesList = mutableListOf<Int>()
        val respuestasList = mutableListOf<Int>()
        val opciones2List = mutableListOf<List<String>>()

        for (tema in temas) {
            for (pregunta in tema.preguntas) {
                idList.add(pregunta.id)
                opcionesList.add(pregunta.opciones)
                respuestasList.add(pregunta.respuesta)
                opciones2List.add(pregunta.opciones2)
            }
        }

        // Actualizamos el estado del ViewModel
        this.temas = temas
        idPreguntas = idList
        opcionesPreguntas = opcionesList
        respuestasPreguntas = respuestasList
        opciones2Preguntas = opciones2List
    }
}