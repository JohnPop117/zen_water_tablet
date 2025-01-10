package com.example.zen_water_tablet

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zen_water_tablet.ui.AppViewModel
import com.example.zen_water_tablet.ui.CallScreen
import com.example.zen_water_tablet.ui.DrawingFadingLinesCanvasScreen
import com.example.zen_water_tablet.ui.DrawingSettingsScreen
import com.example.zen_water_tablet.ui.StartScreen

enum class ScreenTitles(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Draw_Settings(title = R.string.draw_settings),
    Draw_App(title = R.string.draw_app),
    Call_App(title = R.string.call_app)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalAppBar(
    currentScreen: ScreenTitles,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title, "User")) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {

            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            val uiState by viewModel.uiState.collectAsState()
            if (uiState.appBarSecondaryButtonText > 0) {
                Button(onClick = uiState.appBarSecondaryButton) {
                    Text(text = stringResource(id = uiState.appBarSecondaryButtonText))
                }
            }
        }
    )
}

@Composable
fun PersonalAppScreen (
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = ScreenTitles.valueOf(
        backStackEntry?.destination?.route ?: ScreenTitles.Start.name
    )

    Scaffold(
        topBar = {
            PersonalAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                viewModel=viewModel,
                navigateUp = { navController.navigateUp() }
            )
        },
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = ScreenTitles.Start.name,
            enterTransition = { EnterTransition.None},
            exitTransition = { ExitTransition.None},
            modifier = Modifier
                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        ) {

            composable(route = ScreenTitles.Start.name) {
                uiState.appBarSecondaryButton={}
                uiState.appBarSecondaryButtonText=-1
                uiState.main_image = R.drawable::class.java.fields.filter{ LocalContext.current.resources.getResourceEntryName(it.getInt(null)).endsWith("main_image") }.get(0).getInt(null)

                StartScreen(drawAppClick = { navController.navigate(route = ScreenTitles.Draw_App.name)
                                           }, navController=navController,main_image = uiState.main_image,
                    modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium)))
            }
            composable(route = ScreenTitles.Draw_App.name) {
                uiState.appBarSecondaryButtonText=R.string.draw_settings
                uiState.appBarSecondaryButton={navController.navigate(route = ScreenTitles.Draw_Settings.name)}
                DrawingFadingLinesCanvasScreen(
                    drawSettings = uiState.drawSettings,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = ScreenTitles.Draw_Settings.name) {
                uiState.appBarSecondaryButton={navController.navigate(route = ScreenTitles.Draw_App.name)}
                uiState.appBarSecondaryButtonText=R.string.draw_app
                DrawingSettingsScreen(
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = ScreenTitles.Call_App.name) {
                uiState.appBarSecondaryButton={}
                uiState.appBarSecondaryButtonText=-1
                CallScreen(
                    uiState = uiState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}