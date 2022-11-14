package bruhcollective.itaysonlab.jetispot.ui.hub.components

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import bruhcollective.itaysonlab.jetispot.ui.screens.search.SearchViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun Searchbar(
  modifier: Modifier = Modifier,
  viewModel: SearchViewModel, virtualPagerState: PagerState,
  clearFunc: () -> Unit,
  enterFunc: () -> Unit
) {
  TextField(
    value = viewModel.searchQuery,
    onValueChange = { viewModel.searchQuery = it },
    modifier = modifier,
    shape = CircleShape,
    placeholder = {
      if (viewModel.searchQuery.text.isEmpty()) {
        Text(text = "What would you like to listen?")
      }
    },
    leadingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = null) },
    trailingIcon = {
      AnimatedVisibility(
        visible = viewModel.searchQuery.text.isNotEmpty(),
        enter = slideIn(
          spring(0.6f, 220f),
          { IntOffset(60, 0) }) + fadeIn(),
        exit = slideOut(
          spring(),
          { IntOffset(60, 0) }) + fadeOut(animationSpec = tween(durationMillis = 200))
      ) {
        IconButton(onClick = clearFunc) {
          Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
        }
      }
    },
    colors = TextFieldDefaults.textFieldColors(
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent
    ),
    singleLine = true,
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions(onSearch = { enterFunc() })
  )
}