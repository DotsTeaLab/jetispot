package bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.fullscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Expand
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.core.SpPlayerServiceManager
import bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.NowPlayingViewModel
import bruhcollective.itaysonlab.jetispot.ui.shared.PlayPauseButton

@Composable
fun Lyrics(
  viewModel: NowPlayingViewModel,
  isTextFullscreen: Boolean,
  damping: Float,
  stiffness: Float
) {
  val lyricsScrollBehavior = rememberLazyListState()
  val screenHeight =
    LocalConfiguration.current.screenHeightDp.dp +
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 1.dp

  LazyColumn() {
    item {
      Row(
        modifier = Modifier
          .height(
            animateDpAsState(
              if (isTextFullscreen) 0.dp else 56.dp,
              spring(damping * 1.2f, stiffness * 1f)
            ).value
          )
          .animateContentSize(spring(damping, stiffness)),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Rounded.MenuBook, contentDescription = "", modifier = Modifier.padding(start = 16.dp))

        Column(
          Modifier
            .weight(1f)
            .height(64.dp),
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = "Dummy lyrics text very bruh burgh burb",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            style = TextStyle(platformStyle = PlatformTextStyle(false)),
            maxLines = 3,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
              .padding(horizontal = 4.dp)
          )
        }

        Icon(Icons.Rounded.Expand, contentDescription = "", modifier = Modifier.padding(end = 16.dp))
      }

      Column(
        Modifier
          .height(
            animateDpAsState(
              if (isTextFullscreen) screenHeight else 0.dp,
              spring(damping * 1.3f, stiffness * 1f)
            ).value
          )
          .padding(horizontal = 22.dp)
          .systemBarsPadding()
          .fillMaxWidth()
          .animateContentSize(spring(damping * 1.3f, stiffness * 1f))
      ) {
        LyricsMiniplayer(viewModel)

        Box(Modifier.weight(1f)) {
          LazyColumn(
            state = lyricsScrollBehavior,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
          ) {
            item {
              Column(Modifier.statusBarsPadding()) {
                repeat(100) {
                  Text(
                    text = "Dummy lyrics text",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 0.dp)
                  )
                }
              }
            }
          }

//              Box(
//                modifier = Modifier
//                  .fillMaxHeight(0.2f)
//                  .fillMaxWidth()
//                  .background(
//                    Brush.verticalGradient(
//                      listOf(
//                        animateColorAsState(
//                          if (lyricsScrollBehavior.firstVisibleItemIndex != 1)
//                            monet.surfaceVariant
//                          else
//                            Color.Transparent
//                        ).value,
//                        Color.Transparent
//                      )
//                    )
//                  )
//              )
//              Box(
//                modifier = Modifier
//                  .fillMaxHeight(0.2f)
//                  .fillMaxWidth()
//                  .align(Alignment.BottomCenter)
//                  .background(
//                    Brush.verticalGradient(
//                      listOf(
//                        Color.Transparent,
//                        animateColorAsState(
//                          if (lyricsScrollBehavior.firstVisibleItemIndex != 0)
//                            monet.surfaceVariant
//                          else
//                            Color.Transparent
//                        ).value,
//                      )
//                    )
//                  )
//              )
        }

        Row(
          Modifier
            .height(128.dp)
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          AnimatedVisibility(
            visible = isTextFullscreen,
            enter = slideInVertically(spring(damping * 1.3f, stiffness)) { 5000 },
            exit = slideOutVertically(spring(damping * 1.3f, stiffness)) { 5000 }
          ) {
            Surface(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .height(72.dp)
                .width(106.dp)
                .clickable(
                  interactionSource = remember { MutableInteractionSource() },
                  indication = rememberRipple(color = MaterialTheme.colorScheme.primary)
                ) { viewModel.togglePlayPause() }
            ) {
              PlayPauseButton(
                isPlaying = viewModel.currentState.value == SpPlayerServiceManager.PlaybackState.Playing,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .size(64.dp)
                  .align(Alignment.CenterVertically)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun LyricsMiniplayer(viewModel: NowPlayingViewModel) {
  Column(
    Modifier
      .fillMaxWidth()
      .height(72.dp)
      .padding(vertical = 0.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      viewModel.currentTrack.value.title,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      fontSize = 18.sp,
      fontWeight = FontWeight.Medium
    )

    Text(
      viewModel.currentTrack.value.artist,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      fontSize = 14.sp,
      modifier = Modifier.padding(top = 2.dp)
    )
  }
}