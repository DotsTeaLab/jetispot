package bruhcollective.itaysonlab.jetispot.ui.screens.config

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetispot.ui.monet.color
import bruhcollective.itaysonlab.jetispot.ui.monet.google.scheme.Scheme

@Composable
fun ColorSchemePreviewBox(
  primary: Color = MaterialTheme.colorScheme.primary,
  tertiary: Color = MaterialTheme.colorScheme.tertiary,
  secondary: Color = MaterialTheme.colorScheme.secondary,
  schemeColor: String = "",
  dark: Boolean = true,
  onClick: () -> Unit
){
  var colorPrimary: Color = Color.Black
  var colorSecondary: Color = Color.Black
  var colorTertiary: Color = Color.Black

  if (schemeColor.isNotEmpty()){
    val scheme = if (dark) {
      Scheme.dark(android.graphics.Color.parseColor(schemeColor))
    } else {
      Scheme.light(android.graphics.Color.parseColor(schemeColor))
    }

    colorPrimary = scheme.primary.color()
    colorSecondary = scheme.secondary.color()
    colorTertiary = scheme.primaryContainer.color().copy(alpha = 0.5f)
  } else {
    colorPrimary = primary
    colorSecondary = secondary
    colorTertiary = tertiary
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .padding(2.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.background)
      .size(64.dp)
      .clickable(onClick = onClick)
  ) {
    Box(modifier = Modifier.clip(CircleShape).size(54.dp)){
      Column(modifier = Modifier.fillMaxHeight()){
        Box(modifier = Modifier.background(colorPrimary).size(width = 54.dp, height = 27.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          Box(modifier = Modifier.background(colorSecondary).size(width = 27.dp, height = 27.dp))

          Box(modifier = Modifier.background(colorTertiary).size(width = 27.dp, height = 27.dp))
        }
      }
    }
  }
}





@Composable
fun colorSchemePreviewBoxV2(
  primary: Color = MaterialTheme.colorScheme.primary,
  tertiary: Color = MaterialTheme.colorScheme.tertiary,
  secondary: Color = MaterialTheme.colorScheme.secondary,
  schemeColor: String = "",
  dark: Boolean = true
){
  var colorPrimary: Color = Color.Black
  var colorSecondary: Color = Color.Black
  var colorTertiary: Color = Color.Black

  if (schemeColor.isNotEmpty()){
    val scheme = if (dark) {
      Scheme.dark(android.graphics.Color.parseColor(schemeColor))
    } else {
      Scheme.light(android.graphics.Color.parseColor(schemeColor))
    }

    colorPrimary = scheme.primary.color()
    colorTertiary = scheme.primaryContainer.color().copy(alpha = 0.5f)
    colorSecondary = scheme.secondary.color()
  } else {
    colorPrimary = primary
    colorTertiary = tertiary
    colorSecondary = secondary
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.background)
      .size(64.dp)
      .padding(2.dp)
  ) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.clip(CircleShape).size(54.dp)){
      Column(modifier = Modifier.fillMaxHeight()) {
        Box(modifier = Modifier.background(colorTertiary).size(width = 54.dp, height = 27.dp))

        Box(modifier = Modifier.background(colorSecondary).size(width = 54.dp, height = 27.dp))
      }
      Canvas(modifier = Modifier.size(27.dp)){
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawCircle(
          color = colorPrimary,
          center = Offset(x = canvasWidth, y = canvasHeight),
          radius = size.minDimension / 2
        )
      }
    }
  }
}