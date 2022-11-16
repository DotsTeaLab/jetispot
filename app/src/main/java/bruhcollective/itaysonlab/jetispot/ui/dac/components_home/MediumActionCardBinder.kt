package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetispot.ui.monet.ColorToScheme
import bruhcollective.itaysonlab.jetispot.ui.shared.MarqueeText
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.shared.Subtext
import bruhcollective.itaysonlab.jetispot.ui.shared.dynamic_blocks.DynamicFilledPlayButton
import bruhcollective.itaysonlab.jetispot.ui.shared.dynamic_blocks.DynamicTonalLikeButton
import bruhcollective.itaysonlab.jetispot.ui.shared.navClickable
import com.spotify.dac.player.v1.proto.PlayCommand

@Composable
fun MediumActionCardBinder(
  title: String,
  subtitle: String,
  artistName: String = "",
  contentType: String,
  fact: String,
  gradientColor: String,
  navigateUri: String,
  likeUri: String,
  imageUri: String,
  imagePlaceholder: String,
  playCommand: PlayCommand
) {
  val curScheme = MaterialTheme.colorScheme
  val isDark = isSystemInDarkTheme()
  var colorScheme by remember { mutableStateOf(curScheme) }

  LaunchedEffect(gradientColor) {
    val clr = android.graphics.Color.parseColor("#$gradientColor")
    colorScheme = ColorToScheme.convert(clr, isDark)
  }

  MaterialTheme(colorScheme = colorScheme) {
    when (subtitle == "" && artistName != "") {
      true -> EmptySubtitleCard(imageUri, likeUri, navigateUri, imagePlaceholder, artistName, title, contentType, playCommand)
      else -> NormalCard(imageUri, likeUri, navigateUri, imagePlaceholder, title, subtitle, fact, playCommand)
    }
  }
}


@Composable
private fun EmptySubtitleCard(
  imageUri: String,
  likeUri: String,
  navigateUri: String,
  imagePlaceholder: String,
  artistName: String,
  title: String,
  contentType: String,
  playCommand: PlayCommand
) {
  OutlinedCard(
    shape = RoundedCornerShape(32.dp),
    modifier = Modifier
      .fillMaxWidth()
      .navClickable { navController -> navController.navigate(navigateUri) }
  ) {
    Column(Modifier.padding(16.dp)) {
      Row {
        PreviewableAsyncImage(
          imageUrl = imageUri,
          placeholderType = imagePlaceholder,
          modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .size(128.dp)
        )

        Column(
          Modifier
            .height(128.dp)
            .padding(start = 16.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Column(verticalArrangement = Arrangement.Center) {
            Text(
              text = title,
              fontSize = 24.sp,
              maxLines = 3,
            )
          }
          
          Row(verticalAlignment = Alignment.CenterVertically) {
            DynamicTonalLikeButton(
              objectUrl = likeUri,
              Modifier
                .size(42.dp)
                .aspectRatio(1f)
            )

            Column(
              Modifier
                .padding(horizontal = 8.dp)
                .weight(1f, true),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              MarqueeText(
                text = artistName,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Center,
                basicGradientColor = MaterialTheme.colorScheme.surface
              )
              Subtext(text = contentType)
            }

            DynamicFilledPlayButton(
              command = playCommand,
              Modifier
                .size(42.dp)
                .aspectRatio(1f)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun NormalCard(
  imageUri: String,
  likeUri: String,
  navigateUri: String,
  imagePlaceholder: String,
  title: String,
  subtitle: String,
  fact: String,
  playCommand: PlayCommand
) {
  OutlinedCard(
    shape = RoundedCornerShape(32.dp),
    modifier = Modifier
      .fillMaxWidth()
      .navClickable { navController -> navController.navigate(navigateUri) }
  ) {
    Column(Modifier.padding(16.dp)) {
      Row {
        PreviewableAsyncImage(
          imageUrl = imageUri,
          placeholderType = imagePlaceholder,
          modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .size(96.dp)
        )

        Column(Modifier.padding(start = 16.dp, top = 2.dp)) {
          Text(text = title, fontSize = 18.sp, maxLines = 2)
          Spacer(modifier = Modifier.height(8.dp))
          Subtext(text = subtitle, maxLines = 3)
        }
      }

      Row(
        Modifier.fillMaxWidth().padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        DynamicTonalLikeButton(
          objectUrl = likeUri,
          Modifier.size(42.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
          Subtext(text = fact, modifier = Modifier.padding(end = 8.dp))

          DynamicFilledPlayButton(
            command = playCommand,
            Modifier.size(42.dp),
            Icons.Rounded.PlayArrow
          )
        }
      }
    }
  }
}