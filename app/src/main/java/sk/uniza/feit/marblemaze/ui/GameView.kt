package sk.uniza.feit.marblemaze.ui

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

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    private val world = World(8, 12)
    private val frame: Bitmap = createBitmap(world.width * PPC, world.height * PPC)
    private val frameCanvas = Canvas(frame)
    private val placement = RectF()
    @Volatile private var running = false
    @Volatile var tilt = Vector2(0f, 0f)
    private var thread: Thread? = null

    private val paintBall = Paint().apply {
        color = ContextCompat.getColor(context, R.color.fg); isAntiAlias = false }
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
        val scale = Math.min(width.toFloat() / frame.width, height.toFloat() / frame.height)
        val drawH = frame.height * scale * 1.2f
        val drawW = frame.width * scale
        val left = (width - drawW) / 2
        val top = (height - drawH) / 2
        placement.set(left, top, left + drawW, top + drawH)

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
        val ball = world.ball
        frameCanvas.drawCircle(
            ball.position.x * PPC,
            ball.position.y * PPC,
            ball.radius * PPC,
            paintBall
        )
        canvas.drawColor(bgColor)
        canvas.drawBitmap(frame, null, placement, paintPixel)
    }

    companion object {
        const val PPC = 12
    }

}