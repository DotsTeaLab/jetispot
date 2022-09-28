package bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.fullscreen

import android.text.format.DateUtils
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.core.SpPlayerServiceManager
import bruhcollective.itaysonlab.jetispot.ui.ext.blendWith
import bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.NowPlayingViewModel
import bruhcollective.itaysonlab.jetispot.ui.shared.MarqueeText
import bruhcollective.itaysonlab.jetispot.ui.shared.PlayPauseButton
import bruhcollective.itaysonlab.jetispot.ui.shared.navClickable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope
import androidx.compose.material3.MaterialTheme.colorScheme as monet


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ControlsHeader(
  scope: CoroutineScope,
  bottomSheetState: BottomSheetState,
  viewModel: NowPlayingViewModel
) {
  Row(horizontalArrangement = Arrangement.SpaceBetween) {
    Column(modifier = Modifier.weight(0.9f)) {
      MarqueeText(
        text = viewModel.currentTrack.value.title,
        modifier = Modifier
          .padding(horizontal = 14.dp)
          .navClickable { viewModel.navigateToSource(scope, bottomSheetState, it) },
        fontSize = 24.sp,
        color = monet.onSecondaryContainer.copy(0.85f),
        fontWeight = FontWeight.ExtraBold,
          basicGradientColor = if (isSystemInDarkTheme())
          MaterialTheme.colorScheme.surface.blendWith(monet.primary, ratio = 0.05f)
        else
          MaterialTheme.colorScheme.surface.blendWith(monet.primary, ratio = 0.1f)
      )

      Spacer(Modifier.height(2.dp))

      MarqueeText(
        text = viewModel.currentTrack.value.artist,
        modifier = Modifier
          .padding(horizontal = 14.dp)
          .navClickable { viewModel.navigateToArtist(scope, bottomSheetState, it) },
        overflow = TextOverflow.Ellipsis,
        fontSize = 18.sp,
        color = monet.onSecondaryContainer.copy(alpha = 0.7f),
        basicGradientColor = if (isSystemInDarkTheme())
          MaterialTheme.colorScheme.surface.blendWith(monet.primary, ratio = 0.05f)
        else
          MaterialTheme.colorScheme.surface.blendWith(monet.primary, ratio = 0.1f)
      )
    }

    Box(modifier = Modifier.weight(0.1f).align(Alignment.CenterVertically)) {
      Icon(
        imageVector = Icons.Rounded.Favorite,
        contentDescription = "",
        tint = monet.onSecondaryContainer.copy(0.85f),
        modifier = Modifier.padding(end = 12.dp).size(26.dp)
      )
    }
  }
}

