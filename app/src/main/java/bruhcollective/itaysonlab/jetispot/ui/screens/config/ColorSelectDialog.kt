package bruhcollective.itaysonlab.jetispot.ui.screens.config

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bruhcollective.itaysonlab.jetispot.R
import bruhcollective.itaysonlab.jetispot.ui.shared.AppPreferences
import bruhcollective.itaysonlab.jetispot.ui.shared.colorSchemePreviewBoxV1

@Composable
fun ColorSelectDialog(navController: NavController) {
  AlertDialog(
    onDismissRequest = { navController.popBackStack() },
    title = { Text(stringResource(id = R.string.config_color_theme), textAlign = TextAlign.Center) },

    text = {
      Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(id = R.string.theme_reload_message), textAlign = TextAlign.Center)

        Row(Modifier.align(Alignment.CenterHorizontally)){
          colorSchemePreviewBoxV1(SchemeColor = "#1DB954", onClick = { AppPreferences.ColorScheme = "#1DB954" }) // TODO: maybe kill all activity and start MainActivity?
          colorSchemePreviewBoxV1(SchemeColor = "#134D2B", onClick = { AppPreferences.ColorScheme = "#134D2B" })
          colorSchemePreviewBoxV1(SchemeColor = "#4DA818", onClick = { AppPreferences.ColorScheme = "#4DA818" })
          colorSchemePreviewBoxV1(SchemeColor = "#A1A818", onClick = { AppPreferences.ColorScheme = "#A1A818" })
        }

        Row(Modifier.align(Alignment.CenterHorizontally)){
          colorSchemePreviewBoxV1(SchemeColor = "#EB4034", onClick = { AppPreferences.ColorScheme = "#EB4034" })
          colorSchemePreviewBoxV1(SchemeColor = "#B60A0D", onClick = { AppPreferences.ColorScheme = "#B60A0D" })
          colorSchemePreviewBoxV1(SchemeColor = "#6E1E32", onClick = { AppPreferences.ColorScheme = "#6E1E32" })
          colorSchemePreviewBoxV1(SchemeColor = "#B60A86", onClick = { AppPreferences.ColorScheme = "#B60A86" })
        }

        Row(Modifier.align(Alignment.CenterHorizontally)){
          colorSchemePreviewBoxV1(SchemeColor = "#056786", onClick = { AppPreferences.ColorScheme = "#056786" })
          colorSchemePreviewBoxV1(SchemeColor = "#009182", onClick = { AppPreferences.ColorScheme = "#009182" })
        }
      }
    },
    confirmButton = {
      Row(Modifier.padding(all = 8.dp), horizontalArrangement = Arrangement.Center) {
        Button(modifier = Modifier.fillMaxWidth(), onClick = { navController.popBackStack() }
        ) {
          Text(stringResource(id = R.string.close))
        }
      }
    }
  )
}