package sk.uniza.feit.marblemaze.game

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector2(val x: Float, val y: Float) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun times(other: Float) = Vector2(x * other, y * other)
    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
    operator fun div(n: Float) = Vector2(x / n, y / n)
    fun dist(other: Vector2): Float = sqrt((other.x - x).pow(2) + (other.y - y).pow(2))
}