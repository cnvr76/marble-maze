package sk.uniza.feit.marblemaze.game

import kotlin.math.sqrt

class World(val level: Level) {
    private val radius = 0.3f
    val ball = Ball(Vector2(level.start.x + 0.5f, level.start.y + 0.5f), radius)

    fun update(tilt: Vector2, deltaTime: Float) {
        val step = deltaTime / SUBSTEPS
        repeat(SUBSTEPS) {
            ball.update(tilt * TILT_ACCELERATION, step)
            keepInsideBoard()
            wallCollisions()
        }
    }

    private fun keepInsideBoard() {
        if (ball.position.y + ball.radius > level.config.rows) {
            ball.position = ball.position.copy(y = level.config.rows - ball.radius)
            ball.velocity = ball.velocity.copy(y = -ball.velocity.y * Ball.BOUNCE)
        } else if (ball.position.y - ball.radius < 0f) {
            ball.position = ball.position.copy(y = ball.radius)
            ball.velocity = ball.velocity.copy(y = -ball.velocity.y * Ball.BOUNCE)
        }

        if (ball.position.x + ball.radius > level.config.cols) {
            ball.position = ball.position.copy(x = level.config.cols - ball.radius)
            ball.velocity = ball.velocity.copy(x = -ball.velocity.x * Ball.BOUNCE)
        } else if (ball.position.x - ball.radius < 0f) {
            ball.position = ball.position.copy(x = ball.radius)
            ball.velocity = ball.velocity.copy(x = -ball.velocity.x * Ball.BOUNCE)
        }
    }

    private fun wallCollisions() {
        for ((_, left, right, top, bottom) in level.walls) {
            val bpos = ball.position

            val closestX = bpos.x.coerceIn(left, right)
            val closestY = bpos.y.coerceIn(top, bottom)

            val dx = bpos.x - closestX
            val dy = bpos.y - closestY
            val dist: Float = sqrt(dx * dx + dy * dy)
            if (dist >= ball.radius || dist == 0f) continue

            val nx = dx / dist
            val ny = dy / dist

            val depth = ball.radius - dist
            ball.position = Vector2(bpos.x + nx * depth, bpos.y + ny * depth)

            val bvel = ball.velocity
            val intoWall = bvel.x * nx + bvel.y * ny
            if (intoWall <= 0) {
                val k = (1 + Ball.BOUNCE) * intoWall
                ball.velocity = Vector2(bvel.x - k * nx, bvel.y - k * ny)
            }
        }
    }

    companion object {
        const val TILT_ACCELERATION = 40f
        const val SUBSTEPS = 4
    }
}