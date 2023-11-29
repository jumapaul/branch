package com.example.branch.presentation.navigation

sealed class NavigationRoutes(val route: String) {
    data object LoginScreen : NavigationRoutes(route = "login")

    data object HomeScreen : NavigationRoutes(route = "home")
    data object ChatScreen : NavigationRoutes(route = "chat")
}