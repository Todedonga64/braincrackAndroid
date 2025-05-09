package movil.braincrack

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun TopicsView(navegar: NavController, name: String) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/raw/topics")
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
            Button(onClick = {navegar.navigate("exactamaniaca/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(start = 65.dp, top = 85.dp)) {
                Text(text = "Go!")
            }
            Button(onClick = {navegar.navigate("lombiletras/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(end = 65.dp, top = 85.dp)) {
                Text(text = "Go!")
            }
            Button(onClick = {navegar.navigate("chismesito/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.CenterEnd)
                    .padding(end = 65.dp, top = 40.dp)) {
                Text(text = "Go!")
            }
            Button(onClick = {navegar.navigate("geobrain/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.BottomStart)
                    .padding(start = 65.dp, bottom = 60.dp)) {
                Text(text = "Go!")
            }
            Button(onClick = {navegar.navigate("datonauta/${Uri.encode(name)}")},
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(end = 65.dp, bottom = 60.dp)) {
                Text(text = "Go!")
            }
        }
    }
}