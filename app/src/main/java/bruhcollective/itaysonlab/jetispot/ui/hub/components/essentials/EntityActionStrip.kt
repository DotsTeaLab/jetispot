package bruhcollective.itaysonlab.jetispot.ui.hub.components.essentials

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.core.objs.hub.HubEvent
import bruhcollective.itaysonlab.jetispot.core.objs.hub.HubItem
import bruhcollective.itaysonlab.jetispot.ui.ext.compositeSurfaceElevation
import bruhcollective.itaysonlab.jetispot.ui.hub.HubScreenDelegate
import bruhcollective.itaysonlab.jetispot.ui.hub.clickableHub
import bruhcollective.itaysonlab.jetispot.ui.shared.MarqueeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityActionStrip (
  delegate: HubScreenDelegate,
  item: HubItem,
  scrollBehavior: TopAppBarScrollBehavior
) {
  val scrolled = scrollBehavior.state.collapsedFraction >= 0.01f

  Row(
    Modifier
      .fillMaxWidth()
      .height(56.dp)
      .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Box(
      Modifier.weight(1f).align(Alignment.CenterVertically),
      contentAlignment = Alignment.CenterStart
    ) {
      androidx.compose.animation.AnimatedVisibility(
        visible = !scrolled,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Text(
          item.text!!.title!!,
          fontSize = 24.sp,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.animateContentSize()
        )
      }
      androidx.compose.animation.AnimatedVisibility(
        visible = scrolled,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        MarqueeText(
          item.text!!.title!!,
          fontSize = 24.sp,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    Spacer(Modifier.width(16.dp))

    Row {
      IconButton(onClick = { delegate.toggleMainObjectAddedState() },
        Modifier
          .clip(shape = CircleShape)
          .align(Alignment.CenterVertically)
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .size(56.dp)
      ) {
        Icon(
          if (delegate.getMainObjectAddedState().value) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
          null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.padding(horizontal = 2.dp))

      Box() {
        Box(
          Modifier
            .clip(RoundedCornerShape(32.dp))
            .height(56.dp)
            .width(142.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickableHub(item.children!![0])
        ) {
          Icon(
            imageVector = Icons.Outlined.PlayArrow,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            contentDescription = null,
            modifier = Modifier
              .size(28.dp)
              .align(Alignment.Center)
          )
        }

        if ((item.children[0].events?.click as? HubEvent.PlayFromContext)?.data?.player?.options?.player_options_override?.shuffling_context != false) {
          Box(
            Modifier
              .align(Alignment.BottomEnd)
              .offset(4.dp, 4.dp)
              .clip(CircleShape)
              .size(22.dp)
              .background(MaterialTheme.colorScheme.compositeSurfaceElevation(4.dp))
          ) {
            Icon(
              imageVector = Icons.Rounded.Shuffle,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = null,
              modifier = Modifier
                .padding(4.dp)
                .align(Alignment.Center)
            )
          }
        }
      }
    }
  }
}