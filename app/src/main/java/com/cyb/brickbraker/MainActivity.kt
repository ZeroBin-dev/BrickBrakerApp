package com.cyb.brickbraker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.cyb.brickbraker.common.Constants
import com.cyb.brickbraker.components.GameView
import com.cyb.brickbraker.ui.theme.BrickBrakerTheme

class MainActivity : ComponentActivity() {
    private lateinit var startButton: Button
    private lateinit var restartButton: Button
    private lateinit var gameViewContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Constants.init(this)

        // XML 레이아웃 설정
        setContentView(R.layout.activity_main)

        // UI 요소 초기화
        startButton = findViewById(R.id.startButton)
        restartButton = findViewById(R.id.restartButton)
        gameViewContainer = findViewById(R.id.gameViewContainer)

        // 시작 버튼 클릭 리스너
        startButton.setOnClickListener {
            startGame()
        }

        // 재시작 버튼 클릭 리스너
        restartButton.setOnClickListener {
            resetGame()
        }
    }

    private fun startGame() {
        startButton.visibility = View.GONE // 시작 버튼 숨기기
        restartButton.visibility = View.GONE // 재시작 버튼 숨기기

        // GameView 생성 및 추가
        val gameView = GameView(this)
        gameViewContainer.addView(gameView)
    }

    private fun resetGame() {
        // GameView의 인스턴스를 가져와서 resetGame() 호출
        val gameView = gameViewContainer.getChildAt(0) as? GameView
        gameView?.resetGame()

        // 재시작 버튼 숨기기
        restartButton.visibility = View.GONE
        startButton.visibility = View.GONE // 필요 시 시작 버튼도 숨길 수 있음

        // 게임을 다시 시작하려면 게임 상태를 초기화
        startGame() // startGame 메소드 호출
    }
}
