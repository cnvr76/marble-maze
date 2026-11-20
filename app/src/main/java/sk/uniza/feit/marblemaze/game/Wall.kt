package sk.uniza.feit.marblemaze.game

data class Wall(val cell: Cell, val left: Float, val right: Float, val top: Float, val bottom: Float) {
    val center: Vector2
        get() = Vector2((left + right) / 2f, (top + bottom) / 2f)

    companion object {
        const val THICKNESS = 0.1f
    }
}