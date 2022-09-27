package bruhcollective.itaysonlab.jetispot.ui.bottomsheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bruhcollective.itaysonlab.jetispot.R
import bruhcollective.itaysonlab.jetispot.ui.screens.config.ColorSchemePreviewBox
import bruhcollective.itaysonlab.jetispot.ui.shared.AppPreferences

@Composable
fun ColorSelectSheet(navController: NavController) {
  Column(Modifier.fillMaxWidth().navigationBarsPadding()) {
    Divider(
      modifier = Modifier
        .width(32.dp)
        .padding(vertical = 14.dp)
        .clip(CircleShape)
        .align(Alignment.CenterHorizontally),
      thickness = 4.dp,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f)
    )

    Text(
      stringResource(id = R.string.config_color_theme),
      fontSize = 22.sp,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
    )

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(stringResource(id = R.string.theme_reload_message), textAlign = TextAlign.Center)

      Row(Modifier.padding(top = 8.dp)){
        ColorSchemePreviewBox(
          schemeColor = "#1DB954",
          onClick = { AppPreferences.ColorScheme = "#1DB954" }
        ) // TODO: maybe kill all activity and start MainActivity?
        ColorSchemePreviewBox(
          schemeColor = "#134D2B",
          onClick = { AppPreferences.ColorScheme = "#134D2B" }
        )
        ColorSchemePreviewBox(
          schemeColor = "#4DA818",
          onClick = { AppPreferences.ColorScheme = "#4DA818" }
        )
        ColorSchemePreviewBox(
          schemeColor = "#A1A818",
          onClick = { AppPreferences.ColorScheme = "#A1A818" }
        )
      }

      Row {
        ColorSchemePreviewBox(
          schemeColor = "#EB4034",
          onClick = {
            AppPreferences.ColorScheme = "#EB4034"
          }
        )
        ColorSchemePreviewBox(
          schemeColor = "#B60A0D",
          onClick = {
            AppPreferences.ColorScheme = "#B60A0D"
          }
        )
        ColorSchemePreviewBox(
          schemeColor = "#6E1E32",
          onClick = {
            AppPreferences.ColorScheme = "#6E1E32"
          }
        )
        ColorSchemePreviewBox(
          schemeColor = "#B60A86",
          onClick = {
            AppPreferences.ColorScheme = "#B60A86"
          }
        )
      }

      Row(Modifier.padding(bottom = 8.dp)){
        ColorSchemePreviewBox(
          schemeColor = "#056786",
          onClick = {
            AppPreferences.ColorScheme = "#056786"
          }
        )
        ColorSchemePreviewBox(
          schemeColor = "#009182",
          onClick = { AppPreferences.ColorScheme = "#009182"
          }
        )
      }
    }
  }
}