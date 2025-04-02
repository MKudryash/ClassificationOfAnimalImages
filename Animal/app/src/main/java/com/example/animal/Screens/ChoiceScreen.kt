package com.example.animal.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.animal.Navigation.NavigationRoutes

@Composable
fun ChoiceScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {navController.navigate(NavigationRoutes.GALLERY)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF608FB7))
        ) {
            Text(text = "Ready-made gallery", color = Color.White, fontSize = 18.sp)
        }

        Button(
            onClick = {navController.navigate(NavigationRoutes.LOADURL)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCDD3D6))
        ) {
            Text(text = "Uploading an image using a url", color = Color.White, fontSize = 18.sp)
        }

        Button(
            onClick = {navController.navigate(NavigationRoutes.LOADIMAGE)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E616B))
        ) {
            Text(text = "Uploading an image from the gallery", color = Color.White, fontSize = 18.sp)
        }
    }
}