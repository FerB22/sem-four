package com.example.semfour.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.semfour.ui.screen.*
import kotlinx.coroutines.launch

// ── Rutas de navegación ────────────────────────────────────────────────────

sealed class Screen(val route: String) {
    data object Main : Screen("main")

    data class SubjectDetail(val subjectId: String = "{subjectId}") :
        Screen("subject_detail/{subjectId}") {
        fun createRoute(id: String) = "subject_detail/$id"
    }

    data class TopicDetail(val topicId: String = "{topicId}") :
        Screen("topic_detail/{topicId}") {
        fun createRoute(id: String) = "topic_detail/$id"
    }

    data class StudySession(val topicId: String = "{topicId}", val sessionType: String = "{sessionType}") :
        Screen("session/{topicId}/{sessionType}") {
        fun createRoute(topicId: String, type: String) = "session/$topicId/$type"
    }
}

private data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem("Hoy", Icons.Default.Home),
    BottomNavItem("Horario", Icons.Default.CalendarMonth),
    BottomNavItem("Materias", Icons.Default.School),
    BottomNavItem("Hábitos", Icons.Default.LocalFireDepartment),
    BottomNavItem("Ajustes", Icons.Default.Settings),
)

/**
 * Raíz de la app Compose: NavHost con soporte para gestos horizontales (HorizontalPager)
 * y navegación fluida a sub-pantallas.
 */
@Composable
fun SemFourApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        enterTransition = {
            fadeIn(animationSpec = tween(220, easing = EaseOutCubic))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(160, easing = EaseInCubic))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(220, easing = EaseOutCubic))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(160, easing = EaseInCubic))
        }
    ) {
        // ── Pantalla Principal con Pager y Swipe Horizontal ──
        composable(Screen.Main.route) {
            MainPagerScreen(navController = navController)
        }

        // ── Sub-pantalla: Detalle de Asignatura (Slide Transition) ──
        composable(
            route = Screen.SubjectDetail().route,
            arguments = listOf(navArgument("subjectId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(tween(260, easing = EaseOutCubic)) { it / 4 } + fadeIn(tween(260))
            },
            exitTransition = {
                slideOutHorizontally(tween(200, easing = EaseInCubic)) { -it / 4 } + fadeOut(tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(tween(240, easing = EaseOutCubic)) { -it / 4 } + fadeIn(tween(240))
            },
            popExitTransition = {
                slideOutHorizontally(tween(200, easing = EaseInCubic)) { it / 4 } + fadeOut(tween(200))
            }
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
            SubjectDetailScreen(
                subjectId = subjectId,
                onTopicClick = { topicId ->
                    navController.navigate(Screen.TopicDetail().createRoute(topicId))
                },
                onStartSession = { topicId, sessionType ->
                    navController.navigate(Screen.StudySession().createRoute(topicId, sessionType))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Sub-pantalla: Detalle del Tema ──
        composable(
            route = Screen.TopicDetail().route,
            arguments = listOf(navArgument("topicId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(tween(260, easing = EaseOutCubic)) { it / 4 } + fadeIn(tween(260))
            },
            exitTransition = {
                slideOutHorizontally(tween(200, easing = EaseInCubic)) { -it / 4 } + fadeOut(tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(tween(240, easing = EaseOutCubic)) { -it / 4 } + fadeIn(tween(240))
            },
            popExitTransition = {
                slideOutHorizontally(tween(200, easing = EaseInCubic)) { it / 4 } + fadeOut(tween(200))
            }
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            TopicDetailScreen(
                topicId = topicId,
                onStartSession = { id, sessionType ->
                    navController.navigate(Screen.StudySession().createRoute(id, sessionType))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Sub-pantalla: Sesión de Estudio / Temporizador ──
        composable(
            route = Screen.StudySession().route,
            arguments = listOf(
                navArgument("topicId") { type = NavType.StringType },
                navArgument("sessionType") { type = NavType.StringType }
            ),
            enterTransition = {
                slideInVertically(tween(280, easing = EaseOutCubic)) { it / 4 } + fadeIn(tween(240))
            },
            exitTransition = {
                slideOutVertically(tween(220, easing = EaseInCubic)) { it / 4 } + fadeOut(tween(220))
            },
            popEnterTransition = {
                fadeIn(tween(200))
            },
            popExitTransition = {
                slideOutVertically(tween(220, easing = EaseInCubic)) { it / 4 } + fadeOut(tween(220))
            }
        ) {
            StudySessionScreen(
                onSessionCompleted = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Contenedor principal con HorizontalPager para deslizar entre pestañas con gestos táctiles.
 */
@Composable
private fun MainPagerScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { context.getSharedPreferences("semfour_prefs", android.content.Context.MODE_PRIVATE) }
    var showFirstTimeGuide by remember {
        mutableStateOf(!prefs.getBoolean("has_seen_guide_v1", false))
    }

    if (showFirstTimeGuide) {
        AppGuideDialog(onDismiss = {
            prefs.edit().putBoolean("has_seen_guide_v1", true).apply()
            showFirstTimeGuide = false
        })
    }

    val pagerState = rememberPagerState(initialPage = 0) { bottomNavItems.size }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    val selected = pagerState.currentPage == index

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(if (selected) 24.dp else 22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold
                                else androidx.compose.ui.text.font.FontWeight.Normal
                            )
                        },
                        selected = selected,
                        onClick = {
                            if (pagerState.currentPage != index) {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            key = { it },
            beyondViewportPageCount = 2,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> DashboardScreen(
                    onStartSession = { topicId, sessionType ->
                        navController.navigate(Screen.StudySession().createRoute(topicId, sessionType))
                    },
                    onOpenSubject = { subjectId ->
                        navController.navigate(Screen.SubjectDetail().createRoute(subjectId))
                    }
                )
                1 -> ScheduleScreen(
                    onSubjectClick = { subjectId ->
                        navController.navigate(Screen.SubjectDetail().createRoute(subjectId))
                    }
                )
                2 -> SubjectsScreen(
                    onSubjectClick = { subjectId ->
                        navController.navigate(Screen.SubjectDetail().createRoute(subjectId))
                    }
                )
                3 -> HabitsScreen()
                4 -> SettingsScreen()
            }
        }
    }
}
