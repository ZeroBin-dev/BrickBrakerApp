package com.cyb.brickbraker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    var gameStarted by remember { mutableStateOf(false) }

    if (gameStarted) {
        AndroidView(
            factory = { context -> GameView(context) }, // GameView를 Compose에서 렌더링
            modifier = modifier.fillMaxSize()
        )
    } else {
        StartButton { gameStarted = true } // 게임 시작 버튼 클릭 시 게임 시작
    }

}

@Composable
fun StartButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClick) {
            Text(text = "게임 시작")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    BrickBrakerTheme {
        GameScreen()
    }
}