package com.remenod.oop1_lab4.game.physics

import com.remenod.oop1_lab4.PhysicsData
import com.remenod.oop1_lab4.game.GameObject
import com.remenod.oop1_lab4.game.figures.Figure
import kotlin.math.*

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
        vx -= PhysicsData.ax * 0.5f / mass
        vy += PhysicsData.ay * 0.5f / mass
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
            vx = -vx * (bounce * (1f / sqrt(mass)))
        }

        // RIGHT
        if (b.right > width) {
            figure.centerX -= (b.right - width)
            vx = -vx * (bounce * (1f / sqrt(mass)))
        }

        // TOP
        if (b.top < 0f) {
            figure.centerY -= b.top
            vy = -vy * (bounce * (1f / sqrt(mass)))

        }

        // BOTTOM
        if (b.bottom > height) {
            figure.centerY -= (b.bottom - height)
            vy = -vy * (bounce * (1f / sqrt(mass)))
        }
    }

    companion object {
        fun resolveCollision(a: GameObject, b: GameObject) {
            val polyA = a.figure.getPolygonPoints()
            val polyB = b.figure.getPolygonPoints()

            val sat = satCollision(polyA, polyB)

            if (!sat.collided) return

            // Fix MTV direction
            var mtvX = sat.mtvX
            var mtvY = sat.mtvY

            if (sqrt(mtvX*mtvX + mtvY*mtvY) < 0.00001f) return

            val dirX = b.figure.centerX - a.figure.centerX
            val dirY = b.figure.centerY - a.figure.centerY

            // If MTV is directed inward — reverse
            if (dirX * mtvX + dirY * mtvY < 0) {
                mtvX = -mtvX
                mtvY = -mtvY
            }

            // Limit penetration depth

            val correctedDepth = min(sat.depth, 10f)

            if (sat.depth <= 0.00001f) return

            // normalized MTV (axis intersection)
            val nx = mtvX / sat.depth
            val ny = mtvY / sat.depth

            // Position correction
            val totalMass = a.physics.mass + b.physics.mass
            val moveA = correctedDepth * (b.physics.mass / totalMass)
            val moveB = correctedDepth * (a.physics.mass / totalMass)

            a.figure.centerX -= nx * moveA
            a.figure.centerY -= ny * moveA

            b.figure.centerX += nx * moveB
            b.figure.centerY += ny * moveB

            // Relative velocity
            val rvx = b.physics.vx - a.physics.vx
            val rvy = b.physics.vy - a.physics.vy

            val velAlongNormal = rvx * nx + rvy * ny

            // If objects are already moving in different directions, we do not apply recoil.
            if (velAlongNormal > 0) return

            // Impulse with attenuation
            val e = (a.physics.bounce + b.physics.bounce) / 2f

            // weakening the force of the impulse so that they do not “explode”
            val impulseScale = 0.5f

            val massFactor = 1f / sqrt((a.physics.mass + b.physics.mass) / 2f)

            val j = impulseScale * massFactor *
                    (-(1 + e) * velAlongNormal) /
                    (1f / a.physics.mass + 1f / b.physics.mass)

            val impulseX = j * nx
            val impulseY = j * ny

            a.physics.vx -= impulseX / a.physics.mass
            a.physics.vy -= impulseY / a.physics.mass

            b.physics.vx += impulseX / b.physics.mass
            b.physics.vy += impulseY / b.physics.mass
        }
    }

}
