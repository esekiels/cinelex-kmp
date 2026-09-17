package co.esekiels.cinelex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.esekiels.cinelex.common.platform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
	        SmokeScreen()
        }
    }
}

@Composable
internal fun SmokeScreen() {
	MaterialTheme {
		Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			Text("Compose is talking to ${platform().name}")
		}
	}
}

@Preview
@Composable
fun AppAndroidPreview() {
	SmokeScreen()
}
