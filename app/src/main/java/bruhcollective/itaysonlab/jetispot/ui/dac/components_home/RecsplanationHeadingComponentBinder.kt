package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.ui.navigation.LocalNavigationController
import bruhcollective.itaysonlab.jetispot.ui.shared.MediumText
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.shared.navClickable
import com.spotify.home.dac.component.heading.v1.proto.RecsplanationHeadingSingleTextComponent
import com.spotify.home.dac.component.v1.proto.RecsplanationHeadingComponent
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecsplanationHeadingComponentBinder(item: RecsplanationHeadingComponent) {
  val navController = LocalNavigationController.current

  Card(
    shape = RoundedCornerShape(32.dp),
    modifier = Modifier.padding(top = 8.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
  ) {
    Row(
      Modifier
        .clickable { navController.navigate(item.navigateUri) }
        .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
      PreviewableAsyncImage(
        imageUrl = item.imageUri,
        placeholderType = "none",
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
      )

      Column(
        Modifier
          .padding(horizontal = 12.dp)
          .align(Alignment.CenterVertically)) {
        Text(item.subtitle.uppercase(), fontSize = 12.sp)
        MediumText(item.title, modifier = Modifier.padding(top = 0.dp), fontSize = 18.sp)
      }
    }
  }
}

@Composable
fun RecsplanationHeadingSingleTextComponentBinder(item: RecsplanationHeadingSingleTextComponent) {
  // text before second
  val first = item.highlightedText.text.take(item.highlightedText.startInclusive)
  // artist or playlist name
  val second = item.highlightedText.text.substring(item.highlightedText.startInclusive, item.highlightedText.endExclusive)
  // third is displayed when first and second arent being displayed
  val third = item.highlightedText.text.takeLast(abs(item.highlightedText.text.length - (first.length + second.length)))

  Row(
    Modifier
      .padding(top = 8.dp)
      .clip(CircleShape)
      .navClickable { navController -> navController.navigate(item.navigateUri) },
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (item.imageUri.isNotEmpty()) {
        PreviewableAsyncImage(
          imageUrl = item.imageUri,
          placeholderType = "none",
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
        )

        Spacer(Modifier.width(8.dp))
      }

      OutlinedCard(shape = CircleShape) {
        Text(
          first + second + third, fontSize = 14.sp,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
      }
    }
  }
}