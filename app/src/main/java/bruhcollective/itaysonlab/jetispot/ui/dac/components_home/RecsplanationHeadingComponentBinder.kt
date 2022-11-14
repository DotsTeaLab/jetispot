package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.ui.navigation.LocalNavigationController
import bruhcollective.itaysonlab.jetispot.ui.shared.MediumText
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.shared.SubtextOverline
import bruhcollective.itaysonlab.jetispot.ui.shared.navClickable
import com.spotify.home.dac.component.heading.v1.proto.RecsplanationHeadingSingleTextComponent
import com.spotify.home.dac.component.v1.proto.RecsplanationHeadingComponent
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecsplanationHeadingComponentBinder(
  item: RecsplanationHeadingComponent
) {
  val navController = LocalNavigationController.current

  Card(
    shape = RoundedCornerShape(32.dp),
    modifier = Modifier.padding(start = 12.dp, top = 40.dp, bottom = 16.dp),
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
          .align(Alignment.CenterVertically)
      ) {
        Text(item.subtitle.uppercase(), fontSize = 12.sp)
        MediumText(item.title, modifier = Modifier.padding(top = 0.dp), fontSize = 18.sp)
      }
    }
  }
}

@Composable
fun RecsplanationHeadingSingleTextComponentBinder(
  item: RecsplanationHeadingSingleTextComponent
) {
  val text = remember(item.highlightedText) {
    val first = item.highlightedText.text.take(item.highlightedText.startInclusive)
    val second = item.highlightedText.text.substring(item.highlightedText.startInclusive, item.highlightedText.endExclusive)
    val third = item.highlightedText.text.takeLast(abs(item.highlightedText.text.length - (first.length + second.length)))

    buildAnnotatedString {
      append(first)
      withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
        append(second)
      }
      append(third)
    }
  }

  Row(Modifier.padding(vertical = 8.dp).navClickable { navController ->
    navController.navigate(item.navigateUri)
  }.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    if (item.imageUri.isNotEmpty()) {
      PreviewableAsyncImage(imageUrl = item.imageUri, placeholderType = "none", modifier = Modifier
        .size(24.dp)
        .clip(CircleShape))

      Spacer(Modifier.width(8.dp))
    }

    Text(text, fontSize = 16.sp)
  }
}