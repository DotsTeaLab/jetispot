package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.ui.ext.dynamicUnpack
import bruhcollective.itaysonlab.jetispot.ui.navigation.LocalNavigationController
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.theme.CustomShapes
import com.spotify.home.dac.component.v1.proto.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun SectionComponentBinder(
  item: SectionComponent
) {
  val list = item.componentsList.map { it.dynamicUnpack() }
  val lazyListState = rememberLazyListState()

  LazyRow(
    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    state = lazyListState,
    flingBehavior = rememberSnapperFlingBehavior(
      lazyListState,
      snapOffsetForItem = SnapOffsets.Start,
      decayAnimationSpec = rememberSplineBasedDecay(),
      springAnimationSpec = spring(0.1f, 20f)
    )
  ) {
    items(list) { listItem ->
      when (listItem) {
        is AlbumCardMediumComponent -> MediumCard(
          title = listItem.title,
          subtitle = listItem.subtitle,
          navigateUri = listItem.navigateUri,
          imageUri = listItem.imageUri,
          imagePlaceholder = "album"
        )

        is PlaylistCardMediumComponent -> MediumCard(
          title = listItem.title,
          subtitle = listItem.subtitle,
          navigateUri = listItem.navigateUri,
          imageUri = listItem.imageUri,
          imagePlaceholder = "playlist"
        )

        is ArtistCardMediumComponent -> MediumArtistCard(
          title = listItem.title,
          subtitle = listItem.subtitle,
          navigateUri = listItem.navigateUri,
          imageUri = listItem.imageUri,
          imagePlaceholder = "artist"
        )

        is EpisodeCardMediumComponent -> MediumCard(
          title = listItem.title,
          subtitle = listItem.subtitle,
          navigateUri = listItem.navigateUri,
          imageUri = listItem.imageUri,
          imagePlaceholder = "podcasts"
        )

        is ShowCardMediumComponent -> MediumCard(
          title = listItem.title,
          subtitle = listItem.subtitle,
          navigateUri = listItem.navigateUri,
          imageUri = listItem.imageUri,
          imagePlaceholder = "podcasts"
        )
      }
    }
  }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun MediumCard(
  title: String,
  subtitle: String,
  navigateUri: String,
  imageUri: String,
  imagePlaceholder: String
) {
  val navController = LocalNavigationController.current
  Box(
    Modifier
      .clip(RoundedCornerShape(28.dp))
      .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
      .clickable { navController.navigate(navigateUri) }
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(172.dp).padding(bottom = 4.dp)
    ) {
      var drawnTitle = false

      // Had to wrap the image in another composable due to weird padding when
      // image couldn't be retrieved
      Box(Modifier.padding(top = 14.dp, start = 14.dp, end = 14.dp)) {
        PreviewableAsyncImage(
          imageUrl = imageUri,
          placeholderType = imagePlaceholder,
          modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp))
        )
      }

      Column(
        Modifier.height(96.dp).padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center
      ) {
        if (title.isNotEmpty()) {
          drawnTitle = true

          Text(
            title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 20.sp,
            style = TextStyle(
              platformStyle = PlatformTextStyle(false)
            ).plus(MaterialTheme.typography.titleMedium),
            textAlign = TextAlign.Start
          )
        }

        if (subtitle.isNotEmpty()) {
          Text(
            subtitle,
            modifier = Modifier
              .padding(top = if (drawnTitle) 6.dp else 0.dp)
              .fillMaxWidth(),
            style = TextStyle(
              platformStyle = PlatformTextStyle(false)
            ).plus(MaterialTheme.typography.bodySmall),
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
          )
        }
      }
    }
  }
}

@Composable
fun MediumArtistCard(
  title: String,
  subtitle: String,
  navigateUri: String,
  imageUri: String,
  imagePlaceholder: String
) {
  val navController = LocalNavigationController.current

  Box(
    Modifier
      .clip(RoundedCornerShape(28.dp))
      .clickable { navController.navigate(navigateUri) }
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround,
      modifier = Modifier.width(172.dp).height(258.dp).padding(bottom = 4.dp)
    ) {
      // Had to wrap the image in another composable due to weird padding when
      // image couldn't be retrieved
      Box(Modifier.weight(1f).aspectRatio(1f)) {
        PreviewableAsyncImage(
          imageUrl = imageUri,
          placeholderType = imagePlaceholder,
          modifier = Modifier.clip(CustomShapes().BlobShape).fillMaxSize()
        )
      }

      Column(
        Modifier
          .height(56.dp)
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (title.isNotEmpty()) {
          Text(
            title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 20.sp,
            style = TextStyle(
              platformStyle = PlatformTextStyle(false)
            ).plus(MaterialTheme.typography.labelLarge),
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}