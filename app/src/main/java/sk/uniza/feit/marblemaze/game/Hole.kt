package sk.uniza.feit.marblemaze.game

sealed class Hole(val center: Vector2, val radius: Float)

class TrapHole(center: Vector2, radius: Float) : Hole(center, radius) {
    companion object {
        const val SAFE_DISTANCE = 2f
        const val MIN_SELF_DISTANCE = 1.5f
    }
}

class ExitHole(val cell: Cell, radius: Float) : Hole(cell.center, radius) {
    companion object {
        const val MIN_PLAYER_DISTANCE = 8f
    }
}