package bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.core.SpPlayerServiceManager
import bruhcollective.itaysonlab.jetispot.ui.shared.MarqueeText
import bruhcollective.itaysonlab.jetispot.ui.shared.PlayPauseButton

@Composable
fun NowPlayingMiniplayer(viewModel: NowPlayingViewModel, modifier: Modifier, bsOffset: Float) {
  val miniplayerAlpha by animateFloatAsState(  1f - bsOffset * 3)

  Box(modifier.alpha(miniplayerAlpha)) {
    LinearProgressIndicator(
      progress = viewModel.currentPosition.value.progressRange,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier
        .height(2.dp)
        .fillMaxWidth()
    )

    Row(
      Modifier
        .height(64.dp)
        .fillMaxWidth(),
      Arrangement.SpaceBetween,
      Alignment.CenterVertically
    ) {
      Column(
        Modifier
          .padding(start = 80.dp, top = 0.dp)
          .weight(1f, false)) {
        MarqueeText(
          viewModel.currentTrack.value.title,
          color = MaterialTheme.colorScheme.onSecondaryContainer,
          fontSize = 16.sp,
          fontWeight = FontWeight.Medium,
          basicGradientColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )

        MarqueeText(
          viewModel.currentTrack.value.artist,
          color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
          fontSize = 12.sp,
          modifier = Modifier.padding(top = 4.dp),
          basicGradientColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
      }

      Surface(
        modifier = Modifier
          .width(92.dp)
          .fillMaxHeight()
          .padding(vertical = 12.dp, horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.1f),
        shape = CircleShape
      ) {
        PlayPauseButton(
          viewModel.currentState.value == SpPlayerServiceManager.PlaybackState.Playing,
          MaterialTheme.colorScheme.onSecondaryContainer.copy(0.85f),
          Modifier
            .width(56.dp)
            .align(Alignment.CenterVertically)
            .clickable { viewModel.togglePlayPause() }
        )
      }
    }
  }
}