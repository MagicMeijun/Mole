package tw.edu.pu.csim.tcyang.mole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import tw.edu.pu.csim.tcyang.mole.ui.theme.MoleTheme
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoleTheme {
                MoleScreen()
            }
        }
    }
}

// --- ViewModel ---
class MoleViewModel : ViewModel() {
    var counter by mutableStateOf(0)
        private set
    var stay by mutableStateOf(60)
        private set

    var maxX by mutableStateOf(0)
        private set
    var maxY by mutableStateOf(0)
        private set

    var offsetX by mutableStateOf(0)
        private set
    var offsetY by mutableStateOf(0)
        private set

    private var gameOver by mutableStateOf(false)
    private var jobActive = false

    fun incrementCounter() {
        if (!gameOver) counter++
    }

    fun getArea(gameSize: IntSize, moleSize: Int) {
        maxX = (gameSize.width - moleSize).coerceAtLeast(0)
        maxY = (gameSize.height - moleSize).coerceAtLeast(0)
        moveMole()
    }

    fun moveMole() {
        if (!gameOver) {
            offsetX = (0..maxX).random()
            offsetY = (0..maxY).random()
        }
    }

    fun startGame() {
        if (jobActive) return  // 避免重複啟動
        gameOver = false
        jobActive = true
        viewModelScope.launch {
            while (stay > 0) {
                delay(1000L)
                stay--
                moveMole()
            }
            gameOver = true
            jobActive = false
        }
    }

    fun resetGame() {
        counter = 0
        stay = 60
        gameOver = false
        moveMole()
        startGame()
    }

    fun isGameOver(): Boolean = gameOver
}

// --- Composable ---
@Composable
fun MoleScreen(moleViewModel: MoleViewModel = viewModel()) {
    val counter = moleViewModel.counter
    val stay = moleViewModel.stay
    val gameOver = moleViewModel.isGameOver()

    val density = LocalDensity.current
    val moleSizeDp = 150.dp
    val moleSizePx = with(density) { moleSizeDp.roundToPx() }

    // 啟動遊戲
    LaunchedEffect(Unit) {
        moleViewModel.startGame()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 顯示分數或遊戲結束文字
        Text(
            text = if (!gameOver) "分數: $counter \n時間: $stay 秒"
            else "遊戲結束！\n你的分數: $counter",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        // 遊戲區域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onSizeChanged { intSize ->
                    moleViewModel.getArea(intSize, moleSizePx)
                },
            contentAlignment = Alignment.TopStart
        ) {
            if (!gameOver) {
                Image(
                    painter = painterResource(id = R.drawable.mole),
                    contentDescription = "地鼠",
                    modifier = Modifier
                        .offset { IntOffset(moleViewModel.offsetX, moleViewModel.offsetY) }
                        .size(moleSizeDp)
                        .clickable { moleViewModel.incrementCounter() }
                )
            }
        }

        // 重新開始按鈕
        if (gameOver) {
            Button(
                onClick = { moleViewModel.resetGame() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("重新開始")
            }
        }
    }
}
