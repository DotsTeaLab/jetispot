package bruhcollective.itaysonlab.jetispot.ui.shared.dynamic_blocks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DynamicLikeButton(
  objectUrl: String,
  modifier: Modifier = Modifier
) {
  IconButton(
    onClick = { TODO() },
    modifier = modifier
  ) {
    Icon(Icons.Rounded.Favorite, contentDescription = null)
  }
}
@Composable
fun DynamicTonalLikeButton(
  objectUrl: String,
  modifier: Modifier = Modifier
) {
  FilledTonalIconButton(
    onClick = { TODO() },
    modifier = modifier
  ) {
    Icon(Icons.Rounded.Favorite, contentDescription = null)
  }
}