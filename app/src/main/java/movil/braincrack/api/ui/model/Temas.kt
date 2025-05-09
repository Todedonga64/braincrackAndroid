package movil.braincrack.api.ui.model

data class Temas (
    val id: String,
    val nombre: String,
    val preguntas: List<Pregunta>
)