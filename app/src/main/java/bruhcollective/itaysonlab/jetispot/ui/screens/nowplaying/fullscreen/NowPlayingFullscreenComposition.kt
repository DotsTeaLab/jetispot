package bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.fullscreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
  isLyricsFullscreenAction: () -> Unit,
  isLyricsFullscreen: Boolean
) {
  val scope = rememberCoroutineScope()

  var artworkPositionCalc by remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }
  val screenHeight =
    LocalConfiguration.current.screenHeightDp.dp +
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 1.dp
  val screenWidth = LocalConfiguration.current.screenWidthDp

  val damping = NPAnimationDamping!!
  val stiffness = NPAnimationStiffness!!
  
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        if (isSystemInDarkTheme())
          animateColorAsState(
            monet.surface.blendWith(monet.primary, 0.05f), tween(500)
          ).value
        else
          animateColorAsState(
            monet.surface.blendWith(monet.primary, 0.1f), tween(500)
          ).value
      )
  ) {
    ApplicationTheme() {
      Box(modifier = Modifier
        .alpha(1f - bsOffset)
        .fillMaxSize()
        .background(monet.compositeSurfaceElevation(3.dp)))
    }

    Row(
      Modifier
        .padding(
          start = max(animateDpAsState((13f * (1f - bsOffset)).dp, spring(damping, stiffness)).value, 0.dp),
          top = max(animateDpAsState((4f * (1f - bsOffset)).dp, spring(damping, stiffness)).value, 0.dp)
        )
    ) {
      animateIntOffsetAsState(
        IntOffset(
          x = (((screenWidth) * bsOffset) * (1f - bsOffset)).toInt(),
          y =
          if (isLyricsFullscreen)
            -2500
          else
            ((bsOffset * 2500 * (1f - bsOffset)) + (artworkPositionCalc.top * bsOffset)).toInt()
        ),
        spring(damping, stiffness)
      ).value.let {
        Surface(
          color = Color.Transparent,
          modifier = Modifier
            .width(
              animateDpAsState(
                ((54 * (1f - bsOffset)) + (bsOffset * (screenWidth))).dp,
                spring(damping, stiffness)
              ).value
            )
            .size(((54 * (1f - bsOffset)) + (bsOffset * (screenWidth * 0.975f))).dp)
            .aspectRatio(1f)
            .absoluteOffset { it }
        ) {
          ArtworkPager(viewModel, mainPagerState, bsOffset)
        }
      }
    }

    Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
      // main content
      Column() {
        Column(
          modifier = Modifier
            .alpha(bsOffset)
            .fillMaxHeight()
            .padding(
              top = max(
                animateDpAsState(
                  if (isLyricsFullscreen) 0.dp else 16.dp,
                  spring(damping, stiffness)
                ).value,
                0.dp
              ),
              bottom = animateDpAsState(
                if (isLyricsFullscreen)
                  0.dp
                else
                  WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
              ).value
            ),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          NowPlayingHeader(
            stateTitle = stringResource(id = viewModel.getHeaderTitle()),
            onCloseClick = {
              if (queueOpened)
                setQueueOpened(false)
              else
                scope.launch { bottomSheetState.collapse() }
            },
            queueStateProgress = animateFloatAsState(
              targetValue = if (queueOpened) 1f else 0f,
              animationSpec = tween(500, easing = FastOutSlowInEasing)
            ).value,
            state = viewModel.getHeaderText(),
            modifier = Modifier
              .statusBarsPadding()
              .fillMaxWidth()
              .weight(1f, false)
              .padding(horizontal = 16.dp)
          )

          Column(
            Modifier
              .fillMaxWidth()
              .weight(5f, false)
              .height((screenWidth * 0.9).dp)
              .onGloballyPositioned { artworkPositionCalc = it.boundsInParent() }
          ) { }

          Column(
            Modifier
              .padding(horizontal = 8.dp)
              .weight(1f, false)) {
            ControlsHeader(scope, bottomSheetState, viewModel)
            ControlsSeekbar(viewModel)
          }

          Box(Modifier.weight(1f, false)) {
            ControlsMainButtons(viewModel, queueOpened, setQueueOpened)
          }


          ControlsBottomAccessories(
            { isLyricsFullscreenAction() },
            viewModel,
            queueOpened,
            setQueueOpened,
            isLyricsFullscreen,
            damping,
            stiffness,
          )
        }
      }
    }

    NowPlayingQueue(
      viewModel = viewModel,
      modifier = Modifier.fillMaxSize(),
      rvStateProgress = animateFloatAsState(
        targetValue = if (queueOpened) 1f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing)
      ).value
    )
  }
}