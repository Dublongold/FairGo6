package one.two.fairgo6.views.forEasierLive

object FromRotationToValue {
    operator fun get(value: Float): Float {
        return when(value) {
            4f, 94f, 229f, 319f -> 0f
            27f -> 2f
            49f, 184f -> 1.2f
            71f, 116f, 297f -> 10f
            139f -> 2.1f
            161f -> 40f
            207f -> 5f
            252f -> 20f
            274f -> 1.4f
            341f -> 15f
            else -> -1f
        }
    }
}