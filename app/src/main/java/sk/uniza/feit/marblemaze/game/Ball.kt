package sk.uniza.feit.marblemaze.game

class Ball(var position: Vector2, val radius: Float) {
    var velocity: Vector2 = Vector2(0f, 0f)

    fun update(acceleration: Vector2, deltaTime: Float) {
        velocity += acceleration * deltaTime
        velocity *= 1 - FRICTION * deltaTime
        position += velocity * deltaTime
    }

    companion object {
        const val FRICTION = 2f
        const val BOUNCE = 0.3f
    }
}