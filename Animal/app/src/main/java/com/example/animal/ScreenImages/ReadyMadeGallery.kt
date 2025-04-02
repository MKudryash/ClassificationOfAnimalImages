package com.example.animal.ScreenImages

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.animal.ImageClassifier
import com.example.animal.R


@Composable
fun ReadyMadeGallery(navController: NavHostController) {
    var result by remember { mutableStateOf("") }
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val imageClass = ImageClassifier(context)


    val imageResources = listOf(
        R.drawable.butterfly,
        R.drawable.chicken,
        R.drawable.spider,
        R.drawable.cow,
        R.drawable.dog,
        R.drawable.elep,
        R.drawable.horse,
        R.drawable.squirrel,
        R.drawable.cat,
        R.drawable.sheep,
    )

    val bitmap = BitmapFactory.decodeResource(
        context.resources,
        imageResources[currentImageIndex]
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)

        Button(onClick = {
            result = "The predicted animal: ${imageClass.classifyImage(bitmap)}"
        }) {
            Text("Classify Image")
        }
        Button(
            onClick = {
                currentImageIndex = (currentImageIndex + 1) % imageResources.size
                result = ""
            }
        ) {
            Text("The following image")
        }
        Text(result)
    }
}