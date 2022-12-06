package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.ui.ext.blendWith
import bruhcollective.itaysonlab.jetispot.ui.ext.dynamicUnpack
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.shared.navClickable
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.spotify.home.dac.component.v1.proto.*
import java.lang.Integer.max
import androidx.compose.material3.MaterialTheme.colorScheme as monet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutsBinder(
  item: ShortcutsSectionComponent
) {
  val screenWidth = LocalConfiguration.current.screenWidthDp
  val shortcutWidth = remember {
    derivedStateOf {
      if (screenWidth <= 280)
        screenWidth.dp
      else
        /*
        it will count how many would fit in a single row "screenWidth / 208".
        this way it will evenly fill the space by maxing out the width, but for
        larger screens the cards won't be wider than 208.dp.
        */
        min(
          ((screenWidth / max((screenWidth / 208), 2)) - 22).dp,
          208.dp
        )
    }
  }.value
  val shortcutModifier = Modifier
    .height(72.dp)
    .width(shortcutWidth)
    .clip(RoundedCornerShape(16.dp))
    .background(monet.inverseOnSurface.blendWith(monet.primaryContainer, 0.1f))

  FlowRow(
    mainAxisSize = if (LocalConfiguration.current.screenWidthDp >= 620 ) SizeMode.Wrap else SizeMode.Expand,
    mainAxisSpacing = 12.dp,
    mainAxisAlignment = FlowMainAxisAlignment.Center,
    crossAxisSpacing = 12.dp,
    crossAxisAlignment = FlowCrossAxisAlignment.Center
  ) {
    item.shortcutsList.map { it.dynamicUnpack() }.chunked(1).forEachIndexed { idx, pairs ->
      pairs.forEachIndexed { xIdx, xItem ->
        when (xItem) {
          is AlbumCardShortcutComponent -> ShortcutComponentBinder(
            xItem.navigateUri,
            xItem.imageUri,
            "album",
            xItem.title,
            shortcutModifier.navClickable { it.navigate(xItem.navigateUri) }
          )

          is PlaylistCardShortcutComponent -> ShortcutComponentBinder(
            xItem.navigateUri,
            xItem.imageUri,
            "playlist",
            xItem.title,
            shortcutModifier.navClickable { it.navigate(xItem.navigateUri) }
          )

          is ShowCardShortcutComponent -> ShortcutComponentBinder(
            xItem.navigateUri,
            xItem.imageUri,
            "podcasts",
            xItem.title,
            shortcutModifier.navClickable { it.navigate(xItem.navigateUri) }
          )

          is ArtistCardShortcutComponent -> ShortcutComponentBinder(
            xItem.navigateUri,
            xItem.imageUri,
            "artist",
            xItem.title,
            shortcutModifier.navClickable { it.navigate(xItem.navigateUri) }
          )

          is EpisodeCardShortcutComponent -> ShortcutComponentBinder(
            xItem.navigateUri,
            xItem.imageUri,
            "podcasts",
            xItem.title,
            shortcutModifier.navClickable { it.navigate(xItem.navigateUri) }
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShortcutComponentBinder(
  navigateUri: String,
  imageUrl: String,
  imagePlaceholder: String,
  title: String,
  modifier: Modifier
) {
  Box(modifier = modifier) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PreviewableAsyncImage(
      imageUrl = imageUrl,
      placeholderType = imagePlaceholder,
      modifier = Modifier
        .fillMaxSize()
        .blur(512.dp)
        .alpha(0.1f)
    )

    Row(
      Modifier
        .fillMaxSize()
        .padding(12.dp)) {
      PreviewableAsyncImage(
        imageUrl = imageUrl,
        placeholderType = imagePlaceholder,
        modifier = Modifier
          .fillMaxHeight()
          .aspectRatio(1f)
          .clip(RoundedCornerShape(4.dp))
      )

      Text(
        title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .padding(start = 12.dp)
          .align(CenterVertically)
          .alpha(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) 0.9f else 1f)
      )
    }
  }
}

