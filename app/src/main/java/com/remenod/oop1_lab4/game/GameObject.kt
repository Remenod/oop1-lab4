package com.remenod.oop1_lab4.game

import com.remenod.oop1_lab4.game.figures.Figure
import com.remenod.oop1_lab4.game.physics.PhysicsBody
import com.remenod.oop1_lab4.game.physics.satCollision
import kotlin.math.*

class GameObject(
    val figure: Figure,
    val physics: PhysicsBody
)
