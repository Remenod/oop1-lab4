package com.remenod.oop1_lab4.game.physics

import com.remenod.oop1_lab4.PhysicsData
import com.remenod.oop1_lab4.game.figures.Figure

data class AABB(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

class PhysicsBody(
    var vx: Float = 0f,
    var vy: Float = 0f,
    var mass: Float = 1f,
    var bounce: Float = 0.8f
) {

    fun applyAccelerometer(dt: Float) {
        vx -= PhysicsData.ax * 0.5f
        vy += PhysicsData.ay * 0.5f
    }

    fun update(figure: Figure, dt: Float) {
        figure.centerX += vx
        figure.centerY += vy
    }

    fun bounceFromWalls(figure: Figure, width: Float, height: Float) {
        val b = figure.bounds()

        // LEFT
        if (b.left < 0f) {
            figure.centerX -= b.left
            vx = -vx * bounce
        }

        // RIGHT
        if (b.right > width) {
            figure.centerX -= (b.right - width)
            vx = -vx * bounce
        }

        // TOP
        if (b.top < 0f) {
            figure.centerY -= b.top
            vy = -vy * bounce
        }

        // BOTTOM
        if (b.bottom > height) {
            figure.centerY -= (b.bottom - height)
            vy = -vy * bounce
        }
    }

}
