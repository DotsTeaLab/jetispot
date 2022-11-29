package bruhcollective.itaysonlab.jetispot.ui.hub.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.core.objs.hub.HubItem
import bruhcollective.itaysonlab.jetispot.ui.hub.LocalHubScreenDelegate
import bruhcollective.itaysonlab.jetispot.ui.hub.components.essentials.PlaylistEntityActionStrip
import bruhcollective.itaysonlab.jetispot.ui.navigation.LocalNavigationController
import bruhcollective.itaysonlab.jetispot.ui.shared.MarqueeText
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.shared.evo.ImageBackgroundTopAppBar
import bruhcollective.itaysonlab.jetispot.ui.shared.evo.ImageTopAppBar
import coil.compose.AsyncImage

@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlaylistHeader(item: HubItem, scrollBehaviour: TopAppBarScrollBehavior) {
  val navController = LocalNavigationController.current
  val delegate = LocalHubScreenDelegate.current

  val screenWidth = LocalConfiguration.current.screenWidthDp.dp
  val padding = 16
  val actionStripHeight = 64.dp

  val collapsedFraction = run { scrollBehaviour.state.collapsedFraction }
  val inverseCollapsedFraction = run { 1f - collapsedFraction }

  val fraction = if (collapsedFraction > 0.98f) 0f else 1f

  val damping = remember { derivedStateOf { 0.85f } }.value
  val stiffness = remember { derivedStateOf { 800f } }.value

  // i do what works when idk whats best :P
  // feel free to make pull requests with optimisations you think are necessary!
  val imageAlpha = run { 1f - collapsedFraction * 1.3f }
  val imageBlur = run { 32.dp * collapsedFraction }

  val buttonBackground by animateColorAsState(
    MaterialTheme.colorScheme.background.copy(fraction),
    animationSpec = spring(stiffness = 400f)
  )
  val buttonPadding by animateDpAsState(
    ((padding * 1.6) * inverseCollapsedFraction + (4 * collapsedFraction)).dp,
    spring(damping, stiffness)
  )
  val imageHeight by animateDpAsState(
    (min(412.dp, screenWidth) * inverseCollapsedFraction),
    spring(damping, stiffness)
  )
  val imagePadding by animateDpAsState(
    (padding * inverseCollapsedFraction).dp,
    spring(damping, stiffness)
  )
  val animatedActionStripHeight by animateDpAsState(
    actionStripHeight * inverseCollapsedFraction,
    spring(damping, stiffness)
  )

  val imageSize = DpSize(
    min(
      (412.dp * inverseCollapsedFraction + (screenWidth * collapsedFraction)),
      screenWidth
    ),
    imageHeight
  )


//  val darkTheme = isSystemInDarkTheme()
//  val dominantColor = remember { mutableStateOf(Color.Transparent) }
//  val dominantColorAsBg = animateColorAsState(dominantColor.value)

//  LaunchedEffect(Unit) {
//    launch {
//      if (dominantColor.value != Color.Transparent) return@launch
//      dominantColor.value =
//        delegate.calculateDominantColor(item.images?.main?.uri.toString(), darkTheme)
//    }
//  }

  ImageTopAppBar(
    title = {
      MarqueeText(
        text = item.text?.title!!,
        overflow = TextOverflow.Ellipsis,
      )
    },
    image = {
      Column() {
        Box(contentAlignment = Alignment.Center) {
          Box() {
            Column() {
              Box(
                Modifier
                  .clip(RoundedCornerShape(24.dp))
                  .blur(imageBlur)
              ) {
                PreviewableAsyncImage(
                  item.images?.main?.uri,
                  "playlist",
                  modifier = Modifier
                    .size(imageSize)
                    .alpha(imageAlpha)
                    .padding(max(imagePadding, 0.dp))
                    .clip(RoundedCornerShape(24.dp)),
                  when (imageHeight > screenWidth) {
                    true -> ContentScale.FillBounds
                    false -> ContentScale.Crop
                  }
                )
              }

              Box(Modifier.height(animatedActionStripHeight).alpha(inverseCollapsedFraction)) {
                PlaylistEntityActionStrip(delegate, item, scrollBehaviour)
              }
            }
          }

          if (screenWidth < 476.dp) IconButton(
            onClick = { navController.popBackStack() },
            colors = IconButtonDefaults.outlinedIconButtonColors(containerColor = buttonBackground),
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(buttonPadding)
          ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
          }

          if (screenWidth < 476.dp) IconButton(
            onClick = { /*TODO*/ },
            colors = IconButtonDefaults.outlinedIconButtonColors(containerColor = buttonBackground),
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(buttonPadding)
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "Options for ${item.text!!.title!!} by ${item.text!!.subtitle!!}"
            )
          }
        }
      }
    },
//    description = {
//      item.text?.subtitle?.let {
//        Column(Modifier.height(96.dp), verticalArrangement = Arrangement.Bottom) {
//          Row(
//            Modifier
//              .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null
//              ) { navController.navigate(item.custom?.get("owner_username") as String) }
//          ) {
//            PreviewableAsyncImage(
//              imageUrl = item.custom?.get("owner_pic") as String,
//              placeholderType = "user",
//              modifier = Modifier
//                .size(32.dp)
//                .clip(CircleShape)
//            )
//
//            Text(
//              text = item.custom["owner_name"] as String,
//              fontSize = 14.sp,
//              modifier = Modifier
//                .align(Alignment.CenterVertically)
//                .padding(start = 12.dp)
//            )
//          }
////          Text(
////            text = "${item.custom?.get("likes_count") as Long} likes • ${item.custom["total_duration"] as String}",
////            fontSize = 12.sp,
////            maxLines = 1
////          )
//
//          Text(
//            text = it,
//            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
//            fontSize = 12.sp,
//            overflow = TextOverflow.Ellipsis,
//            style = TextStyle(platformStyle = PlatformTextStyle(false)),
//            modifier = Modifier.padding(top = if (it != "") 8.dp else 0.dp)
//          )
//        }
//      }
//    },
    actions = {
      IconButton(onClick = { /*TODO*/ }) {
        if (screenWidth >= 476.dp) Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = "Options for ${item.text!!.title!!} by ${item.text!!.subtitle!!}"
        )
        else Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = "Options for ${item.text!!.title!!} by ${item.text!!.subtitle!!}",
          tint = Color.Transparent
        )
      }
    },
    scrollBehavior = scrollBehaviour,
    appBarHeight = imageSize.width + actionStripHeight,
    navigationIcon = {
      IconButton(onClick = { navController.popBackStack() }) {
        if (screenWidth >= 476.dp) Icon(
          Icons.Rounded.ArrowBack,
          contentDescription = "Back"
        )
        else Icon(
          Icons.Rounded.ArrowBack,
          contentDescription = "Back",
          tint = Color.Transparent
        )
      }
    },
    windowInsets = WindowInsets.statusBars
  )
}


