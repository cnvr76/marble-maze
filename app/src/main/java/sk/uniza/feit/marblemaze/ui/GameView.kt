package sk.uniza.feit.marblemaze.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
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
import sk.uniza.feit.marblemaze.game.Cell
import sk.uniza.feit.marblemaze.game.MazeConfig
import sk.uniza.feit.marblemaze.game.MazeGenerator

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    private lateinit var world: World
    private lateinit var frame: Bitmap
    private lateinit var frameCanvas: Canvas
    private val placement = RectF()
    @Volatile private var running = false
    @Volatile var tilt = Vector2(0f, 0f)
    private var thread: Thread? = null

    private val paintBall = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg); isAntiAlias = false
    }
    private val wallPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg); isAntiAlias = false
    }
    private val bgColor = ContextCompat.getColor(context, R.color.bg)
    private val paintPixel = Paint().apply { isFilterBitmap = false; isAntiAlias = false }

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        if (!::world.isInitialized) {
            val rows = (COLS * height / width.toFloat()).toInt()
            val level = MazeGenerator.generate(MazeConfig(rows, COLS), Cell(0, rows - 1))
            world = World(level)
            frame = createBitmap(COLS * BIT, rows * BIT)
            frameCanvas = Canvas(frame)
        }
        placement.set(0f, 0f, width.toFloat(), height.toFloat())

        if (thread == null) {
            running = true
            thread = Thread {loop()}.also { it.start() }
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
            val canvas = holder.lockCanvas() ?: continue

            try { render(canvas) }
            finally { holder.unlockCanvasAndPost(canvas) }
        }
    }

    private fun render(canvas: Canvas) {
        frameCanvas.drawColor(bgColor)

        for ((wleft, wright, wtop, wbottom) in world.level.walls) {
            frameCanvas.drawRect(wleft * BIT, wtop * BIT, wright * BIT, wbottom * BIT, wallPaint)
        }

        val ball = world.ball
        frameCanvas.drawCircle(
            ball.position.x * BIT,
            ball.position.y * BIT,
            ball.radius * BIT,
            paintBall
        )
        canvas.drawColor(bgColor)
        canvas.drawBitmap(frame, null, placement, paintPixel)
    }

    companion object {
        const val BIT = 10
        const val COLS = 5
    }

}