package com.remenod.oop1_lab4.game.figures

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.*

class Circle(
    centerX: Float,
    centerY: Float,
    val radius: Float
) : Figure(centerX, centerY) {
    override fun drawBlack(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    override fun hide(canvas: Canvas, backgroundPaint: Paint) {
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)
    }

    override fun intersects(other: Figure): Boolean =
        other.intersectsCircle(this)

    override fun intersectsCircle(circle: Circle): Boolean {
        val dx = centerX - circle.centerX
        val dy = centerY - circle.centerY
        val r = radius + circle.radius
        return dx*dx + dy*dy < r*r
    }

    override fun intersectsSquare(square: Square): Boolean {
        val half = square.side / 2
        val cx = centerX
        val cy = centerY
        val sx = square.centerX
        val sy = square.centerY

        val dx = max(sx - half, min(cx, sx + half))
        val dy = max(sy - half, min(cy, sy + half))
        val dist2 = (cx - dx)*(cx - dx) + (cy - dy)*(cy - dy)
        return dist2 < radius*radius
    }

    override fun intersectsRhomb(rhomb: Rhomb): Boolean {
        val halfW = rhomb.horDiag / 2
        val halfH = rhomb.vertDiag / 2
        val dx = abs(centerX - rhomb.centerX)
        val dy = abs(centerY - rhomb.centerY)
        return (dx < halfW + radius) && (dy < halfH + radius)
    }

    override fun boundingRadius() = radius
}
