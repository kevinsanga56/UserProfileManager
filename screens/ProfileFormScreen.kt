package com.example.userprofilemanager.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProfileFormScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    val hobbies = remember { mutableStateMapOf("Reading" to false, "Traveling" to false, "Coding" to false) }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User Profile Form", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Age
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Gender Selection
        Text("Gender:")
        Row {
            listOf("Male", "Female", "Other").forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == option,
                        onClick = { gender = option }
                    )
                    Text(option)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Hobbies
        Text("Hobbies:")
        hobbies.keys.forEach { hobby ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .toggleable(
                        value = hobbies[hobby] ?: false,
                        onValueChange = { hobbies[hobby] = it }
                    )
            ) {
                Checkbox(
                    checked = hobbies[hobby] ?: false,
                    onCheckedChange = { hobbies[hobby] = it }
                )
                Text(hobby)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Notifications Switch
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enable Notifications")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button with Navigation
        Button(
            onClick = {
                isSubmitted = true

                // Encode parameters properly
                val encodedName = Uri.encode(name)
                val encodedEmail = Uri.encode(email)
                val encodedPhone = Uri.encode(phone)
                val encodedAge = Uri.encode(age)
                val encodedGender = Uri.encode(gender)

                // Navigate safely
                navController.navigate("profile_display/$encodedName/$encodedEmail/$encodedPhone/$encodedAge/$encodedGender")
            }
        ) {
            Text("Submit & View Profile")
        }

        // Show confirmation message after submission
        if (isSubmitted) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Profile submitted successfully!", color = MaterialTheme.colorScheme.primary)
        }
    }
}
