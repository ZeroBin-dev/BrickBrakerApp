package com.cyb.brickbraker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.cyb.brickbraker.common.Constants
import com.cyb.brickbraker.components.GameView
import com.cyb.brickbraker.ui.theme.BrickBrakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Constants.init(this)
        setContent {
            BrickBrakerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    var selectedStage by remember { mutableStateOf<Int?>(null) }

    if (selectedStage != null) {
        // 선택된 스테이지에 따라 게임 화면을 보여줍니다.
        AndroidView(
            factory = { context -> GameView(context, selectedStage!!) },
            modifier = modifier.fillMaxSize()
        )
    } else {
        StageSelection { stage -> selectedStage = stage } // 스테이지 선택
    }
}

@Composable
fun StageSelection(onStageSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "스테이지 선택",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 각 스테이지 버튼
        for (stage in 1..3) {
            StageButton(stage = stage, onClick = { onStageSelected(stage) })
        }
    }
}

@Composable
fun StageButton(stage: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // 버튼 색상 설정
    ) {
        Text(text = "Stage $stage", fontSize = 20.sp, color = Color.White) // 버튼 텍스트
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    BrickBrakerTheme {
        GameScreen()
    }
}
