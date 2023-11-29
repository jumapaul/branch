package com.example.branch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.branch.presentation.home.HomeScreen
import com.example.branch.presentation.login.LoginScreen
import com.example.branch.presentation.message.ChatScreen

@Composable
fun NavGraph(navHostController: NavHostController) {

    NavHost(
        navController = navHostController, startDestination = NavigationRoutes.LoginScreen.route
    ) {
        composable(NavigationRoutes.LoginScreen.route) {
            LoginScreen(navController = navHostController)
        }

        composable(NavigationRoutes.HomeScreen.route) {
            HomeScreen(navController = navHostController)
        }

        composable(
            route = NavigationRoutes.ChatScreen.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val threadId = backStackEntry.arguments?.getInt("id")
            if (threadId != null) {
                ChatScreen(threadId = threadId)
            }
        }
    }
}