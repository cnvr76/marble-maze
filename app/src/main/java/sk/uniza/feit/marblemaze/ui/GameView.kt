package sk.uniza.feit.marblemaze.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import sk.uniza.feit.marblemaze.game.Vector2
import sk.uniza.feit.marblemaze.game.World
import androidx.core.graphics.createBitmap
import sk.uniza.feit.marblemaze.R
import sk.uniza.feit.marblemaze.game.Ball
import sk.uniza.feit.marblemaze.game.Cell
import sk.uniza.feit.marblemaze.game.MazeConfig
import sk.uniza.feit.marblemaze.game.MazeGenerator

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    private lateinit var world: World
    private lateinit var config: MazeConfig
    private lateinit var frame: Bitmap
    private lateinit var frameCanvas: Canvas
    private val placement = RectF()
    @Volatile private var running = false
    @Volatile var tilt = Vector2(0f, 0f)
    private var thread: Thread? = null
    var onBallDeath: (() -> Unit)? = null

    private val paintBall = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg); isAntiAlias = false
    }
    private val wallPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg); isAntiAlias = false
    }
    private val holePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg_dim); isAntiAlias = false
    }
    private val bgColor = ContextCompat.getColor(context, R.color.bg)
    private val paintPixel = Paint().apply { isFilterBitmap = false; isAntiAlias = false }

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {}

    @SuppressLint("ThreadConstraint")
    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        if (!::world.isInitialized) {
            val rows = (COLS * height / width.toFloat()).toInt()

            config = MazeConfig(rows, COLS, 0.6f, 0.4f)
            val level = MazeGenerator.generate(config, Cell(0, rows - 1))
            world = World(level)
            frame = createBitmap(COLS * BIT, rows * BIT)
            frameCanvas = Canvas(frame)
        }
        placement.set(0f, 0f, width.toFloat(), height.toFloat())

        if (thread == null) {
            running = true
            thread = Thread { loop() }.also { it.start() }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        running = false
        thread?.join()
        thread = null
    }

    private fun loop() {
        var last = System.nanoTime()
        while (running) {
            val now = System.nanoTime()
            val deltaTime = ((now - last) / 1_000_000_000f).coerceAtMost(0.05f)
            last = now

            world.update(tilt, deltaTime)
            if (world.ball.state == Ball.State.DEATH) {
                running = false
                post { onBallDeath?.invoke() }
            }
            val canvas = holder.lockCanvas() ?: continue

            try { render(canvas) }
            finally { holder.unlockCanvasAndPost(canvas) }
        }
    }

    private fun render(canvas: Canvas) {
        frameCanvas.drawColor(bgColor)

        for ((_, wleft, wright, wtop, wbottom) in world.level.walls) {
            frameCanvas.drawRect(wleft * BIT, wtop * BIT, wright * BIT, wbottom * BIT, wallPaint)
            // frameCanvas.drawCircle(wleft * BIT, wtop * BIT, 1f, Paint().apply { color = Color.GREEN; isAntiAlias = false })
        }

        for (hole in world.level.holes) {
            frameCanvas.drawCircle(hole.x * BIT, hole.y * BIT, hole.radius * BIT, holePaint)
        }

        val ball = world.ball
        frameCanvas.drawCircle(
            ball.position.x * BIT,
            ball.position.y * BIT,
            ball.radius * ball.scale * BIT,
            paintBall
        )
        canvas.drawColor(bgColor)
        canvas.drawBitmap(frame, null, placement, paintPixel)
    }

    fun regenerate(start: Cell = world.level.start) {
        world = World(MazeGenerator.generate(config, start))
    }

    companion object {
        const val BIT = 10
        const val COLS = 5
    }

}