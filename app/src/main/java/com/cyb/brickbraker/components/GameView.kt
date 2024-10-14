package com.cyb.brickbraker.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.cyb.brickbraker.common.Constants


/**
 * 벽돌깨기 메인 뷰
 */
class GameView(context: Context, private val stageNumber: Int, attrs: AttributeSet? = null, ) : SurfaceView(context, attrs),
    SurfaceHolder.Callback {

    private val paddle = Paddle() // 패들
    private val ball = Ball() // 볼
    private val bricks = mutableListOf<Brick>() // 벽돌리스트
    private var isRunning = false // 실행중
    private var isGameOver = false // 게임종료
    private var gameThread: Thread? = null // 쓰레드
    private var stage: Stage? = null // 스테이지
    private var currentStage = stageNumber // 현재 스테이지
    private var remainingBlocks = 0 // 남는 블록 수
    private var onStageUpdatedListener: ((Int, Int) -> Unit)? = null
    private var onStageClearedListener: (() -> Unit)? = null
    private val textPaint = Paint().apply {
        color = Color.WHITE // 텍스트 색상
        textSize = 50f // 텍스트 크기
        style = Paint.Style.FILL // 채우기 스타일
    }

    init {
        holder.addCallback(this)
        // 초기 벽돌 설정
        setupBricks(currentStage)
    }

    // 벽돌 설정
    private fun setupBricks(curStage: Int) {
        bricks.clear() // 이전 스테이지의 벽돌 삭제
        stage = Stage(curStage)
        stage!!.setupBricks()
        bricks.addAll(stage!!.bricks) // 새로 생성된 벽돌 추가
        remainingBlocks = stage!!.getTotal()
    }

    fun nextStage() {
        val nextStageNumber = (stage?.stageNumber ?: 1) + 1

        if (nextStageNumber <= 3) {
            setupBricks(nextStageNumber)  // 다음 스테이지로 변경
            startGame()
        } else {
            println("모든 스테이지 클리어!")
        }
    }

    // 게임 진행
    private fun gameLoop() {
        while (isRunning) {
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    checkCollisions()  // 충돌 감지
                    ball.update()  // 공의 위치 업데이트
                    canvas.drawColor(Color.BLACK)  // 배경 색상 설정

                    // 현재 스테이지 및 남은 블록 수 그리기
                    val textXPosition = Constants.screenWidth / 2 // 중앙 정렬 X 좌표
                    val textYPosition = Constants.screenHeight * 2 / 3 // 화면 중앙 쪽 Y 좌표 (2/3 지점)

                    // 텍스트를 그릴 때, 가운데 정렬을 위해 텍스트의 너비를 고려하여 X 위치 조정
                    canvas.drawText(
                        "Stage: $currentStage",
                        textXPosition - textPaint.measureText("Stage: $currentStage") / 2,
                        textYPosition.toFloat(),
                        textPaint
                    )
                    canvas.drawText(
                        "Remaining Blocks: $remainingBlocks",
                        textXPosition - textPaint.measureText("Remaining Blocks: $remainingBlocks") / 2,
                        textYPosition + 60f,
                        textPaint
                    )


                    paddle.draw(canvas)  // 패들 그리기
                    ball.draw(canvas)  // 공 그리기
                    bricks.forEach { it.draw(canvas) }  // 벽돌 그리기
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    // 충돌 감지
    private fun checkCollisions() {
        // 1. 패들 충돌
        if (ball.rect.intersect(paddle.rect)) {
            val paddleWidth = paddle.rect.width()
            val hitPosition = (ball.rect.centerX() - paddle.rect.left) / paddleWidth

            // 패들 4등분
            when {
                hitPosition < 0.25 -> { // 1/4 왼쪽
                    ball.reverseY() // Y축 반전
                    ball.dx = -Math.abs(ball.speed) // 왼쪽으로 이동
                }

                hitPosition < 0.5 -> { // 2/4 중앙 왼쪽
                    ball.reverseY() // Y축 반전
                    ball.dx = -ball.speed * 0.5f // 왼쪽으로 이동 (속도 감소)
                }

                hitPosition < 0.75 -> { // 3/4 중앙 오른쪽
                    ball.reverseY() // Y축 반전
                    ball.dx = ball.speed * 0.5f // 오른쪽으로 이동 (속도 감소)
                }

                else -> { // 4/4 오른쪽
                    ball.reverseY() // Y축 반전
                    ball.dx = Math.abs(ball.speed) // 오른쪽으로 이동
                }
            }

            // 공이 패들 위쪽에 고정되도록 위치 조정
            ball.rect.bottom = paddle.rect.top // 패들의 위쪽과 일치하도록 설정

            // 추가 위치 조정 (패들에서 약간 떨어뜨리기)
            ball.rect.offset(0f, -ball.radius) // 공을 패들에서 약간 위로 이동

            return // 패들 충돌 처리 후 종료
        }

        // 2. 벽(좌,우) 충돌
        else if (ball.rect.left <= 0 || ball.rect.right >= Constants.screenWidth) {
            ball.reverseX() // X축 반전
        }

        // 3. 벽(위) 충돌
        else if (ball.rect.top <= 0) {
            ball.reverseY() // Y축 반전
        }

        // 4. 벽(아래) 충돌
        else if (ball.rect.bottom >= Constants.screenHeight) {
            gameOver()
        } else {
            // 5. 벽돌 충돌
            val iterator = bricks.iterator()
            while (iterator.hasNext()) {
                val brick = iterator.next()
                if (ball.rect.intersect(brick.rect)) {
                    ball.reverseY() // 공의 Y축 방향 반전
                    iterator.remove() // 벽돌 제거
                    break
                }
            }
        }

        remainingBlocks = bricks.size // 현재 남아 있는 블록 수

        // 남은 블록 수가 0이 되면 스테이지 클리어 이벤트 호출
        if (remainingBlocks == 0) {
            onStageClearedListener?.invoke() // 스테이지 클리어 이벤트 호출
            nextStage()
        }

        updateStageInfo(currentStage, remainingBlocks)
    }

    // 게임 오버 처리
    private fun gameOver() {
        // 게임 오버 플래그 설정
        isGameOver = true
        isRunning = false

        // UI 스레드에서 게임 오버 버튼 표시
        (context as Activity).runOnUiThread {
            // 버튼 생성
            val restartButton = Button(context).apply {
                text = "재시작"
                setOnClickListener {
                    resetGame() // 재시작 메서드 호출
                    visibility = View.GONE // 버튼 숨기기
                    startGame()
                }
            }

            // FrameLayout을 사용하여 버튼을 중앙에 위치시키기
            val layout = FrameLayout(context).apply {
                addView(restartButton)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }

            // 버튼의 레이아웃 파라미터 설정
            restartButton.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER // 버튼 위치 중앙으로 설정
            }

            // 레이아웃에 버튼 추가 (여기서는 ViewGroup에 추가해야 함)
            (context as Activity).addContentView(
                layout,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    fun startGame() {
        isRunning = true
        gameThread = Thread {
            gameLoop()
        }
        gameThread?.start()
    }

    private fun resetGame() {
        // 게임 재시작 시 초기 상태로 되돌림
        isGameOver = false
        isRunning = true

        // 공과 패들 위치 초기화
        ball.resetBall()
        paddle.resetPaddle()

        this.invalidate()  // 화면을 다시 그리기
    }

    fun setOnStageUpdatedListener(listener: (Int, Int) -> Unit) {
        onStageUpdatedListener = listener
    }

    fun setOnStageClearedListener(listener: () -> Unit) {
        onStageClearedListener = listener
    }

    fun updateStageInfo(stage: Int, blocks: Int) {
        currentStage = stage
        remainingBlocks = blocks
        onStageUpdatedListener?.invoke(currentStage, remainingBlocks) // UI에 업데이트 알림
    }

    // 패들 움직이기
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isGameOver) { // 게임이 오버되지 않은 경우에만 패들 이동
            event?.let {
                paddle.moveTo(it.x)  // 터치 이벤트로 패들 이동
            }
        }
        return true
    }

    // 시작
    override fun surfaceCreated(holder: SurfaceHolder) {
        isRunning = true
        startGame()
    }

    // 변경
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    // 끝
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isRunning = false
    }
}
