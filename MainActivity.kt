package com.example.userprofilemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.userprofilemanager.screens.HomeScreen
import com.example.userprofilemanager.screens.ProfileFormScreen
import com.example.userprofilemanager.screens.ProfileDisplayScreen
import com.example.userprofilemanager.ui.theme.UserProfileManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            UserProfileManagerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavigationGraph(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.ProfileForm,
        NavigationItem.ProfileDisplay
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute?.startsWith(item.route) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        val destination = if (item.route == "profile_display") {
                            "profile_display/Not Provided/Not Provided/Not Provided/Not Provided/Not Provided"
                        } else item.route

                        navController.navigate(destination) {
                            popUpTo(NavigationItem.Home.route) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) { HomeScreen() }
        composable(NavigationItem.ProfileForm.route) { ProfileFormScreen(navController) }

        composable(
            route = "profile_display/{name}/{email}/{phone}/{age}/{gender}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType; defaultValue = DEFAULT_VALUE },
                navArgument("email") { type = NavType.StringType; defaultValue = DEFAULT_VALUE },
                navArgument("phone") { type = NavType.StringType; defaultValue = DEFAULT_VALUE },
                navArgument("age") { type = NavType.StringType; defaultValue = DEFAULT_VALUE },
                navArgument("gender") { type = NavType.StringType; defaultValue = DEFAULT_VALUE }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            ProfileDisplayScreen(
                navController,
                name = args?.getString("name") ?: DEFAULT_VALUE,
                email = args?.getString("email") ?: DEFAULT_VALUE,
                phone = args?.getString("phone") ?: DEFAULT_VALUE,
                age = args?.getString("age") ?: DEFAULT_VALUE,
                gender = args?.getString("gender") ?: DEFAULT_VALUE
            )
        }
    }
}

private const val DEFAULT_VALUE = "Not Provided"