@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LargePlaylistHeader(
  item: HubItem,
  scrollBehavior: TopAppBarScrollBehavior
) {
  val navController = LocalNavigationController.current

  ImageBackgroundTopAppBar(
    description = {
      Text(
        item.text!!.subtitle!!,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
        modifier = Modifier.fillMaxWidth(),
        overflow = TextOverflow.Ellipsis,
        maxLines = 8,
        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
      )
    },
    title = {
      MarqueeText(
        item.text!!.title!!,
        Modifier.fillMaxWidth(),
        overflow = TextOverflow.Ellipsis
      )
    },
    smallTitle = {
      MarqueeText(
        item.text!!.title!!,
        Modifier.fillMaxWidth(),
        overflow = TextOverflow.Ellipsis
      )
    },
    picture = {
      AsyncImage(
        model = item.images?.main?.uri,
        contentDescription = null,
        Modifier
          .fillMaxSize(),
        contentScale = ContentScale.FillWidth
      )
    },
    navigationIcon = {
      IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
      }
    },
    actions = {
      IconButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
          .clip(CircleShape)
          .size(38.dp)
          .background(
            MaterialTheme.colorScheme
              .surfaceColorAtElevation(3.dp)
              .copy(0.5f)
          )
      ) {
        Icon(Icons.Rounded.Favorite, contentDescription = null)
      }

      Spacer(modifier = Modifier.padding(horizontal = 4.dp))

      IconButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
          .clip(CircleShape)
          .size(38.dp)
          .background(
            MaterialTheme.colorScheme
              .surfaceColorAtElevation(3.dp)
              .copy(0.5f)
          )
      ) {
        Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = "Options for ${item.text!!.title!!} by ${item.text!!.subtitle!!}",
        )
      }
    },
    scrollBehavior = scrollBehavior,
    maxHeight = 256.dp,
    gradient = true,
    navigationIconPresent = true
  )

//  Column {
//    Box(
//      Modifier
//        .fillMaxWidth()
//        .height(240.dp)
//    ) {
//    PlaylistHeaderAdditionalInfo(navController, delegate, item.custom)
//    EntityActionStrip(navController, delegate, item)
//  }
}


@Composable
fun PlaylistHeaderAdditionalInfo(
  custom: Map<String, Any>?
) {
  custom ?: return
  Row(
    Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {


  }

  Spacer(modifier = Modifier.height(6.dp))
}