package bruhcollective.itaysonlab.jetispot.ui.bottomsheets

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bruhcollective.itaysonlab.jetispot.R
import bruhcollective.itaysonlab.jetispot.ui.shared.AppPreferences

@Composable
fun ArtworkAnimationSheet(navController: NavController) {
  var dampingValue by remember { mutableStateOf(AppPreferences.NPAnimationDamping) }
  var stiffnessValue by remember { mutableStateOf(AppPreferences.NPAnimationStiffness!!) }

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
      stringResource(id = R.string.config_edit_animation),
      fontSize = 22.sp,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
    )

    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text("Damping: $dampingValue", textAlign = TextAlign.Center)

      Slider(
        value = dampingValue!!,
        onValueChange = {
          dampingValue = it
          AppPreferences.NPAnimationDamping = it
        },
        valueRange = 0f..2f
      )

      Text("Stiffness: $stiffnessValue", textAlign = TextAlign.Center)

      Slider(
        value = stiffnessValue,
        onValueChange = {
          stiffnessValue =  it
          AppPreferences.NPAnimationStiffness = it
        },
        valueRange = 0f..2000f
      )

      OutlinedButton(
        onClick = {
          AppPreferences.NPAnimationDamping = 0.75f
          AppPreferences.NPAnimationStiffness = 800f
          navController.popBackStack()
          Toast.makeText(
            navController.context,
            "Artwork animation settings set to default",
            Toast.LENGTH_LONG
          ).show()
        },
        modifier = Modifier.padding(vertical = 8.dp)
      ) {
        Text(stringResource(id = R.string.edit_animation_set_default))
      }
    }
  }
}