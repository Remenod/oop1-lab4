package com.remenod.oop1_lab4.game

import com.remenod.oop1_lab4.game.figures.Figure
import com.remenod.oop1_lab4.game.physics.PhysicsBody

class GameObject(
    val figure: Figure,
    val physics: PhysicsBody
) {
    companion object {
        fun resolveCollision(a: GameObject, b: GameObject) {
            val dx = b.figure.centerX - a.figure.centerX
            val dy = b.figure.centerY - a.figure.centerY
            val dist = kotlin.math.sqrt(dx*dx + dy*dy)

            if (dist == 0f) return

            val nx = dx / dist
            val ny = dy / dist

            val dvx = b.physics.vx - a.physics.vx
            val dvy = b.physics.vy - a.physics.vy

            val dot = dvx*nx + dvy*ny
            if (dot > 0) return

            val bounce = (a.physics.bounce + b.physics.bounce) / 2

            val j = -(1 + bounce) * dot / (1/a.physics.mass + 1/b.physics.mass)

            val impulseX = j * nx
            val impulseY = j * ny

            a.physics.vx -= impulseX / a.physics.mass
            a.physics.vy -= impulseY / a.physics.mass

            b.physics.vx += impulseX / b.physics.mass
            b.physics.vy += impulseY / b.physics.mass
        }
    }
}
