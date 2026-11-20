package sk.uniza.feit.marblemaze.game

data class Hole(val x: Float, val y: Float, val radius: Float) {
    val center: Vector2
        get() = Vector2(x, y)
    companion object {
        const val SAFE_DISTANCE = 2f
        const val MIN_SELF_DISTANCE = 1.5f
    }
}