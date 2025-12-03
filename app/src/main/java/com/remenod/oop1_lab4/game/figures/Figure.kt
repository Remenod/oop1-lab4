package com.remenod.oop1_lab4.game.figures

import android.graphics.Canvas
import android.graphics.Paint
import com.remenod.oop1_lab4.game.physics.AABB

abstract class Figure(
    var centerX: Float,
    var centerY: Float
) {
    abstract fun drawBlack(canvas: Canvas, paint: Paint)
    abstract fun hide(canvas: Canvas, backgroundPaint: Paint)

    open fun moveRight() {}

    abstract fun intersects(other: Figure): Boolean

    abstract fun intersectsCircle(circle: Circle): Boolean
    abstract fun intersectsSquare(square: Square): Boolean
    abstract fun intersectsRhomb(rhomb: Rhomb): Boolean

    // wall collision
    abstract fun bounds(): AABB

    abstract fun getPolygonPoints(): List<Pair<Float, Float>>

    abstract fun containsPoint(x: Float, y: Float): Boolean
}
