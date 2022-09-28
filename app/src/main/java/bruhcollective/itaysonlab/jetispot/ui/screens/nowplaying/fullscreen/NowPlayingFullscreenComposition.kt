package bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.fullscreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import bruhcollective.itaysonlab.jetispot.ui.ext.blendWith
import bruhcollective.itaysonlab.jetispot.ui.ext.compositeSurfaceElevation
import bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.NowPlayingViewModel
import bruhcollective.itaysonlab.jetispot.ui.shared.AppPreferences.NPAnimationDamping
import bruhcollective.itaysonlab.jetispot.ui.shared.AppPreferences.NPAnimationStiffness
import bruhcollective.itaysonlab.jetispot.ui.theme.ApplicationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme.colorScheme as monet

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun NowPlayingFullscreenComposition (
  queueOpened: Boolean,
  setQueueOpened: (Boolean) -> Unit,
  bottomSheetState: BottomSheetState,
  mainPagerState: PagerState,
  viewModel: NowPlayingViewModel,
  bsOffset: Float,
  setLyricsOpened: () -> Unit,
  lyricsOpened: Boolean
) {
  val damping = kotlin.math.max(NPAnimationDamping!!, 0.0000001f)
  val stiffness = kotlin.math.max(NPAnimationStiffness!!, 0.0000001f)

  val scope = rememberCoroutineScope()

  val screenWidth = LocalConfiguration.current.screenWidthDp

//  val queueProgress by animateFloatAsState(
//    if (queueOpened) 1f else 0f,
//    animationSpec = tween(500, easing = FastOutSlowInEasing)
//  )
  val queueAnim by animateFloatAsState(
    targetValue = if (queueOpened) 1f else 0f,
    animationSpec = tween(500, easing = FastOutSlowInEasing)
  )
//  val lyricsProgress by animateFloatAsState(
//    if (lyricsOpened) 1f else 0f,
//    tween(500, easing = FastOutSlowInEasing)
//  )

  val backgroundColor by animateColorAsState(
    if (isSystemInDarkTheme())
      monet.surface.blendWith(monet.primary, 0.05f)
    else
      monet.surface.blendWith(monet.primary, 0.1f),
    tween(500)
  )

//  val anySuperProgress = remember(queueProgress, lyricsProgress) {
//    if (queueProgress > 0f) {
//      queueProgress
//    } else {
//      lyricsProgress
//    }
//  }

  Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
    val smallArtworkPaddingStart by animateDpAsState((16 * (1f - bsOffset)).dp, spring(damping, stiffness))
    val smallArtworkPaddingTop by animateDpAsState((8 * (1f - bsOffset)).dp, spring(damping, stiffness))
    val artworkPaddingHorizontal by animateDpAsState((22 * bsOffset).dp, spring(damping, stiffness))
    val artworkShape by animateDpAsState((8 + (24f * bsOffset)).dp)
    val artworkSize by animateDpAsState(
      ((48 * (1f - bsOffset)) + (bsOffset * (screenWidth))).dp,
      spring(damping, stiffness)
    )

    var artworkPositionCalc by remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }
    val artworkOffset by animateIntOffsetAsState(
      IntOffset(
        x = (((screenWidth) * bsOffset) * (1f - bsOffset)).toInt(),
        y = if (lyricsOpened)
          -2500
        else
          ((bsOffset * 2500 * (1f - bsOffset)) + (artworkPositionCalc.topLeft.y * bsOffset)).toInt()
      ),
      spring(damping, stiffness)
    )

    val mainContentPaddingBottom by animateDpAsState(
      if (lyricsOpened)
        0.dp
      else
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
      spring(damping * 1.1f, stiffness * 0.9f)
    )

    ApplicationTheme {
      Box(Modifier.alpha(1f - bsOffset).fillMaxSize().background(monet.compositeSurfaceElevation(3.dp)))
    }

    Row(Modifier.padding(start = max(smallArtworkPaddingStart, 0.dp), top = max(smallArtworkPaddingTop, 0.dp))) {
      Surface(
        color = Color.Transparent,
        modifier = Modifier.absoluteOffset { artworkOffset }.size(artworkSize)
      ) {
        ArtworkPager(
          viewModel,
          mainPagerState,
          bsOffset,
          Modifier
            .padding(horizontal = max(0.dp, artworkPaddingHorizontal))
            .aspectRatio(1f)
            .clip(RoundedCornerShape(artworkShape))
        )
      }
    }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
      // main content
      Column {
        Column(
          modifier = Modifier
            .alpha(bsOffset)
            .fillMaxHeight()
            .padding(bottom = max(mainContentPaddingBottom, 0.dp)),
          verticalArrangement = Arrangement.SpaceAround
        ) {
          NowPlayingHeader(
            stateTitle = stringResource(id = viewModel.getHeaderTitle()),
            onCloseClick = {
              if (queueOpened)
                setQueueOpened(false)
              else
                scope.launch { bottomSheetState.collapse() }
            },
            queueStateProgress = queueAnim,
            state = viewModel.getHeaderText(),
            modifier = Modifier
              .statusBarsPadding()
              .fillMaxWidth()
              .weight(0.57f, false)
              .padding(horizontal = 16.dp)
          )

          Column(
            Modifier
              .size((screenWidth).dp)
              .weight(1.15f)
              .aspectRatio(1f)
              .onGloballyPositioned { artworkPositionCalc = it.boundsInParent() }
          ) { }

          Column(Modifier.padding(horizontal = 8.dp).height(104.dp).weight(0.3f, false)) {
            ControlsHeader(scope, bottomSheetState, viewModel)
            ControlsSeekbar(viewModel)
          }

          Box(Modifier.weight(0.25f, false)) {
            ControlsMainButtons(viewModel, queueOpened, setQueueOpened)
          }

          ControlsBottomAccessories(
            { setLyricsOpened() },
            viewModel,
            queueOpened,
            setQueueOpened,
            lyricsOpened,
            damping * 1.3f,
            stiffness * 0.9f,
          )
        }
      }
    }

    NowPlayingQueue(
      viewModel = viewModel,
      modifier = Modifier.fillMaxSize(),
      rvStateProgress = queueAnim
    )
  }
}