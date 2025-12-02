package com.remenod.oop1_lab4.game.figures

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.remenod.oop1_lab4.game.physics.AABB
import kotlin.math.*

class Rhomb(
    centerX: Float,
    centerY: Float,
    val horDiag: Float,
    val vertDiag: Float
) : Figure(centerX, centerY) {

    override fun drawBlack(canvas: Canvas, paint: Paint) {
        val path = Path()
        path.apply {
            moveTo(centerX - horDiag/2, centerY)
            lineTo(centerX, centerY - vertDiag/2)
            lineTo(centerX + horDiag/2, centerY)
            lineTo(centerX, centerY + vertDiag/2)
            close()
        }
        canvas.drawPath(path, paint)
    }

    override fun hide(canvas: Canvas, backgroundPaint: Paint) {
        val path = Path()
        path.apply {
            moveTo(centerX - horDiag/2, centerY)
            lineTo(centerX, centerY - vertDiag/2)
            lineTo(centerX + horDiag/2, centerY)
            lineTo(centerX, centerY + vertDiag/2)
            close()
        }
        canvas.drawPath(path, backgroundPaint)
    }

    override fun intersects(other: Figure): Boolean =
        other.intersectsRhomb(this)

    override fun intersectsCircle(circle: Circle): Boolean =
        circle.intersectsRhomb(this)

    override fun intersectsSquare(square: Square): Boolean =
        square.intersectsRhomb(this)

    override fun intersectsRhomb(rhomb: Rhomb): Boolean {
        val halfW = horDiag / 2 + rhomb.horDiag / 2
        val halfH = vertDiag / 2 + rhomb.vertDiag / 2
        return abs(centerX - rhomb.centerX) < halfW &&
                abs(centerY - rhomb.centerY) < halfH
    }

    override fun bounds(): AABB {
        return AABB(
            centerX - horDiag / 2,
            centerY - vertDiag / 2,
            centerX + horDiag / 2,
            centerY + vertDiag / 2
        )
    }

    // more complicated formula for future grow
    override fun getPolygonPoints(): List<Pair<Float, Float>> {
        val hw = horDiag / 2
        val hh = vertDiag / 2

        val cx = centerX
        val cy = centerY
        val a = 45f

        val cosA = cos(a)
        val sinA = sin(a)

        fun rot(x: Float, y: Float): Pair<Float, Float> {
            val rx = x * cosA - y * sinA
            val ry = x * sinA + y * cosA
            return Pair(cx + rx, cy + ry)
        }

        return listOf(
            rot(-hw, -hh),
            rot(+hw, -hh),
            rot(+hw, +hh),
            rot(-hw, +hh)
        )
    }
}
