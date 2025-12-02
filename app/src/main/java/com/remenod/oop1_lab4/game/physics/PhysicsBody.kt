package com.remenod.oop1_lab4.game.physics

import com.remenod.oop1_lab4.game.figures.Figure

class PhysicsBody(
    var vx: Float = 0f,
    var vy: Float = 0f,
    var mass: Float = 1f,
    var bounce: Float = 0.8f
) {

    fun update(figure: Figure, dt: Float) {
        figure.centerX += vx * dt
        figure.centerY += vy * dt
    }

    fun applyGravity(g: Float, dt: Float) {
        vy += g * dt
    }

    fun bounceFromWalls(figure: Figure, width: Float, height: Float) {
        val r = figure.boundingRadius()

        if (figure.centerX - r < 0f && vx < 0) {
            figure.centerX = r
            vx = -vx * bounce
        }
        if (figure.centerX + r > width && vx > 0) {
            figure.centerX = width - r
            vx = -vx * bounce
        }
        if (figure.centerY - r < 0f && vy < 0) {
            figure.centerY = r
            vy = -vy * bounce
        }
        if (figure.centerY + r > height && vy > 0) {
            figure.centerY = height - r
            vy = -vy * bounce
        }
    }
}
