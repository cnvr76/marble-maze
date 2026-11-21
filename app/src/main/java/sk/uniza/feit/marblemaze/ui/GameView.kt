package sk.uniza.feit.marblemaze.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import androidx.core.content.ContextCompat
import sk.uniza.feit.marblemaze.game.Vector2
import sk.uniza.feit.marblemaze.game.World
import androidx.core.graphics.createBitmap
import sk.uniza.feit.marblemaze.R
import sk.uniza.feit.marblemaze.game.Ball
import sk.uniza.feit.marblemaze.game.Cell
import sk.uniza.feit.marblemaze.game.ExitHole
import sk.uniza.feit.marblemaze.game.Level
import sk.uniza.feit.marblemaze.game.MazeConfig
import sk.uniza.feit.marblemaze.game.MazeGenerator
import sk.uniza.feit.marblemaze.game.TrapHole

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    @Volatile private var world: World? = null
    @Volatile private var nextLevel: Level? = null
    var depth: Int = 0
        private set
    private lateinit var config: MazeConfig
    private lateinit var frame: Bitmap
    private lateinit var frameCanvas: Canvas
    private val placement = RectF()
    @Volatile private var running = false
    @Volatile var tilt = Vector2(0f, 0f)
    private var thread: Thread? = null
    var onBallDeath: ((depth: Int) -> Unit)? = null
    var onDepthChanged: ((depth: Int) -> Unit)? = null

    private val paintBall = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg); isAntiAlias = false
    }
    private val wallPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg); isAntiAlias = false
    }
    private val holePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg_dim); isAntiAlias = false
    }
    private val winHolePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.accent); isAntiAlias = false
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
        val rows = (COLS * height / width.toFloat()).toInt()
        frame = createBitmap(COLS * BIT, rows * BIT)
        frameCanvas = Canvas(frame)

        if (world == null) {
//            config = MazeConfig(rows, COLS, 0.2f, 0.4f)
            config = MazeConfig(rows, COLS, 1f, 0f) // EMPTY TEST CONFIG
            Thread {
                val level = MazeGenerator.generate(config, Cell(0, rows - 1))
                loadLevel(level)
            }.start()
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
            val w = world
            if (w == null) {
                Thread.sleep(10)
                continue
            }
            val now = System.nanoTime()
            val deltaTime = ((now - last) / 1_000_000_000f).coerceAtMost(0.05f)
            last = now

            w.update(tilt, deltaTime)
            if (w.ball.state == Ball.State.FELL) {
                val hole = w.ball.fallenHole
                if (hole is ExitHole) {
                    depth++
                    post { onDepthChanged?.invoke(depth) }

                    val next: Level = nextLevel ?: MazeGenerator.generate(config, hole.cell)
                    loadLevel(next)
                } else {
                    running = false
                    post { onBallDeath?.invoke(depth) }
                }
            }
            val canvas = holder.lockCanvas() ?: continue

            try { render(canvas, w) }
            finally { holder.unlockCanvasAndPost(canvas) }
        }
    }

    private fun loadLevel(level: Level) {
        world = World(level)
        prepareNextLevel(level.end.cell)
    }

    private fun render(canvas: Canvas, w: World) {
        frameCanvas.drawColor(bgColor)

        for ((_, wleft, wright, wtop, wbottom) in w.level.walls)
            frameCanvas.drawRect(wleft * BIT, wtop * BIT, wright * BIT, wbottom * BIT, wallPaint)

        for (hole in w.level.holes) {
            if (hole is ExitHole)
                frameCanvas.drawCircle(
                    hole.center.x * BIT,
                    hole.center.y * BIT,
                    hole.radius * BIT,
                    winHolePaint
                )
            else
                frameCanvas.drawCircle(hole.center.x * BIT, hole.center.y * BIT, hole.radius * BIT, holePaint)
        }

        frameCanvas.drawCircle(
            w.ball.position.x * BIT,
            w.ball.position.y * BIT,
            w.ball.radius * w.ball.scale * BIT,
            paintBall
        )
        canvas.drawColor(bgColor)
        canvas.drawBitmap(frame, null, placement, paintPixel)
    }

    fun regenerate() {
        val start: Cell = world?.level?.start ?: return
        Thread { loadLevel(MazeGenerator.generate(config, start)) }.start()
    }

    private fun prepareNextLevel(start: Cell) {
        nextLevel = null
        Thread { nextLevel = MazeGenerator.generate(config, start) }.start()
    }

    companion object {
        const val BIT = 10
        const val COLS = 5
    }

}