package com.remnod.oop1_lab4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class PhysicsView(context: Context) : View(context) {

    private var x = 200f
    private var y = 200f
    private var vx = 0f
    private var vy = 0f

    private val paint = Paint().apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (width == 0 || height == 0) {
            invalidate()
            return
        }

        vx -= PhysicsData.ax * 0.5f
        vy += PhysicsData.ay * 0.5f

        x += vx
        y += vy

        if (x < 50) { x = 50f; vx *= -0.8f }
        if (x > width - 50) { x = (width - 50).toFloat(); vx *= -0.8f }

        if (y < 50) { y = 50f; vy *= -0.8f }
        if (y > height - 50) { y = (height - 50).toFloat(); vy *= -0.8f }

        canvas.drawCircle(x, y, 50f, paint)

        invalidate()
    }
}
