package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.ui.ext.blendWith
import bruhcollective.itaysonlab.jetispot.ui.ext.dynamicUnpack
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.shared.navClickable
import com.spotify.home.dac.component.v1.proto.*
import androidx.compose.material3.MaterialTheme.colorScheme as monet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutsBinder(
  item: ShortcutsSectionComponent
) {
  item.shortcutsList.map { it.dynamicUnpack() }.chunked(2).forEachIndexed { idx, pairs ->
    Row(Modifier.padding(bottom = if (idx != item.shortcutsList.lastIndex / 2) 8.dp else 0.dp)) {
      pairs.forEachIndexed { xIdx, xItem ->
        Box(Modifier.weight(1f).padding(end = if (xIdx == 0) 8.dp else 0.dp)) {
          when (xItem) {
            is AlbumCardShortcutComponent -> ShortcutComponentBinder(
              xItem.navigateUri,
              xItem.imageUri,
              "album",
              xItem.title
            )

            is PlaylistCardShortcutComponent -> ShortcutComponentBinder(
              xItem.navigateUri,
              xItem.imageUri,
              "playlist",
              xItem.title
            )

            is ShowCardShortcutComponent -> ShortcutComponentBinder(
              xItem.navigateUri,
              xItem.imageUri,
              "podcasts",
              xItem.title
            )

            is ArtistCardShortcutComponent -> ShortcutComponentBinder(
              xItem.navigateUri,
              xItem.imageUri,
              "artist",
              xItem.title
            )

            is EpisodeCardShortcutComponent -> ShortcutComponentBinder(
              xItem.navigateUri,
              xItem.imageUri,
              "podcasts",
              xItem.title
            )
          }
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
  title: String
) {
  Box(
    modifier = Modifier
      .height(72.dp)
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(monet.inverseOnSurface.blendWith(monet.primaryContainer, 0.1f))
      .navClickable { navController -> navController.navigate(navigateUri) }
  ) {
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
        .padding(12.dp)
    ) {
      PreviewableAsyncImage(
        imageUrl = imageUrl,
        placeholderType = imagePlaceholder,
        modifier = Modifier.fillMaxHeight().aspectRatio(1f).clip(RoundedCornerShape(4.dp))
      )

      Text(
        title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(start = 12.dp).align(CenterVertically).alpha(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) 0.9f else 1f)
      )
    }
  }
}

