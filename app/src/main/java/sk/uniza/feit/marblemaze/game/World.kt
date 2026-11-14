package sk.uniza.feit.marblemaze.game

class World(val width: Int, val height: Int) {
    private val radius = 0.3f
    val ball = Ball(Vector2(radius * 1.5f, height - radius * 1.5f), radius)

    fun update(tilt: Vector2, deltaTime: Float) {
        ball.update(tilt * TILT_ACCELERATION, deltaTime)
        keepInsideBoard()
    }

    private fun keepInsideBoard() {
        if (ball.position.y + ball.radius > height) {
            ball.position = ball.position.copy(y = height - ball.radius)
            ball.velocity = ball.velocity.copy(y = -ball.velocity.y * Ball.BOUNCE)
        } else if (ball.position.y - ball.radius < 0f) {
            ball.position = ball.position.copy(y = ball.radius)
            ball.velocity = ball.velocity.copy(y = -ball.velocity.y * Ball.BOUNCE)
        }

        if (ball.position.x + ball.radius > width) {
            ball.position = ball.position.copy(x = width - ball.radius)
            ball.velocity = ball.velocity.copy(x = -ball.velocity.x * Ball.BOUNCE)
        } else if (ball.position.x - ball.radius < 0f) {
            ball.position = ball.position.copy(x = ball.radius)
            ball.velocity = ball.velocity.copy(x = -ball.velocity.x * Ball.BOUNCE)
        }
    }

    companion object {
        const val TILT_ACCELERATION = 40f
    }
}