@Composable
fun ControlsSeekbar(viewModel: NowPlayingViewModel) {
  var isSeekbarDragging by remember { mutableStateOf(false) }
  var seekbarDraggingProgress by remember { mutableStateOf(0f) }

  val elapsedTime = remember(viewModel.currentPosition.value, isSeekbarDragging, seekbarDraggingProgress) {
    val ms = if (isSeekbarDragging) {
      (seekbarDraggingProgress * viewModel.currentTrack.value.duration).toLong()
    } else {
      viewModel.currentPosition.value.progressMilliseconds
    } / 1000L

    DateUtils.formatElapsedTime(ms)
  }

  val totalTime = remember(viewModel.currentPosition.value) {
    DateUtils.formatElapsedTime(viewModel.currentTrack.value.duration / 1000L)
  }

  Box {
    Slider(
      modifier = Modifier.padding(horizontal = 6.dp),
      value = if (isSeekbarDragging) seekbarDraggingProgress else viewModel.currentPosition.value.progressRange,
      colors = SliderDefaults.colors(
        thumbColor = monet.onSecondaryContainer,
        activeTrackColor = monet.onSecondaryContainer.copy(0.85f),
        inactiveTrackColor = monet.onSecondaryContainer.copy(alpha = 0.2f)
      ),
      onValueChange = {
        isSeekbarDragging = true
        seekbarDraggingProgress = it
      },
      onValueChangeFinished = {
        isSeekbarDragging = false
        viewModel.seekTo((seekbarDraggingProgress * viewModel.currentTrack.value.duration).toLong())
      }
    )

    Column(
      modifier = Modifier.padding(horizontal = 13.dp).fillMaxWidth(),
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.Bottom
    ) {
      Row(Modifier.height(52.dp), verticalAlignment = Alignment.Bottom) {
        Text(
          text = elapsedTime,
          color = monet.onSecondaryContainer.copy(0.85f),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )

        Text(text = " / ", color = monet.onSecondaryContainer.copy(0.85f), fontSize = 12.sp)

        Text(
          text = totalTime,
          color = monet.onSecondaryContainer.copy(0.85f),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun ControlsMainButtons(
  viewModel: NowPlayingViewModel,
  queueOpened: Boolean,
  setQueueOpened: (Boolean) -> Unit
) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
  ) {
    IconButton(
      onClick = { /*TODO*/ },
      modifier = Modifier.size(32.dp),
      colors = IconButtonDefaults.iconButtonColors(
        contentColor = monet.onSecondaryContainer.copy(0.85f)
      )
    ) {
      Icon(imageVector = Icons.Rounded.Shuffle, contentDescription = null)
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceEvenly,
      modifier = Modifier.fillMaxWidth(0.85f)
    ) {
      IconButton(
        onClick = { viewModel.skipPrevious() },
        modifier = Modifier
          .size(56.dp)
          .clip(RoundedCornerShape(28.dp))
          .background(monet.onPrimaryContainer.copy(0.1f)),
        colors = IconButtonDefaults.iconButtonColors(
          contentColor = monet.onSecondaryContainer.copy(0.85f)
        )
      ) {
        Icon(
          imageVector = Icons.Rounded.SkipPrevious,
          contentDescription = null,
          modifier = Modifier.size(42.dp)
        )
      }

      Surface(
        color = monet.primaryContainer.blendWith(monet.primary, 0.3f).copy(0.5f),
        modifier = Modifier
          .clip(RoundedCornerShape(28.dp))
          .height(72.dp)
          .width(106.dp)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = monet.primary)
          ) { viewModel.togglePlayPause() }
      ) {
        PlayPauseButton(
          isPlaying = viewModel.currentState.value == SpPlayerServiceManager.PlaybackState.Playing,
          color = monet.onSecondaryContainer.copy(0.85f) /* if (viewModel.currentBgColor.value != Color.Transparent) viewModel.currentBgColor.value else Color.Black*/,
          modifier = Modifier.size(64.dp).align(Alignment.CenterVertically)
        )
      }

      IconButton(
        onClick = { viewModel.skipNext() },
        modifier = Modifier
          .size(56.dp)
          .clip(RoundedCornerShape(28.dp))
          .background(monet.onPrimaryContainer.copy(0.1f)),
        colors = IconButtonDefaults.iconButtonColors(
          contentColor = monet.onSecondaryContainer.copy(0.85f)
        )
      ) {
        Icon(
          imageVector = Icons.Rounded.SkipNext,
          contentDescription = null,
          modifier = Modifier.size(42.dp)
        )
      }
    }

    IconButton(
      onClick = { /*TODO*/ },
      modifier = Modifier.size(32.dp).clip(CircleShape),
      colors = IconButtonDefaults.iconButtonColors(
        contentColor = monet.onSecondaryContainer.copy(0.85f)
      )
    ) {
      Icon(imageVector = Icons.Rounded.Repeat, contentDescription = null)
    }
  }
}

@Composable
fun ControlsBottomAccessories(
  lyricsClickAction: () -> Unit,
  viewModel: NowPlayingViewModel,
  queueOpened: Boolean,
  setQueueOpened: (Boolean) -> Unit,
  isLyricsFullscreen: Boolean,
  damping: Float,
  stiffness: Float
) {
  val hasLyrics = viewModel.spLyricsController.currentLyricsLines.size> 0

  val cardAlpha by animateFloatAsState(if (hasLyrics) 1f else 0f)
  val cardShape by animateDpAsState(if (isLyricsFullscreen) 0.dp else 128.dp, spring(damping, stiffness))
  val sideButtonsSize by animateDpAsState(if (isLyricsFullscreen) 0.dp else 56.dp, spring(damping, stiffness))
  val horizontalPadding by animateDpAsState(if (isLyricsFullscreen) 0.dp else 8.dp, spring(damping, stiffness))

  Row(
    modifier = Modifier.padding(horizontal = max(horizontalPadding, 0.dp)).fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(
      onClick = { /*TODO*/ },
      modifier = Modifier.size(sideButtonsSize),
      colors = IconButtonDefaults.iconButtonColors(
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.85f)
      )
    ) {
      Icon(
        imageVector = Icons.Rounded.VolumeDown,
        contentDescription = null,
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(monet.primaryContainer.blendWith(monet.primary, 0.3f).copy(0.5f))
          .padding(6.dp)
      )
    }

    Card(
      modifier = Modifier.weight(1f).alpha(cardAlpha),
      shape = RoundedCornerShape(if (hasLyrics) max(0.dp, cardShape) else 0.dp)
    ) {
      if (hasLyrics)
        NowPlayingLyricsComposition(viewModel, isLyricsFullscreen, damping, stiffness) { lyricsClickAction() }
    }

    IconButton(
      onClick = { setQueueOpened(!queueOpened) },
      modifier = Modifier.size(sideButtonsSize),
      colors = IconButtonDefaults.iconButtonColors(
        contentColor = monet.onSecondaryContainer.copy(0.85f)
      )
    ) {
      Icon(
        imageVector = Icons.Rounded.QueueMusic,
        contentDescription = null,
        modifier = Modifier.size(26.dp)
      )
    }
  }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtworkPager(
  viewModel: NowPlayingViewModel,
  pagerState: PagerState,
  bsOffset: Float,
  modifier: Modifier = Modifier
) {
  HorizontalPager(count = viewModel.currentQueue.value.size, state = pagerState) { page ->
    if (page == viewModel.currentQueuePosition.value && viewModel.currentTrack.value.artworkCompose != null) {
      Image(
        viewModel.currentTrack.value.artworkCompose!!,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
      )
    } else {
      NowPlayingBackgroundItem(track = viewModel.currentQueue.value[page], modifier = modifier)
    }
  }
}