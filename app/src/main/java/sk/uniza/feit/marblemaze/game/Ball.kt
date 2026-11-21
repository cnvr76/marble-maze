package sk.uniza.feit.marblemaze.game

class Ball(var position: Vector2, val radius: Float) {
    enum class State { ALIVE, FALLING, DEATH }

    var state = State.ALIVE
        private set
    var scale = 1f
        private set
    var velocity: Vector2 = Vector2(0f, 0f)

    fun update(acceleration: Vector2, deltaTime: Float) {
        when (state) {
            State.ALIVE -> roll(acceleration, deltaTime)
            State.FALLING -> fall(deltaTime)
            State.DEATH -> {}
        }
    }

    private fun roll(acceleration: Vector2, deltaTime: Float) {
        velocity += acceleration * deltaTime
        velocity *= 1 - FRICTION * deltaTime
        position += velocity * deltaTime
    }

    private fun fall(deltaTime: Float) {
        scale -= deltaTime / FALL_DURATION
        if (scale <= 0f) {
            scale = 0f;
            state = State.DEATH
        }
    }

    fun fallInto(hole: Hole) {
        if (state != State.ALIVE) return
        position = hole.center
        state = State.FALLING
        velocity = Vector2(0f, 0f)
    }

    companion object {
        const val FRICTION = 3f
        const val BOUNCE = 0.5f
        const val FALL_DURATION = 0.5f
    }
}