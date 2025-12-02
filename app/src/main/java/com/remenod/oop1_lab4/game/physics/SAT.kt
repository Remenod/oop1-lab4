package com.remenod.oop1_lab4.game.physics

import kotlin.math.*

data class SATResult(
    val collided: Boolean,
    val mtvX: Float,
    val mtvY: Float,
    val depth: Float
)

fun satCollision(polyA: List<Pair<Float, Float>>, polyB: List<Pair<Float, Float>>): SATResult {
    var minOverlap = Float.MAX_VALUE
    var mtvX = 0f
    var mtvY = 0f

    fun edgesOf(poly: List<Pair<Float, Float>>) =
        poly.indices.map {
            val p1 = poly[it]
            val p2 = poly[(it + 1) % poly.size]
            Pair(p2.first - p1.first, p2.second - p1.second)
        }

    fun projections(axisX: Float, axisY: Float, poly: List<Pair<Float, Float>>): Pair<Float, Float> {
        var min = Float.MAX_VALUE
        var max = -Float.MAX_VALUE

        for (p in poly) {
            val proj = p.first * axisX + p.second * axisY
            if (proj < min) min = proj
            if (proj > max) max = proj
        }
        return Pair(min, max)
    }

    val axes = mutableListOf<Pair<Float, Float>>()

    axes += edgesOf(polyA).map { Pair(-(it.second), it.first) }
    axes += edgesOf(polyB).map { Pair(-(it.second), it.first) }

    for ((ax, ay) in axes) {
        val len = sqrt(ax * ax + ay * ay)
        if (len == 0f) continue

        val nx = ax / len
        val ny = ay / len

        val (aMin, aMax) = projections(nx, ny, polyA)
        val (bMin, bMax) = projections(nx, ny, polyB)

        if (aMax < bMin || bMax < aMin) {
            return SATResult(false, 0f, 0f, 0f)
        }

        val overlap = min(aMax, bMax) - max(aMin, bMin)
        if (overlap < minOverlap) {
            minOverlap = overlap
            mtvX = nx
            mtvY = ny
        }
    }

    return SATResult(true, mtvX * minOverlap, mtvY * minOverlap, minOverlap)
}
