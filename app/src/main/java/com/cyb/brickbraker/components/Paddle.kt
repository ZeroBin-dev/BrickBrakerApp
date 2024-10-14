package com.cyb.brickbraker.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.cyb.brickbraker.common.Constants

class Paddle() {

    // 패들 색상 지정
    private val paint = Paint().apply {
        color = Color.WHITE
    }

    private val paddleWidth = 500f // 패들 너비
    private val paddleHeight = 40f // 패들 높이

    // 패들 위치 설정 (화면 하단 중앙에 위치)
    var rect = RectF(
        (Constants.screenWidth / 2) - (paddleWidth / 2),
        Constants.screenHeight - 150f,
        (Constants.screenWidth / 2) + (paddleWidth / 2),
        Constants.screenHeight - 150f + paddleHeight
    )

    fun resetPaddle(){
        rect.set(
            (Constants.screenWidth / 2) - (paddleWidth / 2),
            Constants.screenHeight - 150f,
            (Constants.screenWidth / 2) + (paddleWidth / 2),
            Constants.screenHeight - 150f + paddleHeight
        )
    }

    // 패들 이동 메서드
    fun moveTo(x: Float) {
        // 패들의 중심을 x 좌표로 이동
        rect.offsetTo(x - paddleWidth / 2, rect.top)

        // 패들이 화면 바깥으로 나가지 않도록 제한
        if (rect.left < 0) {
            rect.offsetTo(0f, rect.top)  // 왼쪽 경계
        }
        if (rect.right > Constants.screenWidth) {
            rect.offsetTo(Constants.screenWidth - paddleWidth, rect.top)  // 오른쪽 경계
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }
}