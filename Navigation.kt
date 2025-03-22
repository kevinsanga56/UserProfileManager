package com.example.userprofilemanager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val label: String, val icon: ImageVector) {
    data object Home : NavigationItem("home", "Home", Icons.Filled.Home)
    data object ProfileForm : NavigationItem("profile_form", "Profile", Icons.Filled.Person)
    data object ProfileDisplay :
        NavigationItem("profile_display", "Display", Icons.AutoMirrored.Filled.List)
}
