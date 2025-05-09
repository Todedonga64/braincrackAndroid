package movil.braincrack.gamemodes

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
import kotlinx.coroutines.delay
import movil.braincrack.R
import movil.braincrack.api.ui.model.Pregunta
import movil.braincrack.api.ui.viewmodel.SuddenDeathViewModel


@Composable
fun SuddenDeathView(navegar: NavController, name: String) {

    val suddenDeathViewModel: SuddenDeathViewModel = viewModel()

    // Si los temas están vacíos, los obtenemos
    if (suddenDeathViewModel.temas.isEmpty()) {
        suddenDeathViewModel.obtenerDatos()  // Cargar los temas desde la API
    }

    // Ahora podemos usar los temas extraídos
    val temas = suddenDeathViewModel.temas
    val preguntasRestantes = temas.flatMap { it.preguntas }.toMutableList()  // Obtener todas las preguntas

    // Variables de estado para las vidas, puntaje, tiempo y la pregunta actual
    var vidas by remember { mutableStateOf(1) }  // 1 vida
    var puntaje by remember { mutableStateOf(0) }
    var tiempoRestante by remember { mutableStateOf(10) }  // Tiempo inicial de 30 segundos
    var preguntaActual by remember { mutableStateOf<Pregunta?>(null) }  // Inicializamos con null

    // Temporizador para reducir el tiempo restante
    LaunchedEffect(tiempoRestante) {
        if (tiempoRestante > 0) {
            delay(1000)  // Espera 1 segundo
            tiempoRestante -= 1
        } else {
            vidas = 0
        }
    }

    // Usamos LaunchedEffect para inicializar preguntaActual solo cuando las preguntas estén disponibles
    LaunchedEffect(preguntasRestantes) {
        if (preguntasRestantes.isNotEmpty()) {
            preguntaActual = preguntasRestantes.random()  // Asignar la primera pregunta aleatoria
        }
    }

    // Crear un mapa de temas a fondos de pantalla
    val temaAFondo = mapOf(
        "Exacta_Maniaca" to R.drawable.exacta_maniaca,
        "Chismesito_Historico" to R.drawable.chismesito_historico,
        "GeoBrain" to R.drawable.geobrain,
        "Lombiletras" to R.drawable.lombiletras,
        "Dato_Nauta" to R.drawable.dato_nauta
    )

    // Obtener el fondo correspondiente al tema de la pregunta actual
    val temaNombre = temas.find { tema -> tema.preguntas.contains(preguntaActual) }?.nombre
    val fondoTema = temaAFondo[temaNombre] ?: R.drawable.braincrack

    // Mostrar las preguntas
    if (vidas > 0 && preguntaActual != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = fondoTema),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido sobre el fondo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                // Mostrar el tiempo restante en la parte superior
                Text(
                    text = "Tiempo restante: $tiempoRestante s",
                    modifier = Modifier.align(Alignment.Start)
                        .padding(start = 10.dp)// Alineado a la izquierda
                )

                Spacer(modifier = Modifier.height(130.dp))

                // Mostrar la pregunta
                Text(
                    text = preguntaActual?.pregunta ?: "Cargando pregunta...",
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
                                puntaje += 10  // Incrementa el puntaje si es la respuesta correcta
                            } else {
                                vidas -= 1  // Resta una vida si la respuesta es incorrecta
                            }

                            // Eliminar la pregunta actual de las preguntas restantes para no repetirla
                            preguntasRestantes.remove(preguntaActual)

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
                        Text(
                            "Puntaje: $puntaje",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Vidas: $vidas",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    } else {
        // Fin del juego si ya no quedan preguntas o vidas
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = R.drawable.perdido),
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
                Text(
                    "Se acabó el juego $name",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    "Puntaje Final: $puntaje",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Button(onClick = {
                    // Reiniciar el juego si se desea
                    vidas = 1
                    puntaje = 0
                    preguntasRestantes.clear()
                    preguntasRestantes.addAll(temas.flatMap { it.preguntas })
                    if (preguntasRestantes.isNotEmpty()) {
                        preguntaActual = preguntasRestantes.random()
                    }
                    tiempoRestante = 10  // Reinicia el tiempo a 30 segundos
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