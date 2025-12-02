package com.remenod.oop1_lab4.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class GameView(
    context: Context,
    private val objects: List<GameObject>
) : View(context) {

    private val paint = Paint()
    private val backgroundPaint = Paint().apply {
        color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(backgroundPaint)

        for (obj in objects) {
            obj.figure.drawBlack(canvas, paint)
        }
    }
}

