package bruhcollective.itaysonlab.jetispot.ui.screens.config

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import bruhcollective.itaysonlab.jetispot.R
import bruhcollective.itaysonlab.jetispot.ui.shared.AppPreferences

@Composable
fun ArtworkAnimationDialog(navController: NavController) {
  var dampingValue by remember { mutableStateOf(AppPreferences.NPAnimationDamping) }
  var stiffnessValue by remember { mutableStateOf(AppPreferences.NPAnimationStiffness!!) }

  AlertDialog(
    onDismissRequest = { navController.popBackStack() },
    title = { Text(stringResource(id = R.string.config_edit_animation), textAlign = TextAlign.Start) },
    text = {
      Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Damping: $dampingValue", textAlign = TextAlign.Center)

        Slider(
          value = dampingValue!!,
          onValueChange = {
            dampingValue = it
            AppPreferences.NPAnimationDamping = it
          },
          valueRange = 0f..10f
        )

        Text("Stiffness: $stiffnessValue", textAlign = TextAlign.Center)

        Slider(
          value = stiffnessValue,
          onValueChange = {
            stiffnessValue =  it
            AppPreferences.NPAnimationStiffness = it
          },
          valueRange = 0f..10000f
        )
      }
    },
    confirmButton = {
      TextButton(onClick = { navController.popBackStack() }) { Text(stringResource(id = R.string.close)) }
    },
    dismissButton = {
      TextButton(
        onClick = {
          AppPreferences.NPAnimationDamping = 0.75f
          AppPreferences.NPAnimationStiffness = 800f
          navController.popBackStack()
          Toast.makeText(
            navController.context,
            "Artwork animation settings set to default",
            Toast.LENGTH_LONG
          ).show()
        }
      ) {
        Text(stringResource(id = R.string.edit_animation_set_default))
      }
    }
  )
}