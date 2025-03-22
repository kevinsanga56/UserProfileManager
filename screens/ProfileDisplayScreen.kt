package com.example.userprofilemanager.screens

import android.net.Uri
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.example.userprofilemanager.R

@Composable
fun ProfileDisplayScreen(
    navController: NavHostController,
    name: String = "Not Provided",
    email: String = "Not Provided",
    phone: String = "Not Provided",
    age: String = "Not Provided",
    gender: String = "Not Provided"
) {
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profile Details", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDialog = true },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_placeholder),
                        contentDescription = "Profile Picture of $name",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileTextItem(label = "Name", value = name)
                    ProfileTextItem(label = "Email", value = email)
                    ProfileTextItem(label = "Phone", value = phone)
                    ProfileTextItem(label = "Age", value = age)
                    ProfileTextItem(label = "Gender", value = gender)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Favorite Toggle Button
                    IconToggleButton(
                        checked = isFavorite,
                        onCheckedChange = {
                            isFavorite = !isFavorite
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    if (isFavorite) "Added to Favorites" else "Removed from Favorites"
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Mark as favorite",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            Button(onClick = {
                showDialog = false
                navController.navigateToProfileForm(name, email, phone, age, gender)
            }) {
                Text("Edit Profile")
            }

            if (showDialog) {
                ProfileOptionsDialog(
                    onDismiss = { showDialog = false },
                    onEdit = {
                        showDialog = false
                        navController.navigateToProfileForm(name, email, phone, age, gender)
                    },
                    onDelete = {
                        showDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Profile deleted successfully.",
                                actionLabel = "OK"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileTextItem(label: String, value: String) {
    Text("$label: $value", style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun ProfileOptionsDialog(
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Profile Options") },
        text = { Text("Would you like to edit or delete this profile?") },
        confirmButton = {
            Button(onClick = onEdit) { Text("Edit") }
        },
        dismissButton = {
            Button(onClick = onDelete) { Text("Delete") }
        }
    )
}

/**
 * Extension function to navigate safely to the profile form with encoded parameters.
 */
fun NavHostController.navigateToProfileForm(name: String, email: String, phone: String, age: String, gender: String) {
    val encodedName = Uri.encode(name)
    val encodedEmail = Uri.encode(email)
    val encodedPhone = Uri.encode(phone)
    val encodedAge = Uri.encode(age)
    val encodedGender = Uri.encode(gender)

    this.navigate("profile_form/$encodedName/$encodedEmail/$encodedPhone/$encodedAge/$encodedGender")
}
