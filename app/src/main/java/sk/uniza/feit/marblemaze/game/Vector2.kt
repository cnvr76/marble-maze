package sk.uniza.feit.marblemaze.game

data class Vector2(val x: Float, val y: Float) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun times(other: Float) = Vector2(x * other, y * other)
}