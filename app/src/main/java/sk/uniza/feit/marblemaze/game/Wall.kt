package sk.uniza.feit.marblemaze.game

data class Wall(val left: Float, val right: Float, val top: Float, val bottom: Float) {
    companion object {
        const val THICKNESS = 0.1f
    }
}