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
fun ChismesitoHistoricoView(navegar: NavController, name: String) {

    val suddenDeathViewModel: SuddenDeathViewModel = viewModel()

    // Si los temas están vacíos, los obtenemos
    if (suddenDeathViewModel.temas.isEmpty()) {
        suddenDeathViewModel.obtenerDatos()  // Cargar los temas desde la API
    }

    // Obtener solo las preguntas del tema "Exacta_Maniaca"
    val temaExactaManiaca = suddenDeathViewModel.temas.find { it.nombre == "Chismesito_Historico" }
    val preguntasRestantes = temaExactaManiaca?.preguntas?.toMutableList() ?: mutableListOf()

    // Variables de estado para el puntaje y la pregunta actual
    var puntaje by remember { mutableStateOf(0) }
    var preguntasRespondidas by remember { mutableStateOf(0) }
    var respuestasIncorrectas by remember { mutableStateOf(0) }
    var preguntaActual by remember { mutableStateOf<Pregunta?>(null) }  // Inicializamos con null

    // Usamos LaunchedEffect para inicializar preguntaActual solo cuando las preguntas estén disponibles
    LaunchedEffect(preguntasRestantes) {
        if (preguntasRestantes.isNotEmpty()) {
            preguntaActual = preguntasRestantes.random()  // Asignar la primera pregunta aleatoria
        }
    }

    // Mostrar las preguntas
    if (preguntasRestantes.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Fondo de pantalla (puedes cambiarlo según el tema o usar un fondo por defecto)
            Image(
                painter = painterResource(id = R.drawable.chismesito_historico),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido sobre el fondo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(130.dp))

                // Mostrar la pregunta
                Text(text = preguntaActual?.pregunta ?: "Cargando pregunta...",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                )

                Spacer(modifier = Modifier.height(50.dp))

                // Mostrar botones con las opciones
                preguntaActual?.opciones2?.forEachIndexed { index, opcion ->
                    Button(
                        onClick = {
                            if (index + 1 == preguntaActual?.respuesta) {
                                puntaje += 1  // Incrementa el puntaje si es la respuesta correcta
                            } else {
                                respuestasIncorrectas += 1  // Cuenta como incorrecta
                            }

                            // Eliminar la pregunta actual de las preguntas restantes para no repetirla
                            preguntasRestantes.remove(preguntaActual)

                            // Incrementar el contador de preguntas respondidas
                            preguntasRespondidas += 1

                            // Si quedan preguntas, toma una nueva pregunta aleatoria
                            if (preguntasRestantes.isNotEmpty()) {
                                preguntaActual = preguntasRestantes.random()
                            }
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
                        Text("Respuestas Correctas: $puntaje",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp)
                        Text("Respuestas Incorrectas: $respuestasIncorrectas",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp)
                    }
                }
            }
        }
    } else {
        // Fin del juego cuando ya no hay más preguntas
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Fondo de pantalla (puedes cambiarlo según el tema o usar un fondo por defecto)
            Image(
                painter = painterResource(id = R.drawable.acabo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido sobre el fondo
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Se acabó el juego $name",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
                Text("Respuestas Correctas: $puntaje",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
                Text("Respuestas Incorrectas: $respuestasIncorrectas",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)

                Button(onClick = {
                    // Reiniciar el juego si se desea
                    puntaje = 0
                    respuestasIncorrectas = 0
                    preguntasRespondidas = 0
                    preguntasRestantes.clear()
                    temaExactaManiaca?.preguntas?.let { preguntasRestantes.addAll(it) }
                    if (preguntasRestantes.isNotEmpty()) {
                        preguntaActual = preguntasRestantes.random()
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
}