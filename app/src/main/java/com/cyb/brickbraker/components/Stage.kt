package com.cyb.brickbraker.components

import com.cyb.brickbraker.common.Constants

class Stage(val stageNumber: Int) {

    // 벽돌 리스트
    val bricks = mutableListOf<Brick>()
    private var totalRows: Int = 0
    private var totalColumns: Int = 0

    fun setupBricks() {
        when (stageNumber) {
            1 -> setupStage1Bricks()
            2 -> setupStage2Bricks()
            3 -> setupStage3Bricks()
        }
    }

    fun getTotal(): Int {
        return totalRows * totalColumns
    }

    // 스테이지 1 벽돌 배치
    private fun setupStage1Bricks() {
        totalRows = 5
        totalColumns = 8
        val brickWidth = 120f
        val brickHeight = 50f
        val totalBrickWidth = brickWidth * totalColumns + 20f * (totalColumns - 1)
        val startX = (Constants.screenWidth - totalBrickWidth) / 2

        for (i in 0 until totalRows) {
            for (j in 0 until totalColumns) {
                val x = startX + j * (brickWidth + 20f)
                val y = i * (brickHeight + 10f)
                bricks.add(Brick(x, y))
            }
        }
    }

    // 스테이지 2 벽돌 배치
    private fun setupStage2Bricks() {
        totalRows = 6 // 행 수를 늘림
        totalColumns = 12 // 열 수를 늘림
        val brickWidth = 90f // 벽돌 너비를 줄임
        val brickHeight = 40f // 벽돌 높이를 약간 늘림
        val totalBrickWidth = brickWidth * totalColumns + 10f * (totalColumns - 1) // 간격 조정
        val startX = (Constants.screenWidth - totalBrickWidth) / 2

        for (i in 0 until totalRows) {
            for (j in 0 until totalColumns) {
                // 행의 홀수 및 짝수에 대해 다른 패턴 적용
                if (i % 2 == 0) { // 짝수 행에만 벽돌 배치
                    val x = startX + j * (brickWidth + 10f)
                    val y = i * (brickHeight + 15f) // 행 사이 간격 조정
                    bricks.add(Brick(x, y))
                }
            }
        }
    }


    // 스테이지 3 벽돌 배치
    private fun setupStage3Bricks() {
        totalRows = 5 // 행 수
        val totalColumns = 9 // 열 수
        val brickWidth = 100f // 벽돌 너비
        val brickHeight = 60f // 벽돌 높이를 증가시킴
        val totalBrickWidth = brickWidth * totalColumns + 15f * (totalColumns - 1)
        val startX = (Constants.screenWidth - totalBrickWidth) / 2

        for (i in 0 until totalRows) {
            for (j in 0 until totalColumns) {
                // 계단 형태로 배치
                if (j >= totalRows - i) { // 계단 형태 유지
                    val x = startX + j * (brickWidth + 15f) // 간격 조정
                    val y = i * (brickHeight + 10f) // 높이 증가에 따라 조정
                    bricks.add(Brick(x, y))
                }
            }
        }
    }


}