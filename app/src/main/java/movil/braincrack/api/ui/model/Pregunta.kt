package movil.braincrack.api.ui.model

data class Pregunta (
    val id: String,
    val opciones: Int,
    val respuesta: Int,
    val opciones2: List<String>,
    val pregunta: String
)