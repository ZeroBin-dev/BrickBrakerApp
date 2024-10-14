package com.cyb.brickbraker.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

/**
 * 벽돌
 */
class Brick(x: Float, y: Float) {

    // 벽돌 색상 지정
    private val paint = Paint().apply {
        color = Color.BLUE
    }

    val width = 120f // 벽돌 너비
    val height = 50f // 벽돌 높이

    // 벽돌 위치 지정
    var rect = RectF(x, y, x + width, y + height)

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }

}