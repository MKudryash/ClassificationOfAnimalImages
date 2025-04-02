package com.example.animal.Navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.animal.ScreenImages.GalleryUpload
import com.example.animal.ScreenImages.ReadyMadeGallery
import com.example.animal.ScreenImages.UploadImages
import com.example.animal.Screens.ChoiceScreen


@Composable
fun NavHost() {
    val navController =
        rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(navController = navController, startDestination = NavigationRoutes.CHOICE) {

                composable(NavigationRoutes.CHOICE)
                {
                    ChoiceScreen(navController)
                }
                composable(NavigationRoutes.GALLERY)
                {
                    ReadyMadeGallery(navController)
                }
                composable(NavigationRoutes.LOADURL)
                {
                    UploadImages(navController)
                }
                composable(NavigationRoutes.LOADIMAGE)
                {
                    GalleryUpload(navController)
                }

            }
        }
    }
}