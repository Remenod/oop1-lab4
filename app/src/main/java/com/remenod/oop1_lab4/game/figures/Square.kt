package com.remenod.oop1_lab4.game.figures

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.*

class Square(
    centerX: Float,
    centerY: Float,
    val side: Float
) : Figure(centerX, centerY) {
    override fun drawBlack(canvas: Canvas, paint: Paint) {
        val half = side / 2
        canvas.drawRect(centerX - half, centerY - half,
            centerX + half, centerY + half, paint)
    }

    override fun hide(canvas: Canvas, backgroundPaint: Paint) {
        val half = side / 2
        canvas.drawRect(centerX - half, centerY - half,
            centerX + half, centerY + half, backgroundPaint)
    }

    override fun intersects(other: Figure): Boolean =
        other.intersectsSquare(this)

    override fun intersectsCircle(circle: Circle): Boolean =
        circle.intersectsSquare(this)

    override fun intersectsSquare(square: Square): Boolean {
        val halfA = side / 2
        val halfB = square.side / 2
        return abs(centerX - square.centerX) < (halfA + halfB) &&
                abs(centerY - square.centerY) < (halfA + halfB)
    }

    override fun intersectsRhomb(rhomb: Rhomb): Boolean {
        val halfA = side / 2
        val halfW = rhomb.horDiag / 2
        val halfH = rhomb.vertDiag / 2
        return abs(centerX - rhomb.centerX) < (halfA + halfW) &&
                abs(centerY - rhomb.centerY) < (halfA + halfH)
    }
}
