package movil.braincrack.topics

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import movil.braincrack.R
import movil.braincrack.api.ui.model.Pregunta
import movil.braincrack.api.ui.viewmodel.SuddenDeathViewModel

@Composable
fun ExactaManiacaView(navegar: NavController, name: String) {
    val suddenDeathViewModel: SuddenDeathViewModel = viewModel()

    // Si los temas están vacíos, los obtenemos
    if (suddenDeathViewModel.temas.isEmpty()) {
        suddenDeathViewModel.obtenerDatos()  // Cargar los temas desde el ViewModel
    }

    // Obtener las preguntas del tema "Exacta_Maniaca"
    val temaExactaManiaca = suddenDeathViewModel.temas.find { it.nombre == "Exacta_Maniaca" }
    var preguntasRestantes = temaExactaManiaca?.preguntas?.shuffled()?.toMutableList() ?: mutableListOf()

    // Variables de estado para las respuestas correctas e incorrectas
    var respuestasCorrectas by remember { mutableStateOf(0) }
    var respuestasIncorrectas by remember { mutableStateOf(0) }
    var preguntaActual by remember { mutableStateOf<Pregunta?>(null) }

    // Usamos LaunchedEffect para inicializar preguntaActual solo cuando las preguntas estén disponibles
    LaunchedEffect(preguntasRestantes) {
        if (preguntasRestantes.isNotEmpty()) {
            preguntaActual = preguntasRestantes.first()  // Asignar la primera pregunta aleatoria
        }
    }

    // Función para responder a la pregunta
    fun responder(opcionSeleccionada: Int) {
        if (opcionSeleccionada == preguntaActual?.respuesta) {
            respuestasCorrectas += 1
        } else {
            respuestasIncorrectas += 1
        }

        // Eliminar la pregunta actual de la lista de preguntas restantes
        preguntasRestantes.remove(preguntaActual)

        // Si quedan preguntas, asignar una nueva pregunta aleatoria
        if (preguntasRestantes.isNotEmpty()) {
            preguntaActual = preguntasRestantes.random()
        } else {
            // Si no quedan preguntas, mostrar los resultados
            preguntaActual = null // Esto hará que el flujo pase a mostrar los resultados
        }
    }

    // Mostrar las preguntas y opciones
    if (preguntaActual != null) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = R.drawable.exacta_maniaca),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido sobre el fondo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                // Mostrar la pregunta
                Text(
                    text = preguntaActual?.pregunta ?: "Cargando pregunta...",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(50.dp))

                // Mostrar botones con las opciones
                preguntaActual?.opciones2?.forEachIndexed { index, opcion ->
                    Button(
                        onClick = {
                            responder(index + 1)  // Enviar la opción seleccionada
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text(opcion)
                    }
                }

                Box(modifier = Modifier
                    .fillMaxSize()) {
                    Column(modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp)) {
                        Text(
                            "Respuestas correctas: $respuestasCorrectas",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Respuestas incorrectas: $respuestasIncorrectas",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    } else {
        // Si ya no quedan preguntas, mostrar los resultados
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Juego Terminado")
            Text("Respuestas correctas: $respuestasCorrectas")
            Text("Respuestas incorrectas: $respuestasIncorrectas")

            // Botón para reiniciar el juego
            Button(onClick = {
                // Reiniciar el juego
                respuestasCorrectas = 0
                respuestasIncorrectas = 0
                preguntasRestantes = temaExactaManiaca?.preguntas?.shuffled()?.toMutableList() ?: mutableListOf()
                if (preguntasRestantes.isNotEmpty()) {
                    preguntaActual = preguntasRestantes.first()
                }
            }) {
                Text("Reiniciar Juego")
            }

            Button(onClick = { navegar.navigate("gamemodes/${Uri.encode(name)}") }) {
                Text(text = "Regresar a modos de juego")
            }
        }
    }
}