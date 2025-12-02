package com.remenod.oop1_lab4.game.physics

import com.remenod.oop1_lab4.PhysicsData
import com.remenod.oop1_lab4.game.figures.Figure

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
        val r = figure.boundingRadius()

        if (figure.centerX < r) {
            figure.centerX = r
            vx *= -bounce
        }

        if (figure.centerX > width - r) {
            figure.centerX = width - r
            vx *= -bounce
        }

        if (figure.centerY < r) {
            figure.centerY = r
            vy *= -bounce
        }

        if (figure.centerY > height - r) {
            figure.centerY = height - r
            vy *= -bounce
        }
    }
}
