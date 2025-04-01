package com.example.animal

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.animal.ui.theme.AnimalTheme
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImageClassifierScreen()
                }
            }
        }
    }
}

@Composable
fun ImageClassifierScreen() {
    var result by remember { mutableStateOf("") }
    var currentImageIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val imageClass = ImageClassifier(context)


    val imageResources = listOf(
        R.drawable.b5,
        R.drawable.checken_4,
        R.drawable.ragno,
        R.drawable.cow,
        R.drawable.dog,
        R.drawable.elep,
        R.drawable.horse
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
            result = imageClass.classifyImage(bitmap)
            if (result != "") {
                result = "Предсказанное животное: $result"
            } else {
                result = "Неизвестный класс"
            }



        }) {
            Text("Classify Image")
        }
        Button(
            onClick = {  currentImageIndex = (currentImageIndex + 1) % imageResources.size
            result = ""}
        ) {
            Text("Следующее изображение")
        }

        Text(result)
    }
}