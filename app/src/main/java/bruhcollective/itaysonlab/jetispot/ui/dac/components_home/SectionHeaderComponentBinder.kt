package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeaderComponentBinder (text: String) {
  Text(
    text = text,
    color = MaterialTheme.colorScheme.onSurface,
    style = MaterialTheme.typography.headlineSmall,
    modifier = Modifier.padding(start = 12.dp)
  )
}