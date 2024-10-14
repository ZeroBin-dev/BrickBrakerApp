package com.cyb.brickbraker.components

import com.cyb.brickbraker.common.Constants

class Stage(val stageNumber: Int) {

    // 벽돌 리스트
    val bricks = mutableListOf<Brick>()

    fun setupBricks() {
        when (stageNumber) {
            1 -> setupStage1Bricks()
            2 -> setupStage2Bricks()
            3 -> setupStage3Bricks()
        }
    }

    // 스테이지 1 벽돌 배치
    private fun setupStage1Bricks() {
        val totalRows = 5
        val totalColumns = 8
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
        val totalRows = 3
        val totalColumns = 10
        val brickWidth = 100f
        val brickHeight = 40f
        val totalBrickWidth = brickWidth * totalColumns + 15f * (totalColumns - 1)
        val startX = (Constants.screenWidth - totalBrickWidth) / 2

        for (i in 0 until totalRows) {
            for (j in 0 until totalColumns) {
                if (i % 2 == 0) { // 짝수 행에만 벽돌 배치
                    val x = startX + j * (brickWidth + 15f)
                    val y = i * (brickHeight + 10f)
                    bricks.add(Brick(x, y))
                }
            }
        }
    }

    // 스테이지 3 벽돌 배치
    private fun setupStage3Bricks() {
        val brickWidth = 80f
        val brickHeight = 30f

        val brickPositions = listOf(
            Pair(50f, 50f), Pair(150f, 50f), Pair(250f, 50f),
            Pair(100f, 100f), Pair(200f, 100f), Pair(300f, 100f),
            Pair(150f, 150f), Pair(250f, 150f)
        )

        for ((x, y) in brickPositions) {
            bricks.add(Brick(x, y))
        }
    }

}