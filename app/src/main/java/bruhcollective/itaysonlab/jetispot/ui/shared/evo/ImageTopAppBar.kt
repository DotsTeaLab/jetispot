package bruhcollective.itaysonlab.jetispot.ui.shared.evo

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import bruhcollective.itaysonlab.jetispot.ui.ext.blendWith
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageTopAppBar(
  image: @Composable () -> Unit,
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
  appBarHeight: Dp = 500.dp,
  colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
  scrollBehavior: TopAppBarScrollBehavior? = null
) {
  SingleRowTopAppBar(
    modifier = modifier,
    image = image,
    title = title,
    titleTextStyle = MaterialTheme.typography.titleLarge.plus(TextStyle(lineHeight = 20.sp)),
    centeredTitle = false,
    navigationIcon = navigationIcon,
    actions = actions,
    colors = colors,
    windowInsets = windowInsets,
    appBarHeight = appBarHeight,
    scrollBehavior = scrollBehavior
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleRowTopAppBar(
  modifier: Modifier = Modifier,
  image: @Composable () -> Unit,
  title: @Composable () -> Unit,
  titleTextStyle: TextStyle,
  centeredTitle: Boolean,
  navigationIcon: @Composable () -> Unit,
  actions: @Composable() (RowScope.() -> Unit),
  windowInsets: WindowInsets,
  appBarHeight: Dp,
  colors: TopAppBarColors,
  scrollBehavior: TopAppBarScrollBehavior?
) {
  val pinnedHeightPx = with(LocalDensity.current) { 64.dp.toPx() }
  // Sets the app bar's height offset to collapse the entire bar's height when content is
  // scrolled.
  val heightOffsetLimit = with(LocalDensity.current) { -(appBarHeight.toPx() - pinnedHeightPx) }
  SideEffect {
    if (scrollBehavior?.state?.heightOffsetLimit != heightOffsetLimit) {
      scrollBehavior?.state?.heightOffsetLimit = heightOffsetLimit
    }
  }

  // Obtain the container color from the TopAppBarColors using the `overlapFraction`. This
  // ensures that the colors will adjust whether the app bar behavior is pinned or scrolled.
  // This may potentially animate or interpolate a transition between the container-color and the
  // container's scrolled-color according to the app bar's scroll state.
  val colorTransitionFraction = scrollBehavior?.state?.overlappedFraction ?: 0f
  val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
  val appBarContainerColor by animateColorAsState(
    targetValue = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).copy(fraction),
    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
  )
  val titleColor by animateColorAsState(
    targetValue = MaterialTheme.colorScheme.onSurface.copy(if (scrollBehavior?.state?.collapsedFraction ?: 0f > 0.9f) 1f else 0f),
    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
  )

  // Wrap the given actions in a Row.
  val actionsRow = @Composable {
    Row(
      horizontalArrangement = Arrangement.End,
      verticalAlignment = Alignment.CenterVertically,
      content = actions
    )
  }

  // Set up support for resizing the top app bar when vertically dragging the bar itself.
  val appBarDragModifier = if (scrollBehavior != null && !scrollBehavior.isPinned) {
    Modifier.draggable(
      orientation = Orientation.Vertical,
      state = rememberDraggableState { delta ->
        scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffset + delta
      },
      onDragStopped = { velocity ->
        settleAppBar(
          scrollBehavior.state,
          velocity,
          scrollBehavior.flingAnimationSpec,
          scrollBehavior.snapAnimationSpec
        )
      }
    )
  } else {
    Modifier
  }

  // Compose a Surface with a TopAppBarLayout content.
  // The surface's background color is animated as specified above.
  // The height of the app bar is determined by subtracting the bar's height offset from the
  // app bar's defined constant height value (i.e. the ContainerHeight token).
  Surface(modifier = modifier.then(appBarDragModifier), color = appBarContainerColor) {
    val height = LocalDensity.current.run {
      -heightOffsetLimit * (1f - (scrollBehavior?.state?.collapsedFraction ?: 0f)) + pinnedHeightPx
    }

    TopAppBarLayout(
      modifier = Modifier
        .windowInsetsPadding(windowInsets)
        // clip after padding so we don't show the title over the inset area
        .clipToBounds(),
      heightPx = height,
      pinnedHeight = pinnedHeightPx,
      navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
      titleContentColor = titleColor,
      actionIconContentColor = MaterialTheme.colorScheme.onBackground.blendWith(
        MaterialTheme.colorScheme.onSurfaceVariant, scrollBehavior!!.state.collapsedFraction
      ),
      image = image,
      title = title,
      titleTextStyle = titleTextStyle,
      titleAlpha = 1f,
      titleVerticalArrangement = Arrangement.Top,
      titleHorizontalArrangement =
      if (centeredTitle) Arrangement.Center else Arrangement.Start,
      titleBottomPadding = 0,
      hideTitleSemantics = false,
      navigationIcon = navigationIcon,
      actions = actionsRow,
      scrollFraction = scrollBehavior.state.collapsedFraction
    )
  }
}

@Composable
private fun TopAppBarLayout(
  modifier: Modifier,
  heightPx: Float,
  pinnedHeight: Float,
  navigationIconContentColor: Color,
  titleContentColor: Color,
  actionIconContentColor: Color,
  image: @Composable () -> Unit,
  title: @Composable () -> Unit,
  titleTextStyle: TextStyle,
  titleAlpha: Float,
  titleVerticalArrangement: Arrangement.Vertical,
  titleHorizontalArrangement: Arrangement.Horizontal,
  titleBottomPadding: Int,
  hideTitleSemantics: Boolean,
  navigationIcon: @Composable () -> Unit,
  actions: @Composable () -> Unit,
  scrollFraction: Float,
) {
  val animatedScrollFraction by animateFloatAsState(scrollFraction, spring(0.85f, 800f))

  Layout(
    {
      Box(
        Modifier
          .layoutId("navigationIcon")
          .padding(start = TopAppBarHorizontalPadding)
      ) {
        CompositionLocalProvider(
          LocalContentColor provides navigationIconContentColor,
          content = navigationIcon
        )
      }
      Box(
        Modifier
          .layoutId("image")
      ) {
        CompositionLocalProvider(
          content = image
        )
      }
      Box(
        Modifier
          .layoutId("title")
          .padding(horizontal = TopAppBarHorizontalPadding)
          .then(if (hideTitleSemantics) Modifier.clearAndSetSemantics { } else Modifier)
          .graphicsLayer(alpha = titleAlpha)
      ) {
        ProvideTextStyle(value = titleTextStyle) {
          CompositionLocalProvider(
            LocalContentColor provides titleContentColor,
            content = title
          )
        }
      }
      Box(
        Modifier
          .layoutId("actionIcons")
          .padding(end = TopAppBarHorizontalPadding)
      ) {
        CompositionLocalProvider(
          LocalContentColor provides actionIconContentColor,
          content = actions
        )
      }
    },
    modifier = modifier
  ) { measurables, constraints ->
    val navigationIconPlaceable =
      measurables.first { it.layoutId == "navigationIcon" }
        .measure(constraints.copy(minWidth = 0))
    val actionIconsPlaceable =
      measurables.first { it.layoutId == "actionIcons" }
        .measure(constraints.copy(minWidth = 0))

    val maxImageWidth = constraints.maxWidth.coerceAtLeast(0)

    val maxTitleWidth = if (constraints.maxWidth == Constraints.Infinity) {
      constraints.maxWidth
    } else {
      (constraints.maxWidth - navigationIconPlaceable.width - actionIconsPlaceable.width)
        .coerceAtLeast(0)
    }

    val imagePlaceable =
      measurables.first { it.layoutId == "image" }
        .measure(constraints.copy(minWidth = 0, maxWidth = maxImageWidth))

    val titlePlaceable =
      measurables.first { it.layoutId == "title" }
        .measure(constraints.copy(minWidth = 0, maxWidth = maxTitleWidth))

    val imageBaseline =
      if (imagePlaceable[LastBaseline] != AlignmentLine.Unspecified) {
        imagePlaceable[LastBaseline]
      } else {
        0
      }

    // Locate the title's baseline.
    val titleBaseline =
      if (titlePlaceable[LastBaseline] != AlignmentLine.Unspecified) {
        titlePlaceable[LastBaseline]
      } else {
        0
      }

    val layoutHeight = heightPx.roundToInt()

    layout(constraints.maxWidth, layoutHeight) {
      // Navigation icon
      navigationIconPlaceable.placeRelative(
        x = 0,
        y = (pinnedHeight.toInt() - navigationIconPlaceable.height) / 2
      )

      // image
      imagePlaceable.placeRelativeWithLayer(
        x = (constraints.maxWidth - imagePlaceable.width) / 2,
        // position when expanded * inverse scroll + position when collapsed * scroll
        // initial position is the default 0. on scroll the position shifts to being centered vertically
        // 0f collapsed fraction = alignment.top   1f collapsed fraction = alignment.centeredVertically
        y = ((0 * (1f - animatedScrollFraction)) + ((layoutHeight - imagePlaceable.height / 2) * (animatedScrollFraction * 0.1f))).toInt()
      )

      // Title
      titlePlaceable.placeRelative(
        x = when (titleHorizontalArrangement) {
          Arrangement.Center -> (constraints.maxWidth - titlePlaceable.width) / 2
          Arrangement.End ->
            constraints.maxWidth - titlePlaceable.width - actionIconsPlaceable.width
          // Arrangement.Start.
          // An TopAppBarTitleInset will make sure the title is offset in case the
          // navigation icon is missing.
          else -> max(TopAppBarTitleInset.roundToPx(), navigationIconPlaceable.width)
        },
        y = when (titleVerticalArrangement) {
          Arrangement.Center -> (layoutHeight - titlePlaceable.height) / 2
          // Apply bottom padding from the title's baseline only when the Arrangement is
          // "Bottom".
          Arrangement.Bottom ->
            if (titleBottomPadding == 0) layoutHeight - titlePlaceable.height
            else layoutHeight - titlePlaceable.height - max(
              0,
              titleBottomPadding - titlePlaceable.height + titleBaseline
            )
          // Arrangement.Top
          else -> (pinnedHeight.toInt() - titlePlaceable.height) / 2
        }
      )

      // Action icons
      actionIconsPlaceable.placeRelative(
        x = constraints.maxWidth - actionIconsPlaceable.width,
        y = (pinnedHeight.toInt() - actionIconsPlaceable.height) / 2
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
private suspend fun settleAppBar(
  state: TopAppBarState,
  velocity: Float,
  flingAnimationSpec: DecayAnimationSpec<Float>?,
  snapAnimationSpec: AnimationSpec<Float>?
): Velocity {
  // Check if the app bar is completely collapsed/expanded. If so, no need to settle the app bar,
  // and just return Zero Velocity.
  // Note that we don't check for 0f due to float precision with the collapsedFraction
  // calculation.
  if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
    return Velocity.Zero
  }
  var remainingVelocity = velocity
  // In case there is an initial velocity that was left after a previous user fling, animate to
  // continue the motion to expand or collapse the app bar.
  if (flingAnimationSpec != null && abs(velocity) > 1f) {
    var lastValue = 0f
    AnimationState(
      initialValue = 0f,
      initialVelocity = velocity,
    )
      .animateDecay(flingAnimationSpec) {
        val delta = value - lastValue
        val initialHeightOffset = state.heightOffset
        state.heightOffset = initialHeightOffset + delta
        val consumed = abs(initialHeightOffset - state.heightOffset)
        lastValue = value
        remainingVelocity = this.velocity
        // avoid rounding errors and stop if anything is unconsumed
        if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
      }
  }
  // Snap if animation specs were provided.
  if (snapAnimationSpec != null) {
    if (state.heightOffset < 0 &&
      state.heightOffset > state.heightOffsetLimit
    ) {
      AnimationState(initialValue = state.heightOffset).animateTo(
        if (state.collapsedFraction < 0.5f) {
          0f
        } else {
          state.heightOffsetLimit
        },
        animationSpec = snapAnimationSpec
      ) { state.heightOffset = value }
    }
  }

  return Velocity(0f, remainingVelocity)
}

private val TopAppBarHorizontalPadding = 4.dp

private val TopAppBarTitleInset = 16.dp - TopAppBarHorizontalPadding