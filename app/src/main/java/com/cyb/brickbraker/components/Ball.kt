package com.cyb.brickbraker.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.cyb.brickbraker.common.Constants

/**
 * 공
 */
class Ball {

    // 공 색상 지정
    private val paint = Paint().apply {
        color = Color.RED
    }

    var radius = 20f // 공의 크기
    var speed = 10f // 공의 속도
    var dx = speed // x 방향 속도
    var dy = speed // y 방향 속도

    // 공의 초기 위치를 화면 중앙으로 설정
    var rect = RectF(
        (Constants.screenWidth / 2) - radius,
        (Constants.screenHeight / 2) - radius,
        (Constants.screenWidth / 2) + radius,
        (Constants.screenHeight / 2) + radius
    )

    fun resetBall(){
        radius = 20f
        speed = 10f
        dx = speed
        dy = speed

        rect.set(
            (Constants.screenWidth / 2) - radius,
            (Constants.screenHeight / 2) - radius,
            (Constants.screenWidth / 2) + radius,
            (Constants.screenHeight / 2) + radius
        )
    }

    // 공 위치 업데이트
    fun update() {
        rect.offset(dx, dy)
    }

    // 공 좌우 방향 반전 (x축 속도를 반전)
    fun reverseX() {
        dx = -dx
    }

    // 공 상하 방향 반전 (y축 속도를 반전)
    fun reverseY() {
        dy = -dy
    }

    // 공 그리기
    fun draw(canvas: Canvas) {
        update()
        rect.set(
            rect.left,
            rect.top,
            rect.left + radius * 2,
            rect.top + radius * 2
        )
        canvas.drawOval(rect, paint)
    }
}
