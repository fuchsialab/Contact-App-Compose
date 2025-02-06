package com.fuchsia.contactapp.navigation.appNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fuchsia.contactapp.navigation.routes.Routes
import com.fuchsia.contactapp.presentation.ContactViewModel
import com.fuchsia.contactapp.presentation.screens.AddEditScreenUI
import com.fuchsia.contactapp.presentation.screens.HomeScreenUI


@Composable
fun AppNavigation(modifier: Modifier = Modifier, viewModel: ContactViewModel) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HomeScreen) {
        composable<Routes.HomeScreen> {
            HomeScreenUI(viewModel = viewModel, navController = navController)
        }
        composable<Routes.AddEditScreen> {
            val id = it.toRoute<Routes.AddEditScreen>()
            AddEditScreenUI(viewModel = viewModel, navController = navController, contactId = id.id)
        }
    }
}
