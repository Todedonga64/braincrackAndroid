package movil.braincrack

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

@Composable
fun GameModesView(navegar: NavController, name: String) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/raw/gamemode")
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {navegar.navigate("suddendeath/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(end = 160.dp, top = 75.dp)) {
                Text(text = "Go!")
            }
            Button(onClick = {navegar.navigate("topics/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.Center)
                    .padding(start = 160.dp)) {
                Text(text = "Go!")
            }
            Button(onClick = {navegar.navigate("libre/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(end = 160.dp, bottom = 75.dp)) {
                Text(text = "Go!")
            }
        }
    }
}