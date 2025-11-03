package tw.edu.pu.csim.tcyang.mole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.tcyang.mole.ui.theme.MoleTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable



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

@Composable

fun MoleScreen() {

    var counter by rememberSaveable { mutableLongStateOf(0) }

    Box (

        modifier = Modifier.fillMaxSize(),

        Alignment.Center

    ) {

        Text(counter.toString())

    }

    Image(

        painter = painterResource(id = R.drawable.mole),

        contentDescription = "地鼠",

        modifier = Modifier

            .offset { IntOffset(50, 200) }

            .size(150.dp)

            .clickable { counter++ }

    )

}