package bruhcollective.itaysonlab.jetispot.ui.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.core.objs.hub.HubItem
import bruhcollective.itaysonlab.jetispot.ui.hub.clickableHub
import bruhcollective.itaysonlab.jetispot.ui.shared.MarqueeText
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage

@Composable
fun LargerRow (item: HubItem) {
  // Popular releases
  Row(
    Modifier
      .height(132.dp)
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(24.dp))
      .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
  ) {
    Row(Modifier.clickableHub(item).padding(16.dp).fillMaxWidth()) {
      PreviewableAsyncImage(
        imageUrl = item.images?.main?.uri,
        placeholderType = item.images?.main?.placeholder,
        modifier = Modifier
          .padding(end = 16.dp)
          .clip(RoundedCornerShape(10.dp))
          .size(84.dp)
          .aspectRatio(1f)
      )

      Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .padding(vertical = 16.dp)
          .fillMaxHeight()
      ) {
        MarqueeText(
          item.text!!.title!!,
          modifier = Modifier.padding(bottom = 4.dp),
          fontSize = 18.sp,
          basicGradientColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )

        MarqueeText(
          item.text.subtitle!!,
          fontSize = 12.sp,
          basicGradientColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
      }
    }
  }
}