package bruhcollective.itaysonlab.jetispot

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bruhcollective.itaysonlab.jetispot.core.SpAuthManager
import bruhcollective.itaysonlab.jetispot.core.SpPlayerServiceManager
import bruhcollective.itaysonlab.jetispot.core.SpSessionManager
import bruhcollective.itaysonlab.jetispot.ui.AppNavigation
import bruhcollective.itaysonlab.jetispot.ui.ext.compositeSurfaceElevation
import bruhcollective.itaysonlab.jetispot.ui.navigation.LocalNavigationController
import bruhcollective.itaysonlab.jetispot.ui.navigation.NavigationController
import bruhcollective.itaysonlab.jetispot.ui.screens.Screen
import bruhcollective.itaysonlab.jetispot.ui.screens.nowplaying.NowPlayingScreen
import bruhcollective.itaysonlab.jetispot.ui.shared.AppPreferences
import bruhcollective.itaysonlab.jetispot.ui.theme.ApplicationTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var sessionManager: SpSessionManager

  @Inject
  lateinit var authManager: SpAuthManager

  @Inject
  lateinit var playerServiceManager: SpPlayerServiceManager

  private var provider: (() -> NavController)? = null

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    provider?.invoke()?.handleDeepLink(intent)
  }

  override fun onDestroy() {
    provider = null
    super.onDestroy()
  }

  @RequiresApi(Build.VERSION_CODES.S)
  @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    AppPreferences.setup(applicationContext)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      ApplicationTheme {
        val backPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current
        // remembers
        val scope = rememberCoroutineScope()
        val bsState = rememberBottomSheetScaffoldState()

        val bottomSheetNavigator = rememberBottomSheetNavigator()
        val navController = rememberNavController(bottomSheetNavigator)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val lambdaNavController = NavigationController { navController }
        var bsLyricsOpened by remember { mutableStateOf(false) }

        val navBarHeightDp =
          WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

        val bsVisible =
          playerServiceManager.playbackState.value != SpPlayerServiceManager.PlaybackState.Idle

        var bsQueueOpened by remember { mutableStateOf(false) }

        // lambdas
        val bsOffset = {
          val bsProgress = bsState.bottomSheetState.progress

          when {
            bsProgress.from == BottomSheetValue.Collapsed && bsProgress.to == BottomSheetValue.Collapsed -> 0f
            bsProgress.from == BottomSheetValue.Expanded && bsProgress.to == BottomSheetValue.Expanded -> 1f
            bsProgress.to == BottomSheetValue.Expanded -> bsProgress.fraction
            bsProgress.to == BottomSheetValue.Collapsed -> 1f - bsProgress.fraction
            else -> bsProgress.fraction
          }.coerceIn(0f..1f)
        }

        DisposableEffect(
          backPressedDispatcherOwner,
          scope,
          bsState.bottomSheetState.isExpanded,
          bsQueueOpened
        ) {
          val callback = backPressedDispatcherOwner?.onBackPressedDispatcher?.addCallback(
            owner = backPressedDispatcherOwner,
            enabled = bsQueueOpened || bsState.bottomSheetState.isExpanded,
          ) {
            if (bsQueueOpened) {
              bsQueueOpened = false
            } else {
              scope.launch {
                if (bsLyricsOpened)
                  bsLyricsOpened = false
                else
                  bsState.bottomSheetState.collapse()
              }
            }
          }

          onDispose {
            callback?.remove()
          }
        }

        DisposableEffect(navController) {
          provider = { navController }

          onDispose {
            provider = null
          }
        }

        CompositionLocalProvider(LocalNavigationController provides lambdaNavController) {
          ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            scrimColor = MaterialTheme.colorScheme.scrim.copy(0.5f),
            sheetBackgroundColor = MaterialTheme.colorScheme.surface
          ) {
            Scaffold(
              bottomBar = {
                val currentDestination = navBackStackEntry?.destination
                if (Screen.hideNavigationBar.any { it == currentDestination?.route }) return@Scaffold
                NavigationBar(
                  modifier = Modifier
                    .offset {
                      IntOffset(
                        0,
                        ((80.dp + navBarHeightDp).toPx() * bsOffset() * 3f).toInt()
                      )
                    }
                    .background(MaterialTheme.colorScheme.compositeSurfaceElevation(3.dp))
                ) {
                  Screen.showInBottomNavigation.forEach { (screen, icon) ->
                    NavigationBarItem(
                      icon = {
                        Icon(
                          icon,
                          contentDescription = stringResource(screen.title)
                        )
                             },
                      label = { Text(stringResource(screen.title)) },
                      selected = lambdaNavController.controller().backQueue.any {
                        it.destination.route?.startsWith(
                          screen.route
                        ) == true },
                      onClick = {
                        navController.navigate(screen.route) {
                          popUpTo(Screen.NavGraph.route) {
                            saveState = true
                          }
                          launchSingleTop = true
                          restoreState = true
                        }
                      }
                    )
                  }
                }
              }
            ) { innerPadding ->
              val sheetPeekHeight by animateDpAsState(
                if (bsVisible) 80.dp + 64.dp + navBarHeightDp else 0.dp
              )

              BottomSheetScaffold(
                sheetContent = {
                  NowPlayingScreen(
                    bottomSheetState = bsState.bottomSheetState,
                    bsOffset = bsOffset,
                    queueOpened = bsQueueOpened,
                    setQueueOpened = { bsQueueOpened = it },
                    isLyricsFullscreenAction = { bsLyricsOpened =! bsLyricsOpened},
                    isLyricsFullscreen = bsLyricsOpened
                  )

                  if (bsOffset() < 0.9f) bsLyricsOpened = false
                },
                scaffoldState = bsState,
                sheetPeekHeight = sheetPeekHeight,
                backgroundColor = MaterialTheme.colorScheme.surface,
                sheetGesturesEnabled = !bsQueueOpened
              ) { innerScaffoldPadding ->
                val cbc1 = 0.0f
                val cbc2 = 1.36f
                val cbc3 = 0.13f
                val cbc4 = 0.75f

                val cubicEasing = remember { CubicBezierEasing(cbc1, cbc2, cbc3, cbc4) }

                val scale = remember {
                  derivedStateOf {
                    cubicEasing.transform(1f - bsOffset() * 0.8f)
                  }
                }

                val blur = remember { derivedStateOf { (cubicEasing.transform(bsOffset() * 32)).dp } }

                AppNavigation(
                  navController = navController,
                  sessionManager = sessionManager,
                  authManager = authManager,
                  bsVisible = bsVisible,
                  modifier = Modifier
                    .blur(blur.value)
                    .graphicsLayer {
                      scaleX = scale.value
                      scaleY = scale.value
                    }
                    .padding(innerScaffoldPadding)
                    .padding(bottom = if (bsVisible) 0.dp else 80.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}