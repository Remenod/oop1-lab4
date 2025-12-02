package com.remenod.oop1_lab4.game.physics

import com.remenod.oop1_lab4.game.figures.Figure

class PhysicsBody(
    var vx: Float = 0f,
    var vy: Float = 0f,
    var mass: Float = 1f,
    var bounce: Float = 0.8f
) {

    fun updatePosition(figure: Figure, dt: Float) {
        figure.centerX += vx * dt
        figure.centerY += vy * dt
    }

    fun applyGravity(g: Float, dt: Float) {
        vy += g * dt
    }

    fun bounceFromWalls(figure: Figure, width: Int, height: Int, radius: Float) {
        if (figure.centerX - radius < 0 && vx < 0) vx = -vx * bounce
        if (figure.centerX + radius > width && vx > 0) vx = -vx * bounce
        if (figure.centerY - radius < 0 && vy < 0) vy = -vy * bounce
        if (figure.centerY + radius > height && vy > 0) vy = -vy * bounce
    }
}